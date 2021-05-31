
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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@SuppressWarnings("serial")
public class SearchComponent extends VerticalLayout {
	private Button searchButton;
	
	private TextField nameField;
	private SearchDialog searchDialog;


	public SearchComponent() {
		buildMainLayout();
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

	
	private void buildMainLayout() {
		setWidth("100%");
		setHeight("-1px");
		setMargin(false);
		
		// nameField
		nameField = new TextField();
		nameField.setLabel("Name");
		nameField.setWidth("100.0%");
		nameField.setHeight("-1px");
		add(nameField);
		
		// seachButton
		searchButton = new Button();
		searchButton.setText("Suche");
		searchButton.setWidth("-1px");
		searchButton.setHeight("-1px");
		add(searchButton);
	}

}
