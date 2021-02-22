
package org.mgnl.nicki.vaadin.db.listener;

import org.mgnl.nicki.vaadin.db.converter.AbstractConverter;
import org.mgnl.nicki.vaadin.db.data.DataContainer;
import org.mgnl.nicki.vaadin.db.editor.DbBeanValueChangeListener;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;

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

@SuppressWarnings("serial")
public class AttributeInputListener<X, T> implements ValueChangeListener<X> {

	private DataContainer<T> data;
	private DbBeanValueChangeListener objectListener;
	private AbstractConverter<X, T> converter;

	public AttributeInputListener(DataContainer<T> property, DbBeanValueChangeListener objectListener, AbstractConverter<X, T> converter) {
		this.data = property;
		this.objectListener = objectListener;
		this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public void valueChange(ValueChangeEvent<X> event) {
		T value;
		if (converter != null) {
			value = converter.convert(event.getValue());
		} else {
			value = (T) event.getValue();
		}
		data.setValue(value);
		if (objectListener != null) {
			objectListener.valueChange(data.getBean(), data.getAttributeName(), value);
		}
	}

}
