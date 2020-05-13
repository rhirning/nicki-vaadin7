
package org.mgnl.nicki.vaadin.base.dialog;

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

public class NavigationEntry implements Serializable, NavigationElement {
	private static final long serialVersionUID = 4285844482143266130L;
	private String caption;
	private View view;

	public NavigationEntry(String caption, View view) {
		super();
		this.caption = caption;
		this.view = view;
	}

	public String getCaption() {
		return caption;
	}

	public View getView() {
		return view;
	}

	@Override
	public String getNavigationCaption() {
		return getCaption();
	}

}
