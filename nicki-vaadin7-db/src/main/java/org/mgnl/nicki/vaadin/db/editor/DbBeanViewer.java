
package org.mgnl.nicki.vaadin.db.editor;

import java.lang.reflect.Field;


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



import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.db.annotation.Attribute;
import org.mgnl.nicki.db.context.DBContext;
import org.mgnl.nicki.db.context.DBContextManager;
import org.mgnl.nicki.db.helper.BeanHelper;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DbBeanViewer extends VerticalLayout implements NewClassEditor, ClassEditor {

	private Object bean;
	private Button saveButton;
	private boolean create;
	private DbBeanValueChangeListener listener;
	private String dbContextName;
	private String[] hiddenAttributes;
	private FormLayout formLayout;
	private @Getter @Setter boolean readOnly;

	public void setDbBean(Object bean, String...hiddenAttributes) {
		log.debug("Bean: " + bean);
		this.bean = bean;
		this.hiddenAttributes = hiddenAttributes;
		this.create = false;
		buildMainLayout();
	}

	@Override
	public void setDbBean(Object bean) {
		log.debug("Bean: " + bean);
		this.bean = bean;
		this.create = false;
		buildMainLayout();
	}
	
	public DbBeanViewer(DbBeanValueChangeListener listener) {
		this.listener = listener;
	}
	
	public void init(Class<?> classDefinition, Object... foreignObjects ) throws InstantiationException, IllegalAccessException {
		this.bean = classDefinition.newInstance();
		if (foreignObjects != null && foreignObjects.length > 0) {
			for (Object foreignObject : foreignObjects) {
				BeanHelper.addForeignKey(bean, foreignObject);
			}
		}
		this.create = true;
		buildMainLayout();
	}


	private void buildMainLayout() {
		
		setSizeUndefined();
		Label label = new Label(I18n.getText(bean.getClass().getName()));
		formLayout = new FormLayout();
		add(label, formLayout);
		DbBeanFieldFactory factory = new DbBeanFieldFactory(listener, dbContextName);
		factory.addFields(formLayout, bean, create, hiddenAttributes, isReadOnly());
		
		if (!isReadOnly()) {
			saveButton = new Button(I18n.getText("nicki.editor.generic.button.save"));
			saveButton.addClickListener(event -> save());
	
			add(saveButton);
		}
	}

	public void save() {
		if (!verifyMandatory()) {
			Notification.show("Bitte Pflichtfelder füllen", Type.ERROR_MESSAGE);
			return;
		}
		try {
			try (DBContext dbContext = DBContextManager.getContext(dbContextName)) {
				if (create) {
					this.bean = dbContext.create(bean);
				} else {
					this.bean = dbContext.update(bean);
				}
				Notification.show(I18n.getText("nicki.editor.save.info"));
			}
			if (listener != null) {
				listener.close(this);
				listener.refresh(this.bean);
			}
		} catch (Exception e) {
			log.error("Error", e);
		}
	}

	private boolean verifyMandatory() {
		for (Field field : bean.getClass().getDeclaredFields()) {
			Attribute attribute = field.getAnnotation(Attribute.class);
			if (attribute != null && attribute.mandatory()) {
				Object value = BeanHelper.getValue(this.bean, field.getName());
				if (value == null) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isCreate() {
		return create;
	}

	public String getDbContextName() {
		return dbContextName;
	}

	public void setDbContextName(String dbContextName) {
		this.dbContextName = dbContextName;
	}
	
	public String getI18nBase() {
		return "nicki.vaadin.db.";
	}

}
