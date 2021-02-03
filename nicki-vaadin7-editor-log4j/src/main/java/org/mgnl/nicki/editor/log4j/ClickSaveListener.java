
package org.mgnl.nicki.editor.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

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


import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;

public class ClickSaveListener implements ClickListener {
	private static final long serialVersionUID = 8056214403424949582L;

    private static final String ROOT = "Root";
    
    Log4jViewer viewer;
	String loggerName;
	ComboBox<String> comboBox;

	public ClickSaveListener(Log4jViewer viewer, String loggerName, ComboBox<String> comboBox) {
		super();
		this.viewer = viewer;
		this.loggerName = loggerName;
		this.comboBox = comboBox;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		changeLogLevel(loggerName, (String) comboBox.getValue());
		viewer.fillTable();
	}

	private synchronized void changeLogLevel(String loggerName, String level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME); 
		loggerConfig.setLevel(Level.toLevel(level));
		ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.

        Notification.show("LogLevel set for " + (loggerName.equals("") ? ROOT : loggerName));
    }

}
