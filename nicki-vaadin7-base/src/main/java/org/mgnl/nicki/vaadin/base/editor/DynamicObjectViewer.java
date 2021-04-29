
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

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectViewer extends CustomComponent implements NewClassEditor, ClassEditor {

	private VerticalLayout mainLayout;
	private DynamicObject dynamicObject;
	private DynamicObject parent;

	public DynamicObjectViewer() {
	}

	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		log.debug("DynamicObject: " + dynamicObject);
		this.dynamicObject = (DynamicObject) dynamicObject;
		buildMainLayout();
		setSizeFull();
		setCompositionRoot(mainLayout);
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
		setCompositionRoot(mainLayout);
	}


	private VerticalLayout buildMainLayout() {
		
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSizeFull();
		Label label = new Label(dynamicObject.getClass().getName());
		mainLayout.addComponent(label);
		DynamicObjectFieldFactory factory = new DynamicObjectFieldFactory();
		factory.addFields(mainLayout, dynamicObject);
		
		return mainLayout;
	}

	@Override
	public void save() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
