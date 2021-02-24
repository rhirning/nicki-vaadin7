
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


import org.mgnl.nicki.core.i18n.I18n;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class EnterNameDialog extends DialogBase {

	@AutoGenerated
	private AbsoluteLayout mainLayout;

	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;

	@AutoGenerated
	private Button closeButton;

	@AutoGenerated
	private Button createButton;

	@AutoGenerated
	private TextField name;

	@AutoGenerated
	private Label headline;
	
	private String i18nBase;

	EnterNameHandler handler;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	public EnterNameDialog(String messageBase, String title) {
		super(title);
		i18nBase = messageBase;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		name.focus();
		applyI18n(messageBase);
		
		createButton.addClickListener(event -> {
				try {
					getHandler().setName((String) name.getValue());
					close();
				} catch (Exception e) {
					Notification.show(I18n.getText(i18nBase + ".error"),
							e.getMessage(), Notification.Type.ERROR_MESSAGE);
				}
			}
		);
		
		closeButton.addClickListener(event -> close());
		
		createButton.setClickShortcut(KeyCode.ENTER);
	}

	private void applyI18n(String messageBase) {
		headline.setValue(I18n.getText(messageBase + ".headline"));
		createButton.setCaption(I18n.getText(messageBase + ".button.create"));
		closeButton.setCaption(I18n.getText(messageBase + ".button.close"));
	}
		
	public EnterNameHandler getHandler() {
		return handler;
	}
	
	public void setName(String name) {
		this.name.setValue(name);
	}

	public void setHandler(EnterNameHandler handler) {
		this.handler = handler;
		handler.setDialog(this);
		this.name.setValue(handler.getName());
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// headline
		headline = new Label();
		headline.setWidth("400px");
		headline.setHeight("-1px");
		headline.setValue("Headline");
		mainLayout.addComponent(headline, "top:20.0px;left:20.0px;");
		
		// name
		name = new TextField();
		name.setWidth("200px");
		name.setHeight("-1px");
		mainLayout.addComponent(name, "top:60.0px;left:20.0px;");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1, "top:100.0px;left:20.0px;");
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		
		// createButton
		createButton = new Button();
		createButton.setWidth("-1px");
		createButton.setHeight("-1px");
		createButton.setCaption("Create");
		horizontalLayout_1.addComponent(createButton);
		
		// closeButton
		closeButton = new Button();
		closeButton.setWidth("-1px");
		closeButton.setHeight("-1px");
		closeButton.setCaption("Close");
		horizontalLayout_1.addComponent(closeButton);
		
		return horizontalLayout_1;
	}
}
