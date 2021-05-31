
package org.mgnl.nicki.vaadin.base.search;

/*-
 * #%L
 * nicki-vaadin-base
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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.objects.DataModel;
import org.mgnl.nicki.core.objects.DynamicAttribute;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.util.Classes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class DynamicObjectSearchFieldFactory<T extends DynamicObject> implements Serializable {
	private NickiContext context;
	private Map<DynamicAttribute, String> map;
	
	public DynamicObjectSearchFieldFactory(NickiContext context, Map<DynamicAttribute, String> map) {
		this.context = context;
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public Component createField(DynamicAttribute dynAttribute) {
		DynamicAttributeSearchField<T> field = null;
		if (StringUtils.isNotEmpty(dynAttribute.getEditorClass())
				&& StringUtils.isNotBlank(dynAttribute.getSearchFieldClass())) {
			try {
				field = (DynamicAttributeSearchField<T>) Classes.newInstance(dynAttribute.getSearchFieldClass());
			} catch (Exception e) {
				field = null;
				log.error("Error", e);
			}
		}
		if (field == null) {
			field = new TextFieldSearchField<T>();
		}
		field.setWidth("100%");
		field.init(dynAttribute, map);
		return field.getComponent();
	}
	
	
	public void addFields(FormLayout layout, Class<T> clazz) throws InstantiateDynamicObjectException {
		DataModel model = context.getDataModel(clazz);
		for (DynamicAttribute dynAttribute : model.getAttributes().values()) {
			if (dynAttribute.isSearchable()) {
				layout.add(createField(dynAttribute));
			}
		}
	}
	
}
