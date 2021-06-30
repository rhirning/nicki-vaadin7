
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.core.objects.ChildFilter;
import org.mgnl.nicki.core.objects.DynamicObject;
import org.mgnl.nicki.core.data.DataProvider;
import org.mgnl.nicki.core.data.EntryFilter;
import org.mgnl.nicki.core.data.TreeData;


@SuppressWarnings("serial")
public class DynamicObjectRoot implements DataProvider<TreeData>, Serializable {
	private String baseDn;
	private EntryFilter entryFilter;

	public DynamicObjectRoot(String baseDn, EntryFilter entryFilter) {
		super();
		this.baseDn = baseDn;
		this.entryFilter = entryFilter;
	}

	public List<? extends TreeData> getChildren(NickiContext context) {
		List<? extends DynamicObject> list = context.loadChildObjects(baseDn, new ChildFilter());
		Collections.sort(list, new Comparator<DynamicObject>() {

			@Override
			public int compare(DynamicObject o1, DynamicObject o2) {
				// TODO Auto-generated method stub
				return StringUtils.lowerCase(o1.getName()).compareTo(StringUtils.lowerCase(o2.getName()));
			}
		});
		return list;
	}

	public DynamicObject getRoot(NickiContext context) {
		return context.loadObject(baseDn);
	}

	public String getMessage() {
		return "";
	}

	public EntryFilter getEntryFilter() {
		return this.entryFilter;
	}

	@Override
	public Collection<? extends TreeData> getChildren(TreeData parent) {
		return parent.getAllChildren();
	}

}
