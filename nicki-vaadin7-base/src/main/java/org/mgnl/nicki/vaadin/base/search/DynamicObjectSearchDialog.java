
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
import org.mgnl.nicki.vaadin.base.data.DynamicObjectGridColumn;
import org.mgnl.nicki.vaadin.base.helper.GridHelper;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectSearchDialog<T extends DynamicObject> extends VerticalLayout {
	private Button searchButton;
	private Grid<T> table;
	private List<T> container = new ArrayList<>();
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
	}


	private void buildMainLayout() throws InstantiateDynamicObjectException {
		setWidth("100%");
		setMargin(true);
		FormLayout searchLayout = getLayout();
		add(searchLayout);
		DynamicObjectSearchFieldFactory<T> factory = new DynamicObjectSearchFieldFactory<T>(context, searchDataMap);
		factory.addFields(searchLayout, clazz);
		
		searchButton = new Button(I18n.getText("nicki.editor.generic.button.search"));
		searchButton.addClickListener(event -> search());

		add(searchButton);
		table = new Grid<>();
		table.setSelectionMode(SelectionMode.SINGLE);
		container = new ArrayList<>();
		for (DynamicObjectGridColumn<T> column : GridHelper.getColumns(context, clazz)) {
			table.addColumn(column).setHeader(column.getCaption());
		}
		add(table);
		saveButton = new Button(I18n.getText("nicki.editor.generic.button.save"));
		saveButton.addClickListener(event -> save());

		add(saveButton);
		
		
	}

	private FormLayout getLayout() {
		FormLayout layout = new FormLayout();
		layout.setWidth("100%");
		return layout;
	}

	protected void search() {
		container.clear();
		for (SearchResultEntry entry : searchObjects()) {
			T resultEntry = context.loadObject(clazz, entry.getDn());
			container.add(resultEntry);
		}
		table.setItems(container);
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

	private T getSelected() {
		return table.asSingleSelect().getValue();
	}


	public boolean isCreate() {
		return create;
	}

}
