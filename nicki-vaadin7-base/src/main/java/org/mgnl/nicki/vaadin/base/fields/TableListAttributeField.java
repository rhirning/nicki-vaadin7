
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

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.components.EnterNameDialog;
import org.mgnl.nicki.vaadin.base.components.EnterNameHandler;
import org.mgnl.nicki.vaadin.base.components.VaadinHorizontalLayout;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class TableListAttributeField extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private DynamicObject dynamicObject;
	private String attributeName;
	private VaadinHorizontalLayout mainLayout;
	private Grid<String> entries;
	private Button newEntryButton;
	private Button deleteEntryButton;
	private Set<String> data = new TreeSet<String>();
	
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {

		this.dynamicObject = dynamicObject;
		this.attributeName = attributeName;
		
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
		newEntryButton.addClickListener(event -> addEntry());
		deleteEntryButton.addClickListener(event -> deleteEntry());
	}
	
	private void addItem(String value) {
		data.add(value);
		entries.setItems(data);
	}

	protected void deleteEntry() {
		if (entries.asSingleSelect().getValue() != null) {
			String valueToDelete = entries.asSingleSelect().getValue();
			data.remove(valueToDelete);
			entries.setItems(data);
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) dynamicObject.get(attributeName);
			if (values.contains(valueToDelete)) {
				values.remove(valueToDelete);
				dynamicObject.put(attributeName, values);
			}
		}
	}
	protected void addEntry() {
		EnterNameDialog dialog = new EnterNameDialog("nicki.editor.catalogs.entry.new",
				I18n.getText("nicki.editor.catalogs.entry.new.window.title"));
		dialog.setHandler(new NameHandler(""));
		dialog.setWidth("440px");
		dialog.setHeight("500px");
		dialog.setModal(true);
		dialog.open();
	}

	private class NameHandler extends EnterNameHandler {
		
		public NameHandler(String initialName) {
			super(initialName);
		}

		@Override
		public void setName(String name) throws Exception {
			addItem(name);
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) dynamicObject.get(attributeName);
			if (!values.contains(name)) {
				values.add(name);
				dynamicObject.put(attributeName, values);
			}
		}
	}

	
	public Component getComponent(boolean readOnly) {
		mainLayout.setReadOnly(readOnly);
		return mainLayout;
	}
	
	private HorizontalLayout buildMainLayout() {

		mainLayout = new VaadinHorizontalLayout();
		mainLayout.setHeight("160px");
		mainLayout.setSpacing(true);
		
		// top-level component properties
		mainLayout.setWidth("-1px");
		
		// entries
		entries = new Grid<String>();
		entries.setWidth("600px");
		entries.setHeight("100%");
		mainLayout.add(entries);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		mainLayout.add(verticalLayout);
		// newEntryButton
		newEntryButton = new Button();
		newEntryButton.setWidth("-1px");
		newEntryButton.setHeight("-1px");
		newEntryButton.setText("Neu");
		verticalLayout.add(newEntryButton);
		
		// deleteEntryButton
		deleteEntryButton = new Button();
		deleteEntryButton.setWidth("-1px");
		deleteEntryButton.setHeight("-1px");
		deleteEntryButton.setText("Löschen");
		verticalLayout.add(deleteEntryButton);
		return mainLayout;
	}
}
