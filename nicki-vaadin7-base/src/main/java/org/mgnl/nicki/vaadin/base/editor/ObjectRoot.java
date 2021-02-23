
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


import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.vaadin.base.data.ChildObjectWrapper;
import org.mgnl.nicki.vaadin.base.data.ObjectWrapper;

import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.EntryFilter;


@SuppressWarnings("serial")
public class ObjectRoot implements DataProvider<ObjectWrapper>, Serializable {

	private static ObjectWrapper ROOT = new ObjectWrapper("ROOT");
	private Object[] objects;
	private EntryFilter entryFilter;

	public ObjectRoot(Object[] objects, EntryFilter entryFilter) {
		super();
		this.objects = objects;
		this.entryFilter = entryFilter;
	}

	public List<? extends ObjectWrapper> getChildren(NickiContext context) {
		return Arrays.asList(objects).stream().map(ObjectRoot::wrap).collect(Collectors.toList());
	}
	
	public static ObjectWrapper wrap(Object object) {
		return new ObjectWrapper(object);
	}

	public ObjectWrapper getRoot(NickiContext context) {
		return ROOT;
	}

	public String getMessage() {
		return "";
	}

	public EntryFilter getEntryFilter() {
		return this.entryFilter;
	}

	public Collection<? extends ObjectWrapper> getChildren(NickiContext context, ObjectWrapper parent) {
		if (parent == null) {
			List<ObjectWrapper> list = new ArrayList<ObjectWrapper>();
			for (ObjectWrapper objectWrapper : getChildren(context)) {
				list.add(objectWrapper);
			}
			return list;
		} else {
			return getChildren(parent);
		}
	}

	@Override
	public List<ObjectWrapper> getChildren(ObjectWrapper objectWrapper) {
		List<ObjectWrapper> list = new ArrayList<ObjectWrapper>();
		Map<String, Object> attributes = getAttributes(objectWrapper.getObject());
		for (String name : attributes.keySet()) {
			Object child = attributes.get(name);
			list.add(new ChildObjectWrapper(name, child));
		}
		return list;
	}

	private Map<String, Object> getAttributes(Object object) {
		Map<String, Object> map = new HashMap<>();
		addAllAttributes(map, object.getClass(), object);
		return map;
	}

	private void addAllAttributes(Map<String, Object> map, Class<?> clazz, Object object) {
		AccessibleObject.setAccessible(clazz.getDeclaredFields(), true);
		for (Field field : clazz.getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (!map.containsKey(field.getName())) {
					map.put(field.getName(), field.get(object));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (clazz.isArray()) {
			int length = Array.getLength(object);
		    for (int i = 0; i < length; i ++) {
				map.put("" + i, Array.get(object, i));
			}
		}
		if (clazz.getSuperclass() != null) {
			addAllAttributes(map, clazz.getSuperclass(), object);
		}
	}

}
