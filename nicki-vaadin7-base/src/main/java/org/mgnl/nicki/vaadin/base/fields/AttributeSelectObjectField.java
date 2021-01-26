
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

import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.data.ReferenceAttributeDataContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.helper.UIHelper;
import org.mgnl.nicki.vaadin.base.listener.AttributeInputListener;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.NativeSelect;

@SuppressWarnings("serial")
public class AttributeSelectObjectField extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private NickiField<String> field;
	private DataContainer<String> property;
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {

		NativeSelect select = new NativeSelect(getName(dynamicObject, attributeName));
		select.setContainerDataSource(getOptions(dynamicObject, dynamicObject.getModel().getDynamicAttribute(attributeName)));
		select.setItemCaptionPropertyId("name");
		UIHelper.setImmediate(select, true);
		select.select(dynamicObject.getAttribute(attributeName));
		property = new ReferenceAttributeDataContainer(dynamicObject, attributeName);
		select.setValue(property.getValue());
		select.addValueChangeListener(new AttributeInputListener<String>(property, objectListener));
		field = new SelectField(select);

		field.getComponent().setWidth("600px");
	}
	
	@SuppressWarnings("unchecked")
	private Container getOptions(DynamicObject dynamicObject, DynamicAttribute dynamicAttribute) {
		
		Container container = new IndexedContainer();
		container.addContainerProperty("name", String.class, null);
		container.addContainerProperty("dynamicObject", DynamicObject.class, null);
		for (DynamicObject option : dynamicAttribute.getOptions(dynamicObject)) {
			Item item = container.addItem(option.getPath());
			item.getItemProperty("dynamicObject").setValue(option);
			item.getItemProperty("name").setValue(option.getDisplayName());
		}
		return container;
	}

	public Component getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field.getComponent();
	}
}
