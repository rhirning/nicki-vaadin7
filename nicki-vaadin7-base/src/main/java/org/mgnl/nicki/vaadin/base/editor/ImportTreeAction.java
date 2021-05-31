
package org.mgnl.nicki.vaadin.base.editor;

/*-
 * #%L
 * nicki-vaadin-base
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


import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.util.XMLImporter;
import org.mgnl.nicki.vaadin.base.components.DialogBase;
import org.mgnl.nicki.vaadin.base.notification.Notification;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FailedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public class ImportTreeAction extends BaseTreeAction {

	private static final long serialVersionUID = 8567493886374796976L;


	private VerticalLayout mainLayout;
	private Upload upload;
	MemoryBuffer buffer = new MemoryBuffer();
	private DialogBase previewWindow;
	private NickiContext context;
	private String i18nBase;
	private TreeData dynamicObject;
	private TreeEditor treeEditor;


	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	public ImportTreeAction(TreeEditor treeEditor, Class<? extends DynamicObject> classDefinition,
			String name, String i18nBase) {
		super(classDefinition, name, null);
		this.treeEditor = treeEditor;
		this.context = treeEditor.getNickiContext();
		this.i18nBase = i18nBase;
		buildMainLayout();
		add(mainLayout);
	}

	public void execute(TreeData dynamicObject) {
		this.dynamicObject = dynamicObject;
		// TODO: Texte übersetzen
		//importComponent.set(I18n.getText(i18nBase + ".import.caption"));
		//importComponent.setButtonCaption(I18n.getText(i18nBase + ".import.button.caption"));
		upload.addSucceededListener(e -> uploadSucceeded(e));
		upload.addFailedListener(e -> uploadFailed(e));

		// TODO: is this necessary?
//		if (null != this.getParent()) {
//			this.setParent(null);
//		}
		previewWindow = new DialogBase(I18n.getText(i18nBase + ".import.window.title"), this);
		previewWindow.setModal(true);
//		previewWindow.setWidth("1024px");
//		previewWindow.setHeight("520px");
		previewWindow.open();
	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// exportElement
		upload = new Upload(buffer);
		upload.setWidth("-1px");
		upload.setHeight("-1px");
		mainLayout.add(upload);		
		
		return mainLayout;
	}

	public void close() {
		UI.getCurrent().close();
	}

	public void uploadFailed(FailedEvent event) {
		Notification.show("Import failed", "Importing "
                + event.getFileName() + " of type '"
                + event.getMIMEType() + "' failed.", Notification.Type.ERROR_MESSAGE);
	}

	public void uploadSucceeded(SucceededEvent event) {
		try {
			XMLImporter importer = new XMLImporter(context, dynamicObject.getPath(), buffer.getInputStream());
			importer.create();
			treeEditor.refresh(dynamicObject);
			Notification.show("Import successful", Notification.Type.HUMANIZED_MESSAGE);
		} catch (Exception e) {
			Notification.show("Import failed", "Importing "
	                + event.getFileName() + " of type '"
	                + event.getMIMEType() + "' failed.", Notification.Type.ERROR_MESSAGE);
		}
	}

}
