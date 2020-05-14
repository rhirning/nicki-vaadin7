
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


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.shop.base.objects.CatalogArticle;
import org.mgnl.nicki.shop.base.objects.CatalogValueProvider;

public class FreeTextProvider implements CatalogValueProvider{

	@Override
	public Map<String, String> getEntries() {
		return null;
	}

	@Override
	public boolean checkEntry(String entry) {
		return StringUtils.isNotBlank(entry);
	}

	@Override
	public boolean isOnlyDefinedEntries() {
		return false;
	}

	@Override
	public void init(CatalogArticle article) {
	}

	@Override
	public TYPE getType() {
		return TYPE.TEXT_AREA;
	}

}
