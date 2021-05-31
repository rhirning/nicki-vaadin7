
package org.mgnl.nicki.editor.scripts;

/*-
 * #%L
 * nicki-editor-scripts
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
import org.mgnl.nicki.core.context.AppContext;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Org;
import org.mgnl.nicki.dynamic.objects.objects.Script;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.ExportTreeAction;
import org.mgnl.nicki.vaadin.base.editor.ImportTreeAction;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class ScriptEditorComponent extends VerticalLayout implements Serializable, View {
	private NickiApplication nickiApplication;
	private boolean isInit;
	
	public ScriptEditorComponent() {
	}
	
	public ScriptEditorComponent(NickiApplication nickiApplication) {
		this.nickiApplication = nickiApplication;
	}

	
	@SuppressWarnings("unchecked")
	private Component getEditor() {
		ScriptViewer scriptViewer = new ScriptViewer(AppContext.getRequest());

		DataProvider<TreeData> treeDataProvider = new DynamicObjectRoot(Config.getString("nicki.scripts.basedn"), new ShowAllFilter());
		TreeEditor editor = new TreeEditor(getNickiApplication(), getNickiContext(), treeDataProvider, getI18nBase());
		editor.configureClass(Org.class, VaadinIcon.FOLDER_O, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW, Org.class, Script.class );
		editor.configureClass(Script.class, VaadinIcon.FILE_O, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW);
		editor.setClassEditor(Script.class, scriptViewer);
		editor.addAction(new ImportTreeAction(editor, Org.class, I18n.getText(getI18nBase() + ".action.import"), getI18nBase()));
		editor.addAction(new ExportTreeAction(getNickiContext(), Org.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
		editor.addAction(new ExportTreeAction(getNickiContext(), Script.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
		editor.initActions();
		editor.setHeight("100%");
		return editor;
	}

	public String getI18nBase() {
		return "nicki.editor.script";
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
			add(getEditor());
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
