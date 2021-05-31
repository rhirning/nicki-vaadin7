
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


import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.ldap.helper.LdapHelper;
import org.mgnl.nicki.ldap.helper.LdapHelper.LOGIC;
import org.mgnl.nicki.vaadin.base.command.SelectPersonCommand;
import org.mgnl.nicki.vaadin.base.components.DataView;
import org.mgnl.nicki.vaadin.base.components.DialogBase;
import org.mgnl.nicki.vaadin.base.helper.ContainerHelper;
import org.mgnl.nicki.vaadin.base.helper.ValuePair;
import org.mgnl.nicki.vaadin.base.notification.Notification;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;

import lombok.extern.slf4j.Slf4j;


/**
 * TODO: Use internalExternal in query
 * TODO: Use activeInactive in query
 * TODO: Use department in query
 * TODO: Filter: HA des Users
 * 
 * @author rhirning
 *
 */
@Slf4j
@SuppressWarnings("serial")
public class PersonSelectorComponent extends VerticalLayout {
	
	private VerticalLayout resultLayout;
	
	private HorizontalLayout buttonLayout;
	
	private Button detailsButton;
	
	private Button selectButton;
	
	private Grid<Person> searchResult;
	
	private HorizontalLayout filterLayout;
	
	private Label errorLabel;
	
	private RadioButtonGroup<ACTIVE_INACTIVE> activeInactive;
	
	private VerticalLayout searchLayout;
	
	private Button searchButton;
	
	private VerticalLayout formLayout;
	
	private TextField department;
	
	private TextField company;
	
	private TextField givenName;
	
	private TextField name;
	
	private TextField userId;
	public enum WILDCARD {YES, NO};


	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */




	public static final String USER_BASE = "nicki.users.basedn";
	private static Object[] defaultVisibleCols = {
		Person.ATTRIBUTE_DISPLAYNAME,
	};
	private static String[] detailCols = {
		Person.ATTRIBUTE_DISPLAYNAME,
		Person.ATTRIBUTE_LOCATION,
	};

	public enum ACTIVE_INACTIVE {
		ACTIVE(I18n.getText(getI18nBase() + ".status.active")),
		INACTIVE(I18n.getText(getI18nBase() + ".status.inactive")),
		NOT_SET(I18n.getText(getI18nBase() + ".status.notset")),
		RESIGNED(I18n.getText(getI18nBase() + ".status.resigned"));
		
		private String text;
		
		public String getText() {
			return text;
		}

		ACTIVE_INACTIVE(String text) {
			this.text = text;
		}
	
	}

	private NickiContext context;
	private SelectPersonCommand command;
	private Person user;

	private boolean useActiveInactive;
	private boolean showSelect = true;
	private boolean showDetail = true;

	private String filter;
	private Object[] visibleCols;

	private DialogBase detailsWindow;

	public PersonSelectorComponent() {
		buildMainLayout();
		searchResult.setSelectionMode(SelectionMode.SINGLE);
		searchResult.addColumn(Person::getDisplayName).setHeader(I18n.getText(getI18nBase() + ".property.displayName"));
		initI18n();
	}

	public PersonSelectorComponent(Person user) {
		this.user = user;
		log.debug("User = " + this.user.getName());
		buildMainLayout();
		searchResult.setSelectionMode(SelectionMode.SINGLE);
		initI18n();
	}

	public void init(NickiContext ctx, boolean useInternExtern, boolean useActiveInactive, SelectPersonCommand selectCommand) {
		this.context = ctx;
		this.useActiveInactive = useActiveInactive;
		this.command = selectCommand;

		userId.focus();
		initActiveInactive();

		searchButton.addClickListener(event -> {
				if (!isComplete()) {
					searchResult.setItems();
					errorLabel.setText(I18n.getText(getI18nBase() + ".error.searchstring"));
					return;
				} else {
					errorLabel.setText("");
				}
				Collection<Person> result = searchAll();
				searchResult.setItems(result);
				if (result != null && result.size() > 0) {
					selectFirstItem(searchResult, result);
				} else {
					Notification.show(I18n.getText(getI18nBase() + ".error.emptyresult"),
							Notification.Type.HUMANIZED_MESSAGE);
				}
		});

		searchButton.addClickShortcut(Key.ENTER);

		if (isShowSelect()) {
			selectButton.addClickListener(event -> {
					if (getSelectedPerson() != null) {
						command.setSelectedPerson(getSelectedPerson());
					} else {
						Notification.show(I18n.getText(getI18nBase() + ".error.noperson"),
								Notification.Type.WARNING_MESSAGE);
					}
			});
		} else {
			buttonLayout.remove(selectButton);
		}
		if (isShowDetail()) {
			detailsButton.addClickListener(event -> {
					if (getSelectedPerson() != null) {
						showDetails(getSelectedPerson());
					} else {
						Notification.show(I18n.getText(getI18nBase() + ".error.noperson"),
								Notification.Type.WARNING_MESSAGE);
					}
			});
		} else {
			buttonLayout.remove(detailsButton);
		}

	}


