
package org.mgnl.nicki.shop.renderer;

/*-
 * #%L
 * nicki-shop
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


import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle.STATUS;
import org.mgnl.nicki.shop.base.objects.MultipleInstancesCatalogArticle;
import org.mgnl.nicki.shop.base.objects.CatalogValueProvider.TYPE;
import com.vaadin.event.Action;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class BaseTableRenderer extends BaseShopRenderer implements ShopRenderer {

	public static final Action EDIT_ACTION = new Action(I18n.getText("nicki.rights.attribute.action.edit"));

	public static final Action ACTIONS[] = {EDIT_ACTION};
	public static final Action NO_ACTIONS[] = {};

	
	public void editEntry(InventoryArticle iArticle) {
		if (iArticle.getArticle() instanceof MultipleInstancesCatalogArticle) {
			EditSpecifiedArticleHandler handler = new EditSpecifiedArticleHandler(iArticle, this);
			if (isTextArea(iArticle.getArticle())) {
				EnterSpecifierAsTextAreaDialog dialog = new EnterSpecifierAsTextAreaDialog("nicki.rights.specifier",
						I18n.getText("nicki.rights.specifier.define.window.title"));
				dialog.setHandler(handler);
				dialog.init((MultipleInstancesCatalogArticle) iArticle.getArticle());
				dialog.setName(iArticle.getSpecifier());
				dialog.setWidth(600, Unit.PIXELS);
				dialog.setHeight(560, Unit.PIXELS);
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
				
			} else {
				EnterSpecifierAsSelectDialog dialog = new EnterSpecifierAsSelectDialog("nicki.rights.specifier",
						I18n.getText("nicki.rights.specifier.define.window.title"));
				dialog.setHandler(handler);
				dialog.init((MultipleInstancesCatalogArticle) iArticle.getArticle());
				dialog.setName(iArticle.getSpecifier());
				dialog.setWidth(440, Unit.PIXELS);
				dialog.setHeight(500, Unit.PIXELS);
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
			}
		}
	}
	
	public boolean isTextArea(CatalogArticle catalogArticle) {
		if (catalogArticle instanceof MultipleInstancesCatalogArticle) {
			MultipleInstancesCatalogArticle micArticeCatalogArticle = (MultipleInstancesCatalogArticle) catalogArticle;
			if (micArticeCatalogArticle.getCatalogValueProvider() != null
					&& micArticeCatalogArticle.getCatalogValueProvider().getType() == TYPE.TEXT_AREA) {
				return true;
			}
		}
		return false;
	}
	
	public class ActionHandler implements Action.Handler {
		private static final long serialVersionUID = -9121430140825156577L;

		public void handleAction(Action action, Object sender, Object target) {
			if (action == EDIT_ACTION &&
					target instanceof InventoryArticle) {
				InventoryArticle iArticle = (InventoryArticle) target;
				editEntry(iArticle);
			}
		}
		
		public Action[] getActions(Object target, Object sender) {
			if (target instanceof InventoryArticle) {
				InventoryArticle iArticle = (InventoryArticle) target;
				if (iArticle.getArticle() instanceof MultipleInstancesCatalogArticle &&
						iArticle.getStatus() == STATUS.NEW) {
					return ACTIONS;
				}
			} 
			return NO_ACTIONS;
		}
	}

	public class ItemClickListener implements ItemClickEvent.ItemClickListener {

		@Override
		public void itemClick(ItemClickEvent event) {
			if (event.isDoubleClick() && event.getItemId() instanceof InventoryArticle) {
				InventoryArticle iArticle = (InventoryArticle) event.getItemId();
				editEntry(iArticle);
			}
		}
		
	}

}
