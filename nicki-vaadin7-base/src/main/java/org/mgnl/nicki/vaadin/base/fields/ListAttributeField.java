
package org.mgnl.nicki.vaadin.base.fields;

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


import java.io.Serializable;
import java.util.List;

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.data.ListAttributeDataContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.helper.UIHelper;
import org.mgnl.nicki.vaadin.base.listener.AddAttributeListener;
import org.mgnl.nicki.vaadin.base.listener.ListAttributeListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ListAttributeField extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private ComponentContainer container;
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {

		@SuppressWarnings("unchecked")
		List<Object> values = (List<Object>) dynamicObject.get(attributeName);
		container = new VerticalLayout();
		VerticalLayout dataLayout = new VerticalLayout();
		DataContainer<List<String>> property = new ListAttributeDataContainer<List<String>>(dynamicObject, attributeName);
		ListAttributeListener listener = new ListAttributeListener(dynamicObject, attributeName, property, dataLayout, objectListener);
		HorizontalLayout hL= new HorizontalLayout();
		Label label = new Label(getName(dynamicObject, attributeName));
		hL.addComponent(label);
		Button newButton =new Button(I18n.getText("nicki.editor.generic.button.add"));
		newButton.addClickListener(new AddAttributeListener(dataLayout, listener));
		hL.addComponent(newButton);
		container.addComponent(hL);
		if (values != null) {
			for (Object valueObject : values) {
				String value = (String) valueObject;
				TextField input = new TextField(null, value);
				UIHelper.setImmediate(input, true);
				input.addValueChangeListener(listener);
				dataLayout.addComponent(input);
			}
		}
		container.addComponent(dataLayout);
	}

	public ComponentContainer getComponent(boolean readOnly) {
		//container.setReadOnly(readOnly);
		return container;
	}
}
