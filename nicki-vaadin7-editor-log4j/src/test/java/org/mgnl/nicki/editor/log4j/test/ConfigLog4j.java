package org.mgnl.nicki.editor.log4j.test;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ConfigLog4j {


    private static final String ROOT = "Root";
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
				changeLogLevel(loggerName, level);
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
    
    
	public static synchronized void changeLogLevel(String loggerName, String level) {
        Logger logger = null;
        Level logLevel = Level.toLevel(level);
        try
        {
            logger = (ROOT.equalsIgnoreCase(loggerName) ? Logger.getRootLogger() : Logger.getLogger(loggerName));
            logger.setLevel(logLevel);
        }
        catch (Throwable e)
        {
            System.out.println("ERROR Setting LOG4J Logger:" + e);
        }

        System.out.println("LogLevel set for " + (logger.getName().equals("") ? ROOT : logger.getName()) + ": " + logLevel);
    }
}
