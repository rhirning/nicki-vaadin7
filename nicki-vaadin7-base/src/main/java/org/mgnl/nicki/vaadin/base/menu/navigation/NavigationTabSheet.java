package org.mgnl.nicki.vaadin.base.menu.navigation;

/*-
 * #%L
 * nicki-vaadin7-base
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
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

import java.util.List;

import org.mgnl.nicki.vaadin.base.components.NickiVerticalTabSheet;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;

@SuppressWarnings("serial")
public class NavigationTabSheet extends NickiVerticalTabSheet  {
	private NavigationMainView appLayout;
	
	public NavigationTabSheet(NavigationMainView appLayout) {
		this.appLayout = appLayout;
		setWidthFull();
	}

	@Override
	public boolean showSelected(Component selectedPage, boolean checkModify) {
		return appLayout.showView(selectedPage, checkModify);
	}

	public void init(List<NavigationFolder> navigationFolders) {
		Tab tab;
		for (NavigationFolder folder : navigationFolders) {
			if (folder.isSeparator()) {
				tab = addTab((Component) null, new NavigationSeparator());
				tab.getElement().getClassList().add("nav_separator");
			} else {
				tab = addTab(null, folder.getText());
				tab.getElement().getClassList().add("nav_folder");
				for (NavigationEntry entry : folder.getEntries()) {
					tab = addTab(entry.getView(), entry.getCaption());
					tab.getElement().getClassList().add("nav_entry");
				}
			}
		}
	}

	public void selectInNavigation(NavigationEntry entry) {
		// TODO Auto-generated method stub
		
	}


}
