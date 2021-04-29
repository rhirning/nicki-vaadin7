package org.mgnl.nicki.vaadin.base.application;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.MainView;
import org.mgnl.nicki.vaadin.base.menu.application.View;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationEntry;
import org.mgnl.nicki.vaadin.base.navigation.Command;
import org.mgnl.nicki.vaadin.base.navigation.NavigationCommand;
import org.mgnl.nicki.vaadin.base.navigation.NavigationDialog;
import org.mgnl.nicki.vaadin.base.navigation.NavigationHelper;
import com.vaadin.ui.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@SuppressWarnings("serial")
@Slf4j
public abstract class NickiMenuApplication extends NickiApplication implements Serializable {

	private MainView mainView;
	private NavigationCommand command;
	private View navigationView;
	private String configPath;
	private @Setter boolean contentHeightFull;

	public NickiMenuApplication(String configPath) {
		super();
		this.configPath = configPath;
	}
	

	@Override
	public Component getEditor() {
		
		mainView = new MainView((Person) getNickiContext().getUser());
		if (contentHeightFull) {
			mainView.getContentLayout().setHeightFull();
		}
		
		try {
			mainView.addNavigation(this, configPath);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			log.error("Error in menu config: " + configPath, e);
		}

		

		mainView.initNavigation();
		
		String viewParameter = getPage().getUriFragment();
		
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
