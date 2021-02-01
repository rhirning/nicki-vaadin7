
package org.mgnl.nicki.user.admin.app;

/*-
 * #%L
 * nicki-user-admin
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


import javax.json.Json;
import javax.json.JsonObject;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SearchComponent extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button searchButton;
	@AutoGenerated
	private TextField nameField;
	private SearchDialog searchDialog;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public SearchComponent() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		searchButton.addClickListener(event -> {
				JsonObject query = getQuery();
				searchDialog.search(query);
		});

		// TODO add user code here
	}

	protected JsonObject getQuery() {
		JsonObject queryObject = Json.createObjectBuilder()
                .add("name", nameField.getValue()).build();
		return queryObject;
	}

	public void init(SearchDialog searchDialog) {
		this.searchDialog = searchDialog;
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("-1px");
		
		// nameField
		nameField = new TextField();
		nameField.setCaption("Name");
		nameField.setWidth("100.0%");
		nameField.setHeight("-1px");
		mainLayout.addComponent(nameField);
		
		// seachButton
		searchButton = new Button();
		searchButton.setCaption("Suche");
		searchButton.setWidth("-1px");
		searchButton.setHeight("-1px");
		mainLayout.addComponent(searchButton);
		
		return mainLayout;
	}

}
