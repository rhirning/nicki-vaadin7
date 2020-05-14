
package org.mgnl.nicki.editor.catalogs;

/*-
 * #%L
 * nicki-editor-catalogs
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



import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.Target;
import org.mgnl.nicki.core.context.TargetFactory;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.dynamic.objects.objects.Org;
import org.mgnl.nicki.shop.base.objects.Catalog;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.objects.CatalogPage;
import org.mgnl.nicki.vaadin.base.application.AccessGroup;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.application.ShowWelcomeDialog;
import org.mgnl.nicki.vaadin.base.editor.DynamicObjectRoot;
import org.mgnl.nicki.vaadin.base.editor.Icon;
import org.mgnl.nicki.vaadin.base.editor.ShowAllFilter;
import org.mgnl.nicki.vaadin.base.editor.TreeEditor;

import com.vaadin.ui.Component;

@AccessGroup(name = {"nickiAdmins", "IDM-Development"})
@SuppressWarnings("serial")
@ShowWelcomeDialog(
		configKey="nicki.app.editor.catalog.useWelcomeDialog",
		groupsConfigName="nicki.app.editor.catalog.useWelcomeDialogGroups")
public class CatalogEditor extends NickiApplication {

	public CatalogEditor() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getEditor() {
		DataProvider treeDataProvider = new DynamicObjectRoot(Config.getString("nicki.catalogs.basedn"), new ShowAllFilter());
		TreeEditor editor = new TreeEditor(this, getNickiContext(), treeDataProvider, getI18nBase());
		editor.configureClass(Org.class, null, TreeEditor.CREATE.DENY, TreeEditor.DELETE.DENY, TreeEditor.RENAME.DENY, Catalog.class);
		editor.configureClass(Catalog.class, Icon.FOLDER, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW, CatalogPage.class);
		editor.configureClass(CatalogPage.class, Icon.FOLDER, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW, CatalogPage.class, CatalogArticle.class);
		editor.configureClass(CatalogArticle.class, Icon.DOCUMENT, TreeEditor.CREATE.ALLOW, TreeEditor.DELETE.ALLOW, TreeEditor.RENAME.ALLOW);
//		editor.addAction(new PreviewShop(Catalog.class, I18n.getText(getI18nBase() +  ".action.preview")));
//		editor.setClassEditor(CatalogPage.class, new CatalogPageViewer());
		editor.initActions();
		return editor;
	}

	/*
	public class PreviewShop implements TreeAction, Serializable {

		private Class<? extends DynamicObject> targetClass;
		private String name;
		private Window previewWindow;
		public PreviewShop(Class<? extends DynamicObject> classDefinition, String name) {
			this.targetClass = classDefinition;
			this.name = name;
		}

		public void execute(Window parentWindow, DynamicObject dynamicObject) {
			 previewWindow = new Window(I18n.getText(getI18nBase() + ".preview.window.title"),
					new ShopWindow(this, getNickiContext().getUser(), (Catalog)dynamicObject, getI18nBase()));
			 previewWindow.setModal(true);
			 previewWindow.setWidth(1024, Sizeable.UNITS_PIXELS);
			 previewWindow.setHeight(520, Sizeable.UNITS_PIXELS);
			 getMainWindow().addWindow(previewWindow);
		}

		public String getName() {
			return this.name;
		}

		public void close() {
			getMainWindow().removeWindow(previewWindow);
		}

		public Class<? extends DynamicObject> getTargetClass() {
			return targetClass;
		}
		
	}
	*/

	@Override
	public Target getTarget() {
		return TargetFactory.getDefaultTarget();
	}

	@Override
	public String getI18nBase() {
		return "nicki.editor.catalogs";
	}
}
