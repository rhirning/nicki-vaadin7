
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.data.ReferenceAttributeDataContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.listener.AttributeInputListener;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;


@SuppressWarnings("serial")
public class AttributeSelectObjectField extends BaseDynamicAttributeField implements DynamicAttributeField<DynamicObject>, Serializable {

	private Select<DynamicObject> field;
	private DataContainer<DynamicObject> property;
	private List<DynamicObject> options;
	
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<DynamicObject> objectListener) {

		Select<DynamicObject> select = new Select<>();
		select.setLabel(getName(dynamicObject, attributeName));
		select.setItemLabelGenerator(DynamicObject::getName);
		getOptions(dynamicObject, dynamicObject.getModel().getDynamicAttribute(attributeName));
		property = new ReferenceAttributeDataContainer(dynamicObject, attributeName);
		select(dynamicObject.getAttribute(attributeName));
		select.addValueChangeListener(new AttributeInputListener<DynamicObject>(property, objectListener));
	}
	
	private void select(String attribute) {
		for (DynamicObject dynamicObject : options) {
			if (StringUtils.equals(attribute, ((DynamicObject)dynamicObject).getName())) {
				field.setValue(dynamicObject);
			}
		}
	}

	private List<DynamicObject> getOptions(DynamicObject dynamicObject, DynamicAttribute dynamicAttribute) {
		options = new ArrayList<DynamicObject>();
		for (DynamicObject option : dynamicAttribute.getOptions(dynamicObject)) {
			options.add( option);
		}
		return options;
	}

	public Component getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}
}
