
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


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClickSaveListener implements ClickListener {
	private static final long serialVersionUID = 8056214403424949582L;

    private static final String ROOT = "Root";
    
    Log4jViewer viewer;
	String loggerName;
	ComboBox comboBox;

	public ClickSaveListener(Log4jViewer viewer, String loggerName, ComboBox comboBox) {
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
        Logger logger = null;

        try
        {
		logger = (ROOT.equalsIgnoreCase(loggerName) ? LogManager.getRootLogger() : LogManager.getLogger(loggerName));
		Configurator.setLevel(logger.getName(), Level.toLevel(level));
        }
        catch (Throwable e)
        {
            log.debug("ERROR Setting LOG4J Logger:" + e);
        }

        Notification.show("LogLevel set for " + (logger.getName().equals("") ? ROOT : logger.getName()));
    }

}
