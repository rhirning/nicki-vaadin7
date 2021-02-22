
package org.mgnl.nicki.editor.log4j;

/*-
 * #%L
 * nicki-editor-log4j
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

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.FileEntry;
import org.mgnl.nicki.core.data.FileSystemRoot;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;
import org.mgnl.nicki.vaadin.base.editor.Icon;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
@ShowWelcomeDialog(
		configKey="nicki.app.viewer.filesystem.useWelcomeDialog",
		groupsConfigName="nicki.app.viewer.filesystem.useWelcomeDialogGroups")
public class FileSystemViewer extends CustomComponent implements Serializable, View {
	private NickiApplication nickiApplication;
	private boolean isInit;
	
	public FileSystemViewer() {
	}
	
	public FileSystemViewer(NickiApplication nickiApplication) {
		this.nickiApplication = nickiApplication;
	}
	
	@SuppressWarnings("unchecked")
	private Component getEditor() {
		String root = "/";

		DataProvider<TreeData> treeDataProvider = new FileSystemRoot(root, new ShowAllFilter());
		TreeEditor editor = new TreeEditor(getNickiApplication(), getNickiContext(), treeDataProvider, getI18nBase());
		editor.configureClass(FileEntry.class, Icon.FOLDER, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW, FileEntry.class);
		DirectoryEditor directoryEditor = new DirectoryEditor(); 
		editor.setClassEditor(FileEntry.class, directoryEditor);
//		editor.addAction(new ImportTreeAction(editor, Org.class, I18n.getText(getI18nBase() + ".action.import"), getI18nBase()));
//		editor.addAction(new ExportTreeAction(getNickiContext(), Org.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
//		editor.addAction(new ExportTreeAction(getNickiContext(), Script.class, I18n.getText(getI18nBase() + ".action.export"), getI18nBase()));
		editor.initActions();
		editor.setHeight("100%");
		return editor;
	}

	public String getI18nBase() {
		return "nicki.editor.filesystem";
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
