
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




import java.io.Serializable;

import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Org;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.ExportTreeAction;
import org.mgnl.nicki.vaadin.base.editor.ImportTreeAction;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

public class TemplateEditorComponent extends CustomComponent implements Serializable, View {

	private static final long serialVersionUID = -8245147689512577915L;
	private NickiApplication nickiApplication;
	private boolean isInit;

	public TemplateEditorComponent() {
	}


	public TemplateEditorComponent(NickiApplication nickiApplication) {
		this.nickiApplication = nickiApplication;
	}

	@SuppressWarnings("unchecked")
	public Component getEditor() {
		TemplateViewer templateViewer = new TemplateViewer();

		DataProvider<TreeData> dataProvider = new DynamicObjectRoot(getTemplatesRoot(), new ShowAllFilter());
		TreeEditor editor = new TreeEditor(getNickiApplication(), getNickiApplication().getNickiContext(), dataProvider, getI18nBase());
		editor.configureClass(Org.class, VaadinIcons.FOLDER_O, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW, Org.class, Template.class );
		editor.configureClass(Template.class, VaadinIcons.FILE_O, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW);
		editor.setClassEditor(Template.class, templateViewer);
		editor.addAction(new PreviewTemplate(getNickiContext(), Template.class, I18n.getText(getI18nBase() + ".action.preview"), getI18nBase()));
		editor.addAction(new ImportTreeAction(editor, Org.class, I18n.getText(getI18nBase() + ".action.import"), getI18nBase()));
		editor.addAction(new ExportTreeAction(getNickiContext(), Org.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
		editor.addAction(new ExportTreeAction(getNickiContext(), Template.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
		editor.initActions();
		editor.setHeight("100%");

		return editor;
	}

	public String getI18nBase() {
		return "nicki.editor.templates";
	}

	public String getTemplatesRoot() {
		return Config.getString("nicki.templates.basedn");
	}

	public NickiContext getNickiContext() {
		return getNickiApplication().getNickiContext();
	}

	public NickiApplication getNickiApplication() {
		return nickiApplication;
	}

	@Override
	public void init() {
		if (!isInit) {
			setCompositionRoot(getEditor());
			setSizeFull();
			isInit = true;
		}
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void setApplication(NickiApplication application) {
		this.nickiApplication = application;
	}


}
