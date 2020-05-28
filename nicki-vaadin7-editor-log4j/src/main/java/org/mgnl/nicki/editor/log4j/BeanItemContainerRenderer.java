
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
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.mgnl.nicki.core.helper.NameValue;
import com.vaadin.data.util.BeanItemContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanItemContainerRenderer extends Thread implements Runnable {
	BeanItemContainer<NameValue> container;
	OutputStream out;

	public BeanItemContainerRenderer(BeanItemContainer<NameValue> container, OutputStream out) {
		super();
		this.container = container;
		this.out = out;
	}
	public void run() {
		try {
			for (NameValue entry : container.getItemIds()) {
				IOUtils.write(entry.getValue(), out);
				IOUtils.write("\n", out);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("Error reading container", e);
		}

	}
}