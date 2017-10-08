package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class ControllerImpl {

	private static final Logger LOGGER = Logger.getLogger(ControllerImpl.class);
	private static final String IMPORTS = "\n\nimport java.sql.Connection;\nimport java.sql.Date;\nimport java.util.List;\nimport java.util.UUID;\nimport javax.ws.rs.WebApplicationException;\nimport javax.ws.rs.core.Response;\nimport org.apache.log4j.Logger;\nimport org.json.JSONObject;\n\n";	

	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"pk\":\"id\",\"pkType\":\"String\",\"lowerCaseName\":\"true\",\"lowerCaseFieldName\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createControllerImpl(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File createControllerImpl(JSONObject obj) throws Exception {
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
			f = new File(name+"ControllerImpl.java");
			fout = new FileOutputStream(f);
			if (lowerCaseFieldName) {
				pk = pk.toLowerCase();
			}
			StringBuilder finalCode = new StringBuilder();
			String className = new String(name+"ControllerImpl");
			StringBuilder getAllMethodStr = getAllMethodImplementation(name);
			StringBuilder getMethodStr = getMethodImplementation(name);
			StringBuilder createMethodStr = createMethodImplementation(name);
			StringBuilder deleteMethodStr = deleteMethodImplementation(name);
			StringBuilder deleteManyMethodStr = deleteByIdsMethodImplementation(name);
			StringBuilder updateMethodStr = updateMethodImplementation(name);

			finalCode.append(IMPORTS)
			.append("\n@Api(value = \"").append(name).append(" Controller\")")
			.append("\n@Path(\"/users/{userId}/companies/{companyId}/").append(Utilities.lowerFirstLetter(name)).append("\")")
			.append("\npublic class ").append(className).append(" {\n\n")
			.append("\tprivate static Logger LOGGER = Logger.getLogger(").append(className).append(".class);\n\n")
			.append(getMethodStr)
			.append(getAllMethodStr)
			.append(deleteMethodStr)
			.append(deleteManyMethodStr)
			.append(createMethodStr)
			.append(updateMethodStr)
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

	private static StringBuilder getMethodImplementation(String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\tpublic static "+name+" get").append(name).append("(String userId, String companyId, String id) {")
			.append("\n\t\tConnection conn = null;")
			.append("\n\t\ttry {")
			.append("\n\t\t\tconn = DatabaseUtilities.getReadConnection();")
			.append("\n\t\t\t").append(Utilities.createObject(name))
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setId(id);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(" = MySQLManager.get").append(name).append("DAO().get(conn, ").append(Utilities.lowerFirstLetter(name)).append(");")
			.append("\n\t\t\treturn "+Utilities.lowerFirstLetter(name)+";")
			.append("\n\t\t} catch (WebApplicationException e) {")
			.append("\n\t\t\tLOGGER.error(\"get").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), e.getResponse().getStatus()));")
			.append("\n\t\t} catch (Exception e) {")
			.append("\n\t\t\tLOGGER.error(\"get").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), Constants.EXPECTATION_FAILED));")
			.append("\n\t\t}finally {")
			.append("\n\t\t\tDatabaseUtilities.closeConnection(conn);")
			.append("\n\t\t}\n\t}\n");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder getAllMethodImplementation(String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\tpublic static List<"+name+"> get")
			.append(name).append("s(String userId, String companyId) {")
			.append("\n\t\tConnection conn = null;")
			.append("\n\t\ttry {")
			.append("\n\t\t\tconn = DatabaseUtilities.getReadConnection();")
			.append("\n\t\t\t").append(Utilities.createObject(name))
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setCreated_by(userId);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setCompany_id(companyId);")
			.append("\n\t\t\tList<").append(name).append("> ").append(Utilities.lowerFirstLetter(name)).append("s =  MySQLManager.get").append(name).append("DAO().getAll(conn,"+Utilities.lowerFirstLetter(name)+");")
			.append("\n\t\t\treturn "+Utilities.lowerFirstLetter(name)+"s;")
			.append("\n\t\t} catch (WebApplicationException e) {")
			.append("\n\t\t\tLOGGER.error(\"get").append(name).append("s\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), e.getResponse().getStatus()));")
			.append("\n\t\t} catch (Exception e) {")
			.append("\n\t\t\tLOGGER.error(\"get").append(name).append("s\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), Constants.EXPECTATION_FAILED));")
			.append("\n\t\t}finally {")
			.append("\n\t\t\tDatabaseUtilities.closeConnection(conn);")
			.append("\n\t\t}\n\t}\n");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder deleteMethodImplementation(String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\tpublic static Response delete").append(name).append("(String userId, String companyId, String id) {")
			.append("\n\t\tConnection conn = null;")
			.append("\n\t\ttry {")
			.append("\n\t\t\tconn = DatabaseUtilities.getReadWriteConnection();")
			.append("\n\t\t\t").append(Utilities.createObject(name))
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setId(id);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(" = MySQLManager.get").append(name).append("DAO().delete(conn, ").append(Utilities.lowerFirstLetter(name)).append(");")
			.append("\n\t\t\treturn Response.ok(").append(Utilities.lowerFirstLetter(name)).append(").build();")
			.append("\n\t\t} catch (WebApplicationException e) {")
			.append("\n\t\t\tLOGGER.error(\"delete").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), e.getResponse().getStatus()));")
			.append("\n\t\t} catch (Exception e) {")
			.append("\n\t\t\tLOGGER.error(\"delete").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), Constants.EXPECTATION_FAILED));")
			.append("\n\t\t}finally {")
			.append("\n\t\t\tDatabaseUtilities.closeConnection(conn);")
			.append("\n\t\t}\n\t}\n");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder deleteByIdsMethodImplementation(String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\tpublic static Response delete").append(name).append("s(String userId, String companyId, List<String> ids) {")
			.append("\n\t\tConnection conn = null;")
			.append("\n\t\ttry {")
			.append("\n\t\t\tconn = DatabaseUtilities.getReadWriteConnection();")
			.append("\n\t\t\tString commaSeparatedIds = Utilities.toQoutedCommaSeparatedString(ids);")
			.append("\n\t\t\tif(MySQLManager.get").append(name).append("DAO().deleteByIds(conn, commaSeparatedIds)){")
			.append("\n\t\t\t\tJSONObject obj = new JSONObject();")
			.append("\n\t\t\t\tobj.put(\"deleted_ids\", commaSeparatedIds);")
			.append("\n\t\t\t\treturn Response.ok(obj.toString()).build();")
			.append("\n\t\t\t}")
			.append("\n\t\t} catch (WebApplicationException e) {")
			.append("\n\t\t\tLOGGER.error(\"delete").append(name).append("s\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), e.getResponse().getStatus()));")
			.append("\n\t\t} catch (Exception e) {")
			.append("\n\t\t\tLOGGER.error(\"delete").append(name).append("s\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), Constants.EXPECTATION_FAILED));")
			.append("\n\t\t}finally {")
			.append("\n\t\t\tDatabaseUtilities.closeConnection(conn);")
			.append("\n\t\t}")
			.append("\n\t\treturn Response.ok(Constants.FAILURE_STATUS_STR).status(Constants.EXPECTATION_FAILED).build();\n\t}\n");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder createMethodImplementation(String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\tpublic static Response create").append(name).append("(String userId, String companyId, ").append(name).append(" ").append(Utilities.lowerFirstLetter(name)).append(") {")
			.append("\n\t\tConnection conn = null;")
			.append("\n\t\ttry {")
			.append("\n\t\t\tconn = DatabaseUtilities.getReadWriteConnection();")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setId(UUID.randomUUID().toString());")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setCreated_by(userId);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setCompany_id(companyId);")
			.append("\n\t\t\tString currentUtcDateStr = new Date(System.currentTimeMillis()).toString();")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setCreated_at(currentUtcDateStr);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setLast_updated_by(userId);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setLast_updated_at(currentUtcDateStr);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(" = MySQLManager.get").append(name)
			.append("DAO().create(conn, ").append(Utilities.lowerFirstLetter(name)).append(");")
			.append("\n\t\t\treturn Response.ok(").append(Utilities.lowerFirstLetter(name)).append(").build();")
			.append("\n\t\t} catch (WebApplicationException e) {")
			.append("\n\t\t\tLOGGER.error(\"create").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), e.getResponse().getStatus()));")
			.append("\n\t\t} catch (Exception e) {")
			.append("\n\t\t\tLOGGER.error(\"create").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), Constants.EXPECTATION_FAILED));")
			.append("\n\t\t}finally {")
			.append("\n\t\t\tDatabaseUtilities.closeConnection(conn);")
			.append("\n\t\t}\n\t}\n");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder updateMethodImplementation(String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\tpublic static Response update")
					.append(name).append("(String userId, String companyId, String id, ").append(name)
					.append(" ").append(Utilities.lowerFirstLetter(name)).append(") {")
			.append("\n\t\tConnection conn = null;")
			.append("\n\t\ttry {")
			.append("\n\t\t\tconn = DatabaseUtilities.getReadWriteConnection();")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setId(id);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name)).append(".setCompany_id(companyId);")
			.append("\n\t\t\tString currentUtcDateStr = new Date(System.currentTimeMillis()).toString();")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name))
			.append(".setLast_updated_by(userId);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name))
			.append(".setLast_updated_at(currentUtcDateStr);")
			.append("\n\t\t\t").append(Utilities.lowerFirstLetter(name))
			.append(" = MySQLManager.get").append(name).append("DAO().update(conn, ")
			.append(Utilities.lowerFirstLetter(name)).append(");")
			.append("\n\t\t\treturn Response.ok(").append(Utilities.lowerFirstLetter(name)).append(").build();")
			.append("\n\t\t} catch (WebApplicationException e) {")
			.append("\n\t\t\tLOGGER.error(\"update").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), e.getResponse().getStatus()));")
			.append("\n\t\t} catch (Exception e) {")
			.append("\n\t\t\tLOGGER.error(\"update").append(name).append("\",e);")
			.append("\n\t\t\tthrow new WebApplicationException(Utilities.constructResponse(e.getMessage(), Constants.EXPECTATION_FAILED));")
			.append("\n\t\t}finally {")
			.append("\n\t\t\tDatabaseUtilities.closeConnection(conn);")
			.append("\n\t\t}\n\t}\n");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

}
