
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



import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;

import com.vaadin.ui.Component;

@AccessGroup(name = {"nickiAdmins", "IDM-Development"})
@SuppressWarnings("serial")
@ShowWelcomeDialog(
		configKey="nicki.app.editor.scripts.useWelcomeDialog",
		groupsConfigName="nicki.app.editor.scripts.useWelcomeDialogGroups")
public class ScriptEditor extends NickiApplication {
	
	public ScriptEditor() {
		super();
	}

	@Override
	public Component getEditor() {
		ScriptEditorComponent editor = new ScriptEditorComponent(this);
		editor.init();
		return editor;
	}

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.script";
	}
	
}
