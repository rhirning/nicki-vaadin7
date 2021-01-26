
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

import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.AttributeDataContainer;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.listener.BooleanAttributeInputListener;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Field;

@SuppressWarnings("serial")
public class AttributeCheckbox extends BaseDynamicAttributeField implements DynamicAttributeField<String>, Serializable {

	private CheckBox field;
	private DataContainer<String> property;
	public void init (String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<String> objectListener) {

		property = new AttributeDataContainer<String>(dynamicObject, attributeName);
		field = new CheckBox(getName(dynamicObject, attributeName));
		field.setHeight(1.5f, Unit.EM);
		field.setValue(DataHelper.booleanOf(property.getValue()));
		field.addValueChangeListener(new BooleanAttributeInputListener(property, objectListener));
	}

	public Field<Boolean> getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}
	
}
