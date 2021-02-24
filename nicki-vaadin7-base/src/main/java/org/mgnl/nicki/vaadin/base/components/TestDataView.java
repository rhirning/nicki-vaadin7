
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



import java.io.Serializable;
import java.util.Map;

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.listener.TestDataValueChangeListener;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TestDataView extends CustomComponent {

	@AutoGenerated
	private VerticalLayout verticalLayout;

	@AutoGenerated
	private Button newButton;

	@AutoGenerated
	private VerticalLayout testData;

	public static final String SEPARATOR = "=";

	private DataContainer<Map<String, String>> data;
	private ValueChangeListener<String> listener;
	private EnterNameDialog newFieldWindow;
	private String messageKeyBase;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public TestDataView(DataContainer<Map<String, String>> dataContainer, String messageKeyBase) {
		this.data = dataContainer;
		this.messageKeyBase = messageKeyBase + ".testdata";
		buildMainLayout();
		setCompositionRoot(verticalLayout);
		listener = new TestDataValueChangeListener(dataContainer, testData);
		for (String name : data.getValue().keySet()) {
			addField(name, data.getValue().get(name));
		}
		newButton.addClickListener(event -> addNewField());
	}
	
	private void addField(String name, String value) {
		TextField field = new TextField(name, value);
		field.addValueChangeListener(listener);
		field.setWidth("100%");
		testData.addComponent(field);

	}

	protected void addNewField() {
		EnterNameDialog newFieldWindow = new EnterNameDialog(messageKeyBase,
				I18n.getText(messageKeyBase + ".window.title"));
		newFieldWindow.setHandler(new NewFieldHandler(""));
		newFieldWindow.setPositionX(300);
		newFieldWindow.setPositionY(100);
		newFieldWindow.setWidth(440, Unit.PIXELS);
		newFieldWindow.setHeight(500, Unit.PIXELS);
		newFieldWindow.setModal(true);
		UI.getCurrent().addWindow(newFieldWindow);
	}
	
	public class NewFieldHandler extends EnterNameHandler implements Serializable {

		public NewFieldHandler(String initialName) {
			super(initialName);
		}

		public void setName(String name) {
			addField(name, "");
		}

		public void closeEnterNameDialog() {
			UI.getCurrent().removeWindow(newFieldWindow);
		}

	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		setSizeFull();
		// common part: create layout
		verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100.0%");
		verticalLayout.setHeight("-1px");
		verticalLayout.setMargin(false);
		
		// testData
		testData = new VerticalLayout();
		testData.setWidth("100.0%");
		testData.setHeight("-1px");
		testData.setMargin(false);
		verticalLayout.addComponent(testData);
		
		// newButton
		newButton = new Button();
		newButton.setWidth("-1px");
		newButton.setHeight("-1px");
		newButton.setCaption("Neu");
		verticalLayout.addComponent(newButton);
		
		return verticalLayout;
	}

}
