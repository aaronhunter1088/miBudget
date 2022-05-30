package com.miBudget.logging.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Test {
	
    private static Logger LOGGER = null;
    static {
    	System.setProperty("appName", "miBudget");
    	LOGGER = LogManager.getLogger(Log4j2Test.class);
    }
    
    public static void main(String[] args){
    	// Look at Log level for reasons why any oen log level message is not printing
    	LOGGER.debug("This is a debug message");
    	LOGGER.info("This is an info message");
    	LOGGER.warn("This is a warn message");
    	LOGGER.error("This is an error message");
    	LOGGER.fatal("This is a fatal message");
    	cat();
    }
    
    public static void cat() {
    	LOGGER.info("The method name is 'cat'.");
    }
}