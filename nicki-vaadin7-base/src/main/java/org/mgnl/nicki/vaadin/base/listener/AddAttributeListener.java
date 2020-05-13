
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


import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class AddAttributeListener implements ClickListener {
	private AbstractComponentContainer container;
	ListAttributeListener listener;

	public AddAttributeListener(AbstractComponentContainer container, ListAttributeListener listener) {
		this.container = container;
		this.listener = listener;
	}

	public void buttonClick(ClickEvent event) {
		String value = "";
		TextField input = new TextField(null, value);
		input.setImmediate(true);
		input.addValueChangeListener(listener);
		container.addComponent(input);
	}

}
