
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

import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.DataContainer;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.textfield.TextField;

@SuppressWarnings("serial")
public class TestDataValueChangeListener implements ValueChangeListener<ValueChangeEvent<String>> {

	private DataContainer<Map<String, String>> dataContainer;
	private Component testData;
	public TestDataValueChangeListener(DataContainer<Map<String, String>> dataContainer, Component testData) {
		this.dataContainer = dataContainer;
		this.testData = testData;
	}

	public void valueChanged(ValueChangeEvent<String> event) {
		dataContainer.setValue(collectMapValues(testData));
	}

	public Map<String, String> collectMapValues(Component cont) {
		Map<String, String> map = new HashMap<String, String>();
		cont.getChildren().forEach( component -> {
			if (component instanceof TextField) {
				String caption = ((TextField)component).getLabel();
				String value = ((TextField) component).getValue();
				map.put(caption, value);
			}
		});
		return map;
	}

	public List<String> collectValues(Component cont) {
		List<String> list = new ArrayList<String>();
		cont.getChildren().forEach( component ->  {
			if (component instanceof HasValue) {
				String value = (String) ((HasValue<?,?>) component).getValue();
				list.add(value);
			}
		});
		return list;
	}

	
}
