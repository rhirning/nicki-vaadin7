
package org.mgnl.nicki.vaadin.base.components;

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


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.editor.LinkResource;
import org.mgnl.nicki.vaadin.base.editor.PropertyStreamSource;
import org.mgnl.nicki.vaadin.base.helper.UIHelper;

import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Upload;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.Upload.FailedEvent;
import com.vaadin.v7.ui.Upload.FailedListener;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;

@SuppressWarnings("serial")
public class SimpleUploadEditor extends CustomComponent implements Receiver, SucceededListener, FailedListener {

	private HorizontalLayout fileLayout;
	private Link link;
	private Upload upload;
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private DynamicObject dynamicObject;
	private String attributeName;
	private String i18nBase;


	public SimpleUploadEditor(String i18nBase, DynamicObject dynamicObject, String attributeName, String filename, String mimeType) {
		this.i18nBase = i18nBase;
		this.dynamicObject = dynamicObject;
		this.attributeName = attributeName;
		// editor
		HorizontalLayout layout = buildFileLayout();

		if (dynamicObject.get(attributeName) == null) {
			link.setVisible(false);
		}
		// "Download"
		
		link.setCaption(I18n.getText(i18nBase + ".link.caption"));
		link.setTargetName("_blank");
		StreamSource streamSource = new PropertyStreamSource(this.dynamicObject, this.attributeName);
		link.setResource(new LinkResource(streamSource, filename, mimeType));

		// upload.setButtonCaption(I18n.getText(i18nBase + ".upload.caption"));
		upload.setReceiver(this);
		upload.addSucceededListener(this);
		upload.addFailedListener(this);
		
		setCompositionRoot(layout);
	}
	

	private HorizontalLayout buildFileLayout() {
		// common part: create layout
		fileLayout = new HorizontalLayout();
		fileLayout.setWidth("-1px");
		fileLayout.setHeight("-1px");
		fileLayout.setMargin(true);
		fileLayout.setSpacing(true);
		
		// link
		link = new Link();
		link.setCaption("Link");
		link.setWidth("-1px");
		link.setHeight("-1px");
		fileLayout.addComponent(link);
		
		// upload_1
		upload = new Upload();
		UIHelper.setImmediate(upload, true);
		upload.setWidth("-1px");
		upload.setHeight("-1px");
		fileLayout.addComponent(upload);
		
		return fileLayout;
	}


	@Override
	public void uploadFailed(FailedEvent event) {
		// "Fehler beim Hochladendes Files"
		Notification.show(I18n.getText(i18nBase + ".upload.fail"), Type.ERROR_MESSAGE);
	}


	@Override
	public void uploadSucceeded(SucceededEvent event) {
		dynamicObject.put(attributeName, bos.toByteArray());
		// "File erfolgreich hochgeladen: " + bos.size() + " Bytes. Bitte Speichern"
		Notification.show(I18n.getText(i18nBase + ".upload.success", "" + bos.size()));
		link.setEnabled(true);
	}


	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		bos.reset();
		return bos;
	}

}
