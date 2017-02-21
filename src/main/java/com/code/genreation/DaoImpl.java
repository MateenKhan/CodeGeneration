package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class DaoImpl {

	private static final Logger LOGGER = Logger.getLogger(DaoImpl.class);

	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"pk\":\"id\",\"pkType\":\"String\",\"lowerCaseName\":\"true\",\"lowerCaseFieldName\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createDaoImpl(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDaoImpl(JSONObject obj) throws Exception {
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
			boolean lowerCaseFieldName = obj.optBoolean("lowerCaseFieldName");
			String pk = obj.optString("pk");
			if (StringUtils.isEmpty(pk)) {
				throw new Exception("empty pk received");
			}
			String pkType = obj.optString("pkType");
			if (StringUtils.isEmpty(pkType)) {
				throw new Exception("empty pkType received");
			}
			File f = new File(location + name + "DAOImpl.java");
			f.createNewFile();
			// if(!f.exists()){
			// f.createNewFile();
			// }else{
			// throw new Exception("file at location exists:"+location);
			// }
			fout = new FileOutputStream(f);
			if (lowerCaseFieldName) {
				pk = pk.toLowerCase();
			}
			String className = name + "DAOImpl";
			String importStr = Dao.getImports();
			importStr += "import javax.ws.rs.WebApplicationException;\n";
			importStr += "import org.apache.log4j.Logger;\n\n";
			importStr += "import java.util.ArrayList;\n\n";
			String getMethodStr = getMethodImplementation(pkType, pk,name, fields);
			String getAllMethodStr = getAllMethodImplementation(name,fields);
			String deleteMethodStr = deleteMethodImplementation(pkType, pk, name);
			String createMethodStr = createMethodImplementation(name, fields);
			String updateMethodStr = updateMethodImplementation(pkType, pk, name, fields);

			fout.write((importStr).getBytes());
			fout.write(("public class " + className + " implements " + name + "DAO {\n\n").getBytes());
			fout.write(("\tprivate static Logger LOGGER = Logger.getLogger(" + className + ".class);\n\n").getBytes());
			fout.write(("\tprivate " + className + "() {\n\t}\n\n").getBytes());
			String instanceVariableName = Utilities.getCamelCase(className);
			fout.write(("\tprivate static final " + className + " " + instanceVariableName + " = new " + className + "();\n\n").getBytes());
			fout.write(("\tpublic static " + className + " get" + className + "() {\n\t\t return " + instanceVariableName + ";\n\t}\n\n").getBytes());
			fout.write((getMethodStr + getAllMethodStr + deleteMethodStr + createMethodStr + updateMethodStr).getBytes());

			fout.write("\n\n}".getBytes());
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
	}

	private static String getMethodImplementation(String pkType, String pk,String name, JSONArray fields) throws Exception {
		try {
			String getMethodStr = "\t@Override\n\tpublic";
			getMethodStr += Dao.getMethodStr(name);
			getMethodStr = getMethodStr.substring(0, getMethodStr.length() - 3);
			getMethodStr += "{\n";
			getMethodStr += enterLog(name, "get");
			getMethodStr += variableNullCheck(name);
			getMethodStr += "\t\tPreparedStatement pstmt = null;\n\t\tResultSet rset = null;\n";
			getMethodStr += "\t\ttry {\n\t\t\tif (conn != null) {\n";
			getMethodStr += "\t\t\t\t" +"pstmt = conn.prepareStatement(SqlQuerys."+name+".GET_QRY);\n";
			getMethodStr += "\t\t\t\t" +"pstmt.set"+ (Utilities.getCamelCase(Utilities.getTypeString(pkType, null))) +"(1, "+name.toLowerCase()+".get"+ Utilities.getCamelCase(pk)+"());\n";
			getMethodStr += "\t\t\t\trset = pstmt.executeQuery();\n";
			getMethodStr += "\t\t\t\twhile (rset.next()) {\n";
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				getMethodStr += "\t\t\t\t\t"+name.toLowerCase()+".set"+ Utilities.getCamelCase(fieldName) + "(rset.get" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(\""+fieldName+"\"));\n";
			}
			getMethodStr += "\t\t\t\t}\n\t\t\t}\n\t\t}";
			getMethodStr += "catch(WebApplicationException e) {\n\t\t\tLOGGER.error(\"Error retrieving " + name.toLowerCase() + ":\" + " + name.toLowerCase()
					+ ".getId() + \",  \", e);\n\t\t\tthrow e;\n";
			getMethodStr += "\t\t}catch (Exception e) {\n\t\t\tLOGGER.error(e);\n\t\t\tthrow new WebApplicationException(e);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeResultSet(rset);\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n";
			getMethodStr += "\t\t}\n";
			getMethodStr += exitedLog(name, "getAll");
			getMethodStr += "\t\treturn "+name.toLowerCase()+";\n\t}\n\n";
			
			return getMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String getAllMethodImplementation(String name,JSONArray fields) throws Exception {
		try {
			String getAllMethodStr = "\t@Override\n\tpublic";
			getAllMethodStr += Dao.getAllMethodStr(name);
			getAllMethodStr = getAllMethodStr.substring(0, getAllMethodStr.length() - 3);
			getAllMethodStr += "{\n";
			getAllMethodStr += enterLog("", "getAll");
			getAllMethodStr += "\t\tList<"+name+"> result = null;\n";
			getAllMethodStr += "\t\tPreparedStatement pstmt = null;\n\t\tResultSet rset = null;\n";
			getAllMethodStr += "\t\ttry {\n\t\t\tif (conn != null) {\n";
			getAllMethodStr += "\t\t\t\tresult = new ArrayList<"+name+">();\n";
			getAllMethodStr += "\t\t\t\t" +"pstmt = conn.prepareStatement(SqlQuerys."+name+".GET_ALL_QRY);\n";
			getAllMethodStr += "\t\t\t\trset = pstmt.executeQuery();\n";
			getAllMethodStr += "\t\t\t\twhile (rset.next()) {\n";
			getAllMethodStr += "\t\t\t\t\t"+name+" "+name.toLowerCase()+"= new "+name+"();\n";
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				getAllMethodStr += "\t\t\t\t\t"+name.toLowerCase()+".set"+ Utilities.getCamelCase(fieldName) + "(rset.get" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(\""+fieldName+"\"));\n";
			}
			getAllMethodStr += "\t\t\t\t\tresult.add("+name.toLowerCase()+");\n";
			getAllMethodStr += "\t\t\t\t}\n\t\t\t}\n\t\t}";
			getAllMethodStr += "catch(WebApplicationException e) {\n\t\t\tLOGGER.error(\"Error retrieving all " + name.toLowerCase() +"\"+  e);\n\t\t\tthrow e;\n";
			getAllMethodStr += "\t\t}catch (Exception e) {\n\t\t\tLOGGER.error(e);\n\t\t\tthrow new WebApplicationException(e);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeResultSet(rset);\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n";
			getAllMethodStr += "\t\t}\n";
			getAllMethodStr += exitedLog("", "getAll");
			getAllMethodStr += "\t\treturn result;\n\t}\n\n";
			return getAllMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String deleteMethodImplementation(String pkType, String pk,String name) throws Exception {
		try {
			String deleteMethodStr = "\t@Override\n\tpublic";
			deleteMethodStr += Dao.deleteMethodStr(name);
			deleteMethodStr = deleteMethodStr.substring(0, deleteMethodStr.length() - 3);
			deleteMethodStr += "{\n";
			deleteMethodStr += enterLog(name, "delete");
			deleteMethodStr += variableNullCheck(name);
			deleteMethodStr += "\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n";
			deleteMethodStr += "\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys." + name + ".DELETE_QRY);\n";
			deleteMethodStr += "\t\t\t\t" +"pstmt.set"+ (Utilities.getCamelCase(Utilities.getTypeString(pkType, null))) +"(1, "+name.toLowerCase()+".get"+ Utilities.getCamelCase(pk)+"());\n";
			deleteMethodStr += "\t\t\t\tint rowCount = pstmt.executeUpdate();\n";
			deleteMethodStr += "\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(Utilities.constructResponse(\"no record deleted\", 500));\n\t\t\t\t}";
			deleteMethodStr += "\t\t\t}\n\t\t} catch (WebApplicationException e) {\n\t\t\tLOGGER.error(\"Error deleting " + name.toLowerCase() + ":\" + " + name.toLowerCase()
					+ ".getId() + \",  \", e);\n\t\t\tthrow e;\n";
			deleteMethodStr += "\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(e);\n\t\t\tthrow new WebApplicationException(e);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}";
			deleteMethodStr += exitedLog(name, "delete");
			deleteMethodStr += "\t\treturn " + name.toLowerCase() + ";\n\t}\n\n";
			return deleteMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String createMethodImplementation(String name, JSONArray fields) throws Exception {
		try {
			String insertMethodStr = "\t@Override\n\tpublic";
			insertMethodStr += Dao.createMethodStr(name);
			insertMethodStr = insertMethodStr.substring(0, insertMethodStr.length() - 3);
			insertMethodStr += "{\n";
			insertMethodStr += enterLog(name, "create");
			insertMethodStr += variableNullCheck(name);
			insertMethodStr += "\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n\t\t\t\tint ctr = 1;\n";
			insertMethodStr += "\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys." + name + ".INSERT_QRY);\n";
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				insertMethodStr += "\t\t\t\tpstmt.set" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(ctr++, " + name.toLowerCase() + ".get"
						+ Utilities.getCamelCase(fieldName) + "());\n";
			}
			insertMethodStr += "\t\t\t\tint rowCount = pstmt.executeUpdate();\n";
			insertMethodStr += "\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(Utilities.constructResponse(\"no record inserted\", 500));\n\t\t\t\t}";
			insertMethodStr += "\t\t\t}\n\t\t} catch (WebApplicationException e) {\n\t\t\tLOGGER.error(\"Error inserting " + name.toLowerCase() + ":\" + " + name.toLowerCase()
					+ ".getId() + \",  \", e);\n\t\t\tthrow e;\n";
			insertMethodStr += "\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(e);\n\t\t\tthrow new WebApplicationException(e);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}";
			insertMethodStr += exitedLog(name, "create");
			insertMethodStr += "\t\treturn " + name.toLowerCase() + ";\n\t}\n";
			return insertMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static String updateMethodImplementation(String pkType, String pk, String name, JSONArray fields) throws Exception {
		try {
			String updateMethodStr = "\t@Override\n\tpublic";
			updateMethodStr += Dao.updateMethodStr(name);
			updateMethodStr = updateMethodStr.substring(0, updateMethodStr.length() - 3);
			updateMethodStr += "{\n";
			updateMethodStr += enterLog(name, "update");
			updateMethodStr += variableNullCheck(name);
			updateMethodStr += "\t\tPreparedStatement pstmt = null;\n\t\ttry {\n\t\t\tif (conn != null) {\n\t\t\t\tint ctr = 1;\n";
			updateMethodStr += "\t\t\t\tpstmt = conn.prepareStatement(SqlQuerys." + name + ".UPDATE_QRY);\n";
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				if (!fieldName.equals(pk)) {
					updateMethodStr += "\t\t\t\tpstmt.set" + (Utilities.getCamelCase(Utilities.getTypeString(fieldType, null))) + "(ctr++, " + name.toLowerCase() + ".get"
							+ Utilities.getCamelCase(fieldName) + "());\n";
				}
			}
			updateMethodStr += "\t\t\t\tpstmt.set" + (Utilities.getCamelCase(Utilities.getTypeString(pkType, null))) + "(ctr++, " + name.toLowerCase() + ".get"
					+ Utilities.getCamelCase(pk) + "());\n";
			updateMethodStr += "\t\t\t\tint rowCount = pstmt.executeUpdate();\n";
			updateMethodStr += "\t\t\t\tif (rowCount == 0) {\n\t\t\t\t\tthrow new WebApplicationException(Utilities.constructResponse(\"no record updated\", 500));\n\t\t\t\t}";
			updateMethodStr += "\t\t\t}\n\t\t} catch (WebApplicationException e) {\n\t\t\tLOGGER.error(\"Error updating " + name.toLowerCase() + ":\" + " + name.toLowerCase()
					+ ".getId() + \",  \", e);\n\t\t\tthrow e;\n";
			updateMethodStr += "\t\t} catch (Exception e) {\n\t\t\tLOGGER.error(e);\n\t\t\tthrow new WebApplicationException(e);\n\t\t} finally {\n\t\t\tDatabaseUtilities.closeStatement(pstmt);\n\t\t}\n";
			updateMethodStr += exitedLog(name, "update");
			updateMethodStr += "\t\treturn " + name.toLowerCase() + ";\n\t}\n";
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
}
