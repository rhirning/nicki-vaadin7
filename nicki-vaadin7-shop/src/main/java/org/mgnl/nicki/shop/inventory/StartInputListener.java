
package org.mgnl.nicki.shop.inventory;

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


import java.util.Date;

import org.mgnl.nicki.shop.base.inventory.InventoryArticle;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

@SuppressWarnings("serial")
public class StartInputListener implements ValueChangeListener {
	private InventoryArticle article;

	public StartInputListener(InventoryArticle article) {
		this.article = article;
	}

	public void valueChange(ValueChangeEvent event) {
		Object value = event.getProperty().getValue();
		try {
			article.setStart((Date) value);
		} catch (Exception e) {
			article.setStart(null);
		}
	}

}
