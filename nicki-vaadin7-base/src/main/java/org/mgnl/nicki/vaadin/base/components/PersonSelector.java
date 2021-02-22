
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


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.Query;
import org.mgnl.nicki.core.data.SearchQueryHandler;
import org.mgnl.nicki.core.objects.SearchResultEntry;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.helper.UIHelper;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.TextField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class PersonSelector extends CustomComponent {
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Button closeButton;
	@AutoGenerated
	private Button selectButton;
	@AutoGenerated
	private Grid<Person> searchResult;
	@AutoGenerated
	private Button searchButton;
	@AutoGenerated
	private TextField filter;
	public static final String USER_BASE = "nicki.users.basedn";
	public static final String USER_BASE_DEFAULT = "ou=users,o=utopia";

	PersonSelectHandler personSelectHandler;
	
	private NickiContext context;

	public PersonSelector() {
		buildMainLayout();
		filter.focus();
		setCompositionRoot(mainLayout);
		searchResult.addColumn(Person::getName);

		closeButton.addClickListener(event -> personSelectHandler.closePersonSelector());

		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(event -> {
				searchResult.setItems(getPersons(searchPerson((String) event.getValue())));
		});
		
		searchButton.addClickListener(event -> {
			searchResult.setItems(getPersons(searchPerson((String) filter.getValue())));
		});

		selectButton.addClickListener(event -> select());
		
	}
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public void init(NickiContext ctx, PersonSelectHandler handler) {
		this.context = ctx;
		this.personSelectHandler = handler;
	}

	private void select() {
		Person entry = searchResult.asSingleSelect().getValue();
		if (entry != null) {
			this.personSelectHandler.setSelectedPerson(entry);
			this.personSelectHandler.closePersonSelector();
		}
	}

	private List<Person> getPersons(List<SearchResultEntry> resultSet) {
		List<Person> persons = new ArrayList<Person>();
	    for (SearchResultEntry entry : resultSet) {
	    	Person person = context.loadObject(Person.class, entry.getDn());
			persons.add(person);
		}
		return persons;
	}

	public List<SearchResultEntry> searchPerson(String searchString) {
		if (StringUtils.isEmpty(searchString)) {
			searchString = "EMPTY";
			searchResult.setSelectionMode(SelectionMode.NONE);
		} else {
			searchResult.setSelectionMode(SelectionMode.SINGLE);
		}
		Query query = context.getQuery(Config.getString(USER_BASE, USER_BASE_DEFAULT));
		query.addResultAttribute("fullName","fullName");
		query.addResultAttribute("cn","userId");
		query.addSearchValue("objectclass", "Person");
		query.addSearchValue("fullName", searchString + "*");
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


	
	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// filter
		filter = new TextField();
		filter.setWidth("-1px");
		filter.setHeight("-1px");
		mainLayout.addComponent(filter, "top:20.0px;left:20.0px;");
		
		// searchButton
		searchButton = new Button();
		searchButton.setWidth("-1px");
		searchButton.setHeight("-1px");
		searchButton.setCaption("Suche");
		UIHelper.setImmediate(searchButton, true);
		mainLayout.addComponent(searchButton, "top:18.0px;left:180.0px;");
		
		// searchResult
		searchResult = new Grid<>();
		searchResult.setWidth("400px");
		searchResult.setHeight("-1px");
		mainLayout.addComponent(searchResult, "top:60.0px;left:20.0px;");
		
		// selectButton
		selectButton = new Button();
		selectButton.setWidth("-1px");
		selectButton.setHeight("-1px");
		selectButton.setCaption("Ausw�hlen");
		UIHelper.setImmediate(selectButton, true);
		mainLayout.addComponent(selectButton, "top:410.0px;left:20.0px;");
		
		// closeButton
		closeButton = new Button();
		closeButton.setWidth("-1px");
		closeButton.setHeight("-1px");
		closeButton.setCaption("Schliessen");
		UIHelper.setImmediate(closeButton, true);
		mainLayout.addComponent(closeButton, "top:410.0px;left:140.0px;");
		
		return mainLayout;
	}

}
