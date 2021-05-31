
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.mgnl.nicki.core.helper.NameValue;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.View;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class TailViewer extends VerticalLayout implements Serializable, View {

	static final Object VISIBLE_COLUMNS[] = {"value"};
	private TextField path;
	private HorizontalLayout inputPanel;
	private Grid<NameValue> table;
	private Tailer tailer;
	private TailerListener listener = new TailerListener();
	private List<NameValue> container = new ArrayList<NameValue>();
	private TextField numberOfLinesField;
	private long numberOfLines = 1000;
	private Checkbox checkBox;
	private boolean end = true;
	private long lastUse;
	private String activePath;
	private static final long TIMEOUT = 10 * 60 * 1000; // 10 Minutes
	private boolean isInit;
	
	public TailViewer() {
	}
	
	public TailViewer(NickiApplication application) {
	}
	public List<NameValue> getContainer() {
		return container;
	}

	private void buildMainLayout() {
		setSizeFull();
		
		inputPanel = new HorizontalLayout();
		inputPanel.setWidth("100%");
		inputPanel.setHeight("-1px");
		add(inputPanel);
		
		// path
		path = new TextField();
		path.setWidth("100%");
		path.setHeight("-1px");
		inputPanel.add(path);

		LogFileResource logFileResource = new LogFileResource(this);
		StreamResource streamResource = new StreamResource("complete.txt", () -> logFileResource.getStream());
		streamResource.setCacheTime(0);
		Anchor link = new Anchor(streamResource, "download file");
		link.setTarget("_blank");
		inputPanel.add(link);
		
		checkBox = new Checkbox("head", false);
		checkBox.addBlurListener(event -> {
				end = !checkBox.getValue();
		});
		inputPanel.add(checkBox);
		
		Button loadButton = new Button("Load");
		loadButton.addClickListener(event -> reload());
		inputPanel.add(loadButton);
		
		numberOfLinesField = new TextField();
		numberOfLinesField.setValue(Long.toString(numberOfLines));
		numberOfLinesField.addBlurListener(event -> {
				try {
					numberOfLines = Long.valueOf(numberOfLinesField.getValue());
				} catch (NumberFormatException e) {
					Notification.show("Invalid numberOfLines", Type.TRAY_NOTIFICATION);
				}
				checkContainer();
		});
		inputPanel.add(numberOfLinesField);

		ContainerFileResource containerFileResource = new ContainerFileResource(this);
		StreamResource containerStreamResource = new StreamResource("table.txt", () -> containerFileResource.getStream());
		containerStreamResource.setCacheTime(0);
		Anchor containerLink = new Anchor(containerStreamResource, "download");
		containerLink.setTarget("_blank");
		inputPanel.add(containerLink);
		
		Button refreshButton = new Button("Update");
		refreshButton.addClickListener(event -> refresh());
		inputPanel.add(refreshButton);
		
		inputPanel.setFlexGrow(1, path);
		
		table = new Grid<NameValue>();
		table.setHeight("100%");
		table.setItems(container);
		table.setWidth("100%");
		add(table);
		setFlexGrow(1, table);
		
	}

	protected synchronized void checkContainer() {
		if (container.size() > numberOfLines) {
			for (int i = 0; i < container.size() - numberOfLines; i++) {
				container.remove(container.get(0));
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
		container.clear();
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
			container.add(new NameValue("line", line));
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
