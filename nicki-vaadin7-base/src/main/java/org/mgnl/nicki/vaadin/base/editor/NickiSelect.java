
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
import java.util.Collection;

import org.mgnl.nicki.core.data.TreeData;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree.ExpandListener;

public interface NickiSelect extends Serializable {

	void setHeight(String height);

	void setWidth(String width);

	Component getComponent();

	void setImmediate(boolean b);

	void setSelectable(boolean b);

	TreeData getValue();

	void addValueChangeListener(ValueChangeListener listener);

	void addActionHandler(Handler handler);

	void removeItem(Object target);

	void unselect(TreeData objectbject);

	void expandItem(TreeData object);

	void addExpandListener(ExpandListener listener);

	void setContainerDataSource(Container dataSource);

	void setItemCaptionPropertyId(String propertyName);

	void setItemCaptionMode(ItemCaptionMode property);

	void setItemIconPropertyId(String propertyIcon);

	Collection<?> rootItemIds();

	void expandItemsRecursively(Object id);

	void collapseItemsRecursively(TreeData startItemId);


}
