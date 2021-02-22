package org.mgnl.nicki.vaadin.base.editor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.vaadin.base.data.ChildObjectWrapper;
import org.mgnl.nicki.vaadin.base.data.ObjectWrapper;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.server.SerializablePredicate;

@SuppressWarnings("serial")
public class NickiObjectDataProvider extends TreeDataProvider<ObjectWrapper>
		implements DataProvider<ObjectWrapper, SerializablePredicate<ObjectWrapper>> {
	private NickiContext context;
	private org.mgnl.nicki.core.data.DataProvider<ObjectWrapper> treeDataProvider;

	public NickiObjectDataProvider(NickiContext context, org.mgnl.nicki.core.data.DataProvider<ObjectWrapper> treeDataProvider) {
		super(new com.vaadin.data.TreeData<ObjectWrapper>());
		this.context = context;
		this.treeDataProvider = treeDataProvider;
	}

	@Override
	public int getChildCount(HierarchicalQuery<ObjectWrapper, SerializablePredicate<ObjectWrapper>> query) {
		if (!query.getParentOptional().isPresent()) {
			return 1;
		} else {
			ObjectWrapper parent = query.getParentOptional().orElse(treeDataProvider.getRoot(context));
			return getChildren(parent).size();
		}
	}

	@Override
	public Stream<ObjectWrapper> fetchChildren(HierarchicalQuery<ObjectWrapper, SerializablePredicate<ObjectWrapper>> query) {
		if (!query.getParentOptional().isPresent()) {
			List<ObjectWrapper> list = new ArrayList<ObjectWrapper>();
			for (ObjectWrapper objectWrapper : treeDataProvider.getChildren(context)) {
				list.add(objectWrapper);
			}
			return list.stream();
		} else {
			ObjectWrapper parent = query.getParentOptional().orElse(treeDataProvider.getRoot(context));
			return getChildren(parent).stream();
		}
	}
	
	protected List<ObjectWrapper> getChildren(ObjectWrapper objectWrapper) {
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


	@Override
	public boolean hasChildren(ObjectWrapper item) {
		return getAttributes(item).size() > 0;
	}

}
