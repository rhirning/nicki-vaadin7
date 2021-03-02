
package org.mgnl.nicki.vaadin.base.helper;

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


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.i18n.I18n;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContainerHelper {

	public static Collection<ValuePair> getDataContainer(Object data, String[] properties, String i18nBase) {
		Collection<ValuePair> container = new ArrayList<ValuePair>();
		for (String property : properties) {
			addItem(container, data, property, i18nBase);
		}
		return container;
	}
	
	private static <T extends Object> void addItem(Collection<ValuePair> container, Object data, String attributeName, String i18nBase) {
		String translatedName = attributeName;
		if (i18nBase != null) {
			translatedName = I18n.getText(i18nBase + "." + attributeName);
		}
		try {
			
			if (data != null) {
				container.add(new ValuePair(translatedName, BeanUtils.getProperty(data, attributeName)));
				
			} else {
				container.add(new ValuePair(translatedName, ""));
			}
		} catch (Exception e) {
			log.error("Error", e);
		}
	}

	public static Object get(Object object, String name) {
		String methodName =  "get" + StringUtils.capitalize(name);
		try {
			Method method = object.getClass().getMethod(methodName, new Class[]{});
			return method.invoke(object);
		} catch (Exception e) {
			return null;
		}
	}

}
