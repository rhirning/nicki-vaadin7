
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mgnl.nicki.core.i18n.I18n;
import org.mgnl.nicki.vaadin.base.application.NickiApplication;
import org.mgnl.nicki.vaadin.base.menu.application.View;


import com.vaadin.v7.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log4jViewer extends CustomComponent implements View {
	private static final long serialVersionUID = 6677098857979852467L;
	private Panel canvas;
	private Table table;
	private NickiApplication application;
	private boolean isInit;

    /**
     * The root appender.
     */
    private static final String ROOT = "Root";
    /**
     * All the log levels.
     */
    private static final String[] LEVELS = new String[]{
        Level.OFF.toString(),
        Level.FATAL.toString(),
        Level.ERROR.toString(),
        Level.WARN.toString(),
        Level.INFO.toString(),
        Level.DEBUG.toString(),
        Level.ALL.toString() };

    
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
    	table.removeAllItems();
    	addLoggerItem(Logger.getRootLogger());
    	
        for (Logger logger : getLoggers()) {
        	log.debug("Logger added: " + logger.getName());
        	addLoggerItem(logger);
		}
	}
	

    public void fillTable() {
    	refreshTable();
	}


	@SuppressWarnings("unchecked")
	private Item addLoggerItem(Logger logger) {
		String loggerName = (logger.getName().equals("") ? ROOT : logger.getName());
		String inherited = logger.getLevel() == null?"*":"";
		String currentLevel = logger.getEffectiveLevel().toString();
		
		Item item = table.addItem(loggerName);
		item.getItemProperty("title").setValue(loggerName);
		item.getItemProperty("inherited").setValue(inherited);
		
		ComboBox comboBox = new ComboBox();
		for (String level : LEVELS) {
			comboBox.addItem(level);
		}
		comboBox.select(currentLevel);
		item.getItemProperty("comboBox").setValue(comboBox);
		
		Button saveButton = new Button("Save");
		saveButton.addClickListener(new ClickSaveListener(this,loggerName, comboBox));
		item.getItemProperty("saveButton").setValue(saveButton);
		
		return item;
	}


    private List<Logger> getLoggers()
    {
        @SuppressWarnings("unchecked")
		Enumeration<Logger> enm = LogManager.getCurrentLoggers();

        List<Logger> list = new ArrayList<Logger>();

        // Add all current loggers to the list
        while (enm.hasMoreElements())
        {
            list.add(enm.nextElement());
        }
        
        Collections.sort(list, new Comparator<Logger>() {

			@Override
			public int compare(Logger o1, Logger o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

        return list;
    }

	@Override
	public void init() {
		if (!isInit) {
			canvas = new Panel();
			canvas.setSizeFull();
			table = new Table();
			table.setSizeFull();
			canvas.setContent(table);
			table.addContainerProperty("title", String.class, "");
			table.setColumnHeader("title", I18n.getText(getI18nBase() + ".column.title"));
			table.addContainerProperty("inherited", String.class, "");
			table.setColumnHeader("inherited", I18n.getText(getI18nBase() + ".column.inherited"));
			table.addContainerProperty("comboBox", ComboBox.class, null);
			table.setColumnHeader("comboBox", I18n.getText(getI18nBase() + ".column.level"));
			table.addContainerProperty("saveButton", Button.class, null);
			table.setColumnHeader("saveButton", "");
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
