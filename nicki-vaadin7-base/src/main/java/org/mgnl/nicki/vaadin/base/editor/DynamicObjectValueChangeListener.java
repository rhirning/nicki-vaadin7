
package org.mgnl.nicki.vaadin.base.editor;

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


import java.util.List;

import org.mgnl.nicki.core.objects.DynamicObject;

import com.vaadin.ui.Component;

public interface DynamicObjectValueChangeListener<T> {

	void valueChange(DynamicObject dynamicObject, String name, List<T> values);

	void valueChange(DynamicObject dynamicObject, String attributeName, T value);

	boolean acceptAttribute(String name);

	void close(Component component);

	void refresh(DynamicObject dynamicObject);

}
