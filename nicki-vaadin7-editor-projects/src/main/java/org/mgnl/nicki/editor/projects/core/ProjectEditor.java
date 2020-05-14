
package org.mgnl.nicki.editor.projects.core;

/*-
 * #%L
 * nicki-editor-projects
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.auth.SystemContext;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.dynamic.objects.objects.Org;
import org.mgnl.nicki.editor.projects.directories.DirectoryEditor;
import org.mgnl.nicki.editor.projects.members.MemberEditor;
import org.mgnl.nicki.editor.projects.objects.Directory;
import org.mgnl.nicki.editor.projects.objects.Member;
import org.mgnl.nicki.editor.projects.objects.Project;

import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectValueChangeListener;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectViewer;
import org.mgnl.nicki.vaadin.base.editor.Icon;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;

import com.vaadin.ui.Component;

@SuppressWarnings("serial")
@SystemContext
@ShowWelcomeDialog(
		configKey="nicki.app.editor.projects.useWelcomeDialog",
		groupsConfigName="nicki.app.editor.projects.useWelcomeDialogGroups")
public class ProjectEditor extends NickiApplication {

	public ProjectEditor() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getEditor() throws DynamicObjectException {
		ProjectFilter projectFilter = new ProjectFilter(getNickiContext().getUser());
		DataProvider treeDataProvider = new DynamicObjectRoot(Config.getString("nicki.projects.basedn"), projectFilter);
		TreeEditor editor = new TreeEditor(this, getNickiContext(), treeDataProvider, getI18nBase());
		editor.configureClass(Org.class, null, TreeEditor.CREATE.DENY, TreeEditor.DELETE.DENY, TreeEditor.RENAME.DENY, Project.class);
		editor.configureClass(Project.class, Icon.DOCUMENT, TreeEditor.CREATE.DENY, TreeEditor.DELETE.DENY, TreeEditor.RENAME.DENY, Member.class, Directory.class);
		editor.configureClass(Member.class, Icon.USER, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.DENY);
		editor.configureClass(Directory.class, Icon.FOLDER, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.DENY, TreeEditor.RENAME.DENY);
		editor.setClassEditor(Project.class, new ProjectViewer());
		editor.setClassEditor(Member.class, new MemberEditor());
		editor.setClassEditor(Directory.class, new DirectoryEditor());
		editor.setNewClassEditor(Member.class, new DynamicObjectViewer(new SyncListener(editor)));
		editor.initActions();
		return editor;
	}
	
	private class SyncListener implements DynamicObjectValueChangeListener<String>, Serializable {
		private TreeEditor editor;
		public SyncListener(TreeEditor editor) {
			this.editor = editor;
		}

		@Override
		public void valueChange(DynamicObject dynamicObject, String name,
				List<String> values) {
		}

		@Override
		public void valueChange(DynamicObject dynamicObject,
				String attributeName, String value) {
			/* TODO
			if (StringUtils.equals(attributeName, "member")) {
				String namingValue = "";
				if (StringUtils.isNotEmpty(value)) {
					Person member = getNickiContext().loadObject(Person.class, value);
					if (member != null) {
						namingValue = member.getNamingValue();
					}
				}
				dynamicObject.initNew(dynamicObject.getParentPath(), namingValue);
			}
			*/
		}

		public boolean acceptAttribute(String name) {
			if (StringUtils.equals(name, "member")) {
				return true;
			}
			return false;
		}

		
		public void close(Component component) {
// TODO			component.getWindow().getParent().removeWindow(component.getWindow());
		}

		public void refresh(DynamicObject dynamicObject) {
			editor.refresh(dynamicObject);
		}
	}

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.projects";
	}
	
}
