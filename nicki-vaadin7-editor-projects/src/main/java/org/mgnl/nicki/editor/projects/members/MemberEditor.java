
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
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class MemberEditor extends VerticalLayout implements ClassEditor {
	
	private Button saveButton;
	
	private Label label_dummy;
	
	private VerticalLayout directoriesLayout;
	
	private HorizontalLayout horizontalLayout_1;
	
	private Label name;
	private NickiTreeEditor nickiEditor;
	private Member member;

	public void save() {
		directoriesLayout.getChildren().forEach(component ->
			{
				if (component instanceof DirectoryComponent) {
					DirectoryComponent dirComponent = (DirectoryComponent) component;
					dirComponent.save();
				}
			}
		);
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

		name.setText(this.member.getDisplayName());
		for (Directory dir : getProject().getDirectories()) {
			DirectoryComponent comp = new DirectoryComponent(member, dir);
			directoriesLayout.add(comp);
		}
		saveButton.addClickListener(event -> save());		
	}


	private Project getProject() {
		return (Project) nickiEditor.getParent(member);
	}

	
	private void buildMainLayout() {
		setWidth("-1px");
		setHeight("-1px");
		setMargin(false);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		add(horizontalLayout_1);
		
		// directories
		directoriesLayout = buildDirectories();
		add(directoriesLayout);
		
		// label_dummy
		label_dummy = new Label();
		label_dummy.setWidth("-1px");
		label_dummy.setHeight("20px");
		add(label_dummy);
		
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

	
	private VerticalLayout buildDirectories() {
		
		// verticalLayout_1
		directoriesLayout = new VerticalLayout();
		// TODO: setCaption
		//directoriesLayout.setCaption("Verzeichnisse");
		directoriesLayout.setWidth("100.0%");
		directoriesLayout.setHeight("-1px");
		directoriesLayout.setMargin(false);
		
		return directoriesLayout;
	}


}
