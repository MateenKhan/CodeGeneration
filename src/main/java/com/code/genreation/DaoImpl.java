package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class DaoImpl {

	private static final Logger LOGGER = Logger.getLogger(DaoImpl.class);
	private static final String IMPORTS = "\n\nimport java.sql.Connection;\nimport java.sql.PreparedStatement;\nimport java.sql.ResultSet;\nimport java.util.List;\nimport javax.ws.rs.WebApplicationException;\nimport org.apache.log4j.Logger;\nimport java.util.ArrayList;\n\n";
	
	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"pk\":\"id\",\"pkType\":\"String\",\"lowerCaseName\":\"true\",\"lowerCaseFieldName\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createDaoImpl(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File createDaoImpl(JSONObject obj) throws Exception {
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
			String pkType = obj.optString("pkType");
			if (StringUtils.isEmpty(pkType)) {
				throw new Exception("empty pkType received");
			}
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			f = new File(fileLocaiton+name + "DAOImpl.java");
			fout = new FileOutputStream(f);
			if (lowerCaseFieldName) {
				pk = pk.toLowerCase();
			}
			StringBuilder finalCode = new StringBuilder();
			String className = name + "DAOImpl";
			StringBuilder getMethodStr = getMethodImplementation(pkType, pk,name, fields);
			StringBuilder getAllMethodStr = getAllMethodImplementation(name,fields);
			StringBuilder deleteMethodStr = deleteMethodImplementation(pkType, pk, name);
			StringBuilder deleteMethodByIdsStr = deleteByIdsMethodImplementation(pkType, pk, name);
			StringBuilder createMethodStr = createMethodImplementation(name, fields);
			StringBuilder updateMethodStr = updateMethodImplementation(pkType, pk, name, fields);

			String instanceVariableName = Utilities.getCamelCase(className);
			finalCode.append(IMPORTS)
			.append("public class " + className + " implements " + name + "DAO {\n\n")
			.append("\tprivate static Logger LOGGER = Logger.getLogger(" + className + ".class);\n\n")
			.append("\tprivate " + className + "() {\n\t}\n\n")
			.append("\tprivate static final " + className + " " + instanceVariableName + " = new " + className + "();\n\n")
			.append("\tpublic static " + className + " get" + className + "() {\n\t\t return " + instanceVariableName + ";\n\t}\n\n")
			.append(getMethodStr).append(getAllMethodStr).append(deleteMethodStr)
			.append(deleteMethodByIdsStr).append(createMethodStr).append(updateMethodStr)
			.append("\n\n}");
			fout.write((finalCode.toString()).getBytes());
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
		return f;
	}

	private static StringBuilder getMethodImplementation(String pkType, String pk,String name, JSONArray fields) throws Exception {
		try {
			StringBuilder getMethodTemp = new StringBuilder("\t@Override\n\tpublic")
			.append(Dao.getMethodStr(name));
			StringBuilder getMethodStr = new StringBuilder();
			getMethodStr.append(getMethodTemp.substring(0, getMethodTemp.length() - 3));
			getMethodStr.append("{\n")
			.append(enterLog(name, "get"))
			.append(variableNullCheck(name))
			.append("\t\tPreparedStatement pstmt = null;\n\t\tResultSet rset = null;\n")
			.append("\t\ttry {\n\t\t\tif (conn != null) {\n")
			.append("\t\t\t\t" +"pstmt = conn.prepareStatement(SqlQuerys."+name+".GET_QRY);\n")
			.append("\t\t\t\t" +"pstmt.set"+ (Utilities.getCamelCase(Utilities.getTypeString(pkType, null))) +"(1, "+name.toLowerCase()+".get"+ Utilities.getCamelCase(pk)+"());\n")
			.append("\t\t\t\trset = pstmt.executeQuery();\n")
			.append("\t\t\t\twhile (rset.next()) {\n");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				getMethodStr.append("\t\t\t\t\t"+name.toLowerCase()+".set"+ Utilities.getCamelCase(fieldName) + "(rset.get" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(\""+fieldName+"\"));\n");
			}
			getMethodStr.append("\t\t\t\t}\n\t\t\t}")
			.append("\n\t\t}catch (Exception e) {\n\t\t\tLOGGER.error(\"Error retrieving " + name.toLowerCase() + ":\",e);"
					+ "\n\t\t\tthrow new WebApplicationException(e.getMessage(),Constants.EXPECTATION_FAILED);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeResultSet(rset);\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n")
			.append("\t\t}\n")
			.append(exitedLog(name, "getAll"))
			.append("\t\treturn "+name.toLowerCase()+";\n\t}\n\n");
			return getMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder getAllMethodImplementation(String name,JSONArray fields) throws Exception {
		try {
			StringBuilder getAllMethodTempStr = new StringBuilder("\t@Override\n\tpublic")
			.append(Dao.getAllMethodStr(name));
			StringBuilder getAllMethodStr = new StringBuilder(getAllMethodTempStr.substring(0, getAllMethodTempStr.length() - 3))
			.append("{\n")
			.append(enterLog("", "getAll"))
			.append("\t\tList<"+name+"> result = null;\n")
			.append("\t\tPreparedStatement pstmt = null;\n\t\tResultSet rset = null;\n")
			.append("\t\ttry {\n\t\t\tif (conn != null) {\n")
			.append("\t\t\t\tresult = new ArrayList<"+name+">();\n")
			.append("\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys."+name+".GET_ALL_QRY);\n")
			.append("\t\t\t\tpstmt.setString(1, input.getCreated_by());\n")
			.append("\t\t\t\tpstmt.setString(2, input.getCompany_id());\n")
			.append("\t\t\t\trset = pstmt.executeQuery();\n")
			.append("\t\t\t\twhile (rset.next()) {\n")
			.append("\t\t\t\t\t"+name+" "+name.toLowerCase()+"= new "+name+"();\n");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				getAllMethodStr.append( "\t\t\t\t\t"+name.toLowerCase()+".set"+ Utilities.getCamelCase(fieldName) + "(rset.get" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(\""+fieldName+"\"));\n");
			}
			getAllMethodStr.append("\t\t\t\t\tresult.add("+name.toLowerCase()+");\n")
			.append("\t\t\t\t}\n\t\t\t}")
			.append("\n\t\t}catch (Exception e) {\n\t\t\tLOGGER.error(\"Error retrieving all " + name.toLowerCase() +"\", e);"
					+ "\n\t\t\tthrow new WebApplicationException(e.getMessage(),Constants.EXPECTATION_FAILED);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeResultSet(rset);\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n")
			.append("\t\t}\n")
			.append(exitedLog("", "getAll"))
			.append("\t\treturn result;\n\t}\n\n");
			return getAllMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder deleteMethodImplementation(String pkType, String pk,String name) throws Exception {
		try {
			StringBuilder deleteMethodTempStr = new StringBuilder("\t@Override\n\tpublic")
			.append(Dao.deleteMethodStr(name));
			StringBuilder deleteMethodStr = new StringBuilder(deleteMethodTempStr.substring(0, deleteMethodTempStr.length() - 3))
			.append( "{\n")
			.append( enterLog(name, "delete"))
			.append( variableNullCheck(name))
			.append( "\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n")
			.append( "\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys." + name + ".DELETE_QRY);\n")
			.append( "\t\t\t\t" +"pstmt.set"+ (Utilities.getCamelCase(Utilities.getTypeString(pkType, null))) +"(1, "+name.toLowerCase()+".get"+ Utilities.getCamelCase(pk)+"());\n")
			.append( "\t\t\t\tint rowCount = pstmt.executeUpdate();\n")
			.append( "\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(\"no record deleted\", Constants.EXPECTATION_FAILED);\n\t\t\t\t}")
			.append( "\n\t\t\t}")
			.append( "\n\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(\"Error deleting " + name.toLowerCase() + ":\", e);"
					+ "\n\t\t\tthrow new WebApplicationException(e.getMessage(),Constants.EXPECTATION_FAILED);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}\n")
			.append( exitedLog(name, "delete"))
			.append( "\t\treturn " + name.toLowerCase() + ";\n\t}\n\n");
			return deleteMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
	
	private static StringBuilder deleteByIdsMethodImplementation(String pkType, String pk,String name) throws Exception {
		try {
			StringBuilder deleteMethodTempStr = new StringBuilder("\t@Override\n\tpublic")
			.append(Dao.deleteByIdsMethodStr(name));
			StringBuilder deleteMethodStr = new StringBuilder(deleteMethodTempStr.substring(0, deleteMethodTempStr.length() - 3))
			.append("{\n")
			.append(enterLogWithData(name, "delete", "commaSeparatedIds"))
			.append(variableEmptyCheckForStringThrowException("commaSeparatedIds"))
			.append("\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n")
			.append("\t\t\t\tString query = SqlQuerys."+name+".DELETE_BY_IDS_QRY;\n")
			.append("\t\t\t\tquery+=commaSeparatedIds+\");\";\n")
			.append("\t\t\t\tpstmt = conn.prepareStatement(query);\n")
			.append("\t\t\t\tint rowCount = pstmt.executeUpdate();\n")
			.append("\t\t\t\tif (rowCount > 0) {\n")
			.append("\t\t\t\t\treturn true;\n")
			.append("\t\t\t}\n")
			.append("\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(\"no record deleted\", Constants.EXPECTATION_FAILED);\n\t\t\t\t}")
			.append("\n\t\t\t}")
			.append("\n\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(\"Error deleting " + name.toLowerCase() + ":\", e);"
					+ "\n\t\t\tthrow new WebApplicationException(e.getMessage(),Constants.EXPECTATION_FAILED);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}\n")
			.append(exitedLogWithData(name, "delete", "commaSeparatedIds"))
			.append("\t\treturn false;\n\t}\n\n");
			return deleteMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}	

	private static StringBuilder createMethodImplementation(String name, JSONArray fields) throws Exception {
		try {
			StringBuilder insertMethodTempStr = new StringBuilder("\t@Override\n\tpublic")
			.append(Dao.createMethodStr(name));
			StringBuilder insertMethodStr = new StringBuilder(insertMethodTempStr.substring(0, insertMethodTempStr.length() - 3))
			.append("{\n")
			.append(enterLog(name, "create"))
			.append(variableNullCheck(name))
			.append("\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n\t\t\t\tint ctr = 1;\n")
			.append("\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys." + name + ".INSERT_QRY);\n");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				insertMethodStr.append("\t\t\t\tpstmt.set" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(ctr++, " + name.toLowerCase() + ".get"
						+ Utilities.getCamelCase(fieldName) + "());\n");
			}
			insertMethodStr.append("\t\t\t\tint rowCount = pstmt.executeUpdate();\n")
			.append("\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(\"no record inserted\", Constants.EXPECTATION_FAILED);\n\t\t\t\t}")
			.append("\n\t\t\t}")
			.append("\n\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(\"Error inserting " + name.toLowerCase() + ":\",e);"
					+ "\n\t\t\tthrow new WebApplicationException(e.getMessage(),Constants.EXPECTATION_FAILED);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}\n")
			.append(exitedLog(name, "create"))
			.append("\t\treturn " + name.toLowerCase() + ";\n\t}\n");
			return insertMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder updateMethodImplementation(String pkType, String pk, String name, JSONArray fields) throws Exception {
		try {
			StringBuilder updateMethodTempStr = new StringBuilder("\t@Override\n\tpublic")
					.append(Dao.updateMethodStr(name));
			StringBuilder updateMethodStr = new StringBuilder(updateMethodTempStr.substring(0, updateMethodTempStr.length() - 3))
			.append("{\n")
			.append(enterLog(name, "update"))
			.append(variableNullCheck(name))
			.append("\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n\t\t\t\tint ctr = 1;\n")
			.append("\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys." + name + ".UPDATE_QRY);\n");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				if(fieldName.equals("created_by") || fieldName.equals("created_at")){
					continue;
				}
				String fieldType = fieldObj.optString("type");
				if (!fieldName.equals(pk)) {
					updateMethodStr.append("\t\t\t\tpstmt.set" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(ctr++, " + name.toLowerCase() + ".get"
							+ Utilities.getCamelCase(fieldName) + "());\n");
				}
			}
			updateMethodStr.append("\t\t\t\tpstmt.set" + (Utilities.getCamelCase(Utilities.getTypeString(pkType, null))) + "(ctr++, " + name.toLowerCase() + ".get"
					+ Utilities.getCamelCase(pk) + "());\n")
			.append("\t\t\t\tint rowCount = pstmt.executeUpdate();\n")
			.append("\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(\"no record updated\", Constants.EXPECTATION_FAILED);\n\t\t\t\t}")
			.append("\n\t\t\t}")
			.append("\n\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(\"Error updating " + name.toLowerCase() + ":\", e);"
					+ "\n\t\t\tthrow new WebApplicationException(e.getMessage(),Constants.EXPECTATION_FAILED);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}\n")
			.append(exitedLog(name, "update"))
			.append("\t\treturn " + name.toLowerCase() + ";\n\t}\n");
			return updateMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String variableNullCheck(String name) {
		try {
			if (StringUtils.isBlank(name)) {
				return null;
			}
			String result = "";
			result += "\t\tif(" + (name.toLowerCase()) + " == null){\n\t\t\treturn null;\n\t\t}\n";
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
	
//	private static String variableNullCheckThrowException(String name) {
//		try {
//			if (StringUtils.isBlank(name)) {
//				return null;
//			}
//			String result = "";
//			result += "\t\tif(" + (name.toLowerCase()) + " == null){\n\t\t\tthrow new WebApplicationException(\"Invalid input\", Constants.INVALID_INPUT);\n\t\t}\n";
//			return result;
//		} catch (Exception e) {
//			LOGGER.error(e);
//			throw e;
//		}
//	}
//	
	private static String variableEmptyCheckForStringThrowException(String str) {
		try {
			if (StringUtils.isBlank(str)) {
				return null;
			}
			StringBuilder result = new StringBuilder();
			result.append("\t\tif(StringUtils.isBlank("+str+")){");
			result.append( "\n\t\t\tthrow new WebApplicationException(\"Invalid input\", Constants.INVALID_INPUT);\n\t\t}\n");
			return result.toString();
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String enterLog(String name, String method) {
		try {
			if (StringUtils.isBlank(name)) {
				return "\t\tLOGGER.debug(\"entered " + method + "\");\n";
			}
			return "\t\tLOGGER.debug(\"entered " + method + ":\"+" + name.toLowerCase() + ");\n";
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
	
	private static String enterLogWithData(String name, String method, String data) {
		try {
			if (StringUtils.isBlank(name)) {
				return "\t\tLOGGER.debug(\"entered " + method + "\");\n";
			}
			return "\t\tLOGGER.debug(\"entered " + method + ":\"+" + data + ");\n";
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String exitedLog(String name, String method) {
		try {
			if (StringUtils.isBlank(name)) {
				return "\t\tLOGGER.debug(\"exited " + method +"\");\n";
			}
			return "\t\tLOGGER.debug(\"exited " + method + ":\"+" + name.toLowerCase() + ");\n";
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
	
	private static String exitedLogWithData(String name, String method, String data) {
		try {
			if (StringUtils.isBlank(name)) {
				return "\t\tLOGGER.debug(\"exited " + method +"\");\n";
			}
			return "\t\tLOGGER.debug(\"exited " + method + ":\"+" + data + ");\n";
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}
}
