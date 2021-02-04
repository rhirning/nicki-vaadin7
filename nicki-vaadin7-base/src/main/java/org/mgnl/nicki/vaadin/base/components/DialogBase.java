
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


import org.mgnl.nicki.vaadin.base.command.Command;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DialogBase extends Window {
	private static final long serialVersionUID = -3504431507552994635L;
	
	private VerticalLayout layout;

	public DialogBase(Command command) {
		setCaption(command.getTitle());
		init();

        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setHeight("100%");
        layout.setMargin(false);
        layout.setSpacing(false);
        // make it undefined for auto-sizing window
//        layout.setSizeUndefined();
        setContent(layout);
		
	}
	


	public DialogBase(String title) {
		setCaption(title);
		init();
	}



	private void init() {

        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setHeight("100%");
        layout.setMargin(false);
        layout.setSpacing(false);
        // make it undefined for auto-sizing window
//        layout.setSizeUndefined();
        setContent(layout);
		
	}

	public void setCompositionRoot(Component component) {
		layout.addComponent(component);
	}


}
