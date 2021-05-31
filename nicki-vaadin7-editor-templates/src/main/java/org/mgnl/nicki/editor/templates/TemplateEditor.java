
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




import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;

import com.vaadin.flow.component.Component;

@AccessGroup(name = {"nickiAdmins", "IDM-Development"})
@ShowWelcomeDialog(
		configKey="nicki.app.editor.templates.useWelcomeDialog",
		groupsConfigName="nicki.app.editor.templates.useWelcomeDialogGroups")
public class TemplateEditor extends NickiApplication {

	private static final long serialVersionUID = -8245147689512577915L;
    
	public TemplateEditor() {
		super();
	}

	@Override
	public Component getEditor() {
		TemplateEditorComponent editor = new TemplateEditorComponent(this);
		editor.setApplication(this);
		editor.init();
		return editor;
	}

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.templates";
	}

	public String getTemplatesRoot() {
		return Config.getString("nicki.templates.basedn");
	}


}
