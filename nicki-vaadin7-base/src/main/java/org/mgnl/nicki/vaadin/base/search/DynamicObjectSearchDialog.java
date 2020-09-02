
package org.mgnl.nicki.vaadin.base.search;

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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.data.Query;
import org.mgnl.nicki.core.data.SearchQueryHandler;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DataModel;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.SearchResultEntry;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectSearchDialog<T extends DynamicObject> extends CustomComponent {
	private VerticalLayout mainLayout;
	private Button searchButton;
	private Table table;
	private BeanItemContainer<T> container;
	private Button saveButton;
	private boolean create;
	private NickiContext context;
	private Class<T> clazz;
	private DynamicObjectSearcher<T> searcher;
	private Map<DynamicAttribute, String> searchDataMap = new HashMap<DynamicAttribute, String>();

	private String baseDn;

	
	public DynamicObjectSearchDialog(NickiContext context, Class<T> clazz, DynamicObjectSearcher<T> searcher, String baseDn) throws InstantiateDynamicObjectException {
		log.debug("CLass: " + clazz.getName());
		this.context = context;
		this.clazz = clazz;
		this.searcher = searcher;
		this.baseDn = baseDn;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}


	private AbstractLayout buildMainLayout() throws InstantiateDynamicObjectException {
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		AbstractLayout searchLayout = getLayout();
		mainLayout.addComponent(searchLayout);
		DynamicObjectSearchFieldFactory<T> factory = new DynamicObjectSearchFieldFactory<T>(context, searchDataMap);
		factory.addFields(searchLayout, clazz);
		
		searchButton = new Button(I18n.getText("nicki.editor.generic.button.search"));
		searchButton.addClickListener(event -> search());

		mainLayout.addComponent(searchButton);
		table = new Table();
		table.setSelectable(true);
		container = new BeanItemContainer<T>(clazz);
		table.setContainerDataSource(container);
		table.setVisibleColumns(getVisibleColumns());
		table.setColumnHeaders(getColumnHeaders());
		mainLayout.addComponent(table);
		saveButton = new Button(I18n.getText("nicki.editor.generic.button.save"));
		saveButton.addClickListener(event -> save());

		mainLayout.addComponent(saveButton);
		
		
		return mainLayout;
	}

	private GridLayout getLayout() {
		GridLayout layout = new GridLayout();
		layout.setColumns(2);
		layout.setWidth("100%");
		layout.setSpacing(true);
		return layout;
	}


	private String[] getColumnHeaders() {

		List<String> columnsHeaders = new ArrayList<String>();
		try {
			DataModel model = context.getDataModel(clazz);
			for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
				if (dynAttribute.isSearchable()) {
					columnsHeaders.add(I18n.getText(dynAttribute.getCaption(), dynAttribute.getName()));
				}
			}
		} catch (Exception e) {
			log.error("Error reading datamodel", e);
		}
		return columnsHeaders.toArray(new String[]{});
	}


	private Object[] getVisibleColumns() {

		List<String> columns = new ArrayList<String>();
		try {
			DataModel model = context.getDataModel(clazz);
			for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
				if (dynAttribute.isSearchable()) {
					columns.add(dynAttribute.getName());
				}
			}
		} catch (Exception e) {
			log.error("Error reading datamodel", e);
		}
		return columns.toArray(new Object[]{});
	}


	protected void search() {
		container.removeAllItems();
		for (SearchResultEntry entry : searchObjects()) {
			T resultEntry = context.loadObject(clazz, entry.getDn());
			container.addItem(resultEntry);
		}
	}
	


	public List<SearchResultEntry> searchObjects() {
		DataModel model;
		try {
			model = context.getDataModel(clazz);
		} catch (InstantiateDynamicObjectException e) {
			log.error("Error reading datamodel", e);
			return new ArrayList<SearchResultEntry>();
		}
		
		Query query = context.getQuery(baseDn);
		for (String objectClass : model.getObjectClasses()) {
			query.addSearchValue("objectclass", objectClass);
		}
		for (DynamicAttribute dynamicAttribute : searchDataMap.keySet()) {
			query.addSearchValue(dynamicAttribute.getExternalName(), searchDataMap.get(dynamicAttribute));
		}
		return getSearchResult(query);
	}


	private List<SearchResultEntry> getSearchResult(Query query) {
		try {
			SearchQueryHandler handler = context.getSearchHandler(query);
			context.search(handler);
			return handler.getResult();
		} catch (Exception e) {
			log.error("Error", e);
		}
		return new ArrayList<SearchResultEntry>();
	}

	public void save() {
		searcher.setDynamicObject(clazz, getSelected());
	}

	@SuppressWarnings("unchecked")
	private T getSelected() {
		return (T) table.getValue();
	}


	public boolean isCreate() {
		return create;
	}

}
