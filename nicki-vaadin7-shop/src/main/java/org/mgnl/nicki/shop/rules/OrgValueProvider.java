
package org.mgnl.nicki.shop.rules;

/*-
 * #%L
 * nicki-shop
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

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.ldap.helper.LdapHelper;
import org.mgnl.nicki.ldap.helper.LdapHelper.LOGIC;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.vaadin.base.data.TreeContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.TreeSelector;

import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;

@SuppressWarnings("serial")
public class OrgValueProvider extends BasicValueProvider implements ValueProviderComponent, Serializable {
	
	public static final String STOP = "|"; 
	public static final String NO_STOP = ""; 

	private TreeSelector treeSelector;
	private TreeContainer treeContainer;
	private OptionGroup optionGroup;
	private HorizontalLayout layout;
	private String userBaseDn = Config.getString("nicki.users.basedn");
	private String userBasePath = LdapHelper.getSlashPath(null, userBaseDn);

	public OrgValueProvider() {
	}

	public Component getValueList() {
		layout = new HorizontalLayout();
		layout.setSpacing(true);
		treeSelector = new TreeSelector();
		DataProvider treeDataProvider = new DynamicObjectRoot(userBaseDn, new OrgOnlyFilter());
		treeContainer = new TreeContainer(getSelector().getContext(), treeDataProvider, I18n.getText(getI18nBase() + ".org.title"));
		treeSelector.setContainerDataSource(treeContainer.getTree());
		treeSelector.setItemCaptionPropertyId(TreeContainer.PROPERTY_NAME);
		treeSelector.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
		treeSelector.setItemIconPropertyId(TreeContainer.PROPERTY_ICON);

		treeSelector.addExpandListener(event -> {
				DynamicObject object = (DynamicObject) event.getItemId();
				treeContainer.loadChildren(object);
		});
		layout.addComponent(treeSelector.getComponent());
		
		optionGroup = new OptionGroup();
		optionGroup.addItem(STOP);
		optionGroup.setItemCaption(STOP, I18n.getText(getI18nBase() + ".self.title"));

		optionGroup.addItem(NO_STOP);
		optionGroup.setItemCaption(NO_STOP, I18n.getText(getI18nBase() + ".children.title"));

		optionGroup.setValue("|");
		optionGroup.setNullSelectionAllowed(false);
		layout.addComponent(optionGroup);
		return layout;
	}
	
	public String getValue() {
		
		if (treeSelector.getValue() != null) {
			return treeSelector.getValue().getSlashPath(treeContainer.getRoot()) + optionGroup.getValue();
		} else {
			return null;
		}
	}
	
	public String getPersonQuery(CatalogArticle article, String value) {
		value = StringUtils.stripEnd(value, "/");
		if (getType(value) == TYPE.ALL) {
			StringBuilder sb = new StringBuilder();
			LdapHelper.addQuery(sb, getSharpQuery(value), LOGIC.OR);
			LdapHelper.addQuery(sb, getChildrenQuery(value), LOGIC.OR);
			return sb.toString();
		} else {
			return getSharpQuery(value);
		}
	}
	
	private String getSharpQuery(String value) {
		return "nickiDirectory" + "=" + userBasePath + StringUtils.stripEnd(value, STOP);
	}

	private String getChildrenQuery(String value) {
		return "nickiDirectory" + "=" + userBasePath + value + "/*";
	}

	public TYPE getType(String value) {
		if (StringUtils.endsWith(value, STOP)) {
			return TYPE.SELF;
		} else {
			return TYPE.ALL;
		}
	}

	public String getArticleQuery(Person person, Object value) {
		StringBuilder sb = new StringBuilder();
		LdapHelper.addQuery(sb, "nickiRule=" + getSelector().getName() + "=*", LOGIC.OR);
		LdapHelper.negateQuery(sb);
		LdapHelper.addQuery(sb, "nickiRule=" + getSelector().getName() + "=/", LOGIC.OR);

		String path = person.getSlashPath(userBaseDn);
		// strip off username
		path = StringUtils.substringBeforeLast(path, "/");
		LdapHelper.addQuery(sb, "nickiRule=" + getSelector().getName() + "=" + path + "|", LOGIC.OR);

		while(StringUtils.isNotEmpty(path)) {
			LdapHelper.addQuery(sb, "nickiRule=" + getSelector().getName() + "=" + path, LOGIC.OR);
			path = StringUtils.substringBeforeLast(path, "/");
		}
		return sb.toString();
	}




}
