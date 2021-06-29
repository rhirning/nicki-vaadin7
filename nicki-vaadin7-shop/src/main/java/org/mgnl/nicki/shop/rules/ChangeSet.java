
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mgnl.nicki.dynamic.objects.objects.Person;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;

public class ChangeSet {
	private List<ArticleChange> changes = new ArrayList<ArticleChange>();

	public void addToMissing(Person person, CatalogArticle article) {
		changes.add(new ArticleChange(ArticleChange.TYPE.ADD, person, article));
	}
	public void addToSurplus(Person person, CatalogArticle article) {
		changes.add(new ArticleChange(ArticleChange.TYPE.REMOVE, person, article));
	}
	
	public boolean isEmpty() {
		return changes.isEmpty();
	}
	
	public boolean isNotEmpty() {
		return !isEmpty();
	}
	public List<ArticleChange> getChanges() {
		return Collections.unmodifiableList(changes);
	}
}