
package org.mgnl.nicki.editor.templates;

import java.time.LocalDate;

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


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.template.engine.TemplateHelper;
import org.mgnl.nicki.template.engine.TemplateParameter;
import org.mgnl.nicki.vaadin.base.data.DateHelper;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ConfiguredTemplateConfigDialog extends FormLayout implements TemplateConfigDialog {

	private static final long serialVersionUID = -1295818262583276359L;
	
	private Template template;
	private Map<String, Object> params;
	private TemplateConfig templateConfig;

	public ConfiguredTemplateConfigDialog(Template template, Map<String, Object> params, TemplateConfig templateConfig) {
		this.template = template;
		this.params = params;
		this.templateConfig = templateConfig;
		
		buildMainLayout();
	}

	private void buildMainLayout() {
		setWidth("100%");
		setHeight("100%");
		
		List<TemplateParameter> templateParameters = TemplateHelper.getTemplateHandler(template).getTemplateParameters();
		if (templateParameters != null) {
			for (TemplateParameter templateParameter : templateParameters) {
				if (!StringUtils.startsWith(templateParameter.getName(), ".")) {
					if (StringUtils.equalsIgnoreCase("date", templateParameter.getDataType())) {
						DatePicker field = new DatePicker();
						field.setLabel(templateParameter.getDisplayName());
						field.setWidth("-1px");
						field.setHeight("-1px");
						add(field);
						DateHelper.init(field);
						field.addValueChangeListener(new ParamInputListener<DatePicker, LocalDate, LocalDate>(templateParameter.getName(), params, templateConfig));
					} else if (StringUtils.equalsIgnoreCase("string", templateParameter.getDataType())) {
						TextField field = new TextField();
						field.setLabel(templateParameter.getDisplayName());
						field.setWidth("-1px");
						field.setHeight("-1px");
						add(field);
						field.addValueChangeListener(new ParamInputListener<TextField, String, String>(templateParameter.getName(), params, templateConfig));
					} else if (StringUtils.equalsIgnoreCase("static", templateParameter.getDataType())) {
						params.put(templateParameter.getName(), templateParameter.getValue());
					}
				}
			}
		}
	}

}
