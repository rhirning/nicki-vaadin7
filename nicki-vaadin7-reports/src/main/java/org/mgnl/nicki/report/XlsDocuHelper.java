
package org.mgnl.nicki.report;

/*-
 * #%L
 * nicki-reports
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.auth.InvalidPrincipalException;
import org.mgnl.nicki.core.config.Config;
import org.mgnl.nicki.core.context.AppContext;
import org.mgnl.nicki.dynamic.objects.objects.EngineTemplate;
import org.mgnl.nicki.dynamic.objects.objects.Template;
import org.mgnl.nicki.template.engine.ConfigurationFactory;
import org.mgnl.nicki.template.engine.SimpleTemplate;
import org.mgnl.nicki.template.engine.ConfigurationFactory.TYPE;
import org.mgnl.nicki.template.engine.TemplateEngine;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class XlsDocuHelper {
	enum DOC_TYPE {XLS, XLSX}

	@Deprecated
	public static InputStream generate(TYPE type, String templatePath, Map<String, Object> dataModel) throws IOException, TemplateException, InvalidPrincipalException, ParserConfigurationException, SAXException, DocumentException {
		if (type == TYPE.JNDI) {
			return generateJNDI(DOC_TYPE.XLS, templatePath, dataModel);
		} else if (type == TYPE.CLASSPATH) {
			return generateClasspath(DOC_TYPE.XLS, templatePath, dataModel);
		} else {
			return null;
		}
	}

	public static InputStream generateXlsx(TYPE type, String templatePath, Map<String, Object> dataModel) throws IOException, TemplateException, InvalidPrincipalException, ParserConfigurationException, SAXException, DocumentException {
		if (type == TYPE.JNDI) {
			return generateJNDI(DOC_TYPE.XLSX, templatePath, dataModel);
		} else if (type == TYPE.CLASSPATH) {
			return generateClasspath(DOC_TYPE.XLSX, templatePath, dataModel);
		} else {
			return null;
		}
	}

	private static InputStream generateJNDI(DOC_TYPE docType, String templatePath, Map<String, Object> dataModel) throws IOException, TemplateException, InvalidPrincipalException, ParserConfigurationException, SAXException, DocumentException {
		StringBuilder sb = new StringBuilder();
		String parts[] = StringUtils.split(templatePath, "/");
		for (int i = parts.length -1 ; i >= 0; i--) {
			String part = StringUtils.contains(parts[i], ".") ? StringUtils.substringBefore(parts[i], ".") : parts[i];
			sb.append("ou=").append(part).append(",");
		}
		sb.append(Config.getString("nicki.templates.basedn"));
		
		String templateDn = sb.toString();
		if (StringUtils.contains(templateDn, ".")) {
			templateDn = StringUtils.substringBefore(templateDn, ".");
		}
		Template template = AppContext.getSystemContext().loadObject(Template.class, templateDn);
		TemplateEngine engine = TemplateEngine.getInstance(TYPE.JNDI);
		if (docType == DOC_TYPE.XLS) {
			return engine.executeTemplateAsXls(template, templatePath + ".ftl", dataModel);
		} else {
			return engine.executeTemplateAsXlsx(template, templatePath + ".ftl", dataModel);
		}
	}
	private static InputStream generateClasspath(DOC_TYPE docType, String templatePath, Map<String, Object> dataModel) throws IOException, TemplateException, InvalidPrincipalException, ParserConfigurationException, SAXException, DocumentException {

		String base = "/META-INF/templates";
		Configuration cfg = ConfigurationFactory.getInstance().getConfiguration(ConfigurationFactory.TYPE.CLASSPATH,
				base);
		TemplateEngine engine = new TemplateEngine(cfg);
				
		EngineTemplate template = getTemplate(base, templatePath + ".ftl");
		if (docType == DOC_TYPE.XLS) {
			return engine.executeTemplateAsXls(template, templatePath + ".ftl", dataModel);
		} else {
			return engine.executeTemplateAsXlsx(template, templatePath + ".ftl", dataModel);
		}
	}
	

	public static EngineTemplate getTemplate(String base, String path) {
		StringBuilder sb = new StringBuilder();
		sb.append(base).append("/").append(path);
		
		String resourcePath = sb.toString();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SimpleTemplate.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			try (Reader reader = new InputStreamReader(XlsDocuHelper.class.getResourceAsStream(resourcePath), "iso-8859-1")) {
				return (SimpleTemplate) jaxbUnmarshaller.unmarshal(reader);
			}
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
