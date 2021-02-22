
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
import java.util.TreeSet;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.EntryFilter;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.vaadin.base.editor.Icon;
import org.mgnl.nicki.vaadin.base.editor.NickiTreeDataProvider;
import com.vaadin.ui.TreeGrid;

@SuppressWarnings("serial")
public class TreeContainer implements Serializable {
	
	private TreeData root;
	private DataProvider<TreeData> treeDataProvider;
	private Map<Class<? extends TreeData>, Icon> classIcons= new HashMap<>();
	private EntryFilter entryFilter;
	private NickiContext context;
	private NickiTreeDataProvider dataProvider;
	
	public TreeContainer(NickiContext context, DataProvider<TreeData> treeDataProvider) {
		this.context = context;
		this.treeDataProvider = treeDataProvider;
		this.root = treeDataProvider.getRoot(context);
		this.entryFilter = treeDataProvider.getEntryFilter();
	}
	
	public void initTree(TreeGrid<TreeData> treeGrid) {
		treeGrid.addColumn(TreeData::getDisplayName);
		dataProvider = (NickiTreeDataProvider) treeGrid.getDataProvider();
		
        // add Root
		//addRootItems(root);
		dataProvider.refreshAll();
	}
	
	private void addRootItems(TreeData ... rootItems) {
		com.vaadin.data.TreeData<TreeData> data = dataProvider.getTreeData();
        // add Root
		data.addRootItems(rootItems);
	}
	
	public boolean contains(TreeData item) {
		com.vaadin.data.TreeData<TreeData> data = dataProvider.getTreeData();
		return data.contains(item);
	}
	
	public void addItem(TreeData parent, TreeData item) {
		com.vaadin.data.TreeData<TreeData> data = dataProvider.getTreeData();
        // add Root
		data.addItem(parent, item);
	}
	public void setClassIcon(Class<? extends TreeData> classDefinition, Icon icon) {
		this.classIcons.put(classDefinition, icon);
	}

	// TODO: LOADED, icons
	public void addItem(TreeData object) {
		if (this.entryFilter.accepts(object)) {
			addRootItems(object);
			
/*			item.getItemProperty(PROPERTY_LOADED).setValue(false);
			if (this.classIcons.keySet().contains(object.getClass())) {
	            item.getItemProperty(PROPERTY_ICON).setValue(
	                    new ThemeResource(this.classIcons.get(object.getClass()).getResourcePath()));
			}
			*/
		}
	}
	
	public static <T extends TreeData> TreeData cast(T treeData) {
		return (TreeData) treeData;
	}
	
	public void addChildren(TreeData parent,
			Collection<? extends TreeData> children) {
		for (TreeData p : children) {
			if (this.entryFilter.accepts(p) && !contains(p)) {
				addChild(parent, p);
			}
		}
	}

	public void addChild(TreeData parent, TreeData child) {
		if (this.entryFilter.accepts(child)) {
			addItem(parent, child);
		}
	}

	public TreeData getRoot() {
		return root;
	}

	public void setParent(TreeData object, TreeData parent) throws DynamicObjectException {
		com.vaadin.data.TreeData<TreeData> data = dataProvider.getTreeData();
		data.setParent(object, parent);
		dataProvider.refreshAll();
		String newPath = object.getChildPath(parent, object);
		object.moveTo(newPath);
	}

	public TreeData getParent(TreeData child) {
		com.vaadin.data.TreeData<TreeData> data = dataProvider.getTreeData();
		return data.getParent(child);
	}
	
	public void removeChildren(TreeData parent) {
		if (parent != null) {
			com.vaadin.data.TreeData<TreeData> data = dataProvider.getTreeData();
			for (TreeData child : data.getChildren(parent)) {
				data.removeItem(child);
			}
			dataProvider.refreshAll();
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
