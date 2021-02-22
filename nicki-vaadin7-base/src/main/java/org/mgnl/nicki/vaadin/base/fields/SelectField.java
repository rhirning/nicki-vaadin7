
package org.mgnl.nicki.vaadin.base.fields;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.AbstractSingleSelect;

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


import com.vaadin.ui.Component;

public class SelectField implements NickiField<String> {

	private AbstractSingleSelect<String> field;
	
	public SelectField(AbstractSingleSelect<String> field) {
		this.field = field;
	}

	@Override
	public void setValue(String value) {
		field.setValue(value);
	}

	@Override
	public String getValue() {
		return (String) field.getValue();
	}

	@Override
	public void setCaption(String caption) {
		field.setCaption(caption);
	}

	@Override
	public Component getComponent() {
		return field;
	}

	@Override
	public void addValueChangeListener(ValueChangeListener<String> listener) {
		field.addValueChangeListener(listener);
	}

	@Override
	public void setEnabled(boolean enabled) {
		field.setEnabled(enabled);
	}

	@Override
	public void focus() {
		this.field.focus();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		field.setReadOnly(readOnly);
	}

}
