package org.mgnl.nicki.vaadin.base.editor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.objects.DynamicObjectException;

import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.server.SerializablePredicate;

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
		super(new com.vaadin.data.TreeData<TreeData>());
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

	public Stream<TreeData> fetchChildren(HierarchicalQuery<TreeData, SerializablePredicate<TreeData>> query) {
		TreeData parent = query.getParentOptional().orElse(root);
		loadChildren(parent, false);
		return super.fetchChildren(query);
	}

	// TODO: LOADED, children allowed
	
	public void loadChildren(TreeData parent, boolean loadNextGeneration) {
		if (parent == null) {
			loadChildren(root, loadNextGeneration);
			return;
		}
		boolean loaded = loadedSet.contains(parent.getPath());
		if (!loaded) {
			if (parent.childrenAllowed()) {
				if (parent == root) {
				    Collection<? extends TreeData> children = this.treeDataProvider.getChildren(context);
				    if (children != null) {
					    for (TreeData child : children) {
							addItem(parent, child);
							if (loadNextGeneration) {
								loadChildren(child, false);
							}
						}
				    }
		
				} else {
					addChildren(parent, parent.getAllChildren(), loadNextGeneration);
				}
			}
			refreshAll();
			if (loadNextGeneration) {
				loadedSet.add(parent.getPath());
			}
		}
	}
	
	public void addChildren(TreeData parent,
			Collection<? extends TreeData> children, boolean loadNextGeneration) {
		if (children != null) {
			for (TreeData child : children) {
				addChild(parent, child);
				if (loadNextGeneration) {
					loadChildren(child, false);
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
		com.vaadin.data.TreeData<TreeData> data = getTreeData();
        // add Root
		data.addItem(parent, item);
	}
	
	public boolean contains(TreeData item) {
		com.vaadin.data.TreeData<TreeData> data = getTreeData();
		return data.contains(item);
	}

	public TreeData getParent(TreeData child) {
		com.vaadin.data.TreeData<TreeData> data = getTreeData();
		return data.getParent(child);
	}
	
	public void removeChildren(TreeData parent) {
		if (parent != null) {
			com.vaadin.data.TreeData<TreeData> data = getTreeData();
			for (TreeData child : data.getChildren(parent)) {
				data.removeItem(child);
			}
			refreshAll();
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

	public void setParent(TreeData object, TreeData parent) throws DynamicObjectException {
		com.vaadin.data.TreeData<TreeData> data = getTreeData();
		data.setParent(object, parent);
		refreshAll();
		String newPath = object.getChildPath(parent, object);
		object.moveTo(newPath);
	}

}
