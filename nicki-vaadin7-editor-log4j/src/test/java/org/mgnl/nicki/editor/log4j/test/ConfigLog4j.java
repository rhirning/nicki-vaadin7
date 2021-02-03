package org.mgnl.nicki.editor.log4j.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.mgnl.nicki.editor.log4j.Log4jViewer;

public class ConfigLog4j {


    private static final String CONFIG_FILE = "/myLog4j.properties";
    
    public static void main(String args[]) {
    	configLogger();
    }
    
    public static void configLogger() {

		Properties log4jConfig = null;
		try {
			log4jConfig = getPropertiesFromClasspath(CONFIG_FILE);
			for (Object entry : log4jConfig.keySet()) {
				String loggerName = (String) entry;
				String level = log4jConfig.getProperty(loggerName);
				changeLogLevel(loggerName, Level.getLevel(level));
			}
		} catch (Exception e) {
			System.out.println("Error reading " + CONFIG_FILE + "): " + e);
		}
    }
	
	public static Properties getPropertiesFromClasspath(String name) throws IOException {
		Properties properties = new Properties() ;
		properties.load(ConfigLog4j.class.getResourceAsStream(name));
		return properties;
	}
	
	private static synchronized void changeLogLevel(String loggerName, Level level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig;
		if (StringUtils.equals(Log4jViewer.ROOT, loggerName)) {
			loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		} else { 
			loggerConfig = config.getLoggerConfig(loggerName);
		}
		loggerConfig.setLevel(level);
		ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.

        System.out.println("LogLevel set for " + loggerName + ": " + level);

    }
}
