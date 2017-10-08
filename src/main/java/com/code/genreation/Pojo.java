package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.String;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class Pojo {

	private static Logger LOGGER = Logger.getLogger(Pojo.class);

	public static void main(String[] args) throws Exception {
		String str = "{\"location\":\"F:/\",\"name\":\"Company\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"ein\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"},{\"name\":\"phone_number\",\"type\":\"String\"},{\"name\":\"address\",\"type\":\"String\"},{\"name\":\"city\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"},{\"name\":\"country\",\"type\":\"String\"},{\"name\":\"zipcode\",\"type\":\"String\"},{\"name\":\"currency\",\"type\":\"String\"},{\"name\":\"email\",\"type\":\"String\"},{\"name\":\"payment_info\",\"type\":\"String\"},{\"name\":\"createdBy\",\"type\":\"String\"},{\"name\":\"modifiedBy\",\"type\":\"String\"},{\"name\":\"createdDate\",\"type\":\"String\"},{\"name\":\"modifiedDate\",\"type\":\"String\"},{\"name\":\"owner\",\"type\":\"String\"},{\"name\":\"active\",\"type\":\"boolean\"}]}";
		JSONObject obj = Utilities.getJsonFromString(str);
		createPojo(obj);

	}

	@SuppressWarnings("resource")
	public static File createPojo(JSONObject obj) throws Exception {
		File f = null;
		FileOutputStream fout = null;
		try {
			JSONArray fields = obj.optJSONArray("fields");
			if (null == fields || fields.length() == 0) {
				throw new Exception("empty fields received");
			}
			String pojoFileName = Utilities.capitalizeFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(pojoFileName)) {
				throw new Exception("empty name received");
			}
			f = new File(pojoFileName+".java");
			fout = new FileOutputStream(f);
			StringBuilder finalCode = new StringBuilder();
			HashSet<String> imports = new  HashSet<String>();
			StringBuilder fieldsStr = new StringBuilder("");
			StringBuilder getterStr = new StringBuilder("");
			StringBuilder setterStr = new StringBuilder("");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:");
				}
				String fieldType = fieldObj.optString("type");
				fieldType = Utilities.getTypeString(fieldType,imports);
				fieldsStr.append("\tprivate ").append(fieldType).append(" ").append(fieldName + ";\n");
				getterStr.append("\tpublic ").append(fieldType).append(" get")
				.append(Utilities.getCamelCase(fieldName)).append("() {\n\t\treturn ").append(fieldName)
				.append(";\n\t}\n\n");
				setterStr.append("\tpublic void set").append(Utilities.getCamelCase(fieldName))
				.append("(").append(fieldType).append(" ").append(fieldName).append(") {\n\t\tthis.")
				.append(fieldName).append("=").append(fieldName).append(";\n\t}\n\n");

			}
			Iterator<String> importsItr = imports.iterator();
			String importStr = "";
			while(importsItr.hasNext()){
				importStr="import "+importsItr.next()+";\n";
			}
			finalCode.append("\n\n").append(importStr)
			.append("public class ").append(pojoFileName).append(" {\n\n")
			.append(fieldsStr).append("\n")
			.append(getterStr)
			.append(setterStr).append("\n")
			.append("}");
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
