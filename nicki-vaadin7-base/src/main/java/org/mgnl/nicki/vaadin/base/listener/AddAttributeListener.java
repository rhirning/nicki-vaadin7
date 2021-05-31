
package org.mgnl.nicki.vaadin.base.listener;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.textfield.TextField;

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

@SuppressWarnings("serial")
public class AddAttributeListener {
	private HasOrderedComponents<Component> container;
	ListAttributeListener listener;

	public AddAttributeListener(HasOrderedComponents<Component> container, ListAttributeListener listener) {
		this.container = container;
		this.listener = listener;
	}

	public void buttonClick(ClickEvent event) {
		String value = "";
		TextField input = new TextField(null, value);
		input.addValueChangeListener((ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>>) listener);
		container.add(input);
	}

}
