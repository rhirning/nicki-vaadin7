
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
import java.util.Date;

public class FileWrapper {
	private File file;

	public FileWrapper(File file) {
		super();
		this.file = file;
	}

	public File getFile() {
		return file;
	}
	
	public String getPath() throws IOException {
		return file.getCanonicalPath();
	}
	
	public Date getLastModified() {
		return new Date(file.lastModified());
	}
	
	public String getName() {
		return file.getName();
	}
	
	public long getSize() {
		return file.length();
	}

	public String getMod() {
		StringBuilder sb = new StringBuilder();
		sb.append(file.canRead() ? "r" : "-");
		sb.append(file.canWrite() ? "w" : "-");
		sb.append(file.canExecute() ? "x" : "-");
		return sb.toString();
	}

}
