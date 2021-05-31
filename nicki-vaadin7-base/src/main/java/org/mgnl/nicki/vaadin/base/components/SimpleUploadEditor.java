
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
import org.mgnl.nicki.vaadin.base.editor.PropertyStreamSource;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.StreamResource;

@SuppressWarnings("serial")
public class SimpleUploadEditor extends HorizontalLayout implements Receiver {

	private Anchor link;
	private Upload upload;
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private DynamicObject dynamicObject;
	private String attributeName;


	public SimpleUploadEditor(String i18nBase, DynamicObject dynamicObject, String attributeName, String filename, String mimeType) {
		this.dynamicObject = dynamicObject;
		this.attributeName = attributeName;
		// editor
		buildFileLayout();

		if (dynamicObject.get(attributeName) == null) {
			link.setVisible(false);
		}
		// "Download"
		
		link.setText(I18n.getText(i18nBase + ".link.caption"));
		link.setTarget("_blank");
		StreamResource streamResource = new PropertyStreamSource(this.dynamicObject, this.attributeName, filename);
		link.setHref(streamResource);

		// upload.setButtonCaption(I18n.getText(i18nBase + ".upload.caption"));
		upload.setReceiver(this);
		upload.addSucceededListener(event -> {
			dynamicObject.put(attributeName, bos.toByteArray());
			// "File erfolgreich hochgeladen: " + bos.size() + " Bytes. Bitte Speichern"
			Notification.show(I18n.getText(i18nBase + ".upload.success", "" + bos.size()));
			link.setEnabled(true);
		});
		upload.addFailedListener(event -> {
			Notification.show(I18n.getText(i18nBase + ".upload.fail"), Type.ERROR_MESSAGE);
		});
	}
	

	private void buildFileLayout() {
		setWidth("-1px");
		setHeight("-1px");
		setMargin(true);
		setSpacing(true);
		
		// link
		link = new Anchor();
		link.setWidth("-1px");
		link.setHeight("-1px");
		add(link);
		
		// upload_1
		upload = new Upload();
		upload.setWidth("-1px");
		upload.setHeight("-1px");
		add(upload);
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		bos.reset();
		return bos;
	}

}
