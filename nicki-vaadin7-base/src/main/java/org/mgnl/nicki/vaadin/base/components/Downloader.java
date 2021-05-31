package org.mgnl.nicki.vaadin.base.components;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

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


public class Downloader {


	public static void showDownload(String title,  StreamResource source) {
		VerticalLayout layout = new VerticalLayout();
		
		layout.setMargin(true);
		layout.setSpacing(true);
		Anchor anchor = new Anchor(source, title);
		layout.add(anchor);
		
		Dialog dialog = new Dialog(layout);
		dialog.setHeight("200px");
		dialog.setWidth("200px");
		dialog.setModal(true);
		dialog.open();
	}
	
}
