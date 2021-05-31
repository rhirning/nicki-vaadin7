
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
import java.util.stream.Collectors;

import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;

@SuppressWarnings("serial")
public abstract class BaseAttributeListener<T> implements ValueChangeListener<ValueChangeEvent<T>> {

	private DynamicObject dynamicObject;
	private String name;
	public BaseAttributeListener(DynamicObject dynamicObject, String name) {
		this.setDynamicObject(dynamicObject);
		this.setName(name);
	}

	public List<T> collectValues(Component cont) {
		List<T> list = new ArrayList<T>();
		for (Component component : cont.getChildren().collect(Collectors.toList())) {
			if (component instanceof AbstractField) {
				@SuppressWarnings("unchecked")
				T value = ((HasValue<ValueChangeEvent<T>, T>) component).getValue();
				if (value != null) {
					list.add(value);
				}
			}
			if (component.getChildren().count() > 0) {
				component.getChildren().forEach(c -> list.addAll(collectValues(c)));
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
