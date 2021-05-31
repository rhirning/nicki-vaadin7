
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
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.vaadin.base.components.NewClassEditor;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectViewer extends VerticalLayout implements NewClassEditor, ClassEditor {

	private DynamicObject dynamicObject;
	private DynamicObject parent;
	private FormLayout formLayout;

	public DynamicObjectViewer() {
	}

	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		log.debug("DynamicObject: " + dynamicObject);
		this.dynamicObject = (DynamicObject) dynamicObject;
		buildMainLayout();
		setSizeFull();
	}
	
	public void init(TreeData parent, Class<? extends TreeData> classDefinition) throws InstantiateDynamicObjectException, DynamicObjectException {
		this.parent = (DynamicObject) parent;
		try {
			this.dynamicObject = (DynamicObject) this.parent.createChild(classDefinition, "");
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buildMainLayout();
	}


	private void buildMainLayout() {
		
		setMargin(true);
		
		Label label = new Label(dynamicObject.getClass().getName());
		setSizeUndefined();
		formLayout = new FormLayout();
		add(label, formLayout);
		
		DynamicObjectFieldFactory factory = new DynamicObjectFieldFactory();
		factory.addFields(formLayout, dynamicObject);
	}

	@Override
	public void save() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
