
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.v7.data.Property;

@SuppressWarnings("serial")
public class PartDataContainer extends AttributeDataContainer<String> implements DataContainer<String>, Property<String> {
	private String separator;
	private String name;

	public PartDataContainer(DynamicObject dynamicObject, String attributeName, String name, String separator) {
		super(dynamicObject, attributeName);
		this.separator = separator;
		this.name = name;
	}

	@Override
	public String getValue() {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) getDynamicObject().get(getAttributeName());
		for (String value : list) {
			String name = StringUtils.substringBefore(value, this.separator);
			if (StringUtils.equals(name, this.name)) {
				String data = StringUtils.substringAfter(value, this.separator);
				return data;
			}
		}

		return null;
	}

	@Override
	public void setValue(String newValue) {
		String value = (String) newValue;
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) getDynamicObject().get(getAttributeName());
		List<String> remover = new ArrayList<String>();
		for (String oldValue : list) {
			String name = StringUtils.substringBefore(oldValue, "=");
			if (StringUtils.equals(name, this.name)) {
				remover.add(oldValue);
			}
		}
		for (String toBeRemoved : remover) {
			list.remove(toBeRemoved);			
		}
		if (StringUtils.isNotEmpty(value)) {
			list.add(name + separator + value);
		}
	}

}
