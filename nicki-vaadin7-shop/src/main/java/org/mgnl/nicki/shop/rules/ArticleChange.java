
package org.mgnl.nicki.shop.rules;

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


import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.objects.CartEntry.ACTION;

public class ArticleChange {
	public enum TYPE {ADD, REMOVE};
	
	private TYPE type;
	private Person person;
	private CatalogArticle article;
	public ArticleChange(TYPE type, Person person, CatalogArticle article) {
		super();
		this.type = type;
		this.person = person;
		this.article = article;
	}
	public TYPE getType() {
		return type;
	}
	public Person getPerson() {
		return person;
	}
	public CatalogArticle getArticle() {
		return article;
	}
	
	public ACTION getCartEntryType() {
		if (type == TYPE.REMOVE) {
			return ACTION.DELETE;
		} else {
			return ACTION.ADD;
		}
		
	}

}
