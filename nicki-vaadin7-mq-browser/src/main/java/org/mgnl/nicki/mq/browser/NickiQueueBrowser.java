package org.mgnl.nicki.mq.browser;

/*-
 * #%L
 * nicki-vaadin7-mq-browser
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
import org.mgnl.nicki.vaadin.base.components.NickiTabSheet;
import org.mgnl.nicki.vaadin.base.helper.ValuePair;
import org.mgnl.nicki.vaadin.base.menu.application.View;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class NickiQueueBrowser extends VerticalLayout  implements View {
	
	private NickiTabSheet tabSheet;

	
	private VerticalLayout sendLayout;

	
	private TextArea messageTextArea;

	
	private HorizontalLayout horizontalLayout_2;

	
	private Button sendButton;

	
	private VerticalLayout inspectLayout;

	
	private SplitLayout messageSplitPanel;

	
	private Grid<ValuePair> propertiesTable;

	
	private Grid<Message> messageTable;

	
	private HorizontalLayout horizontalLayout_1;

	
	private Button removeButton;

	
	private Button loadButton;

	
	private HorizontalLayout configLayout;

	
	private TextField selectorTextField;

	
	private TextField queueField;

	
	private TextField configBaseTextField;

	private boolean isInit;

	private static final long TIMEOUT = 10;

	
	public NickiQueueBrowser(String configBase, String queue, String selector) {
		buildMainLayout();
		messageTable.setSelectionMode(SelectionMode.MULTI);
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
				Collection<Message> messages = messageTable.asMultiSelect().getValue();
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
					inspect(event.getItem());
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

	/* TODO: showTab(String tabName) wird evtl gar nicht benötigt
	public void showTab(String tabName) {
		for (int i = 0; i < tabSheet.getComponentCount(); i++) {
			Tab tab = tabSheet.getTab(i);
			if (StringUtils.equals(tabName, tab.getCaption())) {
				tabSheet.setSelectedTab(tab);
				break;
			}
		}
	}
	*/
	
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

	protected void inspect(Message message) throws JMSException {
		List<ValuePair> pairs = new ArrayList<>();
		Enumeration<?> propertyNames = message.getPropertyNames();
		while(propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
		
			ValuePair valuePair = new ValuePair(propertyName, message.getStringProperty(propertyName));
			pairs.add(valuePair);
		}
		Date date = new Date(message.getJMSTimestamp());
		pairs.add(new ValuePair("date", date.toString()));
		propertiesTable.setItems(pairs);
	}

	protected void load(String configBase, String queueName, String messageSelector) {
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
		messageTable.setItems(messages);
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
		if (!isInit) {
			messageTable.addColumn(source -> {
				try {
					return source.getJMSMessageID();
				} catch (JMSException e) {
					return "Could not load Message";
				}
			});
			isInit = true;
		}
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void setApplication(NickiApplication application) {
	}

	
	private void buildMainLayout() {
		setWidth("100%");
		setHeight("100%");
		setMargin(false);
		
		// configLayout
		configLayout = buildConfigLayout();
		add(configLayout);
		
		// tabSheet
		tabSheet = buildTabSheet();
		add(tabSheet);
		setFlexGrow(1, tabSheet);
	}

	
	private HorizontalLayout buildConfigLayout() {
		// common part: create layout
		configLayout = new HorizontalLayout();
		configLayout.setWidth("-1px");
		configLayout.setHeight("-1px");
		configLayout.setMargin(true);
		configLayout.setSpacing(true);
		
		// configBaseTextField
		configBaseTextField = new TextField();
		configBaseTextField.setLabel("Config base");
		configBaseTextField.setWidth("-1px");
		configBaseTextField.setHeight("-1px");
		configLayout.add(configBaseTextField);
		
		// queueField
		queueField = new TextField();
		queueField.setLabel("Queue");
		queueField.setWidth("-1px");
		queueField.setHeight("-1px");
		configLayout.add(queueField);
		
		// selectorTextField
		selectorTextField = new TextField();
		selectorTextField.setLabel("Selector");
		selectorTextField.setWidth("-1px");
		selectorTextField.setHeight("-1px");
		configLayout.add(selectorTextField);
		
		
		return configLayout;
	}

	
	private NickiTabSheet buildTabSheet() {
		// common part: create layout
		tabSheet = new NickiTabSheet();
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

	
	private VerticalLayout buildInspectLayout() {
		// common part: create layout
		inspectLayout = new VerticalLayout();
		inspectLayout.setWidth("100.0%");
		inspectLayout.setHeight("100.0%");
		inspectLayout.setMargin(false);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		inspectLayout.add(horizontalLayout_1);
		
		// messageSplitPanel
		messageSplitPanel = buildMessageSplitPanel();
		inspectLayout.add(messageSplitPanel);
		inspectLayout.setFlexGrow(1,  messageSplitPanel);
		
		return inspectLayout;
	}

	
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// loadButton
		loadButton = new Button();
		loadButton.setText("Laden");
		loadButton.setWidth("-1px");
		loadButton.setHeight("-1px");
		horizontalLayout_1.add(loadButton);
		
		// removeButton
		removeButton = new Button();
		removeButton.setText("Löschen");
		removeButton.setWidth("-1px");
		removeButton.setHeight("-1px");
		horizontalLayout_1.add(removeButton);
		
		return horizontalLayout_1;
	}

	
	private SplitLayout buildMessageSplitPanel() {
		// common part: create layout
		messageSplitPanel = new SplitLayout();
		messageSplitPanel.setOrientation(Orientation.HORIZONTAL);
		messageSplitPanel.setWidth("100.0%");
		messageSplitPanel.setHeight("100.0%");
		
		messageSplitPanel.addToPrimary(new Span("Messages"));
		// messageTable
		messageTable = new Grid<>();
		messageTable.setWidth("100.0%");
		messageTable.setHeight("100.0%");
		messageSplitPanel.addToPrimary(messageTable);
		
		// propertiesTable
		messageSplitPanel.addToSecondary(new Span("Properties"));
		propertiesTable = new Grid<>();
		propertiesTable.setWidth("100.0%");
		propertiesTable.setHeight("100.0%");
		messageSplitPanel.addToSecondary(propertiesTable);
		
		return messageSplitPanel;
	}

	
	private VerticalLayout buildSendLayout() {
		// common part: create layout
		sendLayout = new VerticalLayout();
		sendLayout.setWidth("100.0%");
		sendLayout.setHeight("100.0%");
		sendLayout.setMargin(false);
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		sendLayout.add(horizontalLayout_2);
		
		// messageTextArea
		messageTextArea = new TextArea();
		messageTextArea.setLabel("Message");
		messageTextArea.setWidth("100.0%");
		messageTextArea.setHeight("100.0%");
		sendLayout.add(messageTextArea);
		sendLayout.setFlexGrow(1, messageTextArea);
		
		return sendLayout;
	}

	
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setWidth("-1px");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		
		// sendButton
		sendButton = new Button();
		sendButton.setText("Send");
		sendButton.setWidth("-1px");
		sendButton.setHeight("-1px");
		horizontalLayout_2.add(sendButton);
		
		return horizontalLayout_2;
	}

}
