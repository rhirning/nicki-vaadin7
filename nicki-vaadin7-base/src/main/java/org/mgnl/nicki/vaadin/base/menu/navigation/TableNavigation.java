
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


import java.util.List;

import org.mgnl.nicki.vaadin.base.helper.UIHelper;
import org.mgnl.nicki.vaadin.base.menu.application.MainView;

import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.ColumnHeaderMode;
import com.vaadin.v7.ui.VerticalLayout;

public class TableNavigation extends NavigationBase implements Navigation {
	private static final long serialVersionUID = -4231539383235849692L;
	private static final Object VISIBLE_COLUMNS[] = {"navigationCaption"};
	private VerticalLayout layout;
	private Table table;
	private NavigationEntry selected;
	
	public TableNavigation(MainView mainView) {
		super(mainView);
		buildMainLayout();
		setCompositionRoot(layout);
		setSizeFull();
	}

	private VerticalLayout buildMainLayout() {
		layout = new VerticalLayout();
		layout.setHeight("100%");
		layout.setSpacing(false);
		layout.setMargin(false);
		Panel panel = new Panel();
		panel.setHeight("100px");
		panel.setStyleName("logo");
		layout.addComponent(panel);
		panel.addClickListener(event -> restart());
		table = new Table();
		UIHelper.setImmediate(table, true);
		table.setSelectable(true);
		table.setNullSelectionAllowed(false);
		table.addContainerProperty("navigationCaption", String.class, null);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		table.setWidth("100%");
		table.setHeight("100%");
		table.setPageLength(0);

		table.addValueChangeListener(event -> {
				if (table.getValue() instanceof NavigationEntry) {
					NavigationEntry entry = (NavigationEntry) table.getValue();
					if (entry != selected) {
						if (select(entry)) {
							selected = entry;
						} else {
							table.setValue(selected);
						}
					}
				} else {
					if (selected != null) {
						table.setValue(selected);
					}
				}
		});
		
		table.setCellStyleGenerator( (source, itemId, propertyId) -> {
				if (itemId instanceof NavigationFolder) {
					return "folder";
				}
				else if (itemId instanceof NavigationEntry) {
					return "entry";
				} else if (itemId instanceof NavigationSeparator) {
					return "separator";
				}

				return null;
		});

		layout.addComponent(table);
		layout.setExpandRatio(table, 1.0f);
		//layout.setExpandRatio(panel, 0.0f);
		return layout;
	}
	

	public void restart() {
		this.table.select(null);
		super.restart();
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
		table.setContainerDataSource(getContainer());
	}
	
	@Override
	public void selectInNavigation(NavigationEntry entry) {
		table.select(entry);
	}
}
