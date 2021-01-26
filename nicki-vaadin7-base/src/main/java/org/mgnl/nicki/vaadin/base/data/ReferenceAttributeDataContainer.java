
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


import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.v7.data.Property;

@SuppressWarnings("serial")
public class ReferenceAttributeDataContainer implements DataContainer<String>, Property<String> {

	public DynamicObject getDynamicObject() {
		return dynamicObject;
	}

	public String getAttributeName() {
		return attributeName;
	}

	private DynamicObject dynamicObject;
	private String attributeName;
	private boolean readOnly = false;
	
	public ReferenceAttributeDataContainer(DynamicObject dynamicObject, String attributeName) {
		this.dynamicObject = dynamicObject;
		this.attributeName = attributeName;
	}

	public String getValue() {
		return StringUtils.trimToEmpty((String) dynamicObject.get(attributeName));
	}

	public void setValue(String newValue)  {
		dynamicObject.put(attributeName, newValue);
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean newStatus) {
		this.readOnly = newStatus;
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

}
