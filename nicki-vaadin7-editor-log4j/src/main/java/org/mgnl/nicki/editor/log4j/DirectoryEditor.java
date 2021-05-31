
package org.mgnl.nicki.editor.log4j;

/*-
 * #%L
 * nicki-editor-log4j
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



import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.mgnl.nicki.core.data.FileEntry;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.vaadin.base.editor.ClassEditor;
import org.mgnl.nicki.vaadin.base.editor.NickiTreeEditor;
import org.mgnl.nicki.vaadin.base.helper.ContainerHelper;
import org.mgnl.nicki.vaadin.base.helper.ValuePair;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.StreamResource;

@SuppressWarnings("serial")
public class DirectoryEditor extends VerticalLayout implements ClassEditor {
		
	private SplitLayout horizontalSplitPanel;

	
	private VerticalLayout propertiesLayout;

	
	private VerticalLayout tableLayout;

	
	private Grid<File> table;

	private FileEntry fileEntry;


	public void setDynamicObject(NickiTreeEditor nickiEditor,
			TreeData dynamicObject) {
		this.fileEntry = (FileEntry) dynamicObject;
		buildMainLayout();
		if (this.fileEntry != null && this.fileEntry.getFile() != null) {
			File list[] = this.fileEntry.getFile().listFiles((pathname) -> {
					return pathname.isFile();
			});
			if (list != null && list.length > 0) {
				Collection<File> files = Arrays.asList(list);
				table.addColumn(File::getName);
				table.setItems(files);
				table.setSelectionMode(SelectionMode.SINGLE);
				table.addSelectionListener(event -> {
					if (event.getFirstSelectedItem().isPresent()) {
							File itemId = (File) event.getFirstSelectedItem().get();
							showItem(itemId);
					}
				});
			}
		}
	}

	protected void showItem(final File file) {
		clearItem();
		VerticalLayout topLayout = new VerticalLayout();
		propertiesLayout.add(topLayout);
		Grid<ValuePair> propertiesTable = new Grid<ValuePair>();
		propertiesTable.setWidth("100%");
		propertiesTable.setHeight("100%");
		String[] propertiesNames = {"name", "path", "size", "lastModified", "mod"};
		Collection<ValuePair> propertiesContainer = ContainerHelper.getDataContainer(new FileWrapper(file), propertiesNames , null);
		propertiesTable.setItems(propertiesContainer);
		propertiesTable.addColumn(ValuePair::getName).setWidth("200px");
		propertiesTable.addColumn(ValuePair::getValue).setWidth("600px");
// TODO:		propertiesTable.setPageLength(propertiesContainer.size());
		topLayout.add(propertiesTable);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		topLayout.add(buttonLayout);

		Button viewButton = new Button("View");
		buttonLayout.add(viewButton);

		FileResource fileResource = new FileResource(file);
		StreamResource streamResource = new StreamResource(file.getName(), ()->fileResource.getStream());
		streamResource.setCacheTime(0);
		Anchor downloadButton = new Anchor(streamResource, "Download");
		buttonLayout.add(downloadButton);
		

		VerticalLayout panelLayout = new VerticalLayout();
		panelLayout.setWidth("100%");
		panelLayout.setHeight("100%");
		
		final TextArea textArea = new TextArea();
		textArea.setWidth("100%");
		textArea.setHeight("100%");
		panelLayout.add(textArea);
		panelLayout.setFlexGrow(1, textArea);
		
		
		propertiesLayout.add(panelLayout);
		propertiesLayout.setFlexGrow(1, panelLayout);
		viewButton.addClickListener(event -> {
				try {
					textArea.setValue(FileUtils.readFileToString(file, Charset.forName("UTF-8")));
				} catch (IOException e) {
					e.printStackTrace();
				}
		});
		

	}

	protected void clearItem() {
		this.propertiesLayout.removeAll();
	}

	public void save() throws DynamicObjectException, NamingException {
	}


	
	private void buildMainLayout() {
		setSizeFull();
		setMargin(false);
		
		// horizontalSplitPanel
		horizontalSplitPanel = buildHorizontalSplitPanel();
		add(horizontalSplitPanel);
	}

	
	private SplitLayout buildHorizontalSplitPanel() {
		// common part: create layout
		horizontalSplitPanel = new SplitLayout();
		horizontalSplitPanel.setOrientation(Orientation.HORIZONTAL);
		horizontalSplitPanel.setWidth("100.0%");
		horizontalSplitPanel.setHeight("100.0%");
		
		// tableLayout
		tableLayout = buildTableLayout();
		horizontalSplitPanel.addToPrimary(tableLayout);
		
		// propertiesLayout
		propertiesLayout = buildPropertiesLayout();
		horizontalSplitPanel.addToSecondary(propertiesLayout);
		
		return horizontalSplitPanel;
	}

	
	private VerticalLayout buildTableLayout() {
		// common part: create layout
		tableLayout = new VerticalLayout();
		tableLayout.setWidth("100.0%");
		tableLayout.setHeight("100.0%");
		tableLayout.setMargin(false);
		
		// table_1
		table = new Grid<>();
		table.setWidth("100.0%");
		table.setHeight("100.0%");
		tableLayout.add(table);
		tableLayout.setFlexGrow(1, table);
		
		return tableLayout;
	}

	
	private VerticalLayout buildPropertiesLayout() {
		// common part: create layout
		propertiesLayout = new VerticalLayout();
		propertiesLayout.setWidth("100.0%");
		propertiesLayout.setHeight("100.0%");
		propertiesLayout.setMargin(false);

		
		return propertiesLayout;
	}

}
