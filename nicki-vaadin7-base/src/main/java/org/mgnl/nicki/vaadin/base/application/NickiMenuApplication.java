package org.mgnl.nicki.vaadin.base.application;

/*-
 * #%L
 * nicki-vaadin7-base
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.MainView;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationEntry;
import org.mgnl.nicki.vaadin.base.navigation.Command;
import org.mgnl.nicki.vaadin.base.navigation.NavigationCommand;
import org.mgnl.nicki.vaadin.base.navigation.NavigationDialog;
import org.mgnl.nicki.vaadin.base.navigation.NavigationHelper;

import com.vaadin.flow.component.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@SuppressWarnings("serial")
@Slf4j
public abstract class NickiMenuApplication extends NickiApplication implements Serializable {

	private MainView mainView;
	private NavigationCommand command;
	private Component navigationView;
	private String configPath;
	private @Setter boolean contentHeightFull;

	public NickiMenuApplication(String configPath) {
		super();
		this.configPath = configPath;
	}
	

	@Override
	public Component getEditor() {
		
		try {
		
			mainView = new MainView((Person) getNickiContext().getUser(), configPath);
			if (contentHeightFull) {
				mainView.getContentLayout().setHeightFull();
			}
			mainView.addNavigation(this);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			log.error("Error in menu config: " + configPath, e);
		}

		

		mainView.initNavigation();
		
		/* TODO Navigate in App
		String viewParameter = getUI().getUriFragment();
		
		if (StringUtils.isNotBlank(viewParameter)) {
			viewParameter = DataHelper.getPassword(viewParameter);
			List<String> viewParameters = DataHelper.getList(viewParameter, ";");
			log.debug("viewParameter: " + viewParameters);
			command = getNavigationCommand(viewParameters);
			if (command != null) {
				log.debug("Command: " + command);
				navigate(command);
			}
		}
		*/
		
		return mainView;
	}
	
	@Command(name="showView", parameter= {String.class})
	public void showView(String navigationKey) {
		navigationKey = DataHelper.getPassword(navigationKey);
		log.debug("Ich soll jetzt anzeigen: " + navigationKey);
		NavigationEntry entry = mainView.selectMenuItem(navigationKey);
		if (entry != null) {
			navigationView = entry.getView();
		}
	}

	private NavigationCommand getNavigationCommand(List<String> viewParameters) {
		log.debug("viewParameters=" + viewParameters);
		if (viewParameters != null && viewParameters.size() > 0) {
			NavigationCommand command = new NavigationCommand();
			for (String viewParameter : viewParameters) {
				if (viewParameter.contains(":")) {
					String comm = StringUtils.substringBefore(viewParameter, ":");
					Object[] params = StringUtils.split(StringUtils.substringAfter(viewParameter, ":"), ":");
					command.add(comm, params);
				} else {
					command.add(viewParameter);
				}
			}
			return command;
		} else {
			return null;
		}
			
	}

	public void navigate(NavigationCommand command) {
		this.command = command;
		if (command.hasCommands()) {
			NavigationHelper.executeCommands(this, command);
		}
		if (navigationView != null && NavigationDialog.class.isAssignableFrom(navigationView.getClass()) && this.command != null && this.command.hasCommands()) {
			log.debug("Weiterleitung mit: " + command);
			((NavigationDialog)navigationView).navigate(command);
		}
	}

}
