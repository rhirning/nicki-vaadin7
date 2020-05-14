
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


import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.objects.Selector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicValueProvider {
	private Selector selector;
	private String i18nBase;

	public void init(Selector selector, String i18nBase) {
		this.setSelector(selector);
		setI18nBase(i18nBase);
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public Selector getSelector() {
		return selector;
	}

	public static String getLdapName(CatalogArticle article, String selectorName) {
		try {
			Person person = article.getContext().getObjectFactory().getDynamicObject(Person.class);
			return person.getModel().getAttributes().get(selectorName).getExternalName();
		} catch (InstantiateDynamicObjectException e) {
			log.error("Error", e);
			return "INVALID";
		}
	}

	public void setI18nBase(String i18nBase) {
		this.i18nBase = i18nBase;
	}

	public String getI18nBase() {
		return i18nBase;
	}

}
