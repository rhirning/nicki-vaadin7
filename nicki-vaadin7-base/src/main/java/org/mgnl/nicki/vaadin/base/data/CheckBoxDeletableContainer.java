
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
import com.vaadin.ui.CheckBox;

public class CheckBoxDeletableContainer<T extends Deletable> extends IndexedContainer implements Container {
	private static final long serialVersionUID = -5658914311396563600L;
	

	public CheckBoxDeletableContainer(Collection<? extends T> collection, Object columns[]) throws IllegalArgumentException {
		super();
		for (Object object : columns) {
			addContainerProperty(object, String.class, "");
		}
		addContainerProperty("delete", DeleteCheckBox.class, null);
		for (T bean : collection) {
			Item item = addItem(bean);
			for (Object object : columns) {
				@SuppressWarnings("unchecked")
				Property<String> property = item.getItemProperty(object);
				property.setValue(get(bean, (String) object));
			}
			@SuppressWarnings("unchecked")
			Property<DeleteCheckBox<? extends Deletable>> editProperty = item.getItemProperty("delete");
			if (bean.isDeletable()) {
				editProperty.setValue(new DeleteCheckBox<T>(bean));
			}
		}
	}

	@SuppressWarnings("serial")
	public class DeleteCheckBox<T1 extends Deletable> extends CheckBox implements Property<Boolean>, Comparable<DeleteCheckBox<? extends Deletable>> {
		
		public DeleteCheckBox(T1 bean) {
			setData(bean);
			if (bean.isDeleted()) {
				setValue(true);
			} else {
				setValue(false);
			}
			addValueChangeListener(event -> {
					@SuppressWarnings("unchecked")
					T1 bean1 = (T1) getData();
					if ((boolean) event.getProperty().getValue()) {
						bean1.delete();
					} else {
						bean1.undelete();
					}
			});
		}

		@Override
		public int compareTo(DeleteCheckBox<? extends Deletable> o) {
			return this.getValue().compareTo(o.getValue());
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
