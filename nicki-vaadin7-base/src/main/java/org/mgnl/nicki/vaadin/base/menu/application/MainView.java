
package org.mgnl.nicki.vaadin.base.menu.application;

import java.lang.reflect.InvocationTargetException;

/*-
 * #%L
 * nicki-app-menu
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.core.helper.JsonHelper;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.AccessGroupEvaluator;
import org.mgnl.nicki.vaadin.base.application.AccessRole;
import org.mgnl.nicki.vaadin.base.application.AccessRoleEvaluator;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationEntry;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationFolder;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationLabel;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationMainView;
import org.mgnl.nicki.vaadin.base.menu.navigation.NavigationTabSheet;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;
import org.mgnl.nicki.verify.Verify;
import org.mgnl.nicki.verify.VerifyException;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.server.StreamResource;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainView extends AppLayout implements NavigationMainView {

	public static final String LOGO_PATH = "logoPath";
	public static final String LOGO_HEIGHT = "logoHeight";
	public static final String LOGO_WIDTH = "logoWidth";
	public static final String NAVIGATION_WIDTH = "navigationWidth";
	public static final String TITLE = "title";
	
	private @Getter VerticalLayout contentLayout;
	
	
	private NavigationTabSheet navigation;
	private Component activeView;
	private Component startView;
	private Component headline;
	private Person user;
	private List<NavigationFolder> navigationFolders = new ArrayList<NavigationFolder>();
	private ApplicationConfig applicationConfig;
	private Map<String, String> config;
    private final Button logoutButton;
    private @Getter @Setter NickiApplication application;
	private static final long serialVersionUID = 8701670605362637395L;

	public MainView(Person user, String configPath) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		this.user = user;
		this.applicationConfig = JsonHelper.toBean(ApplicationConfig.class, getClass().getResourceAsStream(configPath));
		this.config = applicationConfig.getConfig();
		
		this.navigation = new NavigationTabSheet(this);
        // Header of the menu (the navbar)

		//setPrimarySection(Section.DRAWER);
        // menu toggle
        final DrawerToggle drawerToggle = new DrawerToggle();
        //drawerToggle.addClassName("menu-toggle");
        addToNavbar(drawerToggle);

        // image, logo
        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.setClassName("menu-header");
        addToNavbar(top);


		VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		if (config.containsKey(LOGO_PATH)) {			
			StreamResource resource = new StreamResource("logo.png", () -> MainView2.class.getResourceAsStream(config.get(LOGO_PATH)));
			Image image = new Image(resource, "Restart");
			if (config.containsKey(LOGO_HEIGHT)) {
				image.setHeight(config.get(LOGO_HEIGHT));
			}
			if (config.containsKey(LOGO_WIDTH)) {
				image.setWidth(config.get(LOGO_WIDTH));
			}
			titleLayout.add(image);
			image.addClickListener(event -> restart());
		} else if (config.containsKey(TITLE)) {
	        final H3 title = new H3(config.get(TITLE));
	        titleLayout.add(title);
	        title.addClickListener(event -> restart());
		} else {
			Span image = new Span("Restart");
			titleLayout.add(image);
			image.addClickListener(event -> restart());
		}
        addToDrawer(titleLayout);

		// logout button

        logoutButton = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(e -> logout());
        logoutButton.getElement().setAttribute("title", "Logout (Ctrl+L)");

	}

	private void logout() {
		if (getApplication() != null) {
			getApplication().logout();
		}
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");
        return routerButton;
    }
	
	public void restart() {
		if (this.startView != null) {
			showView(startView, true);
		}
	}
	
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // User can quickly activate logout with Ctrl+L
        attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L,
                KeyModifier.CONTROL);

        // Finally, add logout button for all users
        navigation.init(navigationFolders);
        addToNavbar(logoutButton);
        addToDrawer(navigation);
        restart();
        
        
    }

	public boolean show(NavigationEntry entry) {

		// TODO check is navigation change is allowed
		if (activeView != null && ((View) activeView).isModified()) {
			Notification.show(I18n.getText("nicki.app.menu.message.modified"), Type.HUMANIZED_MESSAGE);
			return false;
		}
//TODO		navigation.selectInNavigation(entry);
		Component view = entry.getView();
		showView(view, false);
		return true;
	}

	public boolean showView(Component view, boolean checkModify) {
		// TODO check is navigation change is allowed
		if (checkModify && activeView != null && ((View) activeView).isModified()) {
			Notification.show(I18n.getText("nicki.app.menu.message.modified"), Type.HUMANIZED_MESSAGE);
			return false;
		}
		setActiveView(view);
		if (view instanceof View) {
			((View)view).init();
		}

		/*
		if (((View) view).needsHeightFull()) {
			contentLayout.setHeightFull();
		} else {
			contentLayout.setHeight("-1px");
		}
		contentLayout.removeAll();
		if (null != getHeadline()) {
			Component headline = getHeadline();
			contentLayout.add(headline);
			contentLayout.add(view);
			// contentLayout.setExpandRatio(headline, 0.01f);
			contentLayout.setFlexGrow(1, view);
		} else {
			contentLayout.add(view);
		}
		*/
		setContent(view);
		return true;
	}

	public Component getActiveView() {
		return activeView;
	}

	public void setActiveView(Component activeView) {
		this.activeView = activeView;
	}

	public void addNavigation(NickiApplication application) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (applicationConfig.getStart() != null
				&& isVisible(applicationConfig.getStart())) {
			this.startView = getView(application, applicationConfig.getStart());
		}
		for (ApplicationChapter chapter : applicationConfig.getChapters()) {
			if (applicationConfig.isAllowed(this.user, chapter.getGroups(), chapter.getRoles())
					&& isVisible(chapter)) {
				for (ApplicationView applicationView : chapter.getViews()) {
					if (applicationConfig.isAllowed(this.user, applicationView.getGroups(), applicationView.getRoles())
							&& isVisible(applicationView)) {
						String labelCaption = I18n.getText(chapter.getChapter());
						String caption = I18n.getText(applicationView.getTitle());
						Component view = getView(application, applicationView);
						String navigation = applicationView.getNavigation();
						addNavigationEntry(labelCaption, caption, view, navigation);
					}
				}
			}
		}
	}

	private boolean isVisible(MenuItem item) {
		if (StringUtils.isNotBlank(item.getRule())) {

			String value = DataHelper.translate(StringUtils.substringBefore(item.getRule(), ":"));
			String rule = StringUtils.substringAfter(item.getRule(), ":");
			try {
				Verify.verifyRule(rule, value, new HashMap<String, String>());
			} catch (VerifyException e) {
				log.debug("Verify: " + e.getMessage());
				return false;
			}
		}
		return true;
	}

	private Component getView(NickiApplication application, ApplicationView applicationView) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		log.debug("View " + DataHelper.getMilli(new Date()) + ": " + applicationView.getView());
		Component view = Classes.newInstance(applicationView.getView());
		((View) view).setApplication(application);
		if (view instanceof ConfigurableView) {
			ConfigurableView configurableView = (ConfigurableView) view;
			configurableView.setConfiguration(applicationView.getConfiguration());
		}
		return view;
	}

	@Deprecated
	public void addNavigationEntry(String labelCaption, String caption, Component view) {
		addNavigationEntry(labelCaption, caption, view, null);
	}

	public void addNavigationEntry(String labelCaption, String caption, Component view, String navigation) {
		if (isAllowed(view.getClass(), this.user)) {
			NavigationFolder folder = getNavigationFolder(labelCaption);
			NavigationEntry entry = new NavigationEntry(caption, view, navigation);
			folder.addEntry(entry);
		}
	}

	public void addNavigationSeparator() {

		NavigationFolder folder = new NavigationFolder();
		navigationFolders.add(folder);
	}

	private NavigationFolder getNavigationFolder(String caption) {
		for (NavigationFolder folder : navigationFolders) {
			if (!folder.isSeparator() && StringUtils.equals(caption, folder.getLabel().getCaption())) {
				return folder;
			}
		}
		NavigationLabel label = new NavigationLabel(caption);
		NavigationFolder folder = new NavigationFolder(label);
		navigationFolders.add(folder);
		return folder;
	}

	public void initNavigation() {
		//restart();
	}

	static public boolean isAllowed(Class<?> clazz, Person user) {
		boolean allowed = false;
		AccessRole roleAnnotation = clazz.getAnnotation(AccessRole.class);
		AccessGroup groupAnnotation = clazz.getAnnotation(AccessGroup.class);
		if (roleAnnotation == null && groupAnnotation == null) {
			allowed = true;
		} else if (roleAnnotation != null) {
			try {
				AccessRoleEvaluator roleEvaluator = roleAnnotation.evaluator().newInstance();
				allowed = roleEvaluator.hasRole(user, roleAnnotation.name());
				allowed |= roleEvaluator.hasRole(user, Config.getStringValues(roleAnnotation.configName()));
			} catch (Exception e) {
				log.error("Could not create AccessRoleEvaluator", e);
				allowed = false;
			}
		}
		if (!allowed && groupAnnotation != null) {
			try {
				AccessGroupEvaluator groupEvaluator = groupAnnotation.evaluator().newInstance();
				allowed = groupEvaluator.isMemberOf(user, groupAnnotation.name());
				if (groupAnnotation.configName() != null && groupAnnotation.configName().length > 0) {
					allowed |= groupEvaluator.isMemberOf(user, Config.getStringValues(groupAnnotation.configName()));
				}
			} catch (Exception e) {
				log.error("Could not create AccessGroupEvaluator", e);
				allowed = false;
			}
		}
		if (!allowed) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append(user.getDisplayName()).append(" tried to access application ");
			errorMsg.append(clazz.getName()).append(". Allowed: ");
			if (roleAnnotation != null) {
				errorMsg.append("Role: ").append(Arrays.toString(roleAnnotation.name()));
			}
			if (groupAnnotation != null) {
				if (errorMsg.length() > 0) {
					errorMsg.append(", ");
				}
				errorMsg.append("Group: ").append(Arrays.toString(groupAnnotation.name()));
			}
			log.debug(errorMsg.toString());
		}
		return allowed;
	}

	public Component getHeadline() {
		return headline;
	}

	public void setHeadline(Component headline) {
		this.headline = headline;
	}

	public NavigationEntry selectMenuItem(String navigationKey) {
		for (NavigationFolder folder : this.navigationFolders) {
			for (NavigationEntry entry : folder.getEntries()) {
				if (StringUtils.equals(navigationKey, entry.getNavigation())) {
					navigation.selectInNavigation(entry);
					return entry;
				}
			}
		}
		return null;
	}

}
