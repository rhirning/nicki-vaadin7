
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
import java.util.Collection;

import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.AttributeDataContainer;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.helper.UIHelper;
import org.mgnl.nicki.vaadin.base.listener.AttributeInputListener;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class AttributeSelectField extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private ComboBox<String> field;
	private DataContainer<String> property;
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {

		ComboBox<String> select = new ComboBox<String>(getName(dynamicObject, attributeName));
//		select.setItemCaptionPropertyId("name");
		UIHelper.setImmediate(select, true);
		select.setSelectedItem(dynamicObject.getAttribute(attributeName));
		property = new AttributeDataContainer<String>(dynamicObject, attributeName);
		select.setValue(property.getValue());
		select.addValueChangeListener(new AttributeInputListener<String>(property, objectListener));
		field = select;
	}
	
	public Component getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}

	public void setOptions(Collection<String> options) {
		field.setItems(options);
		field.setValue(property.getValue());
	}

}
