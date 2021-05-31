
package org.mgnl.nicki.vaadin.base.application;

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
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.auth.AccessTargetContext;
import org.mgnl.nicki.core.auth.DynamicObjectPrincipal;
import org.mgnl.nicki.core.auth.InvalidPrincipalException;
import org.mgnl.nicki.core.auth.LoginTargetContext;
import org.mgnl.nicki.core.auth.NickiPrincipal;
import org.mgnl.nicki.core.auth.SSOAdapter;
import org.mgnl.nicki.core.auth.SystemContext;
import org.mgnl.nicki.core.auth.TargetCallbackHandler;
import org.mgnl.nicki.core.auth.TargetContext;
import org.mgnl.nicki.core.auth.TargetException;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.AppContext;
import org.mgnl.nicki.core.context.DoubleContext;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.vaadin.base.auth.ApplicationLoginDialog;
import org.mgnl.nicki.vaadin.base.auth.LoginDialog;
import org.mgnl.nicki.vaadin.base.command.Command;
import org.mgnl.nicki.vaadin.base.components.ConfirmDialog;
import org.mgnl.nicki.vaadin.base.components.WelcomeDialog;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public abstract class NickiApplication extends Div implements RouterLayout, Serializable, HasDynamicTitle {

	private NickiContext nickiContext;
	private DoubleContext doubleContext;
	private boolean useSystemContext;
	private boolean useWelcomeDialog;
	
	public NickiApplication() {
		init();
	}
	
	public void init(VaadinRequest vaadinRequest) {
		AppContext.setRequest(vaadinRequest);
	}
	
	public void init() {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) UI.getCurrent().getSession().getSession().getAttribute(NickiServlet.NICKI_PARAMETERS);
		AppContext.setRequestParameters(map);
		if (map != null) {
			for (String paramName : map.keySet()) {
				System.out.println(paramName + "=" + map.get(paramName));
			}
		}
		setSizeFull();
		add(new Label("Hallo"));

		if (Config.getBoolean("nicki.application.auth.no")) {
			try {
				showStart();
				return;
			} catch (DynamicObjectException e) {
				log.error("Error", e);
			}
		} else {
			
			if (Config.getBoolean("nicki.application.auth.jaas")) {
				loginJAAS();
			} else {
				loginSSO();
			}
			if (doubleContext != null) {
				try {
					start();
					return;
				} catch (DynamicObjectException e) {
					log.error("Error", e);
				}
			}
	
			showLoginDialog();
		}
	}
	
	@Override
	public String getPageTitle() {
		return I18n.getText(getI18nBase() + ".main.title");
	}

	public void logout() {
		setDoubleContext(null);
		showLoginDialog();
	}
	
	private void showLoginDialog() {
		Component loginDialog = null;
		String loginClass = Config.getString("nicki.application.login.class");
		if (StringUtils.isNotBlank(loginClass)) {
		try {
				loginDialog = Classes.newInstance(loginClass);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				log.error("Error creatin LoginDialog " + loginClass,e.getMessage());
			}
		}
		if (loginDialog == null) {
			loginDialog = new ApplicationLoginDialog();
		}
		if (loginDialog instanceof LoginDialog) {
			LoginDialog dialog = (LoginDialog) loginDialog;
			dialog.setApplication(this);
		}
		removeAll();
		add(loginDialog);
	}
	
	private void loginJAAS() {
		try {

			String contextName;
			if ((isUseSystemContext() || Config.getBoolean("nicki.application.auth.jaas.useSystem") )
					&& Config.getString("nicki.login.context.name.system") != null) {
				contextName = Config.getString("nicki.login.context.name.system");
			} else {
				contextName = Config.getString("nicki.login.context.name");
			}
			log.debug("LoginContext=" + contextName);
			TargetCallbackHandler callbackHandler = new TargetCallbackHandler();
			callbackHandler.setLoginTarget(getLoginTargetName());
			callbackHandler.setTarget(getTargetName());
			callbackHandler.setAccessTarget(getAccessTargetName());
			LoginContext loginContext = new LoginContext(contextName, new Subject(), callbackHandler);
			loginContext.login();
			Set<Principal> principals = loginContext.getSubject().getPrincipals();
			log.debug("principals: " + principals);
			if (principals != null && principals.size() > 0) {
				Principal principal = principals.iterator().next();
				if (principal instanceof DynamicObjectPrincipal) {
					DynamicObjectPrincipal dynamicObjectPrincipal = (DynamicObjectPrincipal) principal;
					DoubleContext context = new DoubleContext();
					log.debug("loginContext: " + dynamicObjectPrincipal.getLoginContext().toString());
					context.setLoginContext(dynamicObjectPrincipal.getLoginContext());
					log.debug("nickiContext: " + dynamicObjectPrincipal.getContext().toString());
					context.setContext(dynamicObjectPrincipal.getContext());					
					setDoubleContext(context);
				}
			}
		} catch (LoginException e) {
			log.error(e.getMessage());
		}
	}

	private String getLoginTargetName() {
		String loginTargetName = null;
		LoginTargetContext annotation = this.getClass().getAnnotation(LoginTargetContext.class);
		if (annotation != null) {
			if (StringUtils.isNotBlank(annotation.configName())) {
				loginTargetName = Config.getString(annotation.configName(), "");
			} else if (StringUtils.isNotBlank(annotation.name())) {
				loginTargetName = annotation.name();
			}
		}
		if (StringUtils.isBlank(loginTargetName)) {
			loginTargetName = getTarget().getName();
		}
		log.debug("LoginTarget=" + loginTargetName);
		return loginTargetName;

	}

	private String getTargetName() {
		String targetName = null;
		TargetContext annotation = this.getClass().getAnnotation(TargetContext.class);
		if (annotation != null) {
			if (StringUtils.isNotBlank(annotation.configName())) {
				targetName = Config.getString(annotation.configName(), "");
			} else if (StringUtils.isNotBlank(annotation.name())) {
				targetName = annotation.name();
			}
		}
		if (StringUtils.isBlank(targetName)) {
			targetName = getTarget().getName();
		}
		log.debug("Target=" + targetName);
		return targetName;

	}

	private String getAccessTargetName() {
		String accessTargetName = null;
		AccessTargetContext annotation = this.getClass().getAnnotation(AccessTargetContext.class);
		if (annotation != null) {
			if (StringUtils.isNotBlank(annotation.configName())) {
				accessTargetName = Config.getString(annotation.configName(), "");
			} else if (StringUtils.isNotBlank(annotation.name())) {
				accessTargetName = annotation.name();
			}
		}
		if (StringUtils.isBlank(accessTargetName)) {
			accessTargetName = getTarget().getName();
		}
		log.debug("accessTarget=" + accessTargetName);
		return accessTargetName;

	}

	private void loginSSO() {
		try {
			String ssoLoginClass = Config.getString("nicki.login.sso");
			log.debug("ssoLoginClass=" + ssoLoginClass);
			if (StringUtils.isNotEmpty(ssoLoginClass)) {
				SSOAdapter adapter = (SSOAdapter) Classes.newInstance(ssoLoginClass);
				String name = adapter.getName();
				char[] password = adapter.getPassword();
				if (name != null && password != null) {
					login(name, new String(password));
					/*
					NickiPrincipal principal = new NickiPrincipal(name, new String(password));
					if (principal != null) {
						DynamicObject user = getTarget().login(principal);
						if (user != null) {
							setDoubleContext(getContext(user, principal.getPassword()));
						}
					}
					*/
				}
			}
		} catch (Exception e) {
			log.error("Error", e.getMessage());
		}
	}
	

	public enum SYSTEM_CONTEXT {YES, NO}
	public DoubleContext getContext(DynamicObject user, String password, SYSTEM_CONTEXT systemContext) throws InvalidPrincipalException, TargetException {
		DoubleContext context = new DoubleContext();
		// LoginTarget
		context.setLoginContext(AppContext.getNamedUserContext(getLoginTargetName(), user, password));
		// Target
		if (systemContext == SYSTEM_CONTEXT.YES) {
			context.setContext(AppContext.getSystemContext(getTargetName(), user));
		} else {
			context.setContext(AppContext.getNamedUserContext(getTargetName(), user, password));
		}
		return context;
	}

	public Object getRequest() {
		return AppContext.getRequest();
	}

	public boolean login(String name, String password) {
		try {
			NickiPrincipal principal = new NickiPrincipal(name, password);
			if (principal != null) {
				DynamicObject user = AppContext.getSystemContext(getLoginTargetName()).login(name, password);
				if (user != null) {
					setDoubleContext(getContext(user, principal.getPassword(),
							isUseSystemContext() ? SYSTEM_CONTEXT.YES : SYSTEM_CONTEXT.NO));
					return true;
				}
			}
		} catch (Exception e) {
			log.debug("Login failed, user=" + name, e);
		}
		this.nickiContext = getTarget().getGuestContext();
		return false;
	}

	public void start() throws DynamicObjectException {
		removeAll();
		if (isAllowed(this.doubleContext.getLoginContext().getUser())) {
			showStart();
		} else {
			logout();
		}
	}
	
	public void showStart() throws DynamicObjectException {
		removeAll();
		if (isUseWelcomeDialog()) {
			add(new WelcomeDialog(this));
		}
		Component editor = getEditor();
		add(editor);
		if (editor instanceof HasSize) {
			((HasSize) editor).setSizeFull();;
		}
	}
	
	public boolean isAllowed(DynamicObject user) {
		boolean allowed = false;
		AccessRole roleAnnotation = getClass().getAnnotation(AccessRole.class);
		AccessGroup groupAnnotation = getClass().getAnnotation(AccessGroup.class);
		if (roleAnnotation == null && groupAnnotation == null) {
			allowed =  true;
		} else if (roleAnnotation != null){
			try {
				Person roleUser = getUser(user, getAccessTargetName());
				AccessRoleEvaluator roleEvaluator = roleAnnotation.evaluator().newInstance();
				allowed = roleEvaluator.hasRole(roleUser, roleAnnotation.name());
				if (roleAnnotation.configName() != null && roleAnnotation.configName().length > 0) {
					allowed |= roleEvaluator.hasRole(roleUser, Config.getStringValues(roleAnnotation.configName()));
				}
			} catch (Exception e) {
				log.error("Could not create AccessRoleEvaluator", e);
				allowed = false;
			}
		}
		if (!allowed && groupAnnotation != null) {
			try {
				Person groupUser = getUser(user, getAccessTargetName());
				AccessGroupEvaluator groupEvaluator = groupAnnotation.evaluator().newInstance();
				allowed = groupEvaluator.isMemberOf(groupUser, groupAnnotation.name());
				if (groupAnnotation.configName() != null && groupAnnotation.configName().length > 0) {
					allowed |= groupEvaluator.isMemberOf(groupUser, Config.getStringValues(groupAnnotation.configName()));
				}
			} catch (Exception e) {
				log.error("Could not create AccessGroupEvaluator", e);
				allowed = false;
			}
		}
		if (!allowed) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append(user).append(" tried to access application ");
			errorMsg.append(getClass().getName()).append(". Allowed: ");
			if (roleAnnotation != null) {
				errorMsg.append("Role: ").append(roleAnnotation.name().toString());
			}
			if (groupAnnotation != null) {
				if (errorMsg.length() > 0) {
					errorMsg.append(", ");
				}
				errorMsg.append("Group: ");
				for (String groupName : groupAnnotation.name()) {
					errorMsg.append(" ").append(groupName);
				}
			}
			log.error(errorMsg.toString());
			Notification.show(I18n.getText("nicki.editor.access.denied", getClass().getName()),
					Type.ERROR_MESSAGE);
		}
		return allowed;
	}

	private Person getUser(DynamicObject user, String targetName) {
		try {
			if (!StringUtils.equals(targetName, user.getContext().getTarget().getName())) {
				NickiContext ctx = AppContext.getSystemContext(targetName);
				log.debug("Authorization context:" + ctx);
				String baseDn = ctx.getTarget().getProperty("baseDn", Config.getString("nicki.users.basedn"));
				List<? extends DynamicObject> list = ctx.loadObjects(Person.class, baseDn, "cn=" + user.getName());
				
				if (list != null && list.size() == 1) {
					log.info("login: loadObjects successful");
					return (Person) list.get(0);
				}
			}
		} catch (Exception e) {
			log.error("Invalid SystemContext", e);
		}
		log.debug("Fallback authorization context:" + user.getContext());
		return (Person) user;
	}

	public String getEditorHeight() {
		return "100%";
	}
	
	public String getEditorWidth() {
		return "100%";
	}
	


	public abstract Component getEditor() throws DynamicObjectException;

	public void setNickiContext(NickiContext context) {
		this.nickiContext = context;
	}

	public NickiContext getNickiContext() {
		return nickiContext;
	}

	public abstract Target getTarget();
	
	public abstract String getI18nBase();

	@Deprecated
	public void setUseSystemContext(boolean useSystemContext) {
		this.useSystemContext = useSystemContext;
	}

	public boolean isUseSystemContext() {
		if (this.getClass().getAnnotation(SystemContext.class) != null) {
			return true;
		} else {
			return useSystemContext;
		}
	}

	public void setUseWelcomeDialog(boolean useWelcomeDialog) {
		this.useWelcomeDialog = useWelcomeDialog;
	}

	public boolean isUseWelcomeDialog() {
		if (useWelcomeDialog) {
			return true;
		}
		ShowWelcomeDialog showWelcomeDialog = this.getClass().getAnnotation(ShowWelcomeDialog.class);
		if (showWelcomeDialog != null) {
			if (Config.exists(showWelcomeDialog.configKey())) {
				return Config.getBoolean(showWelcomeDialog.configKey());
			} else {
				if (showWelcomeDialog.groups() != null && showWelcomeDialog.groups().length > 0) {
					try {
						AccessGroupEvaluator groupEvaluator = showWelcomeDialog.groupEvaluator().newInstance();
						if (groupEvaluator.isMemberOf((Person) doubleContext.getLoginContext().getUser(), showWelcomeDialog.groups())) {
							return true;
						}
					} catch (InstantiationException | IllegalAccessException e) {
						log.error("Could not create AccessGroupEvaluator", e);
					}
				}
				if (StringUtils.isNotBlank(showWelcomeDialog.groupsConfigName())) {
					try {
						AccessGroupEvaluator groupEvaluator = showWelcomeDialog.groupEvaluator().newInstance();
						if (groupEvaluator.isMemberOf((Person) doubleContext.getLoginContext().getUser(), Config.getList(showWelcomeDialog.groupsConfigName(), ",").toArray(new String[0]))) {
							return true;
						}
					} catch (InstantiationException | IllegalAccessException e) {
						log.error("Could not create AccessGroupEvaluator", e);
					}
				}
				if (showWelcomeDialog.roles() != null && showWelcomeDialog.roles().length > 0) {
					try {
						AccessRoleEvaluator roleEvaluator = showWelcomeDialog.roleEvaluator().newInstance();
						if (roleEvaluator.hasRole((Person) doubleContext.getLoginContext().getUser(), showWelcomeDialog.roles())) {
							return true;
						}
					} catch (InstantiationException | IllegalAccessException e) {
						log.error("Could not create AccessRoleEvaluator", e);
					}
				}
				if (StringUtils.isNotBlank(showWelcomeDialog.rolesConfigName())) {
					try {
						AccessRoleEvaluator roleEvaluator = showWelcomeDialog.roleEvaluator().newInstance();
						if (roleEvaluator.hasRole((Person) doubleContext.getLoginContext().getUser(), Config.getList(showWelcomeDialog.rolesConfigName(), ",").toArray(new String[0]))) {
							return true;
						}
					} catch (InstantiationException | IllegalAccessException e) {
						log.error("Could not create AccessRoleEvaluator", e);
					}
				}
			}
		}
		return false;
	}

	public void confirm(Command command) {
		new ConfirmDialog(command).open();;
	}

	public Div getView() {
		return this;
	}

	/* TODO: Theme
	@Override
	public String getTheme() {
		return Config.getString("nicki.application.theme", "reindeer");
	}
	*/

	public void setDoubleContext(DoubleContext doubleContext) {
		this.doubleContext = doubleContext;
		if (doubleContext != null) {
			this.nickiContext = doubleContext.getContext();
		} else {
			this.nickiContext = null;
		}
	}

    public static final class Constants {        
        /** 
         * The location of the login.conf file.</p>
         */
        public static final String LOGIN_CONF = "spnego.login.conf";
        
        /**
         * <p>The location of the krb5.conf file. On Windows, this file will 
         * sometimes be named krb5.ini and reside <code>%WINDOWS_ROOT%/krb5.ini</code> 
         * here.</p>
         * 
         * <p>By default, Java looks for the file in these locations and order:
         * <li>System Property (java.security.krb5.conf)</li>
         * <li>%JAVA_HOME%/lib/security/krb5.conf</li>
         * <li>%WINDOWS_ROOT%/krb5.ini</li>
         * </p>
         */
        public static final String KRB5_CONF = "spnego.krb5.conf";

    	public static final String JAAS_SSO_ENTRY = "NickiSSO";
    	public static final String JAAS_ENTRY = "Nicki";
    	public static final String ATTR_NICKI_CONTEXT = "NICKI_CONTEXT";
    }

	public DoubleContext getContext() {
		return doubleContext;
	}
}
