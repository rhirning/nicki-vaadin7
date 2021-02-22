
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


import java.util.ArrayList;
import java.util.List;

import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public abstract class BaseAttributeListener<T> implements ValueChangeListener<T> {

	private DynamicObject dynamicObject;
	private String name;
	public BaseAttributeListener(DynamicObject dynamicObject, String name) {
		this.setDynamicObject(dynamicObject);
		this.setName(name);
	}

	public List<T> collectValues(AbstractComponentContainer cont) {
		List<T> list = new ArrayList<T>();
		for (Component component : cont) {
			if (component instanceof AbstractField) {
				@SuppressWarnings("unchecked")
				T value = ((AbstractField<T>) component).getValue();
				if (value != null) {
					list.add(value);
				}
			}
			if (component instanceof AbstractComponentContainer) {
				list.addAll(collectValues((AbstractComponentContainer) component));
			}
		}
		return list;
	}
	private void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	private void setDynamicObject(DynamicObject dynamicObject) {
		this.dynamicObject = dynamicObject;
	}
	public DynamicObject getDynamicObject() {
		return dynamicObject;
	}

}
