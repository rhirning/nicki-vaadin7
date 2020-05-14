
package org.mgnl.nicki.editor.catalogs;

/*-
 * #%L
 * nicki-editor-catalogs
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

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.vaadin.base.components.EnterNameHandler;

import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class NewEntryEnterNameHandler extends EnterNameHandler implements Serializable {
	private Table table;

	public NewEntryEnterNameHandler(Table table) {
		super("");
		this.table = table;
	}

	public void setName(String name) throws Exception {
		if (StringUtils.isNotEmpty(name)) {
			table.addItem(new Object[]{name}, name);
		}
	}

}