	protected void selectFirstItem(Grid<Person> table, Collection<Person> list) {
		if (list != null && list.size() > 0) {
			table.select(list.iterator().next());
		}
	}

	private void showDetails(Person person) {
		Collection<ValuePair> container = ContainerHelper.getDataContainer(person, detailCols, getI18nBase() + ".property");


		DataView dataView = new DataView(container, I18n.getText(getI18nBase() + ".details.title"), detailCols.length,
				getI18nBase() + ".property");

		if (detailsWindow != null) {
			detailsWindow.close();
		}
		detailsWindow = new DialogBase(I18n.getText(getI18nBase()
				+ ".details.title"), dataView);
		detailsWindow.setWidth("900px");
		detailsWindow.setHeight("640px");
		detailsWindow.setModal(true);

		detailsWindow.open();
	}



	private void initActiveInactive() {
		if (useActiveInactive) {
			activeInactive.setItems(ACTIVE_INACTIVE.values());
			activeInactive.setRenderer(new TextRenderer<>(ACTIVE_INACTIVE::getText));
			activeInactive.setVisible(true);
		} else {
			activeInactive.setVisible(false);
		}

	}

	protected boolean isComplete() {
		if (validateText(userId.getValue(), 3)) {
			return true;
		}
		if (validateText(name.getValue(), 3)) {
			return true;
		}
		if (validateText(givenName.getValue(), 3)) {
			return true;
		}
		if (validateText(department.getValue(), 2)) {
			return true;
		}
		return false;
	}


	protected Collection<Person> searchAll() {
		String baseDn = Config.getString("nicki.users.basedn");

		StringBuilder filter = new StringBuilder();
		addQuery(filter, "cn", userId.getValue(), LdapHelper.LOGIC.AND, WILDCARD.YES);
		addQuery(filter, "surname", name.getValue(), LdapHelper.LOGIC.AND, WILDCARD.YES);
		addQuery(filter, "givenname", givenName.getValue(), LdapHelper.LOGIC.AND, WILDCARD.YES);
		addQuery(filter, "company", (String) company.getValue(), LdapHelper.LOGIC.AND, WILDCARD.NO);
		addQuery(filter, "ou", department.getValue(), LdapHelper.LOGIC.AND, WILDCARD.YES);

		if (getFilter() != null) {
			LdapHelper.addQuery(filter, getFilter(), LOGIC.AND);
		}

		return this.context.loadObjects(Person.class, baseDn, filter.toString());
	}

	private void addQuery(StringBuilder filter, String attribute, String searchString,
			LOGIC andOR, WILDCARD wildcard) {
		if (StringUtils.isNotBlank(searchString)) {
			LdapHelper.addQuery(filter, attribute + "=" +
					(wildcard==WILDCARD.YES?searchString + "*":StringUtils.replace(searchString,"*","")),
					andOR);
		}
	}

	public Person getSelectedPerson() {
		return searchResult.asSingleSelect().getValue();
	}

	private void initI18n() {
		userId.setLabel(I18n.getText(getI18nBase() + ".caption.userId"));
		name.setLabel(I18n.getText(getI18nBase() + ".caption.name"));
		givenName.setLabel(I18n.getText(getI18nBase() + ".caption.givenName"));
		company.setLabel(I18n.getText(getI18nBase() + ".caption.company"));
		department.setLabel(I18n.getText(getI18nBase() + ".caption.department"));
		searchButton.setText(I18n.getText(getI18nBase() + ".caption.search"));
		selectButton.setText(I18n.getText(getI18nBase() + ".caption.select"));
		detailsButton.setText(I18n.getText(getI18nBase() + ".caption.details"));
	}

	protected static String getI18nBase() {
		return "pnw.gui.personselector";
	}



	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Object[] getVisibleCols() {
		if (visibleCols != null) {
			return visibleCols;
		} else {
			return defaultVisibleCols;
		}
	}

