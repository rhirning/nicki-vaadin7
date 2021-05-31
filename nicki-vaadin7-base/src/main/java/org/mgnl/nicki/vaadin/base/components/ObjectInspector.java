
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

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;

public class ObjectInspector extends VerticalLayout {
	
	private TreeGrid<ObjectWrapper> tree;
	
	private static final long serialVersionUID = 3256191025469832300L;
	public ObjectInspector(Object... objects) {
		buildMainLayout();

		initTree(new ObjectRoot(objects, new ShowAllFilter()));

	}


	public static void inspect(Object... objects) {
		ObjectInspector view = new ObjectInspector(objects);
		DialogBase window = new DialogBase("Inspector", view);
		window.setHeight("600px");
		window.setWidth("800px");
		window.setModal(true);
		window.open();
	}

	private void initTree(DataProvider<ObjectWrapper> treeDataProvider) {
		tree.setDataProvider(new NickiObjectDataProvider(null, treeDataProvider));
		tree.addColumn(ObjectWrapper::getName).setWidth("200px");
		tree.addColumn(ObjectWrapper::getType).setWidth("120px");
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

	
	private void buildMainLayout() {
		// common part: create layout
		setWidth("100%");
		setHeight("100%");
		setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// tree
		tree = new TreeGrid<>();
		tree.setWidth("100.0%");
		tree.setHeight("100.0%");
		add(tree);
	}
}
