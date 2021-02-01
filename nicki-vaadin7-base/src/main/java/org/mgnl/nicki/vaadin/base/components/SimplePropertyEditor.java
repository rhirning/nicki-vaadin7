
package org.mgnl.nicki.vaadin.base.components;

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

import org.mgnl.nicki.vaadin.base.data.DataContainer;

import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
public class SimplePropertyEditor extends CustomComponent {

	private TextField editor;
	private DataContainer<String> data;

	public SimplePropertyEditor(DataContainer<String> dataContainer) {
		this.data = dataContainer;
		// editor
		editor = new TextField();
		editor.setWidth("100%");
		setWidth("100%");
		
		setCompositionRoot(editor);
		editor.setValue(data.getValue()!= null ? data.getValue() : "");
		editor.addValueChangeListener(event -> data.setValue((String) editor.getValue()));
	}

}
