
package org.mgnl.nicki.vaadin.db.fields;

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

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.vaadin.db.data.AttributeDataContainer;
import org.mgnl.nicki.vaadin.db.data.DataContainer;
import org.mgnl.nicki.vaadin.db.editor.DbBeanValueChangeListener;
import org.mgnl.nicki.vaadin.db.listener.AttributeInputListener;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
public class AttributeTextField  extends BaseDbBeanAttributeField implements DbBeanAttributeField, Serializable {

	private AbstractField<String> field;
	private DataContainer<String> property;
	public void init(String attributeName, Object bean, DbBeanValueChangeListener objectListener, String dbContextName) {

		property = new AttributeDataContainer<String>(bean, attributeName);
		field = new TextField(getName(bean, attributeName));
		field.setHeight(2, Unit.EM);
		field.setWidth("600px");
		field.setValue(StringUtils.stripToEmpty(property.getValue()));
		field.addValueChangeListener(new AttributeInputListener<String>(property, objectListener, null, null));
	}

	public Field<String> getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}
	
}
