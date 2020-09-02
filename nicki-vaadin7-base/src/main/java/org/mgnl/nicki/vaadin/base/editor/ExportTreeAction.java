
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

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportTreeAction extends BaseTreeAction {

	private static final long serialVersionUID = 8567493886374796976L;


	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Button close;
	@AutoGenerated
	private Link exportTree;
	@AutoGenerated
	private Link exportElement;
	private Window previewWindow;
	private NickiContext context;
	private String i18nBase;


	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	public ExportTreeAction(NickiContext context, Class<? extends TreeData> classDefinition,
			String name, String i18nBase) {
		super(classDefinition, name);
		this.context = context;
		this.i18nBase = i18nBase;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	public void execute(TreeData dynamicObject) {
		
		close.addClickListener(event -> {
				try {
					close();
				} catch (Exception e) {
					log.error("Error", e);
				}
		});

		exportElement.setCaption(I18n.getText(i18nBase + ".export.exportElement"));
		exportElement.setTargetName("_blank");
		ExportStreamSource exportStreamSource = new ExportStreamSource(dynamicObject, context, true);
		exportElement.setResource(
			new LinkResource(exportStreamSource, 
					StringUtils.replace(dynamicObject.getSlashPath(""), "/", "_") + ".xml",
				"text/xml"));

		exportTree.setCaption(I18n.getText(i18nBase + ".export.exportTree"));
		exportTree.setTargetName("_blank");
		exportStreamSource = new ExportStreamSource(dynamicObject, context, false);
		exportTree.setResource(
			new LinkResource(exportStreamSource, 
					StringUtils.replace(dynamicObject.getSlashPath(""), "/", "_") + ".xml",
				"text/xml"));

		if (null != this.getParent()) {
			this.setParent(null);
		}
		previewWindow = new Window(I18n.getText(i18nBase + ".export.window.title"), this);
		previewWindow.setModal(true);
		previewWindow.setWidth(1024, Unit.PIXELS);
		previewWindow.setHeight(520, Unit.PIXELS);
		UI.getCurrent().addWindow(previewWindow);
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1, "top:0.0px;left:0.0px;");
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(true);
		horizontalLayout_1.setSpacing(true);
		
		// exportElement
		exportElement = new Link();
		exportElement.setCaption("Element exportiern");
		exportElement.setImmediate(true);
		exportElement.setWidth("-1px");
		exportElement.setHeight("-1px");
		horizontalLayout_1.addComponent(exportElement);
		
		// exportTree
		exportTree = new Link();
		exportTree.setCaption("Baum exportieren");
		exportTree.setImmediate(true);
		exportTree.setWidth("-1px");
		exportTree.setHeight("-1px");
		horizontalLayout_1.addComponent(exportTree);
		
		// close
		close = new Button();
		close.setCaption("Schliessen");
		close.setImmediate(false);
		close.setWidth("-1px");
		close.setHeight("-1px");
		horizontalLayout_1.addComponent(close);
		
		return horizontalLayout_1;
	}

	public void close() {
		UI.getCurrent().removeWindow(previewWindow);
	}

}
