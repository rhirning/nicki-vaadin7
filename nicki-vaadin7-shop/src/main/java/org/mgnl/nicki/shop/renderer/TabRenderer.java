
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


import java.util.ArrayList;
import java.util.List;

import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.core.ShopArticle;
import org.mgnl.nicki.shop.core.ShopPage;
import org.mgnl.nicki.shop.core.ShopViewerComponent;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import org.mgnl.nicki.vaadin.base.editor.Icon;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class TabRenderer extends BaseShopRenderer implements ShopRenderer {

	private ShopViewerComponent shopViewerComponent;
	private TabSheet tabSheet;
	private List<ShopRenderer> pageRenderers = new ArrayList<ShopRenderer>();

	public AbstractComponent render(ShopViewerComponent shopViewerComponent, Inventory inventory) {
		this.shopViewerComponent = shopViewerComponent;
		setInventory(inventory);
		tabSheet = new TabSheet();
		tabSheet.setHeight("100%");
		render();
		return tabSheet;
	}
	
	public void render() {
		setInit(true);
		tabSheet.removeAllComponents();
		addTabs(tabSheet);
		setInit(false);
	}
	
	private void addTabs(TabSheet tabSheet) {
		for (CatalogArticle article : shopViewerComponent.getArticles()) {
			addArticleTab(tabSheet, article);
		}
		for (ShopPage page : shopViewerComponent.getPageList()) {
			addPageTab(tabSheet, page);
		}
	}
	
	private void addArticleTab(TabSheet tabSheet, CatalogArticle article) {
		ShopRenderer renderer = new TableRenderer();
		pageRenderers.add(renderer);
		renderer.setParentRenderer(this);
		ShopArticle shopArticle = new ShopArticle(article);
		Tab tab = tabSheet.addTab(renderer.render(shopArticle, getInventory()), article.getDisplayName(), Icon.SETTINGS.getResource());
		((AbstractComponent)tab.getComponent()).setData(renderer);
		tab.getComponent().setHeight("100%");
		tabSheet.addSelectedTabChangeListener(event -> {
				TabSheet source = (TabSheet) event.getSource();
				AbstractComponent component = (AbstractComponent) source.getSelectedTab();
				log.debug(component.getClass().getName());
				ShopRenderer renderer1 = (ShopRenderer) component.getData();
				if (renderer1 != null) {
					renderer1.render();
				}
		});
		
	}

	
	private void addPageTab(TabSheet tabSheet, ShopPage page) {
		ShopRenderer renderer = page.getRenderer();
		
		Tab tab = tabSheet.addTab(renderer.render(page, getInventory()), page.getLabel());
		((AbstractComponent)tab.getComponent()).setData(renderer);
		((AbstractComponent)tab.getComponent()).setHeight("100%");
		tabSheet.addSelectedTabChangeListener(event -> {
				TabSheet source = (TabSheet) event.getSource();
				AbstractComponent tab1 = (AbstractComponent) source.getSelectedTab();
				ShopRenderer renderer1 = (ShopRenderer) tab1.getData();
				if (renderer1 != null) {
					renderer1.render();
				}
		});
	}
}
