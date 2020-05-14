
package org.mgnl.nicki.shop.rules;

/*-
 * #%L
 * nicki-shop
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
import java.util.List;

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.objects.Selector;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.fields.BaseDynamicAttributeField;
import org.mgnl.nicki.vaadin.base.fields.DynamicAttributeField;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class RuleAttributeField extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private CatalogArticle article;
	private String attributeName;
	private HorizontalLayout mainLayout;
	private Table entries;
	private Button newEntryButton;
	private Button deleteEntryButton;
	private Button testButton;
	
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {

		this.article = (CatalogArticle) dynamicObject;
		this.attributeName = attributeName;
		
		buildMainLayout();
		entries.setSelectable(true);
		entries.addContainerProperty(attributeName, String.class, null);

		
		@SuppressWarnings("unchecked")
		List<Object> values = (List<Object>) dynamicObject.get(attributeName);
		for (Object valueObject : values) {
			String value = (String) valueObject;
			addItem(value);
		}
		newEntryButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addEntry(entries);
			}
		});
		deleteEntryButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				deleteEntry(entries);
			}
		});
		testButton.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				showTest();
			}
		});
	}
	
	protected void showTest() {
		
		Collection<Person> persons = RuleManager.getUsers(article, null);
		Table table = new Table();
		table.setWidth(440, Unit.PIXELS);
		table.setHeight(500, Unit.PIXELS);
		table.addContainerProperty(I18n.getText("nicki.editor.catalogs.rule.test.column.title"), String.class,  null);
		table.setVisibleColumns(new Object[] { I18n.getText("nicki.editor.catalogs.rule.test.column.title") });
		table.setColumnHeaders(new String[] { I18n.getText("nicki.editor.catalogs.rule.test.column.title") });
		for (Person person : persons) {
			table.addItem(new String[] {person.getDisplayName()}, person.getDisplayName());
		}

		Window newWindow = new Window(I18n.getText("nicki.editor.catalogs.rule.test.window.title"));
        VerticalLayout layout = new VerticalLayout();
        newWindow.setContent(layout);
        layout.setMargin(true);
        layout.setSpacing(true);

		layout.addComponent(table);
		newWindow.setWidth(500, Unit.PIXELS);
		newWindow.setHeight(620, Unit.PIXELS);
		newWindow.setModal(true);
		UI.getCurrent().addWindow(newWindow);
	}

	private void addItem(String value) {
		entries.addItem(new Object[] {value}, value);
	}

	protected void deleteEntry(Table table) {
		if (table.getValue() != null) {
			String valueToDelete = (String) table.getValue();
			table.removeItem(valueToDelete);
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) article.get(attributeName);
			if (values.contains(valueToDelete)) {
				values.remove(valueToDelete);
				article.put(attributeName, values);
			}
		}
	}
	protected void addEntry(Table table) {
		RuleEditor editor = new RuleEditor(article, "nicki.editor.catalogs.rule.new",
				I18n.getText("nicki.editor.catalogs.rule.new.window.title"));
		editor.setHandler(new RuleHandler());
		editor.setWidth(440, Unit.PIXELS);
		editor.setHeight(500, Unit.PIXELS);
		editor.setModal(true);
		UI.getCurrent().addWindow(editor);
	}

	public class RuleHandler {
		
		public void setRule(Selector selector, String value) {
			String entry = selector.getName() + "=" + value;
			addItem(entry);
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) article.get(attributeName);
			if (!values.contains(entry)) {
				values.add(entry);
				article.put(attributeName, values);
			}
		}
	}

	
	public ComponentContainer getComponent(boolean readOnly) {
		mainLayout.setReadOnly(readOnly);
		return mainLayout;
	}
	
	private HorizontalLayout buildMainLayout() {
		mainLayout = new HorizontalLayout();
		mainLayout.setHeight("160px");
		mainLayout.setSpacing(true);
		
		// top-level component properties
		mainLayout.setWidth("-1px");
		
		// entries
		entries = new Table();
		entries.setWidth("600px");
		entries.setHeight("100%");
		entries.setImmediate(true);
		mainLayout.addComponent(entries);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		mainLayout.addComponent(verticalLayout);
		
		// newEntryButton
		newEntryButton = new Button();
		newEntryButton.setWidth("-1px");
		newEntryButton.setHeight("-1px");
		newEntryButton.setCaption("Neu");
		newEntryButton.setImmediate(false);
		verticalLayout.addComponent(newEntryButton);
		
		// deleteEntryButton
		deleteEntryButton = new Button();
		deleteEntryButton.setWidth("-1px");
		deleteEntryButton.setHeight("-1px");
		deleteEntryButton.setCaption("Löschen");
		deleteEntryButton.setImmediate(false);
		verticalLayout.addComponent(deleteEntryButton);
		
		// testButton
		testButton = new Button();
		testButton.setWidth("-1px");
		testButton.setHeight("-1px");
		testButton.setCaption("Test");
		testButton.setImmediate(false);
		verticalLayout.addComponent(testButton);
		
		return mainLayout;
	}
}
