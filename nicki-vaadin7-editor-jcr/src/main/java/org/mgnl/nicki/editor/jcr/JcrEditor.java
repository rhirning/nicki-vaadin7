
package org.mgnl.nicki.editor.jcr;

/*-
 * #%L
 * nicki-editor-jcr
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



import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.jcr.objects.GenericNodeDynamicObject;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.ExportTreeAction;
import org.mgnl.nicki.vaadin.base.editor.Icon;
import org.mgnl.nicki.vaadin.base.editor.ImportTreeAction;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;

import com.vaadin.ui.Component;

@AccessGroup(name = {"nickiAdmins", "IDM-Development"})
@SuppressWarnings("serial")
@ShowWelcomeDialog(
		configKey="nicki.app.editor.jcr.useWelcomeDialog",
		groupsConfigName="nicki.app.editor.jcr.useWelcomeDialogGroups")
public class JcrEditor extends NickiApplication {
	
	public JcrEditor() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getEditor() {

		DataProvider treeDataProvider = new DynamicObjectRoot("/", new ShowAllFilter());
//		DataProvider treeDataProvider = new DynamicObjectRoot(Config.getProperty("nicki.scripts.basedn"), new ShowAllFilter());
		TreeEditor editor = new TreeEditor(this, getNickiContext(), treeDataProvider, getI18nBase());
		editor.configureClass(GenericNodeDynamicObject.class, Icon.FOLDER, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW, GenericNodeDynamicObject.class);
		editor.addAction(new ImportTreeAction(editor, GenericNodeDynamicObject.class, I18n.getText(getI18nBase() + ".action.import"), getI18nBase()));
		editor.addAction(new ExportTreeAction(getNickiContext(), GenericNodeDynamicObject.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
		editor.setClassEditor(GenericNodeDynamicObject.class, new NodeViewer());
		editor.initActions();

		return editor;
	}

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.jcr";
	}
	
}
