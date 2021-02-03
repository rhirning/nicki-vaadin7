
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


import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.View;


import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.renderers.ComponentRenderer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log4jViewer extends CustomComponent implements View {
	private static final long serialVersionUID = 6677098857979852467L;
	private Panel canvas;
	private Grid<LogLevel> table;
	private NickiApplication application;
	private boolean isInit;

    /**
     * The root appender.
     */
    public static final String ROOT = "Root";

    
	public Log4jViewer() {
	}

    
	public Log4jViewer(NickiApplication application) {
		this.application = application;
	}

	public String getI18nBase() {
		return this.application.getI18nBase();
	}
	

    private void refreshTable() {
    	log.debug("fill Table()");
    	SortedSet<LogLevel> list = new TreeSet<LogLevel>();
    	LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
    	list.add(new LogLevel(this, ""));
    	for (Logger logger : logContext.getLoggers()) {
    		list.add(new LogLevel(this, logger.getName()));
    	}
    	
        table.setItems(list);
	}
	

    public void fillTable() {
    	refreshTable();
	}

	@Override
	public void init() {
		if (!isInit) {
			canvas = new Panel();
			canvas.setSizeFull();
			table = new Grid<>();
			table.setSizeFull();
			canvas.setContent(table);
			table.addColumn(LogLevel::getName).setCaption("Name");
			table.addColumn(LogLevel::getComboBox, new ComponentRenderer()).setCaption("Loglevel");
			setCompositionRoot(canvas);
			setSizeFull();
			isInit = true;
		}
		refreshTable();
		
	}

	@Override
	public boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setApplication(NickiApplication application) {
		this.application = application;
	}

}
