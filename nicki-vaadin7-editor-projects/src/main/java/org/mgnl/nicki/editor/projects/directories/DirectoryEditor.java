
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
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class DirectoryEditor extends VerticalLayout implements ClassEditor {
	
	private Button saveButton;
	
	private Label labelDummy;
	
	private VerticalLayout membersLayout;
	
	private HorizontalLayout horizontalLayout_1;
	
	private Label name;
	private NickiTreeEditor nickiEditor;
	private Directory directory;

	public void setDynamicObject(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		this.nickiEditor = nickiEditor;
		this.directory = (Directory) dynamicObject;
		buildMainLayout();

		name.setText(this.directory.getDisplayName());
		for (Member member : getProject().getMembers()) {
			MemberComponent comp = new MemberComponent(directory, member);
			membersLayout.add(comp);
		}
		saveButton.addClickListener(event -> save());
	}
	
	public void save() {
		membersLayout.getChildren().forEach(component ->  {
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
		});
		nickiEditor.refresh(getProject());
	}

	public DirectoryEditor() {
	}
	
	private Project getProject() {
		return (Project) nickiEditor.getParent(directory);
	}

	
	private void buildMainLayout() {
		setWidth("-1px");
		setHeight("-1px");
		setMargin(false);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		add(horizontalLayout_1);
		
		// members
		membersLayout = buildMembers();
		add(membersLayout);
		
		// label_dummy
		labelDummy = new Label();
		labelDummy.setWidth("-1px");
		labelDummy.setHeight("20px");
		add(labelDummy);
		
		// closeButton
		saveButton = new Button();
		saveButton.setWidth("-1px");
		saveButton.setHeight("-1px");
		saveButton.setText("Speichern");
		add(saveButton);
	}

	
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("40px");
		horizontalLayout_1.setMargin(false);
		
		// name
		name = new Label();
		name.setWidth("-1px");
		name.setHeight("-1px");
		name.setText("Name");
		horizontalLayout_1.add(name);
		
		return horizontalLayout_1;
	}

	
	private VerticalLayout buildMembers() {
		
		// verticalLayout_1
		membersLayout = new VerticalLayout();
		membersLayout.setWidth("100.0%");
		membersLayout.setHeight("-1px");
		membersLayout.setMargin(false);
		
		return membersLayout;
	}

}
