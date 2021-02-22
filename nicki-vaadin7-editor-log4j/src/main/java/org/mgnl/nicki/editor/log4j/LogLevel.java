package org.mgnl.nicki.editor.log4j;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.mgnl.nicki.vaadin.base.menu.application.View;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;

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
		comboBox.setSelectedItem(level);
		comboBox.setTextInputAllowed(false);
		comboBox.addSelectionListener(event -> {
			if (event.getFirstSelectedItem().isPresent()) {
				changeLogLevel(event.getFirstSelectedItem().get());
			}
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
