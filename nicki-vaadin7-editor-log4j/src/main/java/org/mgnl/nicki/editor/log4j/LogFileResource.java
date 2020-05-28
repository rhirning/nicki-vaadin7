
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


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import com.vaadin.server.StreamResource.StreamSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFileResource implements StreamSource {
	private static final long serialVersionUID = -2366308187343189975L;

	TailViewer tailViewer;

	public LogFileResource(TailViewer tailViewer) {
		this.tailViewer = tailViewer;
	}

	@Override
	public InputStream getStream() {
		File file = new File(tailViewer.getPath());
		try {
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			log.error("could not open file: " + tailViewer.getPath());
		}
		return null;
	}

}