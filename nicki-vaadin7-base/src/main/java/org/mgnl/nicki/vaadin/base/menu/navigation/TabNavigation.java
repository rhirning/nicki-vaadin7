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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

@SuppressWarnings("serial")
public class TabNavigation extends NavigationBase implements Navigation {
	private Tabs tabs = new Tabs();
	private Map<Tab, Component> tabsToPages = new HashMap<>();
	private Div pagesDiv = new Div();
	private List<Div> pages = new ArrayList<Div>();
	private List<Tab> tabList = new ArrayList<Tab>();
	private HorizontalLayout layout;
	
	
	
	public TabNavigation(NavigationMainView mainView) {
		super(mainView);
		tabs.setOrientation(Orientation.VERTICAL);
		layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		
		tabs.addSelectedChangeListener(event -> {
		    tabsToPages.values().forEach(page -> page.setVisible(false));
		    Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
		    if (selectedPage != null) {
		    	selectedPage.setVisible(true);
		    }
		});
		add(tabs, pagesDiv);
	}
	
	public void addTab(Component content, String label) {
		addTab(content, new Span(label));
	}
	
	public void addTab(String label) {
		addTab(null, new Span(label));
	}
	
	public void addTab(Component content, String label, VaadinIcon icon) {
		addTab(content, new Icon(icon), new Span(label));
	}

	public void addTab(Component content, Component... components) {
		Tab tab = new Tab(components);
		tabList.add(tab);
		if (content != null) {
			Div page = new Div(content);
			page.setVisible(false);
			pages.add(page);
			tabsToPages.put(tab, page);
			pagesDiv.removeAll();
			pagesDiv.add(pages.toArray(new Div[0]));
		}
		tabs.add(tab);
	}

	public Tab getTab(int i) {
		if (tabList.size() > 0 && i >=0 && i < tabList.size()) {
			return tabList.get(i);
		} else {
			return null;
		}
	}

	@Override
	public void init(List<NavigationFolder> navigationFolders) {
		for (NavigationFolder folder : navigationFolders) {
			if (folder.isSeparator()) {
				addTab((Component) null, new NavigationSeparator());
			} else {
				addTab(folder);
				for (NavigationEntry entry : folder.getEntries()) {
					addTab(entry);
				}
			}
		}
	}

	@Override
	public void selectInNavigation(NavigationEntry entry) {
		// TODO Auto-generated method stub
		
	}
}
