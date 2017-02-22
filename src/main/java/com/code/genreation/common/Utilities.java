package com.code.genreation.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Utilities {

	private static Logger LOGGER = Logger.getLogger(Utilities.class);

	/**
	 * method used to find out if the passed json has any fields
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isValidJson(JSONObject obj) {
		LOGGER.debug("entered isValidJson:" + obj);
		try {
			if (obj != null && obj.length() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		LOGGER.debug("exited isValidJson:" + obj);
		return false;
	}

	/**
	 * method used to get json object from the passed string
	 * 
	 * @param str
	 * @return
	 */
	public static JSONObject getJsonFromString(String str) {
		LOGGER.debug("entered getJsonFromString:" + str);
		try {
			if (!StringUtils.isEmpty(str)) {
				JSONObject obj = new JSONObject(str);
				return obj;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		LOGGER.debug("exited getJsonFromString:" + str);
		return null;
	}

	/**
	 * method used to close FileOutputStream
	 * 
	 * @param fout
	 */
	public static void closeStream(FileOutputStream fout) {
		try {
			if (fout != null) {
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

	/**
	 * method used to lower the case of first character
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String lowerFirstLetter(String str) throws Exception {
		try {
			if (StringUtils.isBlank(str)) {
				return null;
			}
			return (str.charAt(0) + "").toLowerCase() + str.substring(1);
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}

	}
	
	/**
	 * method used to lower the case of first character
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String capitalizeFirstLetter(String str) throws Exception {
		try {
			if (StringUtils.isBlank(str)) {
				return null;
			}
			return (str.charAt(0) + "").toUpperCase() + str.substring(1);
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}

	}

	/**
	 * method used to get method String value which is prepended in pojo member
	 * name
	 * 
	 * @param type
	 * @return
	 */
	public static String getTypeString(String type, HashSet<String> imports) throws Exception {
		try {
			if (StringUtils.isEmpty(type)) {
				throw new Exception("empty field type received:" + type);
			}
			switch (type) {
			case "java.lang.String":
			case "String":
				return "String";
			case "boolean":
				return "boolean";
			case "int":
				return "int";
			case "char":
				return "char";
			case "float":
				return "float";
			case "double":
				return "double";
			case "long":
				return "long";
			case "short":
				return "short";
			case "byte":
				return "byte";
			default:
				break;
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
		return null;
	}

	public static Response constructResponse(String message, int statusHeader) {
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("message", message);
		return Response.status(statusHeader).entity(responseJSON.toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	public static String getJavaTypeFromSql(int sqlType) throws Exception {
		try {
			switch (sqlType) {
			case -7:
			case 16:
				return "boolean";
			case -6:
				return "byte";
			case 5:
				return "short";
			case 4:
				return "int";
			case -5:
				return "long";
			case 6:
			case 8:
			case 3:
				return "double";
			case 7:
				return "float";
			case 2:
				return "java.math.BigDecimal";
			case 1:
			case 12:
			case -1:
				return "String";
			case 91:
				return "java.sql.Date";
			case 92:
				return "java.sql.Time";
			case 93:
				return "java.sql.Timestamp";
			case -2:
			case -3:
			case -4:
				return "byte[]";
			case 0:
				return "NULL";
			case 1111:
				return "OTHER";
			case 2000:
				return "JAVA_OBJECT";
			case 2001:
				return "DISTINCT";
			case 2002:
				return "STRUCT";
			case 2003:
				return "ARRAY";
			case 2004:
				return "2004";
			case 2005:
				return "CLOB";
			case 2006:
				return "REF";
			case 2007:
				return "DATALINK";
			case -8:
				return "ROWID";
			case -15:
				return "NCHAR";
			case -9:
				return "NVARCHAR";
			case -16:
				return "LONGNVARCHAR";
			case 2011:
				return "NCLOB";
			case 2009:
				return "SQLXML";
			case 2012:
				return "REF_CURSOR";
			case 2013:
				return "TIME_WITH_TIMEZONE";
			case 2014:
				return "TIMESTAMP_WITH_TIMEZONE";
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
		return null;
	}

	public static void deleteFilesAsync(List<File> files) {
		try {
			if (files != null && !files.isEmpty()) {
				new Thread() {
					@Override
					public void run() {
						Iterator<File> filesItr = files.iterator();
						while (filesItr.hasNext()) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								LOGGER.error(e);
							}
							filesItr.next().delete();
						}
					}
				}.start();
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	
	public static void closeZipOutputStream(ZipOutputStream zipOutputStream) {
		try {
			if (zipOutputStream != null) {
				zipOutputStream.close();
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	
	public static void closeBufferedInputStream(BufferedInputStream bufferedInputStream) {
		try {
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
}
