
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


import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextArea;

@SuppressWarnings("serial")
public class SimpleEditor extends CustomComponent {

	private TextArea editor;
	private Property<String> data;

	public SimpleEditor(Property<String> dataContainer) {
		this.data = dataContainer;
		// editor
		editor = new TextArea();
		editor.setSizeFull();
		setSizeFull();
		
		setCompositionRoot(editor);
		if (data.getValue() != null) {
			editor.setValue((String) data.getValue());
		}
		editor.addValueChangeListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				data.setValue((String) editor.getValue());
			}
		});
	}

}
