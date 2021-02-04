
package org.mgnl.nicki.vaadin.base.data;

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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.objects.DynamicObject;


@SuppressWarnings("serial")
public class ListPartDataContainer extends AttributeDataContainer<Map<String, String>> implements DataContainer<Map<String, String>> {
	private String separator;

	public ListPartDataContainer(DynamicObject dynamicObject, String attributeName, String separator) {
		super(dynamicObject, attributeName);
		this.separator = separator;
	}

	public Map<String, String> getValue() {
		Map<String, String> values = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) getDynamicObject().get(getAttributeName());
		for (String value : list) {
			String name = StringUtils.substringBefore(value, this.separator);
			String data = StringUtils.substringAfter(value, this.separator);
			values.put(name, data);
		}

		return values;
	}

	public void setValue(Map<String, String> values) {
		List<String> listvalues = new ArrayList<String>();
		for (String name : values.keySet()) {
			listvalues.add(name + this.separator + values.get(name));
		}
		getDynamicObject().put(getAttributeName(), listvalues);
	}

}
