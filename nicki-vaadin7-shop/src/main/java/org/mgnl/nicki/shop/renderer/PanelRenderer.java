
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

import org.mgnl.nicki.shop.core.ShopPage;
import org.mgnl.nicki.shop.core.ShopViewerComponent;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class PanelRenderer extends BaseShopRenderer implements ShopRenderer {

	private ShopViewerComponent shopViewerComponent;
	private VerticalLayout layout;
	private List<ShopRenderer> pageRenderers = new ArrayList<ShopRenderer>();

	public AbstractComponent render(ShopViewerComponent shopViewerComponent, Inventory inventory) {
		this.shopViewerComponent = shopViewerComponent;
		setInventory(inventory);
		layout = new VerticalLayout();
		layout.setHeight("100%");
		render();
		return layout;
	}
	
	@Override
	public void render() {
		setInit(true);
		layout.removeAllComponents();
		addPanels();
		setInit(false);
	}
	
	private void addPanels() {
		for (ShopPage page : shopViewerComponent.getPageList()) {
			ShopRenderer renderer = page.getRenderer();
			pageRenderers.add(renderer);
			renderer.setParentRenderer(this);
			Panel panel = new Panel(page.getLabel());
			Component component = renderer.render(page, getInventory());
			component.setHeight("100%");
			panel.setContent(component);
			layout.addComponent(panel);
		}
	}
}
