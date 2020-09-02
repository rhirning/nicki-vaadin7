
package org.mgnl.nicki.editor.projects.members;

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


import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.editor.projects.objects.Directory;
import org.mgnl.nicki.editor.projects.objects.Member;
import org.mgnl.nicki.editor.projects.objects.Project;
import org.mgnl.nicki.vaadin.base.editor.ClassEditor;
import org.mgnl.nicki.vaadin.base.editor.NickiTreeEditor;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MemberEditor extends CustomComponent implements ClassEditor {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	@AutoGenerated
	private Button saveButton;
	@AutoGenerated
	private Label label_dummy;
	@AutoGenerated
	private Panel directories;
	@AutoGenerated
	private VerticalLayout verticalLayout_1;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Label name;
	private NickiTreeEditor nickiEditor;
	private Member member;
	private VerticalLayout directoriesLayout;

	public void save() {
		for (Component component : directoriesLayout) {
			if (component instanceof DirectoryComponent) {
				DirectoryComponent dirComponent = (DirectoryComponent) component;
				dirComponent.save();
			}
		}
		try {
			member.update();
			Notification.show(I18n.getText("nicki.editor.save.info"));
			nickiEditor.refresh(getProject());
		} catch (Exception e) {
			Notification.show(I18n.getText("nicki.editor.save.error"), 
					e.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	public MemberEditor() {
	}
	
	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		this.nickiEditor = nickiEditor;
		this.member = (Member) dynamicObject;
		buildMainLayout();
		directoriesLayout = (VerticalLayout) directories.getContent();
		setCompositionRoot(mainLayout);

		name.setValue(this.member.getDisplayName());
		for (Directory dir : getProject().getDirectories()) {
			DirectoryComponent comp = new DirectoryComponent(member, dir);
			directoriesLayout.addComponent(comp);
		}
		saveButton.addClickListener(event -> save());		
	}


	private Project getProject() {
		return (Project) nickiEditor.getParent(member);
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// verticalLayout_2
		verticalLayout_2 = buildVerticalLayout_2();
		mainLayout.addComponent(verticalLayout_2, "top:20.0px;left:20.0px;");
		
		return mainLayout;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_2() {
		// common part: create layout
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setWidth("-1px");
		verticalLayout_2.setHeight("-1px");
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setMargin(false);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		verticalLayout_2.addComponent(horizontalLayout_1);
		
		// directories
		directories = buildDirectories();
		verticalLayout_2.addComponent(directories);
		
		// label_dummy
		label_dummy = new Label();
		label_dummy.setWidth("-1px");
		label_dummy.setHeight("20px");
		label_dummy.setImmediate(false);
		verticalLayout_2.addComponent(label_dummy);
		
		// closeButton
		saveButton = new Button();
		saveButton.setWidth("-1px");
		saveButton.setHeight("-1px");
		saveButton.setCaption("Speichern");
		saveButton.setImmediate(true);
		verticalLayout_2.addComponent(saveButton);
		
		return verticalLayout_2;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("40px");
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setMargin(false);
		
		// name
		name = new Label();
		name.setWidth("-1px");
		name.setHeight("-1px");
		name.setValue("Name");
		name.setImmediate(false);
		horizontalLayout_1.addComponent(name);
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private Panel buildDirectories() {
		// common part: create layout
		directories = new Panel();
		directories.setWidth("600px");
		directories.setHeight("-1px");
		directories.setCaption("Verzeichnisse");
		directories.setImmediate(false);
		
		// verticalLayout_1
		verticalLayout_1 = new VerticalLayout();
		verticalLayout_1.setWidth("100.0%");
		verticalLayout_1.setHeight("-1px");
		verticalLayout_1.setImmediate(false);
		verticalLayout_1.setMargin(false);
		directories.setContent(verticalLayout_1);
		
		return directories;
	}


}
