
package org.mgnl.nicki.vaadin.base.editor;

import org.mgnl.nicki.vaadin.base.io.StreamSource;

import com.vaadin.flow.server.StreamResource;

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

public class LinkResource extends StreamResource {
	public LinkResource(String filename, StreamSource streamSource) {
		super(filename, () -> {
			return streamSource.getStream();
		});
		setCacheTime(1);
	}

	private static final long serialVersionUID = -426896041747116523L;
}
