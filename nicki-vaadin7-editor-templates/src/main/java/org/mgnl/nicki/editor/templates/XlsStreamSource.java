
package org.mgnl.nicki.editor.templates;

/*-
 * #%L
 * nicki-editor-templates
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


import java.io.InputStream;
import java.util.Map;

import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.core.context.NickiContext;
import org.mgnl.nicki.template.engine.BasicTemplateStreamSource;

import com.vaadin.server.StreamResource.StreamSource;


public class XlsStreamSource extends BasicTemplateStreamSource implements StreamSource {
	private static final long serialVersionUID = 4222973194514516918L;
	public XlsStreamSource(Template template, NickiContext context, Map<String, Object> params) {
		super(template, context, params, TYPE.XLS);
	}

	public InputStream getStream() {
		return getXlsStream();
	}

}
