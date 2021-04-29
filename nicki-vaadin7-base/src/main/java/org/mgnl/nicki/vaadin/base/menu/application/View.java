
package org.mgnl.nicki.vaadin.base.menu.application;

import org.mgnl.nicki.vaadin.base.application.NickiApplication;

/*-
 * #%L
 * nicki-app-menu
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


import com.vaadin.ui.Component;

public interface View extends Component{

	void init();

	boolean isModified();
	
	void setApplication(NickiApplication application);
	
	default boolean needsHeightFull() {
		return true;
	}

}
