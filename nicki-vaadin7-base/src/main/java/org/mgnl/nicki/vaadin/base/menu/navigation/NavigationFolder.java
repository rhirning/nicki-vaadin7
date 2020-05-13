
package org.mgnl.nicki.vaadin.base.menu.navigation;

/*-
 * #%L
 * nicki-app-menu
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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NavigationFolder implements Serializable, NavigationElement {
	private static final long serialVersionUID = -4136800742337761293L;
	private NavigationLabel label;
	private boolean separator = false;
	
	private List<NavigationEntry> entries = new ArrayList<NavigationEntry>();


	public List<NavigationEntry> getEntries() {
		return entries;
	}

	public NavigationFolder() {
		super();
		this.separator = true;
	}

	public NavigationFolder(NavigationLabel label) {
		super();
		this.label = label;
	}
	
	public void addEntry(NavigationEntry entry) {
		entries.add(entry);
	}

	public NavigationLabel getLabel() {
		return label;
	}

	public boolean isSeparator() {
		return separator;
	}

	@Override
	public String getNavigationCaption() {
		return label.getCaption();
	}

}
