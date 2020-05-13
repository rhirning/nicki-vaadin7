
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
import org.mgnl.nicki.vaadin.base.components.NewClassEditor;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class TreeDataViewer extends CustomComponent implements NewClassEditor, ClassEditor {

	private VerticalLayout mainLayout;
	private TreeData dynamicObject;
	private boolean create;
	private TreeData parent;

	public TreeDataViewer() {
	}

	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		log.debug("DynamicObject: " + dynamicObject);
		this.dynamicObject = dynamicObject;
		this.create = false;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}
	
	public void init(TreeData parent, Class<? extends TreeData> classDefinition) throws InstantiateDynamicObjectException {
		this.parent = parent;
		try {
			this.dynamicObject = this.parent.createChild(classDefinition, "");
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
		return mainLayout;
	}

	public boolean isCreate() {
		return create;
	}

	@Override
	public void save() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
