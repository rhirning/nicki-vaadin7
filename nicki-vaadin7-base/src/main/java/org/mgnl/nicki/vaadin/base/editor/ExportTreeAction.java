
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


import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.vaadin.base.components.DialogBase;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public class ExportTreeAction extends BaseTreeAction {

	private static final long serialVersionUID = 8567493886374796976L;


	
	private VerticalLayout mainLayout;
	
	private HorizontalLayout horizontalLayout_1;
	
	private Anchor exportTree;
	
	private Anchor exportElement;
	private DialogBase previewWindow;
	private NickiContext context;
	private String i18nBase;


	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	public ExportTreeAction(NickiContext context, Class<? extends TreeData> classDefinition,
			String name, String i18nBase) {
		super(classDefinition, name, null);
		this.context = context;
		this.i18nBase = i18nBase;
		buildMainLayout();
		add(mainLayout);
	}

	public void execute(TreeData dynamicObject) {
		

		exportElement.setText(I18n.getText(i18nBase + ".export.exportElement"));
		exportElement.getElement().setAttribute("download", true);
		exportElement.getElement().setAttribute("type", "text/xml");
		exportElement.setHref(new StreamResource(StringUtils.replace(dynamicObject.getSlashPath(""), "/", "_") + ".xml", () -> ExportStreamSource.getStream(dynamicObject, context, true) ));

		exportTree.setText(I18n.getText(i18nBase + ".export.exportTree"));
		exportTree.getElement().setAttribute("download", true);
		exportTree.getElement().setAttribute("type", "text/xml");
		exportTree.setHref(new StreamResource(StringUtils.replace(dynamicObject.getSlashPath(""), "/", "_") + ".xml", () -> ExportStreamSource.getStream(dynamicObject, context, false) ));

		// TODO: is this necessary?
//		if (this.getParent().isPresent()) {
//			this.setParent(null);
//		}
		previewWindow = new DialogBase(I18n.getText(i18nBase + ".export.window.title"), this);
		previewWindow.setModal(true);
//		previewWindow.setWidth("1024px");
//		previewWindow.setHeight("520px");
		previewWindow.open();
	}

	
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.add(horizontalLayout_1);
		return mainLayout;
	}

	
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(true);
		horizontalLayout_1.setSpacing(true);
		
		// exportElement
		exportElement = new Anchor();
		exportElement.setText("Element exportiern");
		exportElement.setWidth("-1px");
		exportElement.setHeight("-1px");
		horizontalLayout_1.add(exportElement);
		
		// exportTree
		exportTree = new Anchor();
		exportTree.setText("Baum exportieren");
		exportTree.setWidth("-1px");
		exportTree.setHeight("-1px");
		horizontalLayout_1.add(exportTree);
		
		return horizontalLayout_1;
	}
}
