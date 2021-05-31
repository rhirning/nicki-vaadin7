
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

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.template.engine.BasicTemplateHandler;
import org.mgnl.nicki.template.engine.TemplateParameter;

import com.vaadin.flow.component.Component;


public class BasicGuiTemplateHandler extends BasicTemplateHandler implements
		GuiTemplateHandler {

	public BasicGuiTemplateHandler() {
		super();
	}

	public Component getConfigDialog(Template template,
			Map<String, Object> params, TemplateConfig templateConfig) {
		return new ConfiguredTemplateConfigDialog(template, params, templateConfig);
	}

	public boolean isComplete(Map<String, Object> params) {
		java.util.List<TemplateParameter> list = getTemplateParameters();
		if (list != null) {
			for (TemplateParameter templateParameter : list) {
				if (!StringUtils.startsWith(templateParameter.getName(), ".")) {
					if (!params.containsKey(templateParameter.getName())) {
						return false;
					} else {
						if (null == params.get(templateParameter.getName())) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}
