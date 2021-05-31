package org.mgnl.nicki.editor.log4j;

/*-
 * #%L
 * nicki-vaadin7-editor-log4j
 * %%
 * Copyright (C) 2020 - 2021 Ralf Hirning
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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.mgnl.nicki.vaadin.base.menu.application.View;
import org.mgnl.nicki.vaadin.base.notification.Notification;

import com.vaadin.flow.component.combobox.ComboBox;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class LogLevel implements Serializable, Comparable<LogLevel>{
	private View view;
	private String name;
	private Logger logger;;
	private Level level;

	public LogLevel(View view, String name) {
		super();
		this.view = view;
		if (StringUtils.isBlank(name)) {
			this.name = Log4jViewer.ROOT;
		} else {
			this.name = name;
		}
		if (StringUtils.equals(Log4jViewer.ROOT, name)) {
			logger = LogManager.getRootLogger();
		} else {
			logger = LogManager.getLogger(name);
		}
		level = logger.getLevel();
	}
	
	public ComboBox<Level> getComboBox() {
		ComboBox<Level> comboBox = new ComboBox<>();
		comboBox.setItems(Level.values());
		comboBox.setValue(level);
		comboBox.setAllowCustomValue(false);
		comboBox.addValueChangeListener(event -> {
			changeLogLevel(event.getValue());
			view.init();
		});
				
		return comboBox;
	}
	
	private void changeLogLevel(Level level) {
		if (StringUtils.equals(Log4jViewer.ROOT, name)) {
			Configurator.setRootLevel(level);
		} else {
			Configurator.setLevel(name, level);
		}
        Notification.show("LogLevel set to " + level + " for " + name);
	}

	@Override
	public int compareTo(LogLevel o) {
		return name.compareTo(o.name);
	}


}
