
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


import java.util.List;
import java.util.Map;

import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.core.ShopViewerComponent;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle.STATUS;
import org.mgnl.nicki.shop.base.objects.MultipleInstancesCatalogArticle;
import org.mgnl.nicki.vaadin.base.editor.Icon;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class TableRenderer extends BaseTableRenderer implements ShopRenderer {

	private ShopViewerComponent shopViewerComponent;
	private Table table;

	public AbstractComponent render(ShopViewerComponent shopViewerComponent, Inventory inventory) {
		this.shopViewerComponent = shopViewerComponent;
		setInventory(inventory);
		render();
		return table;
	}

	@SuppressWarnings("unchecked")
	public void render() {
		setInit(true);
		// collect all articles
		List<CatalogArticle> articles = shopViewerComponent.getAllArticles();
		// create Table

		if (table == null) {
			table = new Table();
			table.setPageLength(0);
			table.setWidth("100%");
			table.setHeight("100%");
			table.addContainerProperty("checkbox", Component.class, "");
			table.setColumnWidth("checkbox", 64);
			table.setColumnHeader("checkbox", "");
			table.addContainerProperty("start", String.class, "");
			table.setColumnWidth("start", 70);
			table.setColumnHeader("start", I18n.getText(CatalogArticle.CAPTION_START));
			table.addContainerProperty("title", String.class, "");
//			table.setColumnWidth("title", 200);
			table.setColumnHeader("title", I18n.getText("nicki.rights.attribute.title.label"));
			//table.addActionHandler(new ActionHandler());
			table.addItemClickListener(new ItemClickListener());

		}
		table.removeAllItems();
		// add articles to table
		for (CatalogArticle article : articles) {
			if (!article.isMultiple()) {
				addArticle(article, getInventory().getArticle(article));
			} else {
		        Button button = new Button("+");
		        button.setData(article);
		        button.setImmediate(true);
		        button.setWidth("-1px");
				if (article.hasDescription()) {
					button.setIcon(Icon.HELP.getResource());
					button.setDescription(article.getDescription());
				}
		        button.addClickListener(event -> {
						CatalogArticle article1 = (CatalogArticle) event.getButton().getData();
						addInstance(article1);
				});

				Item item = table.addItem(article);
				if (item != null) {
					item.getItemProperty("title").setValue(article.getDisplayName());
					item.getItemProperty("checkbox").setValue(button);
					
			        Map<String, InventoryArticle> articleMap = getInventory().getArticles(article);
			        if (articleMap != null) {
				        for (String specifier : articleMap.keySet()) {
							InventoryArticle iArticle = articleMap.get(specifier);
							addMultiArticle(iArticle, iArticle.getStatus());
						}
			        }
				}
			}
		}
		setInit(false);

	}

	protected void addInstance(CatalogArticle catalogArticle) {
		
		NewSpecifiedArticleHandler handler = new NewSpecifiedArticleHandler(catalogArticle, this);

		if (isTextArea(catalogArticle)) {
			EnterSpecifierAsTextAreaDialog dialog = new EnterSpecifierAsTextAreaDialog("nicki.rights.specifier",
					I18n.getText("nicki.rights.specifier.define.window.title"));
			dialog.setHandler(handler);
			dialog.init((MultipleInstancesCatalogArticle) catalogArticle);
			dialog.setWidth(600, Unit.PIXELS);
			dialog.setHeight(560, Unit.PIXELS);
			dialog.setModal(true);
			UI.getCurrent().addWindow(dialog);
			
		} else {
			EnterSpecifierAsSelectDialog dialog = new EnterSpecifierAsSelectDialog("nicki.rights.specifier",
					I18n.getText("nicki.rights.specifier.define.window.title"));
			dialog.setHandler(handler);
			dialog.init((MultipleInstancesCatalogArticle) catalogArticle);
			dialog.setWidth(440, Unit.PIXELS);
			dialog.setHeight(500, Unit.PIXELS);
			dialog.setModal(true);
			UI.getCurrent().addWindow(dialog);
		}
	}

	@SuppressWarnings("unchecked")
	public Item addMultiArticle(InventoryArticle iArticle, STATUS status) {
		CheckBox checkBox = new CheckBox();
		CatalogArticle article = iArticle.getArticle();
		checkBox.setData(iArticle);
		checkBox.setImmediate(true);
		checkBox.setWidth("-1px");
		if (article.hasDescription()) {
			checkBox.setIcon(Icon.HELP.getResource());
			checkBox.setDescription(article.getDescription());
		}
		checkBox.setValue(true);
		if (iArticle.isReadOnly()) {
			checkBox.setEnabled(false);
		} else {
			checkBox.addValueChangeListener(new MulitCheckBoxChangeListener(getInventory(), iArticle, this));
		}
		Item item = table.addItem(iArticle);
		item.getItemProperty("title").setValue(iArticle.getDisplayName());
		item.getItemProperty("checkbox").setValue(checkBox);
		if (iArticle.getStart() != null) {
			item.getItemProperty("start").setValue(DataHelper.getDisplayDay(iArticle.getStart()));
		}

		return item;
	}

	protected void cleanup() {
		
	}

	@SuppressWarnings("unchecked")
	public Item addArticle(CatalogArticle article, InventoryArticle inventoryArticle) {
		CheckBox checkBox = new CheckBox();
		checkBox.setData(article);

		checkBox.setImmediate(true);
		checkBox.setWidth("-1px");
		if (article.hasDescription()) {
			checkBox.setIcon(Icon.HELP.getResource());
			checkBox.setDescription(article.getDescription());
		}

		checkBox.addValueChangeListener(new CheckBoxChangeListener(getInventory(), article, this));

		Item item = table.addItem(article);
		item.getItemProperty("title").setValue(article.getDisplayName());
		item.getItemProperty("checkbox").setValue(checkBox);

		if (inventoryArticle != null) {
			if (inventoryArticle.getStart() != null) {
				item.getItemProperty("start").setValue(DataHelper.getDisplayDay(inventoryArticle.getStart()));
			}
			if (inventoryArticle.getStatus() != InventoryArticle.STATUS.DELETED) {
				checkBox.setValue(true);
			}
			if (inventoryArticle.isReadOnly()) {
				checkBox.setEnabled(false);
			}
			/*
			if (inventoryArticle.getStatus() == STATUS.NEW) {
				checkBox.setEnabled(true);
			} else {
				checkBox.setEnabled(false);
			}
			*/
		}
		return item;
	}

	public Table getTable() {
		return table;
	}



}
