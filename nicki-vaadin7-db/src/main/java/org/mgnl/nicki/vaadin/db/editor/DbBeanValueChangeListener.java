
package org.mgnl.nicki.vaadin.db.editor;

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

import com.vaadin.flow.component.Component;

public interface DbBeanValueChangeListener {

	void valueChange(Object bean, String name, List<Object> values);

	void valueChange(Object bean, String attributeName, Object value);

	boolean acceptAttribute(String name);

	void close(Component component);

	void refresh(Object bean);

}
