
package org.mgnl.nicki.editor.templates;

/*-
 * #%L
 * nicki-editor-templates
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



import java.util.Map;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.vaadin.base.components.DialogBase;
import org.mgnl.nicki.vaadin.base.editor.BaseTreeAction;
import org.mgnl.nicki.vaadin.base.editor.LinkResource;

import com.vaadin.flow.component.html.Anchor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class HtmlPreviewTemplate extends BaseTreeAction {
	private Anchor result;

	private DialogBase previewWindow;
	private NickiContext context;
	private String i18nBase;
	private Map<String, Object> params;

	public HtmlPreviewTemplate(NickiContext context, String i18nBase, Map<String, Object> params) {
		super(null, null, null);
		this.context = context;
		this.i18nBase = i18nBase;
		this.params = params;
		buildMainLayout();
	}

	public void close() {
		previewWindow.close();
	}

	public void execute(TreeData dynamicObject) {
		Template template = (Template) dynamicObject;
		showResultDialog(template, params);
	}

	private void showResultDialog(Template template, Map<String, Object> params) {
		try {
			StringStreamSource streamSource = new StringStreamSource(template, context, params);

			this.result.setHref(new LinkResource(template.getName() + ".html?a=b", streamSource));
			result.setTarget("_blank");
			previewWindow = new DialogBase(I18n.getText(i18nBase + ".preview.window.title"), this);
			previewWindow.setModal(true);
			previewWindow.setWidth("1024px");
			previewWindow.setHeight("520px");
			previewWindow.open();
		} catch (Exception e) {
			log.error("Error", e);
		}
	}

	
	private void buildMainLayout() {
		setWidth("100%");
		setHeight("100%");
		setMargin(true);
		
		// result
		result = new Anchor();
		add(result);
	}

}
