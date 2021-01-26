
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

import org.mgnl.nicki.shop.core.ShopViewerComponent;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class BaseShopRenderer implements ShopRenderer {
	
	private Inventory inventory;
	private ShopRenderer parentRenderer;
	private boolean init;
	

	protected void removeExcept(Layout parent, Component button) {
		List<Component> toBeRemoved = new ArrayList<Component>();
		for (Component component : parent) {
			if (component != button) {
				toBeRemoved.add(component);
			}
		}
		for (Component component : toBeRemoved) {
			parent.removeComponent(component);
		}
	}
	

	public Component getXMLComponent(ShopViewerComponent shopViewerComponent) {
		VerticalLayout layout = new VerticalLayout();
		layout.setHeight("420px");
		// textArea_1
		TextArea xml = new TextArea();
		xml.setWidth("100%");
		xml.setHeight("100%");
		xml.setValue(shopViewerComponent.toString());
		layout.addComponent(xml);

		return layout;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}


	public ShopRenderer getParentRenderer() {
		return parentRenderer;
	}


	public void setParentRenderer(ShopRenderer parentRenderer) {
		this.parentRenderer = parentRenderer;
	}

	@Override
	public void handleChange() {
		if (!init) {
			render();
			if (getParentRenderer() != null) {
				getParentRenderer().render();
			}
		}
	}


	public boolean isInit() {
		return init;
	}


	public void setInit(boolean init) {
		this.init = init;
	}

}
