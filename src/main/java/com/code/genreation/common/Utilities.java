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
	
	
}
