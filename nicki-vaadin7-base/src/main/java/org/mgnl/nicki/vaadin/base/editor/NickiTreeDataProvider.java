package org.mgnl.nicki.vaadin.base.editor;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.objects.DynamicObjectException;

import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.function.SerializablePredicate;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class NickiTreeDataProvider extends TreeDataProvider<TreeData> {
	private NickiContext context;
	private @Getter org.mgnl.nicki.core.data.DataProvider<TreeData> treeDataProvider;
	private @Getter @Setter SerializablePredicate<TreeData> filter = null;
	private TreeSet<String> loadedSet = new TreeSet<>();
	private @Getter TreeData root;
    public NickiTreeDataProvider(NickiContext context, org.mgnl.nicki.core.data.DataProvider<TreeData> treeDataProvider) {
		super(new com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData>());
		this.context = context;
		this.treeDataProvider = treeDataProvider;
		this.root = treeDataProvider.getRoot(context);
	}

	public int XXX_getChildCount(HierarchicalQuery<TreeData, SerializablePredicate<TreeData>> query) {
		if (!query.getParentOptional().isPresent()) {
			return 1;
		} else {
			TreeData parent = query.getParentOptional().orElse(root);
			return parent.getAllChildren().size();
		}
	}

	@SuppressWarnings("unchecked")
	public Stream<TreeData> XXX_fetchChildren(HierarchicalQuery<TreeData, SerializablePredicate<TreeData>> query) {
		if (!query.getParentOptional().isPresent()) {
			return Arrays.asList(treeDataProvider.getRoot(context)).stream();
		} else {
			TreeData parent = query.getParentOptional().orElse(root);
			List<? extends TreeData> children = parent.getAllChildren();
			return children == null || children.isEmpty() ? Stream.empty() : (Stream<TreeData>) children.stream();
		}
	}

    @Override
    public boolean hasChildren(TreeData parent) {
		boolean loaded = loadedSet.contains(parent.getPath());
		if (!parent.childrenAllowed()) {
			return false;
		}
		if (!loaded) {
			loadChildren(parent, false, false);
		}
		return super.hasChildren(parent);
    }

	@Override
	public Stream<TreeData> fetchChildren(HierarchicalQuery<TreeData, SerializablePredicate<TreeData>> query) {
		TreeData parent = query.getParentOptional().orElse(root);
		loadChildren(parent, false, false);
		//refreshAll();
		return super.fetchChildren(query);
	}

	// TODO: LOADED, children allowed
	
	public void loadChildren(TreeData parent, boolean loadNextGeneration, boolean forceLoad) {
		if (parent == null) {
			loadChildren(root, loadNextGeneration, forceLoad);
			return;
		}
		boolean loaded = loadedSet.contains(parent.getPath());
		if (loaded && forceLoad) {
			removeChildren(parent);
			parent.unLoadChildren();
		}
		if (!loaded || forceLoad) {
			if (parent.childrenAllowed()) {
				loadedSet.add(parent.getPath());
				if (parent == root) {
				    Collection<? extends TreeData> children = this.treeDataProvider.getChildren(context);
				    if (children != null) {
					    for (TreeData child : children) {
							addItem(parent, child);
							if (loadNextGeneration) {
								loadChildren(child, false, forceLoad);
							}
						}
				    }
		
				} else {
					addChildren(parent, parent.getAllChildren(), loadNextGeneration, forceLoad);
				}
			}
		}
	}
	
	private void addChildren(TreeData parent,
			Collection<? extends TreeData> children, boolean loadNextGeneration, boolean forceLoad) {
		if (children != null) {
			for (TreeData child : children) {
				addChild(parent, child);
				if (loadNextGeneration) {
					loadChildren(child, false, forceLoad);
				}
			}
		}
	}

	public void addChild(TreeData parent, TreeData child) {
		if (treeDataProvider.getEntryFilter().accepts(child) && !contains(child)) {
			addItem(parent, child);
		}
	}
	
	public void addItem(TreeData parent, TreeData item) {
		com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData> data = getTreeData();
        // add Root
		data.addItem(parent, item);
	}
	
	public void removeItem(TreeData item) {
		com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData> data = getTreeData();
		data.removeItem(item);
	}
	
	public boolean contains(TreeData item) {
		com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData> data = getTreeData();
		return data.contains(item);
	}

	public TreeData getParent(TreeData child) {
		com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData> data = getTreeData();
		return data.getParent(child);
	}
	
	private List<TreeData> removeChildren(TreeData parent) {
		if (parent != null) {
			com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData> data = getTreeData();
			List<TreeData> toBeRemoved = new ArrayList<TreeData>();
			data.getChildren(parent).stream().forEach(child -> toBeRemoved.add(child));
			toBeRemoved.stream().forEach(item -> data.removeItem(item));
			refreshAll();
			return toBeRemoved;
		}
		return new ArrayList<>();
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

	
	public void setParent(TreeData object, TreeData parent) throws DynamicObjectException {
		com.vaadin.flow.data.provider.hierarchy.TreeData<TreeData> data = getTreeData();
		data.setParent(object, parent);
		refreshAll();
		String newPath = object.getChildPath(parent, object);
		object.moveTo(newPath);
	}
	

	private void unload(List<TreeData> children) {
		if (children != null) {
			children.stream().forEach(item -> loadedSet.remove(item));
		}
		
	}

}
