
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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.mgnl.nicki.core.util.Classes;
import org.mgnl.nicki.shop.base.objects.Catalog;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

import lombok.extern.slf4j.Slf4j;
/**
 * ArticleContainer
 * 
 * contains Shop Article elements
 * 
 * @author rhirning
 *
 * Attributes:
 * 
 * type		
 * 		page		collects all articles of a catalog page
 * 		selector	collects articles by an article selector class
 * 		parameter	will be passed to the selector class

 * Different types of ShopArticles declarations:
 * 1. pageRef: articles will be collected by a reference to a catalog page
 * 
 *	<articles type="pageRef" page="/Notes"/>
 * 
 * 2. Explicit list of articles
 * 
 * 	<articles>
 *		<article id="/Host/ChangeMan fuer FB-Freigeber"/>
 *		<article id="/Host/ChangeMan fuer FB-Freigeber"/>
 *	</articles>
 *
 * 3. Combination
 * 
 *	<articles type="pageRef" page="/Notes">
 *		<article id="/Host/ChangeMan fuer FB-Freigeber"/>
 *		<article id="/Host/ChangeMan fuer FB-Freigeber"/>
 *	</articles>
 */
@Slf4j
@SuppressWarnings("serial")
public class ArticleContainer implements Serializable{
	private List<CatalogArticle> articleList = new ArrayList<CatalogArticle>();

	public ArticleContainer(Shop shop, Element pageElement) {
		// Collect referenced articles
		if (isPageRef(pageElement)) {
			String referencedPage = pageElement.getAttributeValue("page");
			this.articleList.addAll(Catalog.getCatalog().getReferencedPage(referencedPage).getArticles());
		}
		
		// Collect articles by selector
		if (isArticelSelect(pageElement)) {
			String selectorClass = pageElement.getAttributeValue("selector");
			String parameter = pageElement.getAttributeValue("parameter");
			try {
				CatalogArticleSelector selector = (CatalogArticleSelector) Classes.newInstance(selectorClass);
				selector.setShopper(shop.getShopper());
				selector.setRecipient(shop.getRecipient());
				List<CatalogArticle> articles = selector.getArticles(parameter);
				if (articles != null && articles.size() > 0) {
					this.articleList.addAll(articles);
				}
			} catch (Exception e) {
				log.error("Error", e);
			}
		}
		
		// Collect explicit articles
		@SuppressWarnings("unchecked")
		List<Element> articles = pageElement.getChildren("article");
		if (articles != null && articles.size() > 0) {
			for (Element articleElement : articles) {
				String catalogArticleId = articleElement.getAttributeValue("id");
				if (StringUtils.isNotEmpty(catalogArticleId)) {
					CatalogArticle article = Catalog.getCatalog().getArticle(catalogArticleId);
					this.articleList.add(article);
				}
			}
		}
	}

	public ArticleContainer() {
	}

	private boolean isPageRef(Element pageElement) {
		return StringUtils.isNotBlank(pageElement.getAttributeValue("page"));
	}

	private boolean isArticelSelect(Element pageElement) {
		return StringUtils.isNotBlank(pageElement.getAttributeValue("selector"));
	}

	public boolean hasArticles() {
		return this.articleList != null && this.articleList.size() > 0;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[articles]\n");
		if (hasArticles()) {
			for (CatalogArticle article : articleList) {
				sb.append(article.toString()).append("\n");
			}
		}
		return sb.toString();
	}

	public List<CatalogArticle> getArticles() {
		return articleList;
	}

	public void addArticle(CatalogArticle catalogArticle) {
		this.articleList.add(catalogArticle);
	}

}
