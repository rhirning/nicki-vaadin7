
package org.mgnl.nicki.shop.objects;

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

import org.mgnl.nicki.core.auth.InvalidPrincipalException;
import org.mgnl.nicki.core.context.AppContext;
import org.mgnl.nicki.core.context.DynamicObjectFactory;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

public class CatalogArticleFactory {
	private static CatalogArticleFactory instance;
	private List<CatalogArticle> articles = new ArrayList<CatalogArticle>();
	
	public static CatalogArticleFactory getInstance() throws InvalidPrincipalException, InstantiateDynamicObjectException {
		if (instance == null) {
			instance = new CatalogArticleFactory();
		}
		return instance;
	}

	public CatalogArticleFactory() throws InvalidPrincipalException, InstantiateDynamicObjectException {
		DynamicObjectFactory objectFactory = AppContext.getSystemContext().getObjectFactory();
		for (CatalogArticle catalogArticle : objectFactory.findDynamicObjects(CatalogArticle.class)) {
			articles.add(catalogArticle);
		}
	}

	public List<CatalogArticle> getArticles() {
		return articles;
	}
	
	
}
