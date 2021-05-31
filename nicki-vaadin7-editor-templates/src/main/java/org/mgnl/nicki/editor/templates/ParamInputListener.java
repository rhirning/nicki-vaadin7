
package org.mgnl.nicki.editor.templates;

/*-
 * #%L
 * nicki-editor-templates
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


import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;


@SuppressWarnings("serial")
public class ParamInputListener<C extends Component, X, T> implements ValueChangeListener<ComponentValueChangeEvent<C, X>> {

	private String name;
	private Map<String, Object> map;
	private TemplateConfig templateConfig;

	public ParamInputListener(String name, Map<String, Object> map, TemplateConfig templateConfig) {
		init(name, map, templateConfig);
	}

	private void init(String name, Map<String, Object> map, TemplateConfig templateConfig) {
		this.name = name;
		this.map = map;
		this.templateConfig = templateConfig;
	}

	public void valueChanged(ComponentValueChangeEvent<C, X> event) {
		map.put(name, event.getValue());
		this.templateConfig.paramsChanged();
	}

	public void selectionChange(SelectionEvent<C, T> event) {
		if (event.getFirstSelectedItem().isPresent()) {
			map.put(name, event.getFirstSelectedItem().get());
		} else {
			map.put(name, null);
		}
		this.templateConfig.paramsChanged();
	}

}
