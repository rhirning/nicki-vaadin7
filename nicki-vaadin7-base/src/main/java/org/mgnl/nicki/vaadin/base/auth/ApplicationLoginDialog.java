
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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class ApplicationLoginDialog extends FlexLayout implements LoginDialog {
	
	private static int MAX_COUNT = 3;

	private TextField userName;
	private PasswordField password;
	private NickiApplication application;
	private int count = 0;

	public ApplicationLoginDialog() {
		buildUI();
	}

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // layout to center login form when there is sufficient screen space
        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(getLoginForm());

        add(centeringLayout);
    }
    
    protected Component getLoginForm() {
    	FormLayout loginLayout = new FormLayout();
    	loginLayout.setSizeUndefined();
    	loginLayout.setHeight("-1px");
//    	H2 title = new H2("Log in");
    	userName = new TextField("User");
    	userName.focus();
    	password = new PasswordField("Passwort");
    	Button loginButton = new Button("Log in");
    	loginButton.getElement().getClassList().add("loginButton");
    	loginButton.setWidth("100%");
    	loginButton.addClickListener(e -> login());
    	loginButton.addClickShortcut(Key.ENTER);
    	loginLayout.add(userName, password, loginButton);
    	return loginLayout;
    }

    private void login() {
		count++;
		if (count > MAX_COUNT) {
			Notification.show(I18n.getText("nicki.application.error.too.many.attempts"), Type.HUMANIZED_MESSAGE);
			return;
		}
		if (getApplication().login(StringUtils.stripToEmpty(userName.getValue()), StringUtils.stripToEmpty(password.getValue()))) {
			try {
				getApplication().start();
			} catch (DynamicObjectException e) {
				log.error("Error", e);
			}
		} else {
		}

	}
	
	public NickiApplication getApplication() {
		return application;
	}

	public void setApplication(NickiApplication application) {
		this.application = application;
	}

}
