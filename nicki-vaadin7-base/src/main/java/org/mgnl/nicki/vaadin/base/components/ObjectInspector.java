
package org.mgnl.nicki.vaadin.base.components;

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


import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.vaadin.base.data.ObjectWrapper;
import org.mgnl.nicki.vaadin.base.editor.NickiObjectDataProvider;
import org.mgnl.nicki.vaadin.base.editor.ObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ObjectInspector extends CustomComponent {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private TreeGrid<ObjectWrapper> tree;
	
	private static final long serialVersionUID = 3256191025469832300L;
	public ObjectInspector(Object... objects) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		initTree(new ObjectRoot(objects, new ShowAllFilter()));

	}


	public static void inspect(Object... objects) {
		ObjectInspector view = new ObjectInspector(objects);
		Window window = new Window("Inspector", view);
		window.setHeight("600px");
		window.setWidth("800px");
		window.setModal(true);
		UI.getCurrent().addWindow(window);
	}

	private void initTree(DataProvider<ObjectWrapper> treeDataProvider) {
		tree.setDataProvider(new NickiObjectDataProvider(null, treeDataProvider));
		tree.addColumn(ObjectWrapper::getName).setWidth(200);
		tree.addColumn(ObjectWrapper::getType).setWidth(120);
		tree.addColumn(ObjectWrapper::getValue);
		
/*		
		tree.addExpandListener(event -> {
				ObjectWrapper objectWrapper = (ObjectWrapper) event.getItemId();
				if (hasChildren(objectWrapper) && !container.hasChildren(objectWrapper)) {
					addChildren(objectWrapper);
				}
		});
		*/
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// tree
		tree = new TreeGrid<>();
		tree.setWidth("100.0%");
		tree.setHeight("100.0%");
		mainLayout.addComponent(tree);
		
		return mainLayout;
	}
	
	


}
