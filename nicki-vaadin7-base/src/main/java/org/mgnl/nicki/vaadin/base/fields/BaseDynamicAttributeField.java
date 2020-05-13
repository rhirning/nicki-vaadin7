
package org.mgnl.nicki.vaadin.base.fields;

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


import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;;

public class BaseDynamicAttributeField {

	public String getName(DynamicObject dynamicObject, String attributeName) {
		String key = "nicki.dynamic-objects." 
			+ StringUtils.substringBefore(dynamicObject.getClass().getSimpleName(), ".")
			+ "."
			+ attributeName;
		String name = I18n.getText(key);
		if (StringUtils.equals(key, name)) {
			name = attributeName;
		}
		return name;
	}
}
