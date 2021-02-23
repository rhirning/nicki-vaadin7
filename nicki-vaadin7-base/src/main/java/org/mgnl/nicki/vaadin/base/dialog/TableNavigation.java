
package org.mgnl.nicki.vaadin.base.dialog;

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


import java.util.List;

import org.mgnl.nicki.vaadin.base.helper.UIHelper;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class TableNavigation extends NavigationBase implements Navigation {
	private static final long serialVersionUID = -4231539383235849692L;
	private VerticalLayout layout;
	private Grid<NavigationElement> table;
	private NavigationEntry selected;
	
	public TableNavigation(NavigationSelector mainView) {
		super(mainView);
		buildMainLayout();
		setCompositionRoot(layout);
		setSizeFull();
	}

	private VerticalLayout buildMainLayout() {
		layout = new VerticalLayout();
		layout.setHeight("100%");
		Panel panel = new Panel();
		panel.setHeight("100px");
		panel.setStyleName("logo");
		layout.addComponent(panel);
		table = new Grid<>();
		UIHelper.setImmediate(table, true);
		table.setSelectionMode(SelectionMode.SINGLE);
		table.addColumn(NavigationElement::getNavigationCaption);
		while (table.getHeaderRowCount() > 0) {
			table.removeHeaderRow(0);
		}
		table.setWidth("100%");
		table.setHeight("100%");

		table.addSelectionListener(event -> {
				if (table.asSingleSelect().getValue() instanceof NavigationEntry) {
					NavigationEntry entry = (NavigationEntry) table.asSingleSelect().getValue();
					if (entry != selected) {
						if (select(entry)) {
							selected = entry;
						} else {
							table.asSingleSelect().setValue(selected);
						}
					}
				} else {
					if (selected != null) {
						table.asSingleSelect().setValue(selected);
					}
				}
		});
		
		table.setStyleGenerator(item -> {
				if (item instanceof NavigationFolder) {
					return "folder";
				}
				else if (item instanceof NavigationEntry) {
					return "entry";
				} else if (item instanceof NavigationSeparator) {
					return "separator";
				}

				return null;
		});

		layout.addComponent(table);
		layout.setExpandRatio(table, 1.0f);
		//layout.setExpandRatio(panel, 0.0f);
		return layout;
	}
	

	@Override
	public void init(List<NavigationFolder> navigationFolders) {
		initContainer();
		for (NavigationFolder folder : navigationFolders) {
			if (folder.isSeparator()) {
				add(new NavigationSeparator());
			} else {
				add(folder);
				for (NavigationEntry entry : folder.getEntries()) {
					add(entry);
				}
			}
		}
		table.setItems(getList());
	}
}
