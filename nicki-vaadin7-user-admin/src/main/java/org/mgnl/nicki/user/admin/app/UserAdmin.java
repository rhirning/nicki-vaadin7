
package org.mgnl.nicki.user.admin.app;

import java.lang.reflect.InvocationTargetException;

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



import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;
import org.mgnl.nicki.vaadin.base.menu.application.MainView;

import com.vaadin.flow.component.Component;

@AccessGroup(name = {"nickiAdmins", "IDM-Development"})
@SuppressWarnings("serial")
@ShowWelcomeDialog(
		configKey="nicki.app.admin.user.useWelcomeDialog",
		groupsConfigName="nicki.app.admin.user.useWelcomeDialogGroups")
public class UserAdmin extends NickiApplication {
	
	public UserAdmin() {
		super();
	}

	@Override
	public Component getEditor() {
		MainView mainView;
		try {
			mainView = new MainView((Person) getNickiContext().getUser(), "");
			mainView.addNavigationEntry(I18n.getText("Administration"),
					I18n.getText("User"), new UserView(), "userAdmin");
	
			mainView.initNavigation();
			
			return mainView;
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.script";
	}
	
}
