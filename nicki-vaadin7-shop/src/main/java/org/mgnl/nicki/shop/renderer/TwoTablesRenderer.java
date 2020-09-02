
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

import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.core.ShopViewerComponent;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle.STATUS;
import org.mgnl.nicki.shop.base.objects.MultipleInstancesCatalogArticle;
import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class TwoTablesRenderer extends BaseTableRenderer implements ShopRenderer {
	
	private ShopViewerComponent shopViewerComponent;
	private HorizontalLayout layout;
	private Table leftTable;
	private Table rightTable;

	public AbstractComponent render(ShopViewerComponent shopViewerComponent, Inventory inventory) {
		this.shopViewerComponent = shopViewerComponent;
		setInventory(inventory);
		initLayout();
		
		render();
		return layout;
	}

	private void initLayout() {
		layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setHeight("100%");
		VerticalLayout leftContainer = new VerticalLayout();
		leftContainer.setHeight("100%");
		VerticalLayout centerContainer = new VerticalLayout();
		VerticalLayout rightContainer = new VerticalLayout();
		rightContainer.setHeight("100%");
		layout.addComponent(leftContainer);
		layout.addComponent(centerContainer);
		layout.addComponent(rightContainer);
		
		layout.setExpandRatio(leftContainer, 0.48f);
		layout.setExpandRatio(centerContainer, 0.04f);
		layout.setExpandRatio(rightContainer, 0.48f);

		// left table
		leftTable = new Table();
		leftTable.setPageLength(0);
		leftTable.setSelectable(true);
		leftTable.setWidth("100%");
		leftTable.setHeight("100%");
		leftTable.addContainerProperty("title", String.class, "");
		leftTable.setColumnWidth("title", -1);
		leftTable.setColumnHeader("title", I18n.getText("nicki.rights.attribute.title.label"));
		
		leftContainer.addComponent(leftTable);
		
		// actionButtons
		Button toRightButton = new Button("==>");
		Button toLeftButton = new Button("<==");
		centerContainer.addComponent(toRightButton);
		centerContainer.addComponent(toLeftButton);
		
		// add
		toRightButton.addClickListener(event -> {
				if (leftTable.getValue() != null) {
					CatalogArticle catalogArticle = (CatalogArticle) leftTable.getValue();
					if (catalogArticle.isMultiple()) {
						addInstance(catalogArticle);
					} else {
						getInventory().addArticle(catalogArticle);
						render();
					}
				}
		});
		
		// remove		
		toLeftButton.addClickListener(event -> {
				if (rightTable.getValue() != null) {
					InventoryArticle iArticle = (InventoryArticle) rightTable.getValue();
					getInventory().removeArticle(iArticle);
					render();
				}
		});
		
		// right table
		rightTable = new Table();
		rightTable.setPageLength(0);
		rightTable.setSelectable(true);
		rightTable.setWidth("100%");
		rightTable.setHeight("100%");
		rightTable.addContainerProperty("title", Component.class, "");
		rightTable.setColumnWidth("title", -1);
		rightTable.setColumnHeader("title", I18n.getText("nicki.rights.attribute.title.label"));
		/*		
		rightTable.addContainerProperty("dateFrom", PopupDateField.class, "");
		rightTable.setColumnWidth("dateFrom", 100);
		rightTable.setColumnHeader("dateFrom", I18n.getText(CatalogArticle.CAPTION_START));
		rightTable.addContainerProperty("attributes", Layout.class, "");
		rightTable.setColumnHeader("attributes", I18n.getText("nicki.rights.attributes.label"));
*/		
		//rightTable.addActionHandler(new ActionHandler());
		rightTable.addItemClickListener(new ItemClickListener());
		rightContainer.addComponent(rightTable);
		
		

	}	

	public void render() {
		renderLeft();
		renderRight();
	}

	
	
	
	@SuppressWarnings("unchecked")
	public void renderLeft() {
		// collect all articles
		List<CatalogArticle> articles = shopViewerComponent.getAllArticles();
		// create Table

		leftTable.removeAllItems();
		// add articles to table
		for (CatalogArticle article : articles) {
			if (article.isMultiple() || !getInventory().hasArticle(article)) {
				Item item = leftTable.addItem(article);
				item.getItemProperty("title").setValue(article.getDisplayName());
			}
		}
		resize();
	}
	
	public void renderRight() {
		// collect all articles
		List<CatalogArticle> articles = shopViewerComponent.getAllArticles();
		// create Table

		rightTable.removeAllItems();
		// add articles to table
		for (CatalogArticle article : articles) {
			if (getInventory().hasArticle(article)) {
				if (!article.isMultiple()) {
					InventoryArticle iArticle = getInventory().getArticle(article);
					addArticleToRight(iArticle, iArticle.getStatus());
				} else {
			        Map<String, InventoryArticle> articleMap = getInventory().getArticles(article);
			        if (articleMap != null) {
				        for (String specifier : articleMap.keySet()) {
							InventoryArticle iArticle = articleMap.get(specifier);
							addMultiArticleToRight(iArticle, iArticle.getStatus());
						}
			        }
				}
			}
		}
		resize();
	}
	
	protected void addInstance(CatalogArticle catalogArticle) {
		EnterSpecifierAsSelectDialog dialog = new EnterSpecifierAsSelectDialog("nicki.rights.specifier",
				I18n.getText("nicki.rights.specifier.define.window.title"));
		NewSpecifiedArticleHandler handler = new NewSpecifiedArticleHandler(catalogArticle, this);
		dialog.setHandler(handler);
		dialog.init((MultipleInstancesCatalogArticle) catalogArticle);
		dialog.setWidth(440, Unit.PIXELS);
		dialog.setHeight(500, Unit.PIXELS);
		dialog.setModal(true);
		UI.getCurrent().addWindow(dialog);		
	}

	@SuppressWarnings("unchecked")
	public Item addArticleToRight(InventoryArticle iArticle, STATUS status) {
		CatalogArticle article = iArticle.getArticle();
		Item item = rightTable.addItem(iArticle);

		Label label = new Label(article.getDisplayName());
		item.getItemProperty("title").setValue(label);
		showEntry(item, article, iArticle);

		return item;
	}

	@SuppressWarnings("unchecked")
	public Item addMultiArticleToRight(InventoryArticle iArticle, STATUS status) {
		CatalogArticle article = iArticle.getArticle();
		Item item = rightTable.addItem(iArticle);

		Label label = new Label(article.getDisplayName() + ": " + iArticle.getSpecifier());
		item.getItemProperty("title").setValue(label);
		showEntry(item, article, iArticle);

		return item;
	}

	protected void cleanup() {
		
	}

	protected void hideEntry(Item item) {
		/*
		item.getItemProperty("dateFrom").setValue(null);
		item.getItemProperty("dateTo").setValue(null);
		item.getItemProperty("attributes").setValue(null);
		*/
//		removeExcept(parent, event.getButton());
	}

	public void showEntry(Item item, CatalogArticle article, InventoryArticle inventoryArticle) {
		/*
		SOURCE source = SOURCE.SHOP;
		Date start = new Date();
		Date end = null;
		boolean toEnabled = true;
		boolean enabled = true;
		
		if (inventoryArticle != null && inventoryArticle.getStatus() != STATUS.NEW) {
			
			start = inventoryArticle.getStart();
			end = inventoryArticle.getEnd();
			
			enabled = false;
			source = inventoryArticle.getSource();
			if (source == SOURCE.RULE) {
				toEnabled = false;
			}
		}
		*/
		/*
		item.getItemProperty("dateFrom").setValue(getStartDateComponent(inventoryArticle, enabled, start));
		item.getItemProperty("dateTo").setValue(getEndDateComponent(inventoryArticle, toEnabled, end));
		item.getItemProperty("attributes").setValue(getVerticalArticleAttributes(article, inventoryArticle, enabled, source));
		*/
//		showArticleAttributes(parent);
	}

	public Table getTable() {
		return leftTable;
	}

	public void resize() {
		/*
		if (leftTable.size() > MAX_TABLE_SIZE) {
			leftTable.setPageLength(MAX_TABLE_SIZE);
		} else {
			leftTable.setPageLength(leftTable.size());
		}
		*/
	}

}
