
package org.mgnl.nicki.vaadin.base.auth;

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



import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class SimpleApplicationLoginDialog extends VerticalLayout implements LoginDialog {
	
	
	private HorizontalLayout horizontalLayout_3;
	
	private Button buttonLogin;
	
	private Label label_1;
	
	private HorizontalLayout horizontalLayout_2;
	
	private PasswordField password;
	
	private Label labelPassword;
	
	private HorizontalLayout horizontalLayout_1;
	
	private TextField username;
	
	private Label labelUsername;

	private static int MAX_COUNT = 3;

	private NickiApplication application;
	private int count = 0;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	public SimpleApplicationLoginDialog() {
		buildMainLayout();

		buttonLogin.addClickListener(event -> {
				count++;
				if (count > MAX_COUNT) {
					Notification.show(I18n.getText("nicki.application.error.too.many.attempts"), Type.HUMANIZED_MESSAGE);
					return;
				}
				if (getApplication().login(StringUtils.stripToEmpty(getUsername()), StringUtils.stripToEmpty(getPassword()))) {
					try {
						getApplication().start();
					} catch (DynamicObjectException e) {
						log.error("Error", e);
					}
				} else {
					Notification.show(I18n.getText("nicki.application.error.invalid.credentials"), Type.HUMANIZED_MESSAGE);
				}
			}
		);
		
		buttonLogin.addClickShortcut(Key.ENTER);
		username.focus();
	}

	public String getPassword() {
		return (String) password.getValue();
	}

	public String getUsername() {
		return (String) username.getValue();
	}

	
	private void buildMainLayout() {
		setWidth("100%");
		setHeight("-1px");
		setMargin(true);
		setSpacing(true);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		add(horizontalLayout_1);
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		add(horizontalLayout_2);
		
		// horizontalLayout_3
		horizontalLayout_3 = buildHorizontalLayout_3();
		add(horizontalLayout_3);
	}

	
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// labelUsername
		labelUsername = new Label();
		labelUsername.setWidth("200px");
		labelUsername.setHeight("-1px");
		labelUsername.setText("Username");
		horizontalLayout_1.add(labelUsername);
		
		// username
		username = new TextField();
		username.setWidth("100.0%");
		username.setHeight("24px");
		horizontalLayout_1.add(username);
		horizontalLayout_1.setFlexGrow(1, username);
		
		return horizontalLayout_1;
	}

	
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setWidth("100.0%");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		horizontalLayout_2.setSpacing(true);
		
		// labelPassword
		labelPassword = new Label();
		labelPassword.setWidth("200px");
		labelPassword.setHeight("-1px");
		labelPassword.setText("Passwort");
		horizontalLayout_2.add(labelPassword);
		
		// password
		password = new PasswordField();
		password.setWidth("100.0%");
		password.setHeight("-1px");
		horizontalLayout_2.add(password);
		horizontalLayout_2.setFlexGrow(1, password);
		
		return horizontalLayout_2;
	}

	
	private HorizontalLayout buildHorizontalLayout_3() {
		// common part: create layout
		horizontalLayout_3 = new HorizontalLayout();
		horizontalLayout_3.setWidth("-1px");
		horizontalLayout_3.setHeight("-1px");
		horizontalLayout_3.setMargin(false);
		horizontalLayout_3.setSpacing(true);
		
		// label_1
		label_1 = new Label();
		label_1.setWidth("200px");
		label_1.setHeight("-1px");
		horizontalLayout_3.add(label_1);
		
		// buttonLogin
		buttonLogin = new Button();
		buttonLogin.setText("Login");
		buttonLogin.setWidth("-1px");
		buttonLogin.setHeight("-1px");
		horizontalLayout_3.add(buttonLogin);
		
		return horizontalLayout_3;
	}

	public NickiApplication getApplication() {
		return application;
	}

	public void setApplication(NickiApplication application) {
		this.application = application;
	}

}