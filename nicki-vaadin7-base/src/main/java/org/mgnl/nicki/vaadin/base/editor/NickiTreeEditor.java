
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.InstantiateDynamicObjectException;
import org.mgnl.nicki.core.data.InvalidActionException;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.command.DeleteCommand;
import org.mgnl.nicki.vaadin.base.components.EnterNameDialog;
import org.mgnl.nicki.vaadin.base.components.EnterNameHandler;
import org.mgnl.nicki.vaadin.base.components.NewClassEditor;
import org.mgnl.nicki.vaadin.base.components.SimpleNewClassEditor;
import org.mgnl.nicki.vaadin.base.data.TreeContainer;

import com.vaadin.event.Action;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class NickiTreeEditor extends CustomComponent {

	public enum CREATE {
		ALLOW, DENY
	};

	public enum DELETE {
		ALLOW, DENY
	};

	public enum RENAME {
		ALLOW, DENY
	};

	private NickiSelect selector;
	private TreeData selectedObject;
	private ClassEditor viewer;
	private TreeContainer treeContainer;
	private String messageKeyBase;
	private DataProvider treeDataProvider;
	private String treeTitle;

	private Map<Class<? extends TreeData>, List<Class<? extends TreeData>>> children
		= new HashMap<>();

	private Map<Class<? extends TreeData>, Map<Action, Class<? extends TreeData>>> actions = new HashMap<>();
	private Map<Class<? extends TreeData>, Action[]> actionsList = new HashMap<>();
	private Map<Class<? extends TreeData>, Action[]> rootActionsList = new HashMap<>();
	private Map<Action, Class<? extends TreeData>> deleteActions = new HashMap<>();
	private Map<Action, Class<? extends TreeData>> renameActions = new HashMap<>();
	private Action refreshAction;

	private List<Class<? extends TreeData>> allowCreate = new ArrayList<>();
	private List<Class<? extends TreeData>> allowDelete = new ArrayList<>();
	private List<Class<? extends TreeData>> allowRename = new ArrayList<>();
	private Map<Class<? extends TreeData>, List<TreeAction>> treeActions = new HashMap<>();

	private Map<Action, TreeAction> treeActionMap = new HashMap<Action, TreeAction>();

	private Map<Class<? extends TreeData>, ClassEditor> classEditors = new HashMap<>();
	private Map<Class<? extends TreeData>, NewClassEditor> newClassEditors = new HashMap<>();
	private NickiContext context;
	private NickiApplication application;
	private HorizontalSplitPanel hsplit;
	private NickiTreeEditor nickiEditor;

	public NickiTreeEditor(NickiApplication application, NickiContext ctx) {
		this.nickiEditor = this;
		this.application = application;
		this.context = ctx;
		this.hsplit = new HorizontalSplitPanel();
		hsplit.setSplitPosition(200, Unit.PIXELS);
		setCompositionRoot(hsplit);
	}

	public void init(NickiSelect select, DataProvider treeDataProvider,
			String messageKeyBase) {

		this.treeDataProvider = treeDataProvider;
		this.messageKeyBase = messageKeyBase;
		this.treeTitle = I18n.getText(this.messageKeyBase + ".tree.title");
		this.treeContainer = new TreeContainer(this.context,
				this.treeDataProvider, this.treeTitle);
		selector = select;
		loadTree();
		
		
		Component selectorComponent = selector.getComponent();
		//selectorComponent.setSizeFull();

		hsplit.setFirstComponent(selectorComponent);
		hsplit.addStyleName(ValoTheme.SPLITPANEL_LARGE);

		selector.setImmediate(true);
		selector.setSelectable(true);
		selector.addValueChangeListener(event -> {
				TreeData selected = (TreeData) selector.getValue();
				if (selected == null) {
					if (viewer != null && selectedObject.isModified()) {
						try {
							// TODO: ask save/not save/back
							viewer.save();
						} catch (Exception e) {
							log.error("Error", e);
						}
					}
					hideClassEditor();
					return;

				}
				if (viewer != null && selectedObject.isModified()) {
					try {
						// TODO: ask save/not save/back
						viewer.save();
					} catch (Exception e) {
						log.error("Error", e);
					}
				}
				setSelectedObject(selected);
				if (selected == null || isRoot(selected)) {
					hideClassEditor();
				} else {
					for (Class<? extends TreeData> clazz : classEditors.keySet()) {
						if (clazz.isAssignableFrom(selected.getClass())) {
							ClassEditor classEditor = classEditors.get(clazz);
							showClassEditor(classEditor, selected);
							return;
						}
					}
					if (DynamicObject.class.isAssignableFrom(selected.getClass())) {
						showClassEditor(new DynamicObjectViewer(), selected);
					} else {
						showClassEditor(new TreeDataViewer(), selected);
						
					}
					/*
					if (classEditors.containsKey(selected.getClass())) {
						ClassEditor classEditor = classEditors.get(selected
								.getClass());
						showClassEditor(classEditor, selected);
					} else {
						showClassEditor(new DynamicObjectViewer(), selected);
					}
					*/
				}
		});

		selector.addActionHandler(new Action.Handler() {

			public void handleAction(Action action, Object sender, Object target) {
				if (action == refreshAction) {
					refresh((TreeData) target);
				}
				if (deleteActions.containsKey(action)) {
					if (target.getClass() == deleteActions.get(action)) {
						getNickiApplication().confirm(
								new DeleteCommand(nickiEditor,
										(TreeData) target));
					}
				} else if (renameActions.containsKey(action)) {
					if (target.getClass() == renameActions.get(action)) {
						try {
							if (!isRoot((TreeData) target)) {
								renameItem((TreeData) target);
							}
						} catch (Exception e) {
							log.error("Error", e);
						}
					}
				} else if (treeActionMap.containsKey(action)) {
					treeActionMap.get(action).execute((TreeData) target);
				} else if (actions.containsKey(target.getClass())) {
					Map<Action, Class<? extends TreeData>> map = actions
							.get(target.getClass());
					if (map.containsKey(action)) {
						create((TreeData) target, map.get(action));
					}
				}
			}

			public Action[] getActions(Object target, Object sender) {
				if (target != null) {
					if (isRoot((TreeData) target)) {
						for (Class<? extends TreeData> clazz : rootActionsList.keySet()) {
							if (clazz.isAssignableFrom(target.getClass())) {
								return rootActionsList.get(clazz);
							}
						}

					} else {
						for (Class<? extends TreeData> clazz : actionsList.keySet()) {
							if (clazz.isAssignableFrom(target.getClass())) {
								return actionsList.get(clazz);
							}
						}
					}
				}
				return null;
			}
		});

		selector.addExpandListener(event -> {
				TreeData object = (TreeData) event.getItemId();
				treeContainer.loadChildren(object);
		});
	}
	
	public void showClassEditor(ClassEditor classEditor, TreeData selected) {

		classEditor.setDynamicObject(getEditor(), selected);
		viewer = classEditor;
		hsplit.setSecondComponent(viewer);
		
	}
	
	public void hideClassEditor() {
		if  (viewer != null) {
			hsplit.removeComponent(viewer);
			viewer = null;
		}
	}

	protected NickiTreeEditor getEditor() {
		return this;
	}

	public void refresh(TreeData object) {
		if (selectedObject != null) {
			selector.unselect(selectedObject);
			setSelectedObject(null);
		}

		hideClassEditor();
		viewer = null;

		collapse(object);
		reloadChildren(object);
		selector.expandItem(object);
	}

	protected boolean isRoot(TreeData dynamicObject) {
		return dynamicObject == this.treeContainer.getRoot();
	}

	public void setClassIcon(Class<? extends TreeData> classDefinition,
			Icon icon) {
		this.treeContainer.setClassIcon(classDefinition, icon);
	}

	public void configureClass(Class<? extends TreeData> parentClass,
			Icon icon, CREATE allowCreate, DELETE allowDelete, RENAME allowRename,
			@SuppressWarnings("unchecked") Class<? extends TreeData>... childClass) {

		List<TreeData> dynamicObjects = new ArrayList<>();
		
		if (DynamicObject.class.isAssignableFrom(parentClass)) {
			try {
				for (TreeData treeData : getNickiContext().getObjectFactory()
						.findDynamicObjects(parentClass)) {
					dynamicObjects.add(treeData);
				}
			} catch (InstantiateDynamicObjectException e) {
				log.error("Error configuring parent class " + parentClass.getName(), e);
				return;
			}			
		} else {
			try {
				dynamicObjects.add(parentClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				log.error("Error configuring parent class " + parentClass.getName(), e);
				return;
			}
		}

		for (Object object : dynamicObjects) {
			TreeData treeData = (TreeData) object;
			if (icon != null) {
				setClassIcon(treeData.getClass(), icon);
			}
			if (allowCreate == CREATE.ALLOW) {
				this.allowCreate.add(treeData.getClass());
			}
			if (allowDelete == DELETE.ALLOW) {
				this.allowDelete.add(treeData.getClass());
			}
			if (allowRename == RENAME.ALLOW) {
				this.allowRename.add(treeData.getClass());
			}

			List<Class<? extends TreeData>> list = children
					.get(treeData.getClass());
			if (list == null) {
				list = new ArrayList<>();
				children.put(treeData.getClass(), list);
			}
			for (int i = 0; i < childClass.length; i++) {				
				if (DynamicObject.class.isAssignableFrom(childClass[i])) {
					try {
						for (TreeData childObject :  getNickiContext()
								.getObjectFactory().findDynamicObjects(
										childClass[i])) {
							list.add(childObject.getClass());
						}
					} catch (InstantiateDynamicObjectException e) {
						log.error("Error configuring child class " + parentClass.getName(), e);
						continue;
					}
				} else {
					list.add(childClass[i]);
				}
			}
}
	}

	public void addAction(TreeAction treeAction) {
		List<TreeAction> actions = this.treeActions.get(treeAction
				.getTargetClass());
		if (actions == null) {
			actions = new ArrayList<TreeAction>();
			this.treeActions.put(treeAction.getTargetClass(), actions);
		}
		actions.add(treeAction);
	}

	public void setClassEditor(Class<? extends TreeData> classdefinition,
			ClassEditor classEditor) {
		this.classEditors.put(classdefinition, classEditor);
	}

	protected void renameItem(TreeData target) {
		EnterNameHandler handler = new RenameObjecttEnterNameHandler(this,
				target);
		EnterNameDialog dialog = new EnterNameDialog(messageKeyBase + ".rename",
				I18n.getText(messageKeyBase
						+ ".rename.window.title"));
		dialog.setHandler(handler);
		dialog.setWidth(440, Unit.PIXELS);
		dialog.setHeight(500, Unit.PIXELS);
		dialog.setModal(true);
		UI.getCurrent().addWindow(dialog);
	}

	protected void create(TreeData parent,
			Class<? extends TreeData> classDefinition) {
		treeContainer.loadChildren(parent);
		try {
			addDynamicObject(parent, classDefinition);
		} catch (Exception e) {
			Notification.show(I18n.getText("nicki.editor.create.error", parent.getName(),
							classDefinition.getSimpleName()), e.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	public void loadTree() {
		selector.setContainerDataSource(treeContainer.getTree());
		selector.setItemCaptionPropertyId(TreeContainer.PROPERTY_NAME);
		selector.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		selector.setItemIconPropertyId(TreeContainer.PROPERTY_ICON);
	}

	private void addDynamicObject(TreeData parent,
			Class<? extends TreeData> classDefinition)
			throws InstantiateDynamicObjectException, DynamicObjectException {
		NewClassEditor editor;
		if (this.newClassEditors.get(classDefinition) != null) {
			editor = this.newClassEditors.get(classDefinition);
			editor.init(parent, classDefinition);
		} else {
			editor = new SimpleNewClassEditor(this, messageKeyBase + "."
					+ getClassName(classDefinition) + ".create",
					I18n.getText(messageKeyBase + "."
							+ getClassName(classDefinition) + ".create.window.title"));
			editor.init(parent, classDefinition);
		}
		if (editor instanceof Window) {
			Window window = (Window) editor;
			window.setWidth(440, Unit.PIXELS);
			window.setHeight(500, Unit.PIXELS);
			window.setModal(true);
			UI.getCurrent().addWindow(window);
		} else {
			log.error("editor must be of type Window");
		}

	}

	public void setNewClassEditor(
			Class<? extends TreeData> classDefinition,
			NewClassEditor newClassEditor) {
		this.newClassEditors.put(classDefinition, newClassEditor);
	}

	protected <T extends TreeData> boolean create(TreeData parent,
			Class<T> classDefinition, String name)
			throws InstantiateDynamicObjectException, DynamicObjectException, InvalidActionException {
		T dynamicObject = null;
		dynamicObject = parent.createChild(classDefinition, name);
		if (dynamicObject != null) {
			treeContainer.addItem(dynamicObject, parent, dynamicObject.childrenAllowed());
			return true;
		}
		return false;
	}

	public void initActions() {
		refresh(treeContainer.getRoot());

		refreshAction = new Action(I18n.getText(this.messageKeyBase
				+ ".action.refresh"));

		for (Class<? extends TreeData> classDefinition : this.children
				.keySet()) {
			List<Action> classActions = new ArrayList<Action>();
			List<Action> rootClassActions = new ArrayList<Action>();
			Map<Action, Class<? extends TreeData>> map = new HashMap<>();
			// treeActions
			List<TreeAction> a = getTreeActions(classDefinition);
			if(a != null && !a.isEmpty()) {
				for (TreeAction treeAction : a) {
					Action action = new Action(treeAction.getName());
					classActions.add(action);
					rootClassActions.add(action);
					treeActionMap.put(action, treeAction);
				}
			}
			if (this.children.get(classDefinition) != null) {
				for (Class<? extends TreeData> childClassPattern : this.children
						.get(classDefinition)) {
					if (this.allowCreate.contains(childClassPattern)) {
						Action childAction = new Action(
								I18n.getText(this.messageKeyBase
										+ ".action."
										+ getClassName(childClassPattern) + ".new"));
						classActions.add(childAction);
						rootClassActions.add(childAction);
						map.put(childAction, childClassPattern);
					}
				}
			}
			// delete
			if (this.allowDelete.contains(classDefinition)) {
				Action deleteAction = new Action(
						I18n.getText(this.messageKeyBase + ".action.delete"));
				classActions.add(deleteAction);
				this.deleteActions.put(deleteAction, classDefinition);
			}
			// rename
			if (this.allowRename.contains(classDefinition)) {
				Action renameAction = new Action(
						I18n.getText(this.messageKeyBase + ".action.rename"));
				classActions.add(renameAction);
				this.renameActions.put(renameAction, classDefinition);
			}
			// refresh
			rootClassActions.add(refreshAction);
			classActions.add(refreshAction);
			//
			actions.put(classDefinition, map);
			rootActionsList.put(classDefinition,
					rootClassActions.toArray(new Action[0]));
			actionsList.put(classDefinition,
					classActions.toArray(new Action[0]));
		}
	}

	private List<TreeAction> getTreeActions(Class<? extends TreeData> classDefinition) {
		List<TreeAction> list = new ArrayList<>();
		for (Class<? extends TreeData> clazz : this.treeActions.keySet()) {
			if (clazz.isAssignableFrom(classDefinition)) {
				list.addAll(this.treeActions.get(clazz));
			}
		}
		return list;
	}

	public Action[] getActions(Object object) {
		return actionsList.get(object.getClass());
	}

	public TreeData getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(TreeData selectedObject) {
		this.selectedObject = selectedObject;
	}

	public void addChild(TreeData parent, TreeData child) {
		this.treeContainer.addChild(parent, child);
	}

	public TreeData getParent(TreeData child) {
		return this.treeContainer.getParent(child);
	}

	public void reloadChildren(TreeData parent) {
		hideClassEditor();
		parent.unLoadChildren();
		this.treeContainer.removeChildren(parent);
		this.treeContainer.loadChildren(parent);
	}

	public List<Class<? extends TreeData>> getAllowedChildren(
			Class<? extends TreeData> classDefinition) {
		return this.children.get(classDefinition);
	}

	public boolean isParent(TreeData parent, TreeData object) {
		return this.treeContainer.isParent(parent, object);
	}

	public void moveObject(TreeData object, TreeData target)
			throws DynamicObjectException {
		this.treeContainer.setParent(object, target);
	}

	public String getClassName(Class<? extends TreeData> classDefinition) {
		return StringUtils.substringAfterLast(classDefinition.getName(), ".");
	}

	public void expandAll() {
		for (Object id : selector.rootItemIds()) {
			selector.expandItemsRecursively(id);
		}
	}

	public void collapse(TreeData object) {
		selector.collapseItemsRecursively(object);
	}

	public String getMessageKeyBase() {
		return messageKeyBase;
	}

	public NickiApplication getNickiApplication() {
		return application;
	}

	public NickiContext getNickiContext() {
		return context;
	}

	public NickiSelect getSelector() {
		return selector;
	}
}
