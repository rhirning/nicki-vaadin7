
package org.mgnl.nicki.editor.projects.directories;

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
public class DirectoryEditor extends CustomComponent implements ClassEditor {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private VerticalLayout verticalLayout_3;
	@AutoGenerated
	private Button saveButton;
	@AutoGenerated
	private Label labelDummy;
	@AutoGenerated
	private Panel members;
	@AutoGenerated
	private VerticalLayout verticalLayout_1;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Label name;
	private NickiTreeEditor nickiEditor;
	private Directory directory;
	private VerticalLayout membersLayout;

	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		this.nickiEditor = nickiEditor;
		this.directory = (Directory) dynamicObject;
		buildMainLayout();
		membersLayout = (VerticalLayout) members.getContent();
		setCompositionRoot(mainLayout);

		name.setValue(this.directory.getDisplayName());
		for (Member member : getProject().getMembers()) {
			MemberComponent comp = new MemberComponent(directory, member);
			membersLayout.addComponent(comp);
		}
		saveButton.addClickListener(event -> save());
	}
	
	public void save() {
		for (Component component : membersLayout) {
			if (component instanceof MemberComponent) {
				MemberComponent memberComponent = (MemberComponent) component;
				memberComponent.save();
				try {
					memberComponent.getMember().update();
					Notification.show(I18n.getText("nicki.editor.save.info"));
				} catch (Exception e) {
					Notification.show(I18n.getText("nicki.editor.save.error"), 
							e.getMessage(), Type.ERROR_MESSAGE);
				}
			}
		}
		nickiEditor.refresh(getProject());
	}

	public DirectoryEditor() {
	}
	
	private Project getProject() {
		return (Project) nickiEditor.getParent(directory);
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// verticalLayout_3
		verticalLayout_3 = buildVerticalLayout_3();
		mainLayout.addComponent(verticalLayout_3, "top:20.0px;left:20.0px;");
		
		return mainLayout;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_3() {
		// common part: create layout
		verticalLayout_3 = new VerticalLayout();
		verticalLayout_3.setWidth("-1px");
		verticalLayout_3.setHeight("-1px");
		verticalLayout_3.setImmediate(false);
		verticalLayout_3.setMargin(false);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		verticalLayout_3.addComponent(horizontalLayout_1);
		
		// members
		members = buildMembers();
		verticalLayout_3.addComponent(members);
		
		// label_dummy
		labelDummy = new Label();
		labelDummy.setWidth("-1px");
		labelDummy.setHeight("20px");
		labelDummy.setImmediate(false);
		verticalLayout_3.addComponent(labelDummy);
		
		// closeButton
		saveButton = new Button();
		saveButton.setWidth("-1px");
		saveButton.setHeight("-1px");
		saveButton.setCaption("Speichern");
		saveButton.setImmediate(true);
		verticalLayout_3.addComponent(saveButton);
		
		return verticalLayout_3;
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
	private Panel buildMembers() {
		// common part: create layout
		members = new Panel();
		members.setWidth("600px");
		members.setHeight("-1px");
		members.setCaption("Mitglieder");
		members.setImmediate(false);
		
		// verticalLayout_1
		verticalLayout_1 = new VerticalLayout();
		verticalLayout_1.setWidth("100.0%");
		verticalLayout_1.setHeight("-1px");
		verticalLayout_1.setImmediate(false);
		verticalLayout_1.setMargin(false);
		members.setContent(verticalLayout_1);
		
		return members;
	}

}
