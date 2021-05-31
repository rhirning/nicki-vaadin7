
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


import java.io.Serializable;
import java.util.Collection;
import org.mgnl.nicki.core.data.TreeData;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.ExpandEvent;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;

public interface NickiSelect<T> extends Serializable {

	void setHeight(String height);

	void setWidth(String width);

	Grid<T> getComponent();

	void setSelectable(boolean b);

	T getValue();

	void addSelectionListener(SelectionListener<Grid<T>, T> listener);

	void removeItem(T target);

	void unselect(T object);

	void setItems(Collection<T>items);

	void setCaption(ValueProvider<T, String> valueProvider);

	// TODO: setIcon
//	void setIcon(ValueProvider<T, ThemeResource> valueProvider);

	Collection<T> rootItemIds();

	void expandItemsRecursively(T id);

	void collapseItemsRecursively(TreeData startItemId);

	void setComponent(Grid<T> component);

	void addExpandListener(ComponentEventListener<ExpandEvent<T, TreeGrid<T>>> listener);

	void expandItems(@SuppressWarnings("unchecked") T... object);


}
