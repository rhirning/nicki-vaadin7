
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

public abstract class DeletableContainer<T extends Deletable> extends IndexedContainer implements Container {
	private static final long serialVersionUID = -5658914311396563600L;
	
	private String deleteCaption;
	private String undeleteCaption;

	public DeletableContainer(Collection<? extends T> collection, Object columns[],
			String deleteCaption, String undeleteCaption) throws IllegalArgumentException {
		super();
		this.deleteCaption = deleteCaption;
		this.undeleteCaption = undeleteCaption;
		for (Object object : columns) {
			addContainerProperty(object, String.class, "");
		}
		addContainerProperty("delete", Component.class, null);
		for (T bean : collection) {
			Item item = addItem(bean);
			for (Object object : columns) {
				@SuppressWarnings("unchecked")
				Property<String> property = item.getItemProperty(object);
				property.setValue(get(bean, (String) object));
			}
			@SuppressWarnings("unchecked")
			Property<Component> editProperty = item.getItemProperty("delete");
			String caption;
			if (bean.isDeletable()) {
				if (bean.isDeleted()) {
				 caption = undeleteCaption;
				} else {
					 caption = deleteCaption;
				}
				editProperty.setValue(new DeleteButton<T>(bean, caption));
			}
		}
	}

	public class DeleteButton<T1 extends Deletable> extends Button implements Property<Component> {
		private static final long serialVersionUID = -5928278839208615249L;
		public DeleteButton(T1 bean, String title) {
			setCaption(title);
			setData(bean);
			addClickListener(event -> {
					@SuppressWarnings("unchecked")
					T1 bean1 = (T1) event.getButton().getData();
					if (!bean1.isDeleted()) {
						bean1.delete();
						event.getButton().setCaption(undeleteCaption);
					} else {
						bean1.undelete();
						event.getButton().setCaption(deleteCaption);
					}
			});
		}
		@Override
		public DeleteButton<T1> getValue() {
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
