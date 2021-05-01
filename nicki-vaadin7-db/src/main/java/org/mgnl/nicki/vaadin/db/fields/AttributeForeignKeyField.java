
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
import java.util.ArrayList;
import java.util.List;

import org.mgnl.nicki.db.annotation.ForeignKey;
import org.mgnl.nicki.db.helper.BeanHelper;
import org.mgnl.nicki.vaadin.db.converter.BeanToIdConverter;
import org.mgnl.nicki.vaadin.db.data.AttributeDataContainer;
import org.mgnl.nicki.vaadin.db.data.DataContainer;
import org.mgnl.nicki.vaadin.db.editor.DbBeanValueChangeListener;
import org.mgnl.nicki.vaadin.db.listener.AttributeInputListener;

import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class AttributeForeignKeyField  extends BaseDbBeanAttributeField implements DbBeanAttributeField, Serializable {

	private ComboBox<Object> field;
	private DataContainer<Long> property;
	public void init(String attributeName, Object bean, DbBeanValueChangeListener objectListener, String dbContextName) {

		property = new AttributeDataContainer<Long>(bean, attributeName);
		field = new ComboBox<Object>(getName(bean, attributeName));
		field.setItemCaptionGenerator(item -> {
			ForeignKey foreignKey = BeanHelper.getForeignKey(bean, attributeName);
			return (String) BeanHelper.getValue(item, foreignKey.display());
		});
		fill(field, bean, attributeName, dbContextName);
		
		if (property != null && property.getValue() != null) {
			Object foreignKeyObject = BeanHelper.getForeignKeyObject(bean, attributeName, dbContextName, property.getValue());
			if (foreignKeyObject != null) {
				field.setValue(foreignKeyObject);
			}
		}
		field.addValueChangeListener(new AttributeInputListener<Object, Long>(property, objectListener, new BeanToIdConverter()));
	}

	private void fill(ComboBox<Object> field, Object bean, String attributeName, String dbContextName) {
		List<Object> objects = new ArrayList<Object>();
		for (Object object: BeanHelper.getForeignKeyValues(bean, attributeName, dbContextName)) {
			objects.add(object);
		}
		field.setItems(objects);
	}

	public ComboBox<Object> getComponent(boolean readOnly) {
		field.setReadOnly(readOnly);
		return field;
	}
	
}
