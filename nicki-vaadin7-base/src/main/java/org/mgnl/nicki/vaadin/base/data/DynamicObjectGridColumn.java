package org.mgnl.nicki.vaadin.base.data;

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

import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.flow.function.ValueProvider;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class DynamicObjectGridColumn<T extends DynamicObject> implements ValueProvider<T, String> {
	public DynamicObjectGridColumn(String attributeName, String caption) {
		this.attributeName = attributeName;
		this.caption = caption;
	}
	private String attributeName;
	private String caption;
	
	@Override
	public String apply(T source) {
		return source.getAttribute(attributeName);
	}
}
