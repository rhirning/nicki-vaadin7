
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


import java.util.Collection;

import org.mgnl.nicki.vaadin.base.components.NoHeaderTreeGrid;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.treegrid.ExpandEvent;
import com.vaadin.flow.component.treegrid.TreeGrid;

@SuppressWarnings("serial")
public class TreeSelector<T> extends BasicNickiSelector<T> implements NickiSelect<T> {
	private TreeGrid<T> component = new NoHeaderTreeGrid<>();

	public TreeSelector() {
		super();
		super.setComponent(component);
	}
	public void setSelectable(boolean selectable) {
		component.setSelectionMode(SelectionMode.SINGLE);
	}


	public void expandItems(@SuppressWarnings("unchecked") T... object) {
		component.expand(object);
	}

	@Override
	public void addExpandListener(ComponentEventListener<ExpandEvent<T, TreeGrid<T>>> listener) {
		component.addExpandListener(listener);
	}

	@Override
	public Collection<T> rootItemIds() {
		return component.getTreeData().getRootItems();
	}

//	@Override
@SuppressWarnings("unchecked")
	//	public void collapseItemsRecursively(TreeData startItemId) {
//		component.collapse(startItemId);
//	}
//
	@Override
	public void expandItemsRecursively(T object) {
		component.expand(object);
	}

}
