
package org.mgnl.nicki.editor.log4j;

/*-
 * #%L
 * nicki-editor-log4j
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


import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.mgnl.nicki.vaadin.base.io.StreamSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContainerFileResource implements StreamSource {


	TailViewer tailViewer;

	public ContainerFileResource(TailViewer tailViewer) {
		this.tailViewer = tailViewer;
	}

	@Override
	public InputStream getStream() {
	    try {
			PipedOutputStream pos = new PipedOutputStream();
			PipedInputStream pis = new PipedInputStream(pos);
			BeanItemContainerRenderer renderer = new BeanItemContainerRenderer(tailViewer.getContainer(), pos);
			renderer.start();
			return pis;
		} catch (IOException e) {
			log.error("could not read container", e);
		}
	    return null;
	}

}
