
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

import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;

@SuppressWarnings("serial")
public class ParamInputListener implements ValueChangeListener {

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

	public void valueChange(ValueChangeEvent event) {
		map.put(name, event.getProperty().getValue());
		this.templateConfig.paramsChanged();
	}

}
