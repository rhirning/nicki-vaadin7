package org.mgnl.nicki.vaadin.base.views;

/*-
 * #%L
 * nicki-vaadin-base
 * %%
 * Copyright (C) 2017 - 2018 Ralf Hirning
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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.ConfigurableView;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Ralf Hirning
 *
 * configuration:
 * 
 * editorGroup					Group for editors
 * storeClass					FQN
 * targetName (optional)		Name of Target where to store the script
 * configPath					Config key for Script path
 * 
 */
public class InfoView extends CustomComponent implements ConfigurableView {
	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private RichTextArea infoText;

	@AutoGenerated
	private HorizontalLayout buttonLayout;

	@AutoGenerated
	private Button saveInfoButton;

	@AutoGenerated
	private Button editInfoButton;

	private static final long serialVersionUID = -4894326575778098227L;
	
	private NickiApplication application;
	
	private boolean isInit;
	
	private InfoStore infoStore;
	
	Map<String, String> configuration;
	
	public InfoView() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@Override
	public void init() {

		if (!isInit) {
			
			try {
				infoStore = Classes.newInstance(configuration.get("storeClass"));
				infoStore.setConfiguration(configuration);
				if (infoStore.getData() != null ) {
					infoText.setValue(infoStore.getData());
				}
			} catch (Exception e) {
				Notification.show("could not load Info", e.getMessage(), Type.ERROR_MESSAGE);
				infoStore = null;
			}
			editInfoButton.setVisible(false);
			saveInfoButton.setVisible(false);
			infoText.addStyleName("hide-richtext-toolbar");

			if (isEditor()) {
				editInfoButton.addClickListener(event -> {
						try {
							if (infoStore.getData() != null ) {
								infoText.setValue(infoStore.getData());
							}
						} catch (Exception e) {
							Notification.show("could not load Info", e.getMessage(), Type.ERROR_MESSAGE);
							infoStore = null;
						}
						infoText.removeStyleName("hide-richtext-toolbar");
						saveInfoButton.setVisible(true);
						editInfoButton.setVisible(false);
				});
	
				saveInfoButton.addClickListener(event -> {
						infoText.addStyleName("hide-richtext-toolbar");
						saveInfoButton.setVisible(false);
						editInfoButton.setVisible(true);
						saveInfo();
				});
				editInfoButton.setVisible(true);
				saveInfoButton.setVisible(false);
			} else {
				infoText.addStyleName("hide-richtext-toolbar");
				editInfoButton.setVisible(false);
				saveInfoButton.setVisible(false);				
			}
		}
		
		isInit = true;
	}

	private boolean isEditor() {
		String editorGroup = configuration.get("editorGroup");
		if (editorGroup != null) {
			for (String group : StringUtils.split(editorGroup, ",")) {
				if (getPerson().isMemberOf(group)) {
					return true;
				}
			}
		}
		return false;
	}

	private Person getPerson() {
		return (Person) application.getContext().getLoginContext().getUser();
	}

	protected void saveInfo() {
		
		if (infoStore != null) {
			try {
				infoStore.setData(infoText.getValue());
				infoStore.save();
			} catch (Exception e) {
				Notification.show("could not update Info", e.getMessage(), Type.ERROR_MESSAGE);
			}
		} else {
			Notification.show("could not create Info", Type.ERROR_MESSAGE);
		}

	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void setApplication(NickiApplication application) {
		this.application = application;
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// buttonLayout
		buttonLayout = buildButtonLayout();
		mainLayout.addComponent(buttonLayout);
		
		// infoText
		infoText = new RichTextArea();
		infoText.setWidth("100.0%");
		infoText.setHeight("100.0%");
		mainLayout.addComponent(infoText);
		mainLayout.setExpandRatio(infoText, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildButtonLayout() {
		// common part: create layout
		buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("-1px");
		buttonLayout.setHeight("-1px");
		buttonLayout.setMargin(false);
		buttonLayout.setSpacing(true);
		
		// editButton
		editInfoButton = new Button();
		editInfoButton.setCaption("Edit");
		editInfoButton.setWidth("-1px");
		editInfoButton.setHeight("-1px");
		buttonLayout.addComponent(editInfoButton);
		
		// saveButton
		saveInfoButton = new Button();
		saveInfoButton.setCaption("Save");
		saveInfoButton.setWidth("-1px");
		saveInfoButton.setHeight("-1px");
		buttonLayout.addComponent(saveInfoButton);
		
		return buttonLayout;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

}
