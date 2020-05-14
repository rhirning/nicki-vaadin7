
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


import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.data.EntryFilter;
import org.mgnl.nicki.core.data.TreeData;

public class OrgOnlyFilter implements EntryFilter {

	@Override
	public <T extends TreeData> boolean accepts(T object) {
		if (object instanceof DynamicObject) {
			DynamicObject dynamicObject = (DynamicObject) object;
			if (dynamicObject.getModel().getObjectClasses().contains("organizationalUnit")) {
				return true;
			}
		}
		return false;
	}

}
