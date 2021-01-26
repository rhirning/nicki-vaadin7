
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



import org.mgnl.nicki.core.helper.DataHelper;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import org.mgnl.nicki.shop.base.inventory.InventoryArticle;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;


public class MulitCheckBoxChangeListener implements Property.ValueChangeListener {
	private static final long serialVersionUID = 5397184007544830202L;
	private Inventory inventory;
	private ShopRenderer renderer;
	public MulitCheckBoxChangeListener(Inventory inventory,
			InventoryArticle inventoryArticle, ShopRenderer renderer) {
		super();
		this.inventory = inventory;
		this.inventoryArticle = inventoryArticle;
		this.renderer = renderer;
	}


	private InventoryArticle inventoryArticle;


	@Override
	public void valueChange(ValueChangeEvent event) {
		String checkedString = String.valueOf(event.getProperty().getValue());
		boolean checked = DataHelper.booleanOf(checkedString);
		if (checked) {
			inventory.addArticle(inventoryArticle);
		} else {
			inventory.removeArticle(inventoryArticle);
		}
		renderer.handleChange();
	}

}
