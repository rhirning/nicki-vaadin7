
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


import java.util.Collection;

import org.mgnl.nicki.core.data.TreeData;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandListener;

@SuppressWarnings("serial")
public class TreeSelector extends BasicNickiSelector implements NickiSelect {
	private Tree component = new Tree();

	public TreeSelector() {
		super();
		super.setComponent(component);
	}
	public void setSelectable(boolean selectable) {
		component.setSelectable(selectable);
	}

	public void addActionHandler(Handler handler) {
		component.addActionHandler(handler);
	}


	@Override
	public void expandItem(TreeData object) {
		component.expandItem(object);
	}

	@Override
	public void addExpandListener(ExpandListener listener) {
		component.addExpandListener(listener);
	}

	@Override
	public Collection<?> rootItemIds() {
		return component.rootItemIds();
	}

	@Override
	public void collapseItemsRecursively(TreeData startItemId) {
		component.collapseItemsRecursively(startItemId);
	}

	@Override
	public void expandItemsRecursively(Object object) {
		component.expandItem(object);
	}

}
