
package org.mgnl.nicki.vaadin.db.data;

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


import org.mgnl.nicki.db.helper.BeanHelper;
import org.mgnl.nicki.db.helper.Type;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class AttributeDataContainer<T> implements DataContainer<T> {
	private Object orgBean;
	private Object bean;
	private String attributeName;
	private boolean readOnly;
	private boolean modified;
	
	public AttributeDataContainer(Object bean, String attributeName) {
		this.orgBean = bean;
		this.bean = bean;
		this.attributeName = attributeName;
	}

	public Object getBean() {
		return bean;
	}

	public String getAttributeName() {
		return attributeName;
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) BeanHelper.getValue(bean, attributeName);
	}

	@SuppressWarnings("unchecked")
	public T getOrgValue() {
		return (T) BeanHelper.getValue(orgBean, attributeName);
	}

	public void setValue(T newValue) {
		BeanHelper.setValue(bean, attributeName, newValue);
		setModified();
	}

	private void setModified() {
		T orgValue = getOrgValue();
		if (orgValue != null) {
			this.modified = ! orgValue.equals(getValue());
		} else {
			this.modified = (null == getValue());
		}
	}

	@SuppressWarnings("unchecked")
	public Class<? extends T> getType() {
		try {
			return  (Class<? extends T>) BeanHelper.getType(bean.getClass(), attributeName);
		} catch (NoSuchFieldException | SecurityException e) {
			log.error("Error reading type", e);
		}
		return null;
	}

	public Type getAttributeType() {
		return  BeanHelper.getTypeOfField(bean.getClass(), attributeName);
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean newStatus) {
		this.readOnly = newStatus;
	}

	@Override
	public boolean isModified() {
		return this.modified;
	}

}
