
package org.mgnl.nicki.vaadin.base.menu.navigation;

import java.util.HashMap;

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
import java.util.Map;

import org.mgnl.nicki.vaadin.base.components.NoHeaderGrid;
import org.mgnl.nicki.vaadin.base.menu.application.MainView;
import org.mgnl.nicki.vaadin.base.menu.application.TableNavigationMainView;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public class TableNavigation extends NavigationBase implements Navigation {
	private static final long serialVersionUID = -4231539383235849692L;
	public static final String LOGO_PATH = "logoPpath";
	public static final String LOGO_HEIGHT = "logoHeight";
	public static final String LOGO_WIDTH = "logoWidth";
	public static final String NAVIGATION_WIDTH = "navigationWidth";
	private Grid<NavigationElement> table;
	private NavigationEntry selected;
	private Map<String, String> config = new HashMap<String, String>();
	
	public TableNavigation(NavigationMainView tableNavigationMainView,  Map<String, String> config ) {
		super(tableNavigationMainView);
		if (config != null) {
			this.config.putAll(config);
		}
		buildMainLayout();
		setSizeFull();
	}

	private void buildMainLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setSpacing(true);
		layout.setPadding(false);
		add(layout);

		VerticalLayout logoLayout = new VerticalLayout();
		logoLayout.setWidthFull();
		logoLayout.setMargin(false);
		logoLayout.setPadding(false);
		layout.add(logoLayout);
		
		if (config.containsKey(LOGO_PATH)) {			
			StreamResource resource = new StreamResource("logo.png", () -> TableNavigation.class.getResourceAsStream(config.get(LOGO_PATH)));
			Image image = new Image(resource, "Restart");
			if (config.containsKey(LOGO_HEIGHT)) {
				image.setHeight(config.get(LOGO_HEIGHT));
			}
			if (config.containsKey(LOGO_WIDTH)) {
				image.setWidth(config.get(LOGO_WIDTH));
			}
			logoLayout.add(image);
			logoLayout.setAlignItems(Alignment.CENTER);
			image.addClickListener(event -> restart());
		} else {
			Span image = new Span("Restart");
			image.setHeight(LOGO_HEIGHT);
			logoLayout.add(image);
			image.addClickListener(event -> restart());
		}
		table = new NoHeaderGrid<>();
		table.setSelectionMode(SelectionMode.SINGLE);
		String width = config.containsKey(NAVIGATION_WIDTH) ? config.get(NAVIGATION_WIDTH) : "-1px";
		table.addComponentColumn(NavigationElement::getNavigationCaption).setWidth(width);
		table.setSizeFull();		

		table.addSelectionListener(event -> {
				if (table.asSingleSelect().getValue() instanceof NavigationEntry) {
					NavigationEntry entry = (NavigationEntry) table.asSingleSelect().getValue();
					if (entry != selected) {
						if (select(entry)) {
							selected = entry;
						} else {
							table.select(selected);
						}
					}
				} else {
					if (selected != null) {
						table.select(selected);
					}
				}
		});
		
		table.setClassNameGenerator(item -> {
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
		
		
		
		layout.add(table);
		layout.setFlexGrow(1, table);
	}
	

	public void restart() {
		this.table.deselectAll();
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
		table.setItems(getContainer());
	}
	
	@Override
	public void selectInNavigation(NavigationEntry entry) {
		table.select(entry);
	}
}
