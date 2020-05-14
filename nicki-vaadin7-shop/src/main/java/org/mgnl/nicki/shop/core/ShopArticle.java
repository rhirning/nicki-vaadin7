
package org.mgnl.nicki.shop.core;

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


public class ShopArticle implements ShopViewerComponent {
	
	List<CatalogArticle> articles = new ArrayList<CatalogArticle>();

	public ShopArticle(CatalogArticle catalogArticle) {
		super();
		this.articles.add(catalogArticle);
	}

	@Override
	public ShopViewerComponent getShopViewerComponent() {
		return this;
	}

	@Override
	public List<ShopPage> getPageList() {
		return new ArrayList<ShopPage>();
	}

	@Override
	public List<CatalogArticle> getArticles() {
		return articles;
	}

	@Override
	public List<CatalogArticle> getAllArticles() {
		return articles;
	}

}