	public void setVisibleCols(Object[] visibleCols) {
		this.visibleCols = visibleCols;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public boolean isShowSelect() {
		return showSelect;
	}

	public void setShowSelect(boolean showSelect) {
		this.showSelect = showSelect;
	}

	public boolean validateText(String value, int minLength) {
		String newValue = StringUtils.remove(value, "*");
		if (StringUtils.isNotBlank(newValue) && newValue.length() >= minLength) {
			return true;
		}
		return false;
	}

	
	private void buildMainLayout() {
		setWidth("100%");
		setHeight("-1px");
		setMargin(false);
		
		// filterLayout
		filterLayout = buildFilterLayout();
		add(filterLayout);
		
		// resultLayout
		resultLayout = buildResultLayout();
		add(resultLayout);
	}

	
	private HorizontalLayout buildFilterLayout() {
		// common part: create layout
		filterLayout = new HorizontalLayout();
		filterLayout.setWidth("-1px");
		filterLayout.setHeight("-1px");
		filterLayout.setMargin(true);
		filterLayout.setSpacing(true);
		
		// formLayout
		formLayout = buildFormLayout();
		filterLayout.add(formLayout);
		
		// searchLayout
		searchLayout = buildSearchLayout();
		filterLayout.add(searchLayout);
		
		// activeInactive
		activeInactive = new RadioButtonGroup<ACTIVE_INACTIVE>();
		filterLayout.add(activeInactive);
		
		// errorLabel
		errorLabel = new Label();
		errorLabel.getElement().getStyle().set("red", null);
		errorLabel.setWidth("100.0%");
		errorLabel.setHeight("180px");
		errorLabel.setText("Label");
		filterLayout.add(errorLabel);
		
		return filterLayout;
	}

	
	private VerticalLayout buildFormLayout() {
		// common part: create layout
		formLayout = new VerticalLayout();
		formLayout.setWidth("-1px");
		formLayout.setHeight("-1px");
		formLayout.setMargin(false);
		formLayout.setSpacing(true);
		
		// userId
		userId = new TextField();
		userId.setLabel("Benutzerkennung");
		userId.setWidth("-1px");
		userId.setHeight("-1px");
		formLayout.add(userId);
		
		// name
		name = new TextField();
		name.setLabel("Nachname");
		name.setWidth("-1px");
		name.setHeight("-1px");
		formLayout.add(name);
		
		// givenName
		givenName = new TextField();
		givenName.setLabel("Vorname");
		givenName.setWidth("-1px");
		givenName.setHeight("-1px");
		formLayout.add(givenName);
		
		// company
		company = new TextField();
		company.setLabel("Firma");
		company.setWidth("-1px");
		company.setHeight("-1px");
		formLayout.add(company);
		
		// department
		department = new TextField();
		department.setLabel("Abteilung");
		department.setWidth("-1px");
		department.setHeight("-1px");
		formLayout.add(department);
		
		return formLayout;
	}

	
	private VerticalLayout buildSearchLayout() {
		// common part: create layout
		searchLayout = new VerticalLayout();
		searchLayout.setWidth("-1px");
		searchLayout.setHeight("100.0%");
		searchLayout.setMargin(true);
		
		// searchButton
		searchButton = new Button();
		searchButton.setText("Suche");
		searchButton.setWidth("-1px");
		searchButton.setHeight("-1px");
		searchLayout.add(searchButton);
		
		return searchLayout;
	}

	
	private VerticalLayout buildResultLayout() {
		// common part: create layout
		resultLayout = new VerticalLayout();
		resultLayout.setWidth("100.0%");
		resultLayout.setHeight("100.0%");
		resultLayout.setMargin(true);
		resultLayout.setSpacing(true);
		
		// searchResult
		searchResult = new Grid<Person>();
		searchResult.setWidth("100.0%");
		searchResult.setHeight("320px");
		resultLayout.add(searchResult);
		resultLayout.setFlexGrow(1, searchResult);
		
		// buttonLayout
		buttonLayout = buildButtonLayout();
		resultLayout.add(buttonLayout);
		
		return resultLayout;
	}

	
	private HorizontalLayout buildButtonLayout() {
		// common part: create layout
		buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("-1px");
		buttonLayout.setHeight("-1px");
		buttonLayout.setMargin(false);
		
		// selectButton
		selectButton = new Button();
		selectButton.setText("Auswählen");
		selectButton.setWidth("-1px");
		selectButton.setHeight("-1px");
		buttonLayout.add(selectButton);
		
		// detailsButton
		detailsButton = new Button();
		detailsButton.setText("Details");
		detailsButton.setWidth("-1px");
		detailsButton.setHeight("-1px");
		buttonLayout.add(detailsButton);
		
		return buttonLayout;
	}

}
