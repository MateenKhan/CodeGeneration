package com.code.genreation.common;

import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Utilities {

	private static Logger LOGGER = Logger.getLogger(Utilities.class);
	
	/**
	 * method used to find out if the passed json has any fields
	 * @param obj
	 * @return
	 */
	public static boolean isValidJson(JSONObject obj){
		LOGGER.debug("entered isValidJson:"+obj);
		try {
			if(obj !=null && obj.length()>0){
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		LOGGER.debug("exited isValidJson:"+obj);
		return false;
	}
	
	/**
	 * method used to get json object from the passed string
	 * @param str
	 * @return
	 */
	public static JSONObject getJsonFromString(String str){
		LOGGER.debug("entered getJsonFromString:"+str);
		try {
			if(!StringUtils.isEmpty(str)){
				JSONObject obj = new JSONObject(str);
				return obj;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		LOGGER.debug("exited getJsonFromString:"+str);
		return null;
	}
	
	/**
	 * method used to close FileOutputStream
	 * @param fout
	 */
	public static void closeStream(FileOutputStream fout){
		try {
			if(fout != null){
				fout.close();
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	/**
	 * method used to convert name to camel case used in setter and getter
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String getCamelCase(String name) throws Exception {
		try {
			if (StringUtils.isEmpty(name)) {
				throw new Exception("empty name received:" + name);
			}
			final StringBuilder result = new StringBuilder(name.length());
			for (final String word : name.split(" ")) {
				if (!StringUtils.isEmpty(word)) {
					result.append(word.substring(0, 1).toUpperCase());
					result.append(word.substring(1).toLowerCase());
				}
				if (!(result.length() == name.length()))
					result.append(" ");
			}
			return result.toString();
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
	
	public static String lowerFirstLetter(String str) throws Exception{
		try {
			if(StringUtils.isBlank(str)){
				return null;
			}
			return (str.charAt(0)+"").toLowerCase()+str.substring(1);
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
		
	}
	
}
