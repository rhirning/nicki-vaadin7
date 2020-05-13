
package org.mgnl.nicki.vaadin.base.editor;

/*-
 * #%L
 * nicki-vaadin-base
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



import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.data.InvalidActionException;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.vaadin.base.components.NewClassEditor;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectViewer extends CustomComponent implements NewClassEditor, ClassEditor {

	private VerticalLayout mainLayout;
	private DynamicObject dynamicObject;
	private Button saveButton;
	private boolean create;
	private DynamicObjectValueChangeListener<String> listener;
	private DynamicObject parent;

	@Deprecated
	public DynamicObjectViewer(DynamicObject dynamicObject) {
		this.dynamicObject = dynamicObject;
		this.create = false;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	public DynamicObjectViewer() {
	}

	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		log.debug("DynamicObject: " + dynamicObject);
		this.dynamicObject = (DynamicObject) dynamicObject;
		this.create = false;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}
	
	public DynamicObjectViewer(DynamicObjectValueChangeListener<String> listener) {
		this.listener = listener;
	}
	
	public void init(TreeData parent, Class<? extends TreeData> classDefinition) throws InstantiateDynamicObjectException, DynamicObjectException {
		this.parent = (DynamicObject) parent;
		try {
			this.dynamicObject = (DynamicObject) this.parent.createChild(classDefinition, "");
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.create = true;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}


	private VerticalLayout buildMainLayout() {
		
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setWidth("100%");
		Label label = new Label(dynamicObject.getClass().getName());
		mainLayout.addComponent(label);
		DynamicObjectFieldFactory factory = new DynamicObjectFieldFactory(listener);
		factory.addFields(mainLayout, dynamicObject, create);
		
		saveButton = new Button(I18n.getText("nicki.editor.generic.button.save"));
		saveButton.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		mainLayout.addComponent(saveButton);
		return mainLayout;
	}

	public void save() {
		try {
			if (create) {
				dynamicObject.create();
			} else {
				Notification.show(I18n.getText("nicki.editor.save.info"));
				dynamicObject.update();
			}
			if (listener != null) {
				listener.close(this);
				listener.refresh(this.parent);
			}
		} catch (Exception e) {
			log.error("Error", e);
		}
	}

	public boolean isCreate() {
		return create;
	}
}
