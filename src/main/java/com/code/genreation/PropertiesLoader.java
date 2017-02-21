package com.code.genreation;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 */
public class PropertiesLoader {
	
	private static Logger logger = Logger.getLogger(PropertiesLoader.class);
	
	private static PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	public static PropertiesLoader getPropertiesLoader(){
		return propertiesLoader;
	}
	/**
	 * method used to load project.properties file
	 * @return boolean result value loading project.properties file
	 */
	public boolean loadProjectProperties(){
		boolean result=false;
		try {
			InputStream inputStream=this.getClass().getResourceAsStream("/project.properties");
			logger.debug("project.properties file loaded");
			PropertyManager.getPropertyManager().getProperties().load(inputStream);
			loadProjectEnvironmentMode();
			result=true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error loading project.properties file");
			logger.error(e);
			System.exit(0);
		}
		return result;
	}

	private void loadProjectEnvironmentMode(){
		try {
			InputStream inputStream=this.getClass().getResourceAsStream("/project.properties");
			PropertyManager.getPropertyManager().getProperties().load(inputStream);
		} catch (Exception e) {
			logger.error(e);
			System.exit(2);
		}
	}
}
