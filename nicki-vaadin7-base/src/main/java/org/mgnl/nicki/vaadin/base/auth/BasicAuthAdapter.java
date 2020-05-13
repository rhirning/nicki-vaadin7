
package org.mgnl.nicki.vaadin.base.auth;

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


import org.mgnl.nicki.core.auth.SSOAdapter;
import com.vaadin.server.VaadinServletRequest;


public class BasicAuthAdapter extends org.mgnl.nicki.core.auth.BasicAuthAdapter implements SSOAdapter {
	public Object getRequest() {
		if (super.getRequest() instanceof VaadinServletRequest) {
			return ((VaadinServletRequest) super.getRequest()).getHttpServletRequest();
		} else {
			return super.getRequest();
		}
	}
	
}
