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

	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createDao(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDao(JSONObject obj) throws Exception {
		FileOutputStream fout = null;
		try {
			String location = obj.optString("location");
			if (StringUtils.isEmpty(location)) {
				throw new Exception("empty location received");
			}
			JSONArray fields = obj.optJSONArray("fields");
			if (null == fields || fields.length() == 0) {
				throw new Exception("empty fields received");
			}
			String name = obj.optString("name");
			if (StringUtils.isEmpty(name)) {
				throw new Exception("empty name received");
			}
			File f = new File(location + name + "DAO.java");
			f.createNewFile();
			// if(!f.exists()){
			// f.createNewFile();
			// }else{
			// throw new Exception("file at location exists:"+location);
			// }
			fout = new FileOutputStream(f);
			String methodStr = getMethods(name);
			String importStr = getImports();
			fout.write(importStr.getBytes());
			fout.write(("public interface " + name + "DAO {\n\n").getBytes());
			fout.write((methodStr + "\n").getBytes());
			fout.write("}".getBytes());
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
	}

	public static String getImports() throws Exception {
		try {
			String importStr = "";
			importStr = "import java.sql.Connection;\nimport java.sql.PreparedStatement;\nimport java.sql.ResultSet;\nimport java.util.List;\n";
			return importStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static String getMethods(String name) throws Exception {
		try {
			String methodStr = "";
			methodStr += getMethodStr(name);
			methodStr += getAllMethodStr(name);
			methodStr += deleteMethodStr(name);
			methodStr += createMethodStr(name);
			methodStr += updateMethodStr(name);
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	/**
	 * used to get Method as String argument must be the pojo name
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String getMethodStr(String name) throws Exception {
		try {
			String methodStr = "";
			methodStr += "\t" + name + " get(Connection conn, " + name + " " + Utilities.lowerFirstLetter(name) + ");\n\n";
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static String getAllMethodStr(String name) throws Exception {
		try {
			String methodStr = "";
			methodStr += "\tList<" + name + "> getAll(Connection conn);\n\n";
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static String deleteMethodStr(String name) throws Exception {
		try {
			String methodStr = "";
			methodStr += "\t" + name + " delete(Connection conn, " + name + " " + Utilities.lowerFirstLetter(name) + ");\n\n";
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static String createMethodStr(String name) throws Exception {
		try {
			String methodStr = "";
			methodStr += "\t" + name + " create(Connection conn, " + name + " " + Utilities.lowerFirstLetter(name) + ");\n\n";
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	public static String updateMethodStr(String name) throws Exception {
		try {
			String methodStr = "";
			methodStr += "\t" + name + " update(Connection conn, " + name + " " + Utilities.lowerFirstLetter(name) + ");\n\n";
			return methodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
}
