
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


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.shop.base.objects.Catalog;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;

@SuppressWarnings("serial")
public class ArticleShopContainer implements ShopContainer{

	private String STATUS_ORDERED; 
	private String STATUS_CANCELED; 
	private String STATUS_PROVISIONED; 

    private String[] visibleColumns = new String[] {
    	PROPERTY_STATUS,
    	PROPERTY_NAME,
    	PROPERTY_PATH
    };
	
	private IndexedContainer container = new IndexedContainer();
	private DynamicObject person;
	private Catalog catalog;

	public ArticleShopContainer(Catalog catalog, DynamicObject person, String i18nBase) {
		super();
		this.catalog = catalog;
		this.person = person;
		STATUS_ORDERED = I18n.getText(i18nBase + ".status.ordered");
		STATUS_CANCELED = I18n.getText(i18nBase + ".status.canceled");
		STATUS_PROVISIONED = I18n.getText(i18nBase + ".status.provisioned");
	}



	@SuppressWarnings("unchecked")
	public IndexedContainer getArticles() throws DynamicObjectException {
		container = new IndexedContainer();
		container.addContainerProperty(PROPERTY_STATUS, String.class, null);
		container.addContainerProperty(PROPERTY_NAME, String.class, null);
		container.addContainerProperty(PROPERTY_CATEGORY, String.class, null);

		container.addContainerProperty(PROPERTY_PATH, String.class, null);
		List<CatalogArticle> loadedArticles = catalog.getContext().loadObjects(CatalogArticle.class, catalog.getPath(), "objectClass=nickiShopArticle");

	    if (loadedArticles != null) {
		    for (CatalogArticle article : loadedArticles) {
				Item item = container.addItem(article);
				item.getItemProperty(PROPERTY_NAME).setValue(article.getDisplayName());
				item.getItemProperty(PROPERTY_PATH).setValue(article.getCatalogPath());
				item.getItemProperty(PROPERTY_CATEGORY).setValue(article.get(PROPERTY_CATEGORY).toString());
				if (catalog.hasArticle(getPerson(), article)) {
					item.getItemProperty(PROPERTY_STATUS).setValue(STATUS_PROVISIONED);
				}
			}
	    }
		
		return container;
	}



	@SuppressWarnings("unchecked")
	public void orderItem(Object target) {
		Item item = container.getItem(target);
		item.getItemProperty(PROPERTY_STATUS).setValue(STATUS_ORDERED);
	}
	
	@SuppressWarnings("unchecked")
	public void cancelItem(Object target) {
		Item item = container.getItem(target);
		String oldValue = (String) item.getItemProperty(PROPERTY_STATUS).getValue();

		if (StringUtils.equals(oldValue, STATUS_ORDERED)) {
			item.getItemProperty(PROPERTY_STATUS).setValue("");
		} else if (StringUtils.equals(oldValue, STATUS_PROVISIONED)) {
			item.getItemProperty(PROPERTY_STATUS).setValue(STATUS_CANCELED);
		}
	}



	public void setCategoryFilter(List<Object> values) {
		this.container.removeAllContainerFilters();
		if (values != null && values.size() > 0) {
			for (Object object : values) {
				String filterString = (String) object;
				this.container.addContainerFilter(PROPERTY_CATEGORY, filterString, true, false);
			}
		}
	}



	public Person getPerson() {
		return (Person) person;
	}



	public String[] getVisibleColumns() {
		return visibleColumns;
	}
	

}
