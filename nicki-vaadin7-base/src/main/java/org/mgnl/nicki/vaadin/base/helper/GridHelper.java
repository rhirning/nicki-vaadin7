package org.mgnl.nicki.vaadin.base.helper;

/*-
 * #%L
 * nicki-vaadin7-base
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
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

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DataModel;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.vaadin.base.data.DynamicObjectGridColumn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GridHelper {

	public static <T extends DynamicObject> List<DynamicAttribute> getColumnAttributes(NickiContext context, Class<T> clazz) {
		List<DynamicAttribute> list = new ArrayList<>();
		try {
			DataModel model = context.getDataModel(clazz);
			for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
				if (dynAttribute.isSearchable()) {
					list.add(dynAttribute);
				}
			}
		} catch (Exception e) {
			log.error("Error reading datamodel", e);
		}
		return list;
	}

	
	public static <T extends DynamicObject> List<DynamicObjectGridColumn<T>> getColumns(NickiContext context, Class<T> clazz) {
		List<DynamicObjectGridColumn<T>> list = new ArrayList<DynamicObjectGridColumn<T>>();
		
		for (DynamicAttribute dynAttribute : getColumnAttributes(context, clazz)) {
			if (dynAttribute.isSearchable()) {
				list.add(new DynamicObjectGridColumn<T>(dynAttribute.getName(), I18n.getText(dynAttribute.getCaption(), dynAttribute.getName())));
			}
		}
		return list;
	}
}
