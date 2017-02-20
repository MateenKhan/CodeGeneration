package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class SqlQuerys {
	public static final Logger LOGGER = Logger.getLogger(SqlQuerys.class);

	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"pk\":\"id\",\"lowerCaseName\":\"true\",\"lowerCaseFieldName\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createSqlQuerys(obj);
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void createSqlQuerys(JSONObject obj) throws Exception {
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
			boolean lowerCaseName = obj.optBoolean("lowerCaseName");
			boolean lowerCaseFieldName = obj.optBoolean("lowerCaseFieldName");
			String pk = obj.optString("pk");
			if (StringUtils.isEmpty(pk)) {
				throw new Exception("empty pk received");
			}
			File f = new File(location  + "SqlQuerys.java");
			f.createNewFile();
			// if(!f.exists()){
			// f.createNewFile();
			// }else{
			// throw new Exception("file at location exists:"+location);
			// }
			fout = new FileOutputStream(f);
			String insertQry = "";
			String insertQryValues = "";
			String updateQry = "";
			String deleteQry = "";
			String selectQry = "";
			String selectAllQry = "";
			String tableName;
			if (lowerCaseFieldName) {
				pk = pk.toLowerCase();
			}
			if (lowerCaseName) {
				tableName = name.toLowerCase();
			} else {
				tableName = name;
			}
			insertQry += "INSERT INTO " + tableName + " VALUES( ";
			updateQry += "UPDATE " + tableName + " SET ";
			deleteQry += "DELETE FROM " + tableName + " WHERE ";
			selectQry += "SELECT ";
			selectAllQry += "SELECT * FROM " + tableName + ";";
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (lowerCaseFieldName) {
					fieldName = fieldName.toLowerCase();
				}
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				insertQry += "`" + fieldName + "`, ";
				selectQry += "`" + fieldName + "`, ";
				insertQryValues += "?, ";
				if (!fieldName.equals(pk)) {
					updateQry += "`" + fieldName + "` = ?, ";
				}
			}
			insertQry = insertQry.substring(0, insertQry.length() - 2);
			insertQry += " ) VALUES( " + insertQryValues;
			insertQry = insertQry.substring(0, insertQry.length() - 2);
			insertQry += " );";
			updateQry = updateQry.substring(0, updateQry.length() - 2);
			updateQry += " WHERE `" + pk + "` = ?;";
			deleteQry += " WHERE `" + pk + "` = ?;";
			selectQry = selectQry.substring(0, selectQry.length() - 2);
			selectQry += " from `" + tableName + "` WHERE `" + pk + "` = ?;";
			String className = "SqlQuerys";
			fout.write(("public class " + className + "{\n\n").getBytes());
			fout.write(("\tpublic final class " + name + "{\n\n").getBytes());
			fout.write(("\t\tpublic static final String INSERT_QRY = \"" + insertQry + "\";\n\n").getBytes());
			fout.write(("\t\tpublic static final String UPDATE_QRY = \"" + updateQry + "\";\n\n").getBytes());
			fout.write(("\t\tpublic static final String DELETE_QRY = \"" + deleteQry + "\";\n\n").getBytes());
			fout.write(("\t\tpublic static final String GET_QRY = \"" + selectQry + "\";\n\n").getBytes());
			fout.write(("\t\tpublic static final String GET_ALL_QRY = \"" + selectAllQry + "\";\n\n").getBytes());
			fout.write(("\t}\n}").getBytes());
			
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
	}

}

