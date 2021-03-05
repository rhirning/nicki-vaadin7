
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

import org.mgnl.nicki.core.objects.DynamicObjectException;
import org.mgnl.nicki.core.data.TreeData;
import org.mgnl.nicki.vaadin.base.components.EnterNameHandler;

import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class RenameObjecttEnterNameHandler extends EnterNameHandler implements Serializable {
	private NickiTreeEditor editor;
	private TreeData dynamicObject;

	public RenameObjecttEnterNameHandler(NickiTreeEditor nickiEditor, TreeData dynamicObject) {
		super("");
		this.editor = nickiEditor;
		this.dynamicObject = dynamicObject;
	}

	public void closeEnterNameDialog() {
		UI.getCurrent().removeWindow(getDialog());
	}

	public void setName(String name) throws DynamicObjectException {
		TreeData parent = editor.getParent(dynamicObject);
		dynamicObject.renameObject(name);
		parent.unLoadChildren();
		editor.refresh(parent);
	}

	@Override
	public String getName() {
		return dynamicObject.getName();
	}

}
