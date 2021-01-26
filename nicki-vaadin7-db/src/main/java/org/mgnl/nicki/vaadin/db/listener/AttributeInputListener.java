
package org.mgnl.nicki.vaadin.db.listener;

import java.util.Locale;

import org.mgnl.nicki.vaadin.db.data.DataContainer;
import org.mgnl.nicki.vaadin.db.editor.DbBeanValueChangeListener;

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


import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.converter.AbstractStringToNumberConverter;

@SuppressWarnings("serial")
public class AttributeInputListener<T> implements ValueChangeListener {

	private DataContainer<T> property;
	private DbBeanValueChangeListener objectListener;
	private AbstractStringToNumberConverter<T> converter;
	private T dummy;

	public AttributeInputListener(DataContainer<T> property, DbBeanValueChangeListener objectListener, AbstractStringToNumberConverter<T> converter, T dummy) {
		this.property = property;
		this.objectListener = objectListener;
		this.converter = converter;
		this.dummy = dummy;
	}

	@SuppressWarnings("unchecked")
	public void valueChange(ValueChangeEvent event) {
		T value;
		if (converter != null) {
			value = converter.convertToModel((String) event.getProperty().getValue(), (Class<? extends T>) dummy.getClass(), Locale.GERMANY);
		} else {
			value = (T) event.getProperty().getValue();
		}
		property.setValue(value);
		if (objectListener != null) {
			objectListener.valueChange(property.getBean(), property.getAttributeName(), value);
		}
	}

}
