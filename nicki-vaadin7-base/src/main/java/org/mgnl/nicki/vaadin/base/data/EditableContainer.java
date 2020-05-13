
package org.mgnl.nicki.vaadin.base.data;

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


import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public abstract class EditableContainer<T extends Editable> extends IndexedContainer implements Container {
	private static final long serialVersionUID = -5658914311396563600L;

	public EditableContainer(Collection<? extends T> collection, Object columns[]) throws IllegalArgumentException {
		super();
		for (Object object : columns) {
			addContainerProperty(object, String.class, "");
		}
		addContainerProperty("edit", Component.class, null);
		for (T bean : collection) {
			Item item = addItem(bean);
			for (Object object : columns) {
				@SuppressWarnings("unchecked")
				Property<String> property = item.getItemProperty(object);
				property.setValue(get(bean, (String) object));
			}
			@SuppressWarnings("unchecked")
			Property<Component> editProperty = item.getItemProperty("edit");
			if (bean.isEditable()) {
				editProperty.setValue(new EditButton(bean, "edit"));
			}
		}
	}

	public class EditButton extends Button implements Property<Component> {
		private static final long serialVersionUID = -5928278839208615249L;
		public EditButton(T bean, String title) {
			setCaption(title);
			addStyleName("link");
			setData(bean);
			addClickListener(new ClickListener() {
				private static final long serialVersionUID = -900229029571991404L;

				@Override
				public void buttonClick(ClickEvent event) {
					@SuppressWarnings("unchecked")
					T bean = (T) event.getButton().getData();
					edit(bean);
					
				}
			});
		}
		@Override
		public EditButton getValue() {
			return this;
		}
		@Override
		public void setValue(Component newValue)
				throws ReadOnlyException {
			throw new ReadOnlyException();
		}
		@Override
		public Class<? extends Component> getType() {
			return this.getType();
		}


		
	}

	abstract public void edit(T bean);


	public static String get(Object object, String name) {
		String methodName =  "get" + StringUtils.capitalize(name);
		try {
			Method method = object.getClass().getMethod(methodName, new Class[]{});
			return (String) method.invoke(object);
		} catch (Exception e) {
			return "";
		}
	}
}
