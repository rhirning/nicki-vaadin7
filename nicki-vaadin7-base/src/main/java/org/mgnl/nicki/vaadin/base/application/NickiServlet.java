
package org.mgnl.nicki.vaadin.base.application;

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


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.vaadin.flow.server.VaadinServlet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NickiServlet extends VaadinServlet {
	private static final long serialVersionUID = 6114917183362066306L;
	public static final String NICKI_PARAMETERS = "NICKI_PARAMETERS";
	private static final String[] PARAMETERS = {
		"nickiToken", "nickiName", "nickiPassword"
	};
	

	public NickiServlet() {
		super();
	}


	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute(NICKI_PARAMETERS) == null) {
			Map<String, String> map = new HashMap<String, String>();
			for (String param : PARAMETERS) {
				if (StringUtils.isNotBlank(request.getParameter(param))) {
					map.put(param, request.getParameter(param));
				}
			}
			request.getSession().setAttribute(NICKI_PARAMETERS, map);
			log.debug("ParameterMap: " + map);
		}
		super.service(request, response);
	}


	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}


}
