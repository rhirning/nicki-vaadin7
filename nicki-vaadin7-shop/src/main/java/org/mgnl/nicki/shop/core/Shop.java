
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


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.mgnl.nicki.core.helper.XMLHelper;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.shop.base.objects.Catalog;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

@SuppressWarnings("serial")
public class Shop implements ShopViewerComponent, Serializable {
	private String renderer;
	private List<ShopPage> pageList = new ArrayList<ShopPage>();
	private Document document;
	private Person shopper;
	private Person recipient;

	public Shop (String classLoaderPath, Person shopper, Person recipient) throws JDOMException, IOException {
		document = XMLHelper.documentFromClasspath(this.getClass(), classLoaderPath);
		this.shopper = shopper;
		this.recipient = recipient;
		load();
	}
	
	private void load() {
		Element root = document.getRootElement();
		
		this.renderer = StringUtils.trimToNull(root.getAttributeValue("renderer"));
		
		@SuppressWarnings("unchecked")
		List<Element> pages = root.getChildren("page");
		if (pages != null && pages.size() > 0) {
			for (Element pageElement : pages) {
				pageList.add(new ShopPage(this, pageElement));
			}
		}
		
	}

	public Document getDocument() {
		return document;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ShopPage page : pageList) {
			sb.append(page.toString()).append("\n");
		}
		return sb.toString();
	}

	public CatalogArticle getArticle(String catalogArticleId) {
		return Catalog.getCatalog().getArticle(catalogArticleId);
	}

	public List<ShopPage> getPageList() {
		return pageList;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public String getRenderer() {
		return renderer;
	}

	public boolean hasPages() {
		return pageList != null && pageList.size() > 0;
	}

	public List<CatalogArticle> getArticles() {
		return new ArrayList<CatalogArticle>();
	}

	public List<CatalogArticle> getAllArticles() {
		List<CatalogArticle> articles = new ArrayList<CatalogArticle>();
		if (hasPages()) {
			for (ShopPage page : getPageList()) {
				articles.addAll(page.getArticles());
			}
		}
		return articles;
	}

	public ShopViewerComponent getShopViewerComponent() {
		return this;
	}


	public void setRecipient(Person recipient) {
		this.recipient = recipient;
	}

	public Person getRecipient() {
		return recipient;
	}

	public void setShopper(Person shopper) {
		this.shopper = shopper;
	}

	public Person getShopper() {
		return shopper;
	}

}
