
package org.mgnl.nicki.report;

import java.io.Serializable;

/*-
 * #%L
 * nicki-reports
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


import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.EntryFilter;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.dynamic.objects.objects.Org;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.editor.templates.TemplateConfig;
import org.mgnl.nicki.editor.templates.TemplateEditorComponent;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class ReportComponent extends TemplateEditorComponent implements Serializable, View {
    

	@SuppressWarnings("unchecked")
	@Override
	public Component getEditor() {

		DataProvider<TreeData> dataProvider = new DynamicObjectRoot(getTemplatesRoot(), getEntryFilter());
		TreeEditor editor = new TreeEditor(getNickiApplication(), getNickiContext(), dataProvider, getI18nBase());
		editor.configureClass(Org.class, VaadinIcons.FOLDER_O, TreeEditor.CREATE.DENY, TreeEditor.DELETE.DENY, TreeEditor.RENAME.DENY, Org.class, Template.class );
		editor.configureClass(Template.class, VaadinIcons.FILE_O, TreeEditor.CREATE.DENY, TreeEditor.DELETE.DENY, TreeEditor.RENAME.DENY);
		TemplateConfig templateConfig = new TemplateConfig();
		boolean usePreview = Config.getBoolean("nicki.report.usePreview", false);
		templateConfig.setUsePreview(usePreview);
		editor.setClassEditor(Template.class, templateConfig);
		editor.initActions();
		editor.setHeight("100%");

		return editor;
	}

	public EntryFilter getEntryFilter() {
		return new ShowAllFilter();
	}
	


	@Override
	public String getI18nBase() {
		return "nicki.application.reports";
	}

}
