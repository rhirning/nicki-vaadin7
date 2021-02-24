
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


import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.editor.projects.objects.Project;
import org.mgnl.nicki.vaadin.base.editor.ClassEditor;
import org.mgnl.nicki.vaadin.base.editor.NickiTreeEditor;
import org.mgnl.nicki.vaadin.base.fields.AttributeSelectObjectField;
import org.mgnl.nicki.vaadin.base.fields.AttributeTextAreaField;
import org.mgnl.nicki.vaadin.base.fields.AttributeTextField;
import org.mgnl.nicki.vaadin.base.fields.DynamicAttributeField;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProjectDataView extends CustomComponent implements ClassEditor {
	private static final long serialVersionUID = -8525549254470827769L;
	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private NativeSelect<DynamicObject> projectDeputy;
	@AutoGenerated
	private TextArea projectDescription;
	@AutoGenerated
	private TextField projectDirectory;
	@AutoGenerated
	private TextField projectName;
	
	private DynamicAttributeField<String> projectNameField;
	private DynamicAttributeField<String> projectDirectoryField;
	private DynamicAttributeField<String> projectDescriptionField;
	private DynamicAttributeField<DynamicObject> deputyField;
	private Button saveButton;
	
	private Project project;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 * @param project 
	 */
	public ProjectDataView() {
	}

	public void setDynamicObject(NickiTreeEditor nickiEditor,
			TreeData dynamicObject) {
		this.project = (Project) dynamicObject;
		this.projectNameField = new AttributeTextField();
		this.projectNameField.init("name", project, null);
		this.projectDirectoryField = new AttributeTextField();
		this.projectDirectoryField.init("projectdirectory", project, null);
		this.projectDescriptionField = new AttributeTextAreaField();
		this.projectDescriptionField.init("description", project, null);
		this.deputyField = new AttributeSelectObjectField();
		this.deputyField.init("deputy", project, null);
		
		buildMainLayout();
		setCompositionRoot(mainLayout);
		setWidth("100%");
		setHeight(400, Unit.PIXELS);
		if (project.isProjectDeputyLeader(nickiEditor.getNickiContext().getUser())) {
			this.projectDeputy.setReadOnly(true);
		}
	}

	@SuppressWarnings("unchecked")
	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		
		// top-level component properties
		setWidth("100.0%");
		
		// projectName
		projectName = (TextField) this.projectNameField.getComponent(true);
		projectName.setWidth("400px");
		projectName.setCaption("Projektname");
		mainLayout.addComponent(projectName);
		
		// projectDirectory
		projectDirectory = (TextField) this.projectDirectoryField.getComponent(true);
		projectDirectory.setWidth("400px");
		projectDirectory.setCaption("Verzeichnis");
		mainLayout.addComponent(projectDirectory);
		
		// projectDescription
		projectDescription = (TextArea) this.projectDescriptionField.getComponent(false);
//		projectDescription.setWidth("400px");
		projectDescription.setCaption("Projektbeschreibung");
		mainLayout.addComponent(projectDescription);
		
		// projectDeputy
		projectDeputy = (NativeSelect<DynamicObject>) this.deputyField.getComponent(false);
		projectDeputy.setWidth("400px");
		projectDeputy.setHeight("-1px");
		projectDeputy.setCaption("Stellvertreter");
		mainLayout.addComponent(projectDeputy);
		
		saveButton = new Button(I18n.getText("nicki.editor.generic.button.save"));
		saveButton.addClickListener(event -> {
				try {
					project.update();
					Notification.show(I18n.getText("nicki.editor.save.info"));
				} catch (DynamicObjectException e) {
					Notification.show(I18n.getText("nicki.editor.save.error"), 
							e.getMessage(), Type.ERROR_MESSAGE);
				}
		});
		mainLayout.addComponent(saveButton);

		
		return mainLayout;
	}

	@Override
	public void save() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
