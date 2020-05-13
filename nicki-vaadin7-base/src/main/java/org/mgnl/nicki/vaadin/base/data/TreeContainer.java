
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


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.EntryFilter;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.vaadin.base.editor.Icon;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.ThemeResource;

@SuppressWarnings("serial")
public class TreeContainer implements Serializable {
	public static final String PROPERTY_NAME = "name"; 
	public static final String PROPERTY_LOADED = "loaded"; 
	public static final String PROPERTY_ICON = "icon"; 
	
	private HierarchicalContainer container = new HierarchicalContainer();
	private String name;
	private TreeData root;
	private DataProvider treeDataProvider;
	private Map<Class<? extends TreeData>, Icon> classIcons= new HashMap<>();
	private EntryFilter entryFilter;
	private NickiContext context;
	
	public TreeContainer(NickiContext context, DataProvider treeDataProvider, String name) {
		this.context = context;
		this.treeDataProvider = treeDataProvider;
		this.root = treeDataProvider.getRoot(context);
		this.name = name;
		this.entryFilter = treeDataProvider.getEntryFilter();
	}
	@SuppressWarnings("unchecked")
	public HierarchicalContainer getTree() {
		container = new HierarchicalContainer();
		container.addContainerProperty(PROPERTY_NAME, String.class, null);
		container.addContainerProperty(PROPERTY_LOADED, Boolean.class, null);
        container.addContainerProperty(PROPERTY_ICON, ThemeResource.class, null);
        
        // add Root
//	    root = ObjectBuilder.loadObject(this.parentPath);
	    Item item = container.addItem(root);
		item.getItemProperty(PROPERTY_NAME).setValue(name);
		item.getItemProperty(PROPERTY_LOADED).setValue(false);
		item.getItemProperty(PROPERTY_ICON).setValue(null);
	    
	    return container;
	}
	
	public void setClassIcon(Class<? extends TreeData> classDefinition, Icon icon) {
		this.classIcons.put(classDefinition, icon);
	}

	@SuppressWarnings("unchecked")
	public Item addItem(TreeData object) {
		if (this.entryFilter.accepts(object)) {
			Item item = container.addItem(object);
			item.getItemProperty(PROPERTY_NAME).setValue(object.getDisplayName());
			item.getItemProperty(PROPERTY_LOADED).setValue(false);
			if (this.classIcons.keySet().contains(object.getClass())) {
	            item.getItemProperty(PROPERTY_ICON).setValue(
	                    new ThemeResource(this.classIcons.get(object.getClass()).getResourcePath()));
			}
			return item;
		}
		return null;
	}

	public void loadChildren(TreeData parent) {
		@SuppressWarnings("unchecked")
		Property<Boolean> loaded = container.getItem(parent).getItemProperty(PROPERTY_LOADED);
		if (!loaded.getValue()) {
			if (parent == this.root) {
			    Collection<? extends TreeData> objects = this.treeDataProvider.getChildren(context);
			    if (objects != null) {
				    for (TreeData p : objects) {
						if (this.entryFilter.accepts(p)) {
							addItem(p, root, true);
							boolean childrenAllowed = p.childrenAllowed();
							container.setChildrenAllowed(p, childrenAllowed);
						}
					}
			    }

			} else {
				addChildren(parent, parent.getAllChildren());
			}
		}
		loaded.setValue(true);
	}
	
	public void addChildren(Object parent,
			Collection<? extends TreeData> children) {
		for (TreeData p : children) {
			if (this.entryFilter.accepts(p)) {
				addChild(parent, p);
			}
		}
	}

	public void addChild(Object parent, TreeData child) {
		if (this.entryFilter.accepts(child)) {
			boolean childrenAllowed = child.childrenAllowed();
			addItem(child, parent, childrenAllowed);
		}
	}

	@SuppressWarnings("unchecked")
	public void addItem(TreeData object, Object parent, boolean childrenAllowed) {
		if (this.entryFilter.accepts(object)) {
			Item item = container.addItem(object);
			if (item != null) {
				item.getItemProperty(PROPERTY_NAME).setValue(object.getDisplayName());
				item.getItemProperty(PROPERTY_LOADED).setValue(false);
				if (this.classIcons.keySet().contains(object.getClass())) {
		            item.getItemProperty(PROPERTY_ICON).setValue(
		                    new ThemeResource(this.classIcons.get(object.getClass()).getResourcePath()));
				}

				container.setParent(object, parent);
				container.setChildrenAllowed(object, childrenAllowed);
			}
		}
	}

	public TreeData getRoot() {
		return root;
	}

	public void setParent(TreeData object, TreeData parent) throws DynamicObjectException {
		container.setParent(object, parent);
		String newPath = object.getChildPath(parent, object);
		object.moveTo(newPath);
	}

	public TreeData getParent(TreeData child) {
		return (TreeData) container.getParent(child);
	}
	
	@SuppressWarnings("unchecked")
	public void removeChildren(TreeData parent) {
		if (parent != null) {
			Item item = container.getItem(parent);
			if (item != null) {
				while (container.getChildren(parent)!= null && container.getChildren(parent).size() > 0) {
					TreeData child = (TreeData) container.getChildren(parent).iterator().next();
					container.removeItemRecursively(child);
				}
				item.getItemProperty(PROPERTY_LOADED).setValue(false);
			}
		}
	}
	
	public boolean isParent(TreeData parent,
			TreeData child) {
		TreeData object = child;
		while (object != null) {
			if (object == parent) {
				return true;
			}
			object = getParent(object);
		}
		return false;
	}
	public String getRootMessage() {
		return this.treeDataProvider.getMessage();
	}



}
