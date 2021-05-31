
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
import org.mgnl.nicki.vaadin.base.menu.navigation.TableNavigation;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;
import org.mgnl.nicki.verify.Verify;
import org.mgnl.nicki.verify.VerifyException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableNavigationMainView extends VerticalLayout implements NavigationMainView {

	
	private @Getter VerticalLayout contentLayout;
	
	private SplitLayout hsplit;
	
	private TableNavigation navigation;
	private Component activeView;
	private Component startView;
	private Component headline;
	private Person user;
	private List<NavigationFolder> navigationFolders = new ArrayList<NavigationFolder>();
	private ApplicationConfig applicationConfig;
	private static final long serialVersionUID = 8701670605362637395L;

	public TableNavigationMainView(Person user, String configPath) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		this.user = user;
		applicationConfig = JsonHelper.toBean(ApplicationConfig.class, getClass().getResourceAsStream(configPath));
		buildMainLayout();
		hsplit.setOrientation(Orientation.HORIZONTAL);
		hsplit.setSplitterPosition(25);
		navigation = new TableNavigation(this, applicationConfig.getConfig());
		navigation.setSizeFull();
		// navigation = new ListSelectNavigation(this);
		// navigation = new ButtonNavigation(this);
		hsplit.addToPrimary(navigation);
		contentLayout = buildContentLayout();
		hsplit.addToSecondary(contentLayout);
		setPadding(false);

	}

	
	private void buildMainLayout() {
		// common part: create layout
		setSizeFull();
		setMargin(false);
		setSpacing(false);

		// top-level component properties
		setSizeFull();

		// hsplit
		hsplit = buildHsplit();
		add(hsplit);

	}

	
	private SplitLayout buildHsplit() {
		// common part: create layout
		hsplit = new SplitLayout();
		hsplit.setSizeFull();

		return hsplit;
	}

	
	private VerticalLayout buildContentLayout() {
		// common part: create layout
		contentLayout = new VerticalLayout();
		contentLayout.setWidthFull();
		contentLayout.setHeight("-1px");
		contentLayout.setSpacing(false);
		contentLayout.setMargin(false);

		return contentLayout;
	}
	
	// TODO: implement
	public void restart() {
		if (this.startView != null) {
			showView(startView, true);
		}
	}

	public boolean show(NavigationEntry entry) {

		// TODO check is navigation change is allowed
		if (activeView != null && ((View) activeView).isModified()) {
			Notification.show(I18n.getText("nicki.app.menu.message.modified"), Type.HUMANIZED_MESSAGE);
			return false;
		}
		navigation.selectInNavigation(entry);
		Component view = entry.getView();
		showView(view, false);
		return true;
	}

	public boolean showView(Component view, boolean checkModify) {
		setActiveView(view);
		((View) view).init();
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
		navigation.init(navigationFolders);
		restart();
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
