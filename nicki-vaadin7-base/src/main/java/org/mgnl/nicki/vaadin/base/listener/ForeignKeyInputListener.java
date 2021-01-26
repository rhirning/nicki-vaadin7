
package org.mgnl.nicki.vaadin.base.listener;

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

import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;

@SuppressWarnings("serial")
public class ForeignKeyInputListener extends BaseAttributeListener implements ValueChangeListener {

	public ForeignKeyInputListener(DynamicObject dynamicObject, String name) {
		super(dynamicObject, name);
	}

	public void textChange(TextChangeEvent event) {
	}

	public void valueChange(ValueChangeEvent event) {
		String value = (String) event.getProperty().getValue();
		if (StringUtils.isNotEmpty(value)) {
			getDynamicObject().put(getName(), value);
		} else {
			getDynamicObject().remove(getName());
		}
		getDynamicObject().setModified(true);

	}

}
