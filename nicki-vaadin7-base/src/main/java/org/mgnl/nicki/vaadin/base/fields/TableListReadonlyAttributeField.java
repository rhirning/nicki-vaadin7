
package org.mgnl.nicki.vaadin.base.fields;

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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.components.VaadinHorizontalLayout;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TableListReadonlyAttributeField extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private VaadinHorizontalLayout mainLayout;
	private Grid<String> entries;
	private Set<String> data = new TreeSet<String>();
	private String attributeName;
	private DynamicObject dynamicObject;
	
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {
		this.attributeName =attributeName;
		this.dynamicObject = dynamicObject;
		buildMainLayout();
		//entries.setCaption(getName(dynamicObject, attributeName));
		entries.setSelectionMode(SelectionMode.SINGLE);
		entries.addColumn(String::toString);

		
		@SuppressWarnings("unchecked")
		List<Object> values = (List<Object>) dynamicObject.get(attributeName);
		if (values != null) {
			for (Object valueObject : values) {
				String value = (String) valueObject;
				addItem(value);
			}
		}
	}
	
	private void addItem(String value) {
		data.add(value);
		entries.setItems(data);
	}
	
	public ComponentContainer getComponent(boolean readOnly) {
		return mainLayout;
	}
	
	private HorizontalLayout buildMainLayout() {

		mainLayout = new VaadinHorizontalLayout();
		mainLayout.setHeight("160px");
		mainLayout.setSpacing(true);
		
		// top-level component properties
		mainLayout.setWidth("-1px");
		
		// entries
		entries = new Grid<String>(getName(dynamicObject, attributeName));
		entries.setWidth("600px");
		entries.setHeight("100%");
		mainLayout.addComponent(entries);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		mainLayout.addComponent(verticalLayout);
		return mainLayout;
	}
}
