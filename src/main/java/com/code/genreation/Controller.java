package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class Controller {

	private static final Logger LOGGER = Logger.getLogger(Controller.class);
	private static final String IMPORTS = "\n\nimport javax.validation.constraints.NotNull;\nimport javax.ws.rs.DELETE;\nimport javax.ws.rs.GET;\nimport javax.ws.rs.POST;\nimport javax.ws.rs.PUT;\nimport javax.ws.rs.Path;\nimport javax.ws.rs.PathParam;\nimport javax.ws.rs.Produces;\nimport javax.ws.rs.core.MediaType;\nimport javax.ws.rs.core.Response;\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\n\n";

	public static void main(String[] args) throws Exception {
		try {
			String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"createPojo\":\"true\",\"pk\":\"id\",\"pkType\":\"String\",\"lowerCaseName\":\"true\",\"lowerCaseFieldName\":\"true\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
			JSONObject obj = Utilities.getJsonFromString(str);
			createController(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File createController(JSONObject obj) throws Exception {
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
			f = new File(name+"Controller.java");
			System.out.println(f.getAbsolutePath());
			fout = new FileOutputStream(f);
			if (lowerCaseFieldName) {
				pk = pk.toLowerCase();
			}
			StringBuilder finalCode = new StringBuilder();
			String className = name+"Controller";
			StringBuilder getAllMethodStr = getAllMethodImplementation(className, name);
			StringBuilder createMethodStr = createMethodImplementation(className, name);
			StringBuilder getMethodStr = getMethodImplementation(className, name);
			StringBuilder deleteMethodStr = deleteMethodImplementation(className, name);
			StringBuilder deleteManyMethodStr = deleteManyMethodImplementation(className, name);
			StringBuilder updateMethodStr = updateMethodImplementation(className, name);

			finalCode.append(IMPORTS)
			.append("\n@Api(value = \"").append(name).append(" Controller\")")
			.append("\n@Path(\"/users/{userId}/companies/{companyId}/")
			.append(Utilities.lowerFirstLetter(name)).append("\")")
			.append("\npublic class ").append(className).append(" {\n\n")
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

	private static StringBuilder getMethodImplementation(String className, String name) throws Exception {
		try {
			StringBuilder getMethodStr = new StringBuilder("\n\t@GET\n\t@Path(\"/{id}\")\n\t@ApiOperation(value = \"Returns ")
					.append( name).append("\", ")
			.append("notes = \"Used to get of ").append(name).append(" by id\", ")
			.append("responseContainer = \"java.lang.String\")")
			.append("\n\t@Produces(MediaType.APPLICATION_JSON)")
			.append("\n\tpublic "+name+" get")
			.append(name)
			.append("(@NotNull @PathParam(\"userId\") String userId,@NotNull @PathParam(\"companyId\") String companyId ,@NotNull @PathParam(\"id\") String id){")
			.append("\n\t\treturn ")
			.append(className).append("Impl.get").append(name).append("(userId, companyId, id);")
			.append("\n\t}");
			return getMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder getAllMethodImplementation(String className, String name) throws Exception {
		try {
			StringBuilder getAllMethodStr = new StringBuilder("\n\t@GET\n\t@ApiOperation(value = \"Returns list of ").append(name).append("s\", ")
			.append("notes = \"Used to get list of ").append(name).append("s\", ")
			.append("responseContainer = \"java.lang.String\")")
			.append("\n\t@Produces(MediaType.APPLICATION_JSON)")
			.append("\n\tpublic List<"+name+"> get").append(name)
			.append("s").append("(@NotNull @PathParam(\"userId\") String userId,@NotNull @PathParam(\"companyId\") String companyId){")
			.append("\n\t\treturn ").append(className).append("Impl.get").append(name).append("s(userId, companyId);")
			.append("\n\t}");
			return getAllMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder deleteMethodImplementation(String className, String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\t@DELETE\n\t@Path(\"/{id}\")\n\t@ApiOperation(value = \"Delete ").append(name).append("\", ")
			.append("notes = \"Deletes ").append(name).append(" by id\", ")
			.append("responseContainer = \"java.lang.String\")")
			.append("\n\t@Produces(MediaType.APPLICATION_JSON)")
			.append("\n\tpublic Response delete").append(name)
			.append("(@NotNull @PathParam(\"userId\") String userId,@NotNull @PathParam(\"companyId\") String companyId ,@NotNull @PathParam(\"id\") String id){")
			.append("\n\t\treturn ").append(className).append("Impl.delete").append(name).append("(userId, companyId, id);")
			.append("\n\t}");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder deleteManyMethodImplementation(String className, String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\t@POST\n\t@Path(\"/delete\")\n\t@ApiOperation(value = \"Delete ").append(name).append("\", ")
			.append("notes = \"Deletes ").append(name).append(" by ids\", ")
			.append("responseContainer = \"java.lang.String\")")
			.append("\n\t@Produces(MediaType.APPLICATION_JSON)")
			.append("\n\tpublic Response delete").append(name).append("s")
			.append("(@NotNull @PathParam(\"userId\") String userId,@NotNull @PathParam(\"companyId\") String companyId , List<String> ids){")
			.append("\n\t\treturn ").append(className).append("Impl.delete").append(name)
			.append("s(userId, companyId, ids);")
			.append("\n\t}");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder createMethodImplementation(String className, String name) throws Exception {
		try {
			StringBuilder insertMethodStr = new StringBuilder("\n\t@POST\n\t@ApiOperation(value = \"Create ").append(name).append("\", ")
			.append("notes = \"Used to create ").append(name).append("\", ")
			.append("responseContainer = \"java.lang.String\")")
			.append("\n\t@Produces(MediaType.APPLICATION_JSON)")
			.append("\n\tpublic Response create").append(name).append("(@NotNull @PathParam(\"userId\") String userId,@NotNull @PathParam(\"companyId\") String companyId,")
			.append(name).append(" ").append(Utilities.lowerFirstLetter(name)).append("){")
			.append("\n\t\treturn ").append(className).append("Impl.create").append(name).append("(userId, companyId, ").append(Utilities.lowerFirstLetter(name)).append(");")
			.append("\n\t}");
			return insertMethodStr;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

	private static StringBuilder updateMethodImplementation(String className, String name) throws Exception {
		try {
			StringBuilder result = new StringBuilder("\n\t@PUT\n\t@Path(\"/{id}\")\n\t@ApiOperation(value = \"Update ").append(name).append("\", ")
			.append("notes = \"Update ").append(name).append(" by id\", ")
			.append("responseContainer = \"java.lang.String\")")
			.append("\n\t@Produces(MediaType.APPLICATION_JSON)")
			.append("\n\tpublic Response update").append(name).append("(@NotNull @PathParam(\"userId\") String userId,@NotNull @PathParam(\"companyId\") String companyId ,@NotNull @PathParam(\"id\") String id,")
			.append(name).append(" ").append(name).append("){")
			.append("\n\t\treturn ").append(className).append("Impl.update").append(name).append("(userId, companyId, id, ").append(name).append(");")
			.append("\n\t}");
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		}
	}

}
