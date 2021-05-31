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
import org.mgnl.nicki.vaadin.base.components.StyledText;
import org.mgnl.nicki.vaadin.base.menu.application.ConfigurableView;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

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
public class InfoView extends VerticalLayout implements ConfigurableView {
	
	private Div content;

	private TextArea editText;

	private HorizontalLayout buttonLayout;

	private Button saveInfoButton;

	private Button editInfoButton;

	private static final long serialVersionUID = -4894326575778098227L;
	
	private NickiApplication application;
	
	private boolean isInit;
	
	private InfoStore infoStore;
	
	Map<String, String> configuration;
	
	public InfoView() {
		buildMainLayout();
	}

	@Override
	public void init() {

		if (!isInit) {
			
			try {
				infoStore = Classes.newInstance(configuration.get("storeClass"));
				infoStore.setConfiguration(configuration);
				if (infoStore.getData() != null ) {
					content.removeAll();
					content.add(new StyledText(infoStore.getData()));
//					editText.setValue(infoStore.getData());
				}
			} catch (Exception e) {
				Notification.show("could not load Info", e.getMessage(), Type.ERROR_MESSAGE);
				infoStore = null;
			}
			editInfoButton.setVisible(false);
			saveInfoButton.setVisible(false);
			//infoText.addStyleName("hide-richtext-toolbar");

			if (isEditor()) {
				editInfoButton.addClickListener(event -> {
						try {
							if (infoStore.getData() != null ) {
								editText.setValue(infoStore.getData());
							}
						} catch (Exception e) {
							Notification.show("could not load Info", e.getMessage(), Type.ERROR_MESSAGE);
							infoStore = null;
						}
						content.removeAll();
						content.add(editText);
//						infoText.removeStyleName("hide-richtext-toolbar");
						saveInfoButton.setVisible(true);
						editInfoButton.setVisible(false);
				});
	
				saveInfoButton.addClickListener(event -> {
//						infoText.addStyleName("hide-richtext-toolbar");
						saveInfoButton.setVisible(false);
						editInfoButton.setVisible(true);
						saveInfo();
						content.removeAll();
						try {
							content.add(new StyledText(infoStore.getData()));
						} catch (InfoStoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				});
				editInfoButton.setVisible(true);
				saveInfoButton.setVisible(false);
			} else {
//				infoText.addStyleName("hide-richtext-toolbar");
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
				infoStore.setData(editText.getValue());
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

	private void buildMainLayout() {
		setWidth("100.0%");
		setHeight("100.0%");
		
		// buttonLayout
		buttonLayout = buildButtonLayout();
		add(buttonLayout);
		
		content = new Div();
		content.setSizeFull();
		add(content);
		// editText
		editText = new TextArea();
		editText.setWidth("100.0%");
		editText.setHeight("100.0%");
		//editText.setVisible(false);
		
		
		setFlexGrow(1, content);
	}

	private HorizontalLayout buildButtonLayout() {
		// common part: create layout
		buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("-1px");
		buttonLayout.setHeight("-1px");
		buttonLayout.setMargin(false);
		buttonLayout.setSpacing(true);
		
		// editButton
		editInfoButton = new Button();
		editInfoButton.setText("Edit");
		editInfoButton.setWidth("-1px");
		editInfoButton.setHeight("-1px");
		buttonLayout.add(editInfoButton);
		
		// saveButton
		saveInfoButton = new Button();
		saveInfoButton.setText("Save");
		saveInfoButton.setWidth("-1px");
		saveInfoButton.setHeight("-1px");
		buttonLayout.add(saveInfoButton);
		
		return buttonLayout;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

}
