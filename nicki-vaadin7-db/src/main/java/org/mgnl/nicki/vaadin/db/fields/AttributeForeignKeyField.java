
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
import org.mgnl.nicki.db.annotation.ForeignKey;
import org.mgnl.nicki.db.helper.BeanHelper;
import org.mgnl.nicki.vaadin.db.data.AttributeDataContainer;
import org.mgnl.nicki.vaadin.db.data.DataContainer;
import org.mgnl.nicki.vaadin.db.editor.DbBeanValueChangeListener;
import org.mgnl.nicki.vaadin.db.listener.AttributeInputListener;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class AttributeForeignKeyField  extends BaseDbBeanAttributeField implements DbBeanAttributeField, Serializable {

	private ComboBox field;
	private DataContainer<Long> property;
	public void init(String attributeName, Object bean, DbBeanValueChangeListener objectListener, String dbContextName) {

		property = new AttributeDataContainer<Long>(bean, attributeName);
		field = new ComboBox(getName(bean, attributeName));
		fill(field, bean, attributeName, dbContextName);
		
		if (property != null && property.getValue() != null) {
			field.setValue(property.getValue());
		}
		field.addValueChangeListener(new AttributeInputListener<Long>(property, objectListener, null, 1L));
	}

	private void fill(ComboBox field, Object bean, String attributeName, String dbContextName) {
		
		for (Object object: BeanHelper.getForeignKeyValues(bean, attributeName, dbContextName)) {
			ForeignKey foreignKey = BeanHelper.getForeignKey(bean, attributeName);
			String keyAttribute = BeanHelper.getFieldFromColumnName(bean.getClass(), foreignKey.columnName()).getName();
			Long key = (Long) BeanHelper.getValue(object, keyAttribute);
			String display = (String) BeanHelper.getValue(object, foreignKey.display());
			field.addItem(key);
			field.setItemCaption(key, display);
		}
	}

	public Field<Object> getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}
	
}
