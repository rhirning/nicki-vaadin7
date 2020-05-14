
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


import org.mgnl.nicki.shop.base.inventory.InventoryArticle;

public class EditSpecifiedArticleHandler implements CreateInstanceHandler {

	private InventoryArticle iArticle;
	private ShopRenderer renderer;


	public EditSpecifiedArticleHandler(InventoryArticle iArticle, ShopRenderer renderer) {
		super();
		this.iArticle = iArticle;
		this.renderer = renderer;
	}

	@Override
	public void setName(String value) {
		renderer.getInventory().modifyArticle(iArticle.getArticle(),
				iArticle.getSpecifier(), value);
//		iArticle.setSpecifier(value);
		renderer.handleChange();
	}

	@Override
	public String getName() {
		return "";
	}

}
