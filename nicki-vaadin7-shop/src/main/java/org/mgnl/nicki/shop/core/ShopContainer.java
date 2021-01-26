
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
import java.util.List;

import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.dynamic.objects.objects.Person;

import com.vaadin.v7.data.util.IndexedContainer;

public interface ShopContainer extends Serializable {
	String PROPERTY_NAME = "name"; 
	String PROPERTY_CATEGORY = "category"; 
	String PROPERTY_STATUS = "status"; 
	String PROPERTY_ICON = "icon"; 
	String PROPERTY_PATH= "path"; 
		
	IndexedContainer getArticles() throws DynamicObjectException;

	void orderItem(Object target);
	
	void cancelItem(Object target);

	void setCategoryFilter(List<Object> values);

	Person getPerson();

	String[] getVisibleColumns();
}
