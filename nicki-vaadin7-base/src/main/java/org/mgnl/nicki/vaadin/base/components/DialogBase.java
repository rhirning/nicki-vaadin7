
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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class DialogBase extends Dialog {
	private static final long serialVersionUID = -3504431507552994635L;
	
	private Div div;

	public DialogBase(Command command) {
		this(command.getTitle());
	}
	


	public DialogBase(String title) {
		init();
		setCaption(title);
	}

	public DialogBase(String title, Component components) {
		this(title);
		div.add(components);
	}



	private void setCaption(String title) {
		Span titleSpan = new Span(title);
		div.addComponentAsFirst(titleSpan);
	}



	private void init() {

        div = new Div();
        div.setSizeUndefined();
        add(div);
		
	}

	public void setCompositionRoot(Component component) {
		div.add(component);
	}


}
