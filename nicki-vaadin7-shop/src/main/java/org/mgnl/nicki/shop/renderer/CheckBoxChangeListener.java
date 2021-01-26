
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
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.inventory.Inventory;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;


public class CheckBoxChangeListener implements Property.ValueChangeListener {
	private static final long serialVersionUID = 5397184007544830202L;
	private Inventory inventory;
	private CatalogArticle catalogArticle;
	private TableRenderer tableRenderer;
	public CheckBoxChangeListener(Inventory inventory,
			CatalogArticle catalogArticle, TableRenderer tableRenderer) {
		super();
		this.inventory = inventory;
		this.catalogArticle = catalogArticle;
		this.tableRenderer = tableRenderer;
	}




	@Override
	public void valueChange(ValueChangeEvent event) {
		String checkedString = String.valueOf(event.getProperty().getValue());
		boolean checked = DataHelper.booleanOf(checkedString);
		if (checked) {
			inventory.addArticle(catalogArticle);
		} else {
			inventory.removeArticle(catalogArticle);
		}
		tableRenderer.handleChange();
	}

}
