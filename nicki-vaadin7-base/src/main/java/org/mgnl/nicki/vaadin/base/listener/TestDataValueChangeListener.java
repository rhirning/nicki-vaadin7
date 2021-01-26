
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.v7.ui.Field;

@SuppressWarnings("serial")
public class TestDataValueChangeListener implements ValueChangeListener {

	private Property<Map<String, String>> dataContainer;
	private ComponentContainer testData;
	public TestDataValueChangeListener(Property<Map<String, String>> dataContainer, ComponentContainer testData) {
		this.dataContainer = dataContainer;
		this.testData = testData;
	}

	public void valueChange(ValueChangeEvent event) {
		dataContainer.setValue(collectMapValues(testData));
	}

	public Map<String, String> collectMapValues(ComponentContainer cont) {
		Map<String, String> map = new HashMap<String, String>();
		for (Component component : cont) {
			String caption = component.getCaption();
			if (component instanceof Field) {
				@SuppressWarnings("unchecked")
				String value = ((Field<String>) component).getValue();
				map.put(caption, value);
			}
		}
		return map;
	}

	public List<String> collectValues(ComponentContainer cont) {
		List<String> list = new ArrayList<String>();
		for (Component component : cont) {
			if (component instanceof Field) {
				@SuppressWarnings("unchecked")
				String value = ((Field<String>) component).getValue();
				list.add(value);
			}
		}
		return list;
	}

	
}
