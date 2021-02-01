package org.mgnl.nicki.vaadin.base.components;

/*-
 * #%L
 * nicki-vaadin-base
 * %%
 * Copyright (C) 2017 - 2018 Ralf Hirning
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

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Downloader {


	public static void showDownload(String title,  StreamResource source) {
		FileDownloader fileDownloader = null;
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Button downloadButton = new Button("Download");
		downloadButton.setHeight("-1px");
		downloadButton.setWidth("-1px");
		fileDownloader = new FileDownloader(source);
		if (fileDownloader != null) {
			fileDownloader.extend(downloadButton);
		}
		layout.addComponent(downloadButton);
		Window window = new Window(title, layout);
		window.setHeight("200px");
		window.setWidth("200px");
		window.setModal(true);
		UI.getCurrent().addWindow(window);
	}
	
}
