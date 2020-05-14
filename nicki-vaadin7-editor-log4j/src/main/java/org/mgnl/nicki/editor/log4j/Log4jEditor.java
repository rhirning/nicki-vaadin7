
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



import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

@SuppressWarnings("serial")
@AccessGroup(name = {"nickiAdmins", "IDM-Development"})
@ShowWelcomeDialog(
		configKey="nicki.app.editor.log4j.useWelcomeDialog",
		groupsConfigName="nicki.app.editor.log4j.useWelcomeDialogGroups")
public class Log4jEditor extends NickiApplication {
	
	public Log4jEditor() {
		super();
	}

	@Override
	public Component getEditor() {
		TabSheet tabSheet = new TabSheet();
		TailViewer tailViewer = new TailViewer();
		tailViewer.init();
		tabSheet.addTab(tailViewer, I18n.getText(getI18nBase() + ".tab.tailviewer"));
		FileSystemViewer fileSystemViewer= new FileSystemViewer(this);
		fileSystemViewer.init();
		tabSheet.addTab(fileSystemViewer, I18n.getText(getI18nBase() + ".tab.filesystem"));
		Log4jViewer editor = new Log4jViewer(this);
		editor.init();
		tabSheet.addTab(editor, I18n.getText(getI18nBase() + ".tab.log4j"));
		return tabSheet;
	}

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.log4j";
	}
	
}
