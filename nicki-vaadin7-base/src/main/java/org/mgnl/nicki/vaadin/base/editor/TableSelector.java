
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


import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;

@SuppressWarnings("serial")
public class TableSelector<T> extends BasicNickiSelector<T> {
	private Grid<T> component = new Grid<>();

	public TableSelector() {
		super();
		super.setComponent(component);
	}

	public void setSelectable(boolean selectable) {
		component.setSelectionMode(SelectionMode.SINGLE);
	}

}
