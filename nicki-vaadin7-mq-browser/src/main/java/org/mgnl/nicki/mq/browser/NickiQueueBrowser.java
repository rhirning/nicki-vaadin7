package org.mgnl.nicki.mq.browser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.helper.JsonHelper;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.helper.ValuePair;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class NickiQueueBrowser extends CustomComponent  implements View {

	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private TabSheet tabSheet;

	@AutoGenerated
	private VerticalLayout sendLayout;

	@AutoGenerated
	private TextArea messageTextArea;

	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;

	@AutoGenerated
	private Button sendButton;

	@AutoGenerated
	private VerticalLayout inspectLayout;

	@AutoGenerated
	private HorizontalSplitPanel messageSplitPanel;

	@AutoGenerated
	private Table propertiesTable;

	@AutoGenerated
	private Table messageTable;

	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;

	@AutoGenerated
	private Button removeButton;

	@AutoGenerated
	private Button loadButton;

	@AutoGenerated
	private HorizontalLayout configLayout;

	@AutoGenerated
	private ComboBox countComboBox;

	@AutoGenerated
	private TextField selectorTextField;

	@AutoGenerated
	private TextField queueField;

	@AutoGenerated
	private TextField configBaseTextField;

	private static final long TIMEOUT = 10;
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private MessagesContainerDataSource messagesContainerDataSource;
	private BeanItemContainer<ValuePair> valuePairContainer;
	
	@SuppressWarnings("unchecked")
	public NickiQueueBrowser(String configBase, String queue, String selector) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		messageTable.setSelectable(true);
		messageTable.setMultiSelect(true);
		if (StringUtils.isNotBlank(configBase)) {
			configBaseTextField.setValue(configBase);
			configBaseTextField.setEnabled(false);
		}
		
		if (StringUtils.isNotBlank(queue)) {
			queueField.setValue(queue);
			queueField.setEnabled(false);
		}
		
		if (StringUtils.isNotBlank(selector)) {
			selectorTextField.setValue(selector);
			selectorTextField.setEnabled(false);
		}
		
		loadButton.addClickListener(event -> {
				String configBase1 = configBaseTextField.getValue();
				String queueName = queueField.getValue();
				String messageSelector = selectorTextField.getValue();
				if (StringUtils.isNotBlank(queueName)) {
					load(configBase1, queueName, messageSelector);
				} else {
					Notification.show("Welche Queue?", Type.HUMANIZED_MESSAGE);
				}
		});
		
		removeButton.addClickListener(event -> {
				Collection<Message> messages = (Collection<Message>) messageTable.getValue();
				if (messages != null && messages.size() > 0) {
					removeMessages(messages);
					String configBase1 = configBaseTextField.getValue();
					String queueName = queueField.getValue();
					String messageSelector = selectorTextField.getValue();
					if (StringUtils.isNotBlank(queueName)) {
						load(configBase1, queueName, messageSelector);
					} else {
						Notification.show("Welche Queue?", Type.HUMANIZED_MESSAGE);
					}
				}
		});
		
		messageTable.addItemClickListener(event -> {
				try {
					inspect((BeanItem<Message>) event.getItem());
				} catch (JMSException e) {
					log.error("Error reading message", e);
				}
		});
		
		sendButton.addClickListener(event -> send());
	}

	protected void send() {
		try {
			String configBase = configBaseTextField.getValue();
			String queueName = queueField.getValue();
			if (StringUtils.isNotBlank(queueName)) {
				NickiMessage nickiMessage = JsonHelper.toBean(NickiMessage.class, messageTextArea.getValue());
				sendMessage(configBase, queueName, nickiMessage);
			} else {
				Notification.show("Welche Queue?", Type.HUMANIZED_MESSAGE);
			}

		} catch (IllegalAccessException | InvocationTargetException | InstantiationException | JMSException e) {
			Notification.show("Fehler: " + e.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	public void showTab(String tabName) {
		for (int i = 0; i < tabSheet.getComponentCount(); i++) {
			Tab tab = tabSheet.getTab(i);
			if (StringUtils.equals(tabName, tab.getCaption())) {
				tabSheet.setSelectedTab(tab);
				break;
			}
		}
	}
	
	private void sendMessage(String configBase, String queueName, NickiMessage nickiMessage) throws JMSException {
		Connection connection = null;
		Session session = null;

		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(Config.getString(configBase + ".connector"));
			connection = factory.createConnection("producer",
					Config.getString(configBase + ".user.password.producer"));
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
			MessageProducer producer = session.createProducer(null);
	
			Destination destination = session.createQueue(queueName);
			Message message = session.createTextMessage(nickiMessage.getMessage());
			if (nickiMessage.getProperties() != null) {
				for (String key : nickiMessage.getProperties().keySet()) {
					message.setStringProperty(key, nickiMessage.getProperties().get(key));
				}
			}
			producer.send(destination, message);
			Map<String, String> propertyMap = new HashMap<>();
			@SuppressWarnings("unchecked")
			Enumeration<String> names = message.getPropertyNames();
			while (names.hasMoreElements()) {
				String key = names.nextElement();
				propertyMap.put(key, message.getStringProperty(key));
			}
			log.debug("Sending message, properties=" + propertyMap
					+ ", on queue: " + destination);
			Notification.show("Sending message, properties=" + propertyMap
					+ ", on queue: " + destination, Type.TRAY_NOTIFICATION);
			session.close();
			connection.close();
		} catch (JMSException e) {
			log.error("Error loading messages", e);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				log.error("Error closing JMS session/connection", e);
			}
		}
	}

	protected void inspect(BeanItem<Message> item) throws JMSException {
		if (valuePairContainer == null) {
			valuePairContainer = new BeanItemContainer<>(ValuePair.class);
			propertiesTable.setContainerDataSource(valuePairContainer);
		}
		Message message = item.getBean();
		List<ValuePair> pairs = new ArrayList<>();
		Enumeration<?> propertyNames = message.getPropertyNames();
		while(propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
		
			ValuePair valuePair = new ValuePair(propertyName, message.getStringProperty(propertyName));
			pairs.add(valuePair);
		}
		Date date = new Date(message.getJMSTimestamp());
		pairs.add(new ValuePair("date", date.toString()));
		valuePairContainer.removeAllItems();
		valuePairContainer.addAll(pairs);
	}

	protected void load(String configBase, String queueName, String messageSelector) {
		if (valuePairContainer != null) {
			valuePairContainer.removeAllItems();
		}
		Connection connection = null;
		Session session = null;
		
		List<Message> messages = new ArrayList<>();
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(Config.getString(configBase + ".connector"));
			connection = factory.createConnection("consumer",
					Config.getString(configBase + ".user.password.consumer"));
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
			Queue queue = session.createQueue(queueName);
			connection.start();
			QueueBrowser browser;
			if (StringUtils.isNotBlank(messageSelector)) {
				browser = session.createBrowser(queue, messageSelector);
			} else {
				browser = session.createBrowser(queue);
			}
	        Enumeration<?> e = browser.getEnumeration();
	        while (e.hasMoreElements()) {
	            Message message = (Message) e.nextElement();
	            messages.add(message);
	        }
	        browser.close();
		} catch (JMSException e) {
			log.error("Error loading messages", e);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				log.error("Error closing JMS session/connection", e);
			}
		}
		if (messagesContainerDataSource != null) {
			messagesContainerDataSource.removeAllItems();
			messagesContainerDataSource.add(messages);
		} else {
			messagesContainerDataSource = new MessagesContainerDataSource(messages);
			messageTable.setContainerDataSource(messagesContainerDataSource);
			messageTable.setVisibleColumns("JMSMessageID");
		}
	}
	
	public void removeMessages(Collection<Message> messages) {
		Connection connection = null;
		Session session = null;

		String configBase = configBaseTextField.getValue();
		String queueName = queueField.getValue();
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(Config.getString(configBase + ".connector"));
			connection = factory.createConnection("consumer",
					Config.getString(configBase + ".user.password.consumer"));
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
			Destination destination = session.createQueue(queueName);
			connection.start();
			StringBuilder sb = new StringBuilder();
			for (Message message : messages) {
				if (sb.length() > 0) {
					sb.append(" OR ");
				}
				sb.append("JMSMessageID='" + message.getJMSMessageID() + "'");
			}
			String selector = sb.toString();
			
			MessageConsumer consumer = session.createConsumer(destination, selector);
			// polling for messages
			Message message = null ; 
			
		    while ( (message = consumer.receive(TIMEOUT)) != null ){
	    		message.acknowledge();
		    }
		} catch (JMSException e) {
			log.error("Error loading messages", e);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				log.error("Error closing JMS session/connection", e);
			}
		}

		
	}
	
	public void removeMessage(Message removeMessage) {
		Connection connection = null;
		Session session = null;

		String configBase = configBaseTextField.getValue();
		String queueName = queueField.getValue();
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(Config.getString(configBase + ".connector"));
			connection = factory.createConnection("consumer",
					Config.getString(configBase + ".user.password.consumer"));
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	
			Destination destination = session.createQueue(queueName);
			connection.start();
			
			String selector = "JMSMessageID='"+removeMessage.getJMSMessageID() + "'";
			
			MessageConsumer consumer = session.createConsumer(destination, selector);
			// polling for messages
			Message message = null ; 
			
		    while ( (message = consumer.receive(TIMEOUT)) != null ){
		    	if (StringUtils.equals(removeMessage.getJMSMessageID(), message.getJMSMessageID())) {
		    		message.acknowledge();
		    	}
		    }
		} catch (JMSException e) {
			log.error("Error loading messages", e);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				log.error("Error closing JMS session/connection", e);
			}
		}

		
	}

	@Override
	public void init() {
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void setApplication(NickiApplication application) {
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// configLayout
		configLayout = buildConfigLayout();
		mainLayout.addComponent(configLayout);
		
		// tabSheet
		tabSheet = buildTabSheet();
		mainLayout.addComponent(tabSheet);
		mainLayout.setExpandRatio(tabSheet, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildConfigLayout() {
		// common part: create layout
		configLayout = new HorizontalLayout();
		configLayout.setImmediate(false);
		configLayout.setWidth("-1px");
		configLayout.setHeight("-1px");
		configLayout.setMargin(true);
		configLayout.setSpacing(true);
		
		// configBaseTextField
		configBaseTextField = new TextField();
		configBaseTextField.setCaption("Config base");
		configBaseTextField.setImmediate(false);
		configBaseTextField.setWidth("-1px");
		configBaseTextField.setHeight("-1px");
		configLayout.addComponent(configBaseTextField);
		
		// queueField
		queueField = new TextField();
		queueField.setCaption("Queue");
		queueField.setImmediate(false);
		queueField.setWidth("-1px");
		queueField.setHeight("-1px");
		configLayout.addComponent(queueField);
		
		// selectorTextField
		selectorTextField = new TextField();
		selectorTextField.setCaption("Selector");
		selectorTextField.setImmediate(false);
		selectorTextField.setWidth("-1px");
		selectorTextField.setHeight("-1px");
		configLayout.addComponent(selectorTextField);
		
		// countComboBox
		countComboBox = new ComboBox();
		countComboBox.setCaption("Anzahl");
		countComboBox.setImmediate(false);
		countComboBox.setWidth("-1px");
		countComboBox.setHeight("-1px");
		configLayout.addComponent(countComboBox);
		
		return configLayout;
	}

	@AutoGenerated
	private TabSheet buildTabSheet() {
		// common part: create layout
		tabSheet = new TabSheet();
		tabSheet.setImmediate(true);
		tabSheet.setWidth("100.0%");
		tabSheet.setHeight("100.0%");
		
		// inspectLayout
		inspectLayout = buildInspectLayout();
		tabSheet.addTab(inspectLayout, "Inspect", null);
		
		// sendLayout
		sendLayout = buildSendLayout();
		tabSheet.addTab(sendLayout, "Send", null);
		
		return tabSheet;
	}

	@AutoGenerated
	private VerticalLayout buildInspectLayout() {
		// common part: create layout
		inspectLayout = new VerticalLayout();
		inspectLayout.setImmediate(false);
		inspectLayout.setWidth("100.0%");
		inspectLayout.setHeight("100.0%");
		inspectLayout.setMargin(false);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		inspectLayout.addComponent(horizontalLayout_1);
		
		// messageSplitPanel
		messageSplitPanel = buildMessageSplitPanel();
		inspectLayout.addComponent(messageSplitPanel);
		inspectLayout.setExpandRatio(messageSplitPanel, 1.0f);
		
		return inspectLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// loadButton
		loadButton = new Button();
		loadButton.setCaption("Laden");
		loadButton.setImmediate(true);
		loadButton.setWidth("-1px");
		loadButton.setHeight("-1px");
		horizontalLayout_1.addComponent(loadButton);
		
		// removeButton
		removeButton = new Button();
		removeButton.setCaption("L�schen");
		removeButton.setImmediate(true);
		removeButton.setWidth("-1px");
		removeButton.setHeight("-1px");
		horizontalLayout_1.addComponent(removeButton);
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private HorizontalSplitPanel buildMessageSplitPanel() {
		// common part: create layout
		messageSplitPanel = new HorizontalSplitPanel();
		messageSplitPanel.setImmediate(false);
		messageSplitPanel.setWidth("100.0%");
		messageSplitPanel.setHeight("100.0%");
		
		// messageTable
		messageTable = new Table();
		messageTable.setCaption("Messages");
		messageTable.setImmediate(false);
		messageTable.setWidth("100.0%");
		messageTable.setHeight("100.0%");
		messageSplitPanel.addComponent(messageTable);
		
		// propertiesTable
		propertiesTable = new Table();
		propertiesTable.setCaption("Properties");
		propertiesTable.setImmediate(false);
		propertiesTable.setWidth("100.0%");
		propertiesTable.setHeight("100.0%");
		messageSplitPanel.addComponent(propertiesTable);
		
		return messageSplitPanel;
	}

	@AutoGenerated
	private VerticalLayout buildSendLayout() {
		// common part: create layout
		sendLayout = new VerticalLayout();
		sendLayout.setImmediate(false);
		sendLayout.setWidth("100.0%");
		sendLayout.setHeight("100.0%");
		sendLayout.setMargin(false);
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		sendLayout.addComponent(horizontalLayout_2);
		
		// messageTextArea
		messageTextArea = new TextArea();
		messageTextArea.setCaption("Message");
		messageTextArea.setImmediate(false);
		messageTextArea.setWidth("100.0%");
		messageTextArea.setHeight("100.0%");
		sendLayout.addComponent(messageTextArea);
		sendLayout.setExpandRatio(messageTextArea, 1.0f);
		
		return sendLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setImmediate(false);
		horizontalLayout_2.setWidth("-1px");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		
		// sendButton
		sendButton = new Button();
		sendButton.setCaption("Send");
		sendButton.setImmediate(true);
		sendButton.setWidth("-1px");
		sendButton.setHeight("-1px");
		horizontalLayout_2.addComponent(sendButton);
		
		return horizontalLayout_2;
	}

}
