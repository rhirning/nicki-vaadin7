
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
import java.util.Date;

import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.AttributeDataContainer;
import org.mgnl.nicki.vaadin.base.data.DataContainer;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.listener.AttributeInputListener;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class AttributeDateField  extends BaseDynamicAttributeField implements DynamicAttributeField<Date>, Serializable {

	private AbstractField<Date> field;
	private DataContainer<Date> property;
	public void init(String attributeName, DynamicObject dynamicObject, DynamicObjectValueChangeListener<Date> objectListener) {

		property = new AttributeDataContainer<Date>(dynamicObject, attributeName);
		field = new DateField(getName(dynamicObject, attributeName));
		field.setHeight(2, Unit.EM);
		field.setWidth("600px");
		field.setValue(property.getValue());
		field.addValueChangeListener(new AttributeInputListener<Date>(property, objectListener));
	}

	public Field<Date> getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}
	
}