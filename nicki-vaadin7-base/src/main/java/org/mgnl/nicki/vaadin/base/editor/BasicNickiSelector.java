
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

import org.mgnl.nicki.core.data.TreeData;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public abstract class BasicNickiSelector<T> implements NickiSelect<T> {
	private Grid<T> component;

	@Override
	public void setHeight(String height) {
		component.setHeight(height);
	}

	@Override
	public void setWidth(String width) {
		component.setWidth(width);
	}

	@Override
	public Grid<T> getComponent() {
		return component;
	}

	@Override
	public T getValue() {
		return component.asSingleSelect().getValue();
	}

	@Override
	public void addSelectionListener(SelectionListener<Grid<T>, T> listener) {
		component.addSelectionListener(listener);
	}

	@Override
	public void unselect(T object) {
		component.deselect(object);
	}

	@Override
	public Collection<T> rootItemIds() {
		log.debug("not implemented");
		return null;
	}

	@Override
	public void expandItemsRecursively(T id) {
		log.debug("not implemented");
	}

	@Override
	public void collapseItemsRecursively(TreeData startItemId) {
		log.debug("not implemented");
	}

	@Override
	public void setSelectable(boolean b) {
		if (b) {
			component.setSelectionMode(SelectionMode.SINGLE);
		} else {
			component.setSelectionMode(SelectionMode.NONE);
		}
	}

	@Override
	public void removeItem(T target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setItems(Collection<T> items) {
		component.setItems(items);
	}

	@Override
	public void setCaption(ValueProvider<T, String> valueProvider) {
		component.addColumn(valueProvider);
	}

	@Override
	public void setComponent(Grid<T> component) {
		this.component = component;
	}



}
