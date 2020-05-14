
package org.mgnl.nicki.editor.log4j;

/*-
 * #%L
 * nicki-editor-log4j
 * %%
 * Copyright (C) 2017 Ralf Hirning
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


import java.io.File;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.mgnl.nicki.core.helper.NameValue;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import lombok.extern.slf4j.Slf4j;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@Slf4j
@SuppressWarnings("serial")
public class TailViewer extends CustomComponent implements Serializable, View {

	static final Object VISIBLE_COLUMNS[] = {"value"};

	private VerticalLayout mainLayout;
	private Panel panel;
	private TextField path;
	private HorizontalLayout inputPanel;
	private Table table;
	private Tailer tailer;
	private TailerListener listener = new TailerListener();
	private LinesContainer container = new LinesContainer();
	private TextField numberOfLinesField;
	private long numberOfLines = 1000;
	private CheckBox checkBox;
	private boolean end = true;
	private long lastUse;
	private String activePath;
	private static final long TIMEOUT = 10 * 60 * 1000; // 10 Minutes
	private boolean isInit;
	
	public TailViewer() {
	}
	
	public TailViewer(NickiApplication application) {
	}
	public LinesContainer getContainer() {
		return container;
	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setSizeFull();
		
		inputPanel = new HorizontalLayout();
		inputPanel.setWidth("100%");
		inputPanel.setHeight("-1px");
		mainLayout.addComponent(inputPanel);
		
		// path
		path = new TextField();
		path.setImmediate(true);
		path.setWidth("100%");
		path.setHeight("-1px");
		inputPanel.addComponent(path);

		LogFileResource logFileResource = new LogFileResource(this);
		StreamResource streamResource = new StreamResource(logFileResource, "complete.txt");
		streamResource.setCacheTime(0);
		Link link = new Link("download file", streamResource, "_blank", 1000, 600, BorderStyle.DEFAULT);
		inputPanel.addComponent(link);
		
		checkBox = new CheckBox("head", false);
		checkBox.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 8760727270499695551L;
			@Override
			public void blur(BlurEvent event) {
				end = !checkBox.getValue();
			}
		});
		inputPanel.addComponent(checkBox);
		
		Button loadButton = new Button("Load");
		loadButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -6478602760185041606L;

			@Override
			public void buttonClick(ClickEvent event) {
				reload();
			}
		});
		inputPanel.addComponent(loadButton);
		
		numberOfLinesField = new TextField();
		numberOfLinesField.setValue(Long.toString(numberOfLines));
		numberOfLinesField.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = -4722389160871240275L;
			@Override
			public void blur(BlurEvent event) {
				try {
					numberOfLines = Long.valueOf(numberOfLinesField.getValue());
				} catch (NumberFormatException e) {
					Notification.show("Invalid numberOfLines", Type.TRAY_NOTIFICATION);
				}
				checkContainer();
			}
		});
		inputPanel.addComponent(numberOfLinesField);

		ContainerFileResource containerFileResource = new ContainerFileResource(this);
		StreamResource containerStreamResource = new StreamResource(containerFileResource, "table.txt");
		containerStreamResource.setCacheTime(0);
		Link containerLink = new Link("download", containerStreamResource, "_blank", 1000, 600, BorderStyle.DEFAULT);
		inputPanel.addComponent(containerLink);
		
		Button refreshButton = new Button("Update");
		refreshButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -7103010719060704358L;

			@Override
			public void buttonClick(ClickEvent event) {
				refresh();
			}
		});
		inputPanel.addComponent(refreshButton);
		
		inputPanel.setExpandRatio(path, 1);
		
		// panel
		panel = new Panel();
		panel.setImmediate(true);
		panel.setSizeFull();
		table = new Table();
		table.setHeight("100%");
		table.setContainerDataSource(container);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnHeader("value", "lines");
		panel.setContent(table);
		table.setWidth("100%");
		mainLayout.addComponent(table);
		mainLayout.setExpandRatio(table, 1);
		
		return mainLayout;
	}

	protected synchronized void checkContainer() {
		if (container.size() > numberOfLines) {
			for (int i = 0; i < container.size() - numberOfLines; i++) {
				container.removeItem(container.firstItemId());
			}
		}
	}

	protected void refresh() {
		if (tailer == null) {
			Notification.show("Es wird keine Datei überwacht. Nach 10 Inaktivität wird die Überwachung automatisch gestoppt");
		}
		lastUse = new Date().getTime();
	}

	protected void reload() {
		container.removeAllItems();
		if (tailer != null) {
			tailer.stop();
			tailer = null;
			log.debug("Stop Tailer for File '" + activePath + "'");
		}
		lastUse = new Date().getTime();
		activePath = path.getValue();
		File file = new File(activePath); 
		
		long delayMillis = 1000;
		tailer = Tailer.create(file, listener, delayMillis , end);
		log.debug("Tailer for File '" + activePath + "' started");
	}

	class TailerListener extends TailerListenerAdapter implements Serializable {

		@Override
		public void handle(String line) {
			long now = new Date().getTime();
			if (now - TIMEOUT > lastUse) {
				tailer.stop();
				tailer = null;
				log.debug("Timeout Tailer for File '" + activePath + "'");
			}
			container.addBean(new NameValue("line", line));
			checkContainer();
		}
	}

	public String getPath() {
		return path.getValue();
	}
	@Override
	public void init() {
		if (!isInit) {

			buildMainLayout();
			setCompositionRoot(mainLayout);
			setSizeFull();
			isInit = true;
		}
	}
	@Override
	public boolean isModified() {
		return false;
	}
	@Override
	public void setApplication(NickiApplication application) {
		// TODO Auto-generated method stub
		
	}
		
}
