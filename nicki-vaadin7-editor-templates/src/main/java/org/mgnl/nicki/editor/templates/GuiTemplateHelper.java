
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

import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.template.engine.TemplateHelper;

import com.vaadin.flow.component.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuiTemplateHelper extends TemplateHelper {

	public static GuiTemplateHandler getGuiTemplateHandler(Template template) {
		GuiTemplateHandler handler = null;
		if (template.hasHandler()) {
			try {
				handler = (GuiTemplateHandler) Classes.newInstance(template.getHandler());
			} catch (Exception e) {
				log.error("Error", e);
			}
		}
		if (handler == null) {
			handler = new BasicGuiTemplateHandler();
			handler.setTemplate(template);
		}
		return handler;
	}

	public static Component getConfigDialog(
			Template template, Map<String, Object> params,
			TemplateConfig templateConfig) {
		return getGuiTemplateHandler(template).getConfigDialog(template, params, templateConfig);
	}

	public static boolean isComplete(Template template, Map<String, Object> params) {
		GuiTemplateHandler handler = getGuiTemplateHandler(template);
		return handler.isComplete(params);
	}


}
