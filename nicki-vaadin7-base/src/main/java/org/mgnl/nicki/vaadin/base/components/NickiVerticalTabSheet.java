package org.mgnl.nicki.vaadin.base.components;

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

import lombok.Getter;

@SuppressWarnings("serial")
public class NickiVerticalTabSheet extends HorizontalLayout {
	private Tabs tabs = new Tabs();
	private Map<Tab, Component> tabsToPages = new HashMap<>();
	private @Getter Div pagesDiv = new Div();
	private Tab activeTab;
		
	public NickiVerticalTabSheet() {
		tabs.setOrientation(Orientation.VERTICAL);
		tabs.setSizeFull();
		setMargin(false);
		setSpacing(false);
		setPadding(false);
		
		tabs.addSelectedChangeListener(event -> {
			if (tabs.getSelectedTab() != activeTab) {
			    Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			    if (!showSelected(selectedPage, true)) {
			    	Tab previousTab = event.getPreviousTab();
			    	tabs.setSelectedTab(previousTab);
			    } else {
			    	activeTab = tabs.getSelectedTab();
			    }
			}
		});
		add(tabs, pagesDiv);
	}
	

	public boolean showSelected(Component selectedPage, boolean checkModify) {
		getPagesDiv().removeAll();
		getPagesDiv().add(selectedPage);
		return true;
		
	}
	
	public Tab addTab(Component content, String label) {
		return addTab(content, new Span(label));
	}
	
	public Tab addTab(Component content, String label, VaadinIcon icon) {
		return addTab(content, new Icon(icon), new Span(label));
	}

	public Tab addTab(Component content, Component... components) {
		Tab tab = new Tab(components);
		tabsToPages.put(tab, content);
		tabs.add(tab);
		return tab;
	}
}
