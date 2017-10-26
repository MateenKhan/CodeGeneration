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
	public static File createSqlQuerys(JSONObject obj) throws Exception {
		File f = null;
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
			boolean lowerCaseFieldName = obj.optBoolean("lowerCaseFieldName");
			String pk = obj.optString("pk");
			if (StringUtils.isEmpty(pk)) {
				throw new Exception("empty pk received");
			}
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			f = new File(fileLocaiton+"SqlQuerys.java");
			fout = new FileOutputStream(f);
			StringBuilder finalCode = new StringBuilder();
			StringBuilder insertQry = new StringBuilder("");
			StringBuilder insertQryValues = new StringBuilder("");
			StringBuilder updateQry = new StringBuilder("");
			StringBuilder deleteQry = new StringBuilder("");
			StringBuilder selectQry = new StringBuilder("");
			StringBuilder selectAllQry = new StringBuilder("");
			StringBuilder tableName = new StringBuilder("");
			if (lowerCaseFieldName) {
				pk = pk.toLowerCase();
			}
			tableName.append(name.toLowerCase());
			StringBuilder deleteByIdsQry = new StringBuilder("");
			deleteByIdsQry.append("DELETE FROM " + tableName +" WHERE `id` IN (");
			insertQry.append("INSERT INTO " + tableName + " ( ");
			updateQry.append("UPDATE " + tableName + " SET ");
			deleteQry.append("DELETE FROM " + tableName );
			selectQry.append("SELECT ");
			selectAllQry.append("SELECT ");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (lowerCaseFieldName) {
					fieldName = fieldName.toLowerCase();
				}
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				insertQry.append("`" + fieldName + "`, ");
				selectQry.append("`" + fieldName + "`, ");
				selectAllQry.append("`" + fieldName + "`, ");
				insertQryValues.append("?, ");
				if (!fieldName.equals(pk)) {
					if(!fieldName.equals("created_by") && !fieldName.equals("created_at")){
						updateQry.append("`" + fieldName + "` = ?, ");
					}
				}
			}
			insertQry = new StringBuilder(insertQry.substring(0, insertQry.length() - 2));
			insertQry.append(" ) VALUES( " + insertQryValues);
			insertQry = new StringBuilder(insertQry.substring(0, insertQry.length() - 2));
			insertQry.append(" );");
			updateQry = new StringBuilder(updateQry.substring(0, updateQry.length() - 2));
			updateQry.append(" WHERE `" + pk + "` = ?;");
			deleteQry.append(" WHERE `" + pk + "` = ?;");
			selectQry = new StringBuilder(selectQry.substring(0, selectQry.length() - 2));
			selectAllQry = new StringBuilder(selectAllQry.substring(0, selectAllQry.length() - 2));
			selectQry.append(" FROM `" + tableName + "` WHERE `" + pk + "` = ?;");
			selectAllQry.append(" FROM " + tableName + " where created_by = ? and company_id = ?;");
			String className = "SqlQuerys";
			finalCode.append("\n\npublic class ").append(className).append("{\n\n")
			.append("\tpublic final class ").append(name).append("{\n\n")
			.append("\t\tpublic static final String INSERT_QRY = \"").append(insertQry).append("\";\n")
			.append("\t\tpublic static final String UPDATE_QRY = \"").append(updateQry).append("\";\n")
			.append("\t\tpublic static final String DELETE_QRY = \"").append(deleteQry).append("\";\n")
			.append("\t\tpublic static final String DELETE_BY_IDS_QRY = \"").append(deleteByIdsQry).append("\";\n")
			.append("\t\tpublic static final String GET_QRY = \"").append(selectQry).append("\";\n")
			.append("\t\tpublic static final String GET_ALL_QRY = \"").append(selectAllQry).append("\";\n")
			.append("\t}\n}");
			fout.write((finalCode.toString()).getBytes());
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
		return f;
	}

}

