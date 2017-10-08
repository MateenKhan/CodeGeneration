package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class Dao {

	private static final Logger LOGGER = Logger.getLogger(Dao.class);
	private static final String IMPORTS = "\n\nimport java.sql.Connection;\nimport java.sql.PreparedStatement;\nimport java.sql.ResultSet;\nimport java.util.List;\n\n";

	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createDao(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File createDao(JSONObject obj) throws Exception {
		File file = null;
		FileOutputStream fout = null;
		try {
			JSONArray fields = obj.optJSONArray("fields");
			if (null == fields || fields.length() == 0) {
				throw new Exception("empty fields received");
			}
			String name = Utilities.capitalizeFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(name)) {
				throw new Exception("empty name received");
			}
			StringBuilder finalCode = new StringBuilder();
			file = new File(name + "DAO.java");
			fout = new FileOutputStream(file);
			StringBuilder methodStr = getMethods(name);
			finalCode.append(IMPORTS).append("public interface ").append(name).append("DAO {\n\n").append(methodStr + "\n").append("}");
			fout.write((finalCode.toString()).getBytes());
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
		return file;
	}

	public static StringBuilder getMethods(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append(getMethodStr(name)).append(getAllMethodStr(name)).append(deleteMethodStr(name)).append(deleteByIdsMethodStr(name)).append(createMethodStr(name))
					.append(updateMethodStr(name));
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	/**
	 * used to get Method as String argument must be the pojo name
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder getMethodStr(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append("\t").append(name).append(" get(Connection conn, ").append(name).append(" ").append(Utilities.lowerFirstLetter(name)).append(");\n\n");
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static StringBuilder getAllMethodStr(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append("\tList<").append(name).append("> getAll(Connection conn, ").append(name).append(" input);\n\n");
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static StringBuilder deleteMethodStr(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append("\t").append(name).append(" delete(Connection conn, ").append(name).append(" ").append(Utilities.lowerFirstLetter(name)).append(");\n\n");
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static StringBuilder deleteByIdsMethodStr(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append("\tboolean deleteByIds(Connection conn, String commaSeparatedIds);\n\n");
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static StringBuilder createMethodStr(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append("\t").append(name).append(" create(Connection conn, ").append(name).append(" ").append(Utilities.lowerFirstLetter(name)).append(");\n\n");
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static StringBuilder updateMethodStr(String name) throws Exception {
		try {
			StringBuilder methodStr = new StringBuilder();
			methodStr.append("\t").append(name).append(" update(Connection conn, ").append(name).append(" ").append(Utilities.lowerFirstLetter(name)).append(");\n\n");
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
}
