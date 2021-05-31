package org.mgnl.nicki.vaadin.db.editor;


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
import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.db.annotation.Attribute;
import org.mgnl.nicki.db.helper.BeanHelper;
import org.mgnl.nicki.db.helper.Type;
import org.mgnl.nicki.vaadin.db.fields.AttributeBooleanField;
import org.mgnl.nicki.vaadin.db.fields.AttributeDateField;
import org.mgnl.nicki.vaadin.db.fields.AttributeFloatField;
import org.mgnl.nicki.vaadin.db.fields.AttributeForeignKeyField;
import org.mgnl.nicki.vaadin.db.fields.AttributeIntegerField;
import org.mgnl.nicki.vaadin.db.fields.AttributeLongField;
import org.mgnl.nicki.vaadin.db.fields.AttributeTextField;
import org.mgnl.nicki.vaadin.db.fields.DbBeanAttributeField;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DbBeanFieldFactory implements Serializable {
	private DbBeanValueChangeListener objectListener;
	private String dbContextName;
	
	public DbBeanFieldFactory(DbBeanValueChangeListener objectListener, String dbContextName) {
		this.objectListener = objectListener;
		this.dbContextName = dbContextName;
	}
	
	public Component createField(Object bean, String attributeName, boolean create) {
		java.lang.reflect.Field field;
		try {
			field = bean.getClass().getDeclaredField(attributeName);
		} catch (NoSuchFieldException | SecurityException e) {
			field = null;
		}
		if (field != null) {
			Attribute dbAttribute = BeanHelper.getBeanAttribute(bean.getClass(), attributeName);
			if (dbAttribute != null) {
				DbBeanAttributeField viewField = null;
				if (StringUtils.isNotEmpty(dbAttribute.editorClass())) {
					try {
						viewField = (DbBeanAttributeField) Classes.newInstance(dbAttribute.editorClass());
						viewField.init(attributeName, bean, objectListener, dbContextName);
					} catch (Exception e) {
						viewField = null;
						log.error("Error", e);
					}
				}
				if (viewField == null) {
					if (BeanHelper.isForeignKey(bean, attributeName)) {
						viewField = new AttributeForeignKeyField();
					} else {
						Type type = BeanHelper.getTypeOfField(bean.getClass(), field.getName());
						if (type == Type.DATE) {
							viewField = new AttributeDateField();
						} else if (type == Type.TIMESTAMP) {
							viewField = new AttributeDateField();
						} else if (type == Type.INT) {
							viewField = new AttributeIntegerField();
						} else if (type == Type.LONG) {
							viewField = new AttributeLongField();
						} else if (type == Type.FLOAT) {
							viewField = new AttributeFloatField();
						} else if (type == Type.BOOLEAN) {
							viewField = new AttributeBooleanField();
						} else {
							viewField = new AttributeTextField();
						}
					}
					viewField.init(attributeName, bean, objectListener, dbContextName);
				}
				boolean readOnly = dbAttribute.readonly();
				if (!create && dbAttribute.primaryKey()) {
					readOnly = true;
				}
				return viewField.getComponent(readOnly);
			}
		}
		return null;
	}
	
	
	public void addFields(HasComponents layout, Object bean, boolean create, String[] hiddenAttributes, boolean readonly) {
		for (java.lang.reflect.Field field : BeanHelper.getFields(bean.getClass())) {
			if (hiddenAttributes == null || !DataHelper.contains(hiddenAttributes, field.getName())) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				boolean all = true;
				if (all || !attribute.primaryKey()
						&& (objectListener == null || objectListener.acceptAttribute(field.getName()))) {
					Component component = createField(bean, field.getName(), create);
					if (component != null) {
						((HasSize) component).setWidth("100%");
						if (attribute.primaryKey() || readonly) {
							component.getElement().setProperty("readonly", readonly);
						}
						layout.add(component);
					} else {
						log.debug("no field for " + field.getName());
					}
				}
			}
		}
	}
	
}
