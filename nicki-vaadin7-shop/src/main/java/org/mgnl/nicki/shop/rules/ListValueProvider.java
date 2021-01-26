
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


import java.util.List;

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.ldap.helper.LdapHelper;
import org.mgnl.nicki.ldap.helper.LdapHelper.LOGIC;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.ListSelect;

public class ListValueProvider extends BasicValueProvider implements ValueProviderComponent {

	private AbstractSelect value;
	
	public ListValueProvider() {
	}

	public AbstractSelect getValueList() {
		
		value = new ListSelect();
		value.setCaption(I18n.getText(getI18nBase() + ".value.title"));
		value.setWidth("200px");
		value.setHeight("200px");

		value.setNullSelectionAllowed(false);

		for (String entry : getSelector().getValues()) {
			value.addItem(entry);
		}
		return value;
	}
	
	public String getValue() {
		return (String)value.getValue();
	}

	public String getPersonQuery(CatalogArticle article, String value) {
		return getLdapName(article, getSelector().getName()) + "=" + value;
	}

	public String getArticleQuery(Person person, Object value) {
		StringBuilder sb2 = new StringBuilder();
		LdapHelper.addQuery(sb2, "nickiRule=" + getSelector().getName() + "=*", LOGIC.OR);
		LdapHelper.negateQuery(sb2);
		if (value != null && value instanceof String) {
			String stringValue = (String) value;
			LdapHelper.addQuery(sb2, "nickiRule=" + getSelector().getName() + "=" + stringValue, LOGIC.OR);
		} else if (value != null && value instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) value;
			for (String stringValue : values) {
				LdapHelper.addQuery(sb2, "nickiRule=" + getSelector().getName() + "=" + stringValue, LOGIC.OR);
			}
		}
		return sb2.toString();
	}


}
