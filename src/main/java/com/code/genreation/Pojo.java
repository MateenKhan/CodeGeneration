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
	public static void createPojo(JSONObject obj) throws Exception {
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
			File f = new File(location+name+".java");
			f.createNewFile();
			// if(!f.exists()){
			// f.createNewFile();
			// }else{
			// throw new Exception("file at location exists:"+location);
			// }
			fout = new FileOutputStream(f);
			HashSet<String> imports = new  HashSet<String>();
			String fieldsStr = "";
			String getterStr = "";
			String setterStr = "";
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:" + fieldName);
				}
				String fieldType = fieldObj.optString("type");
				fieldType = Utilities.getTypeString(fieldType,imports);
				fieldsStr += "\tprivate " + fieldType + " " + fieldName + ";\n";
				getterStr += "\tpublic " + fieldType + " get" + Utilities.getCamelCase(fieldName) + "() {\n\t\treturn " + fieldName + ";\n\t}\n\n";
				setterStr += "\tpublic void set" + Utilities.getCamelCase(fieldName) + "("+fieldType +" "+ fieldName+") {\n\t\tthis." + fieldName + "=" + fieldName + ";\n\t}\n\n";

			}
			Iterator<String> importsItr = imports.iterator();
			String importStr = "";
			while(importsItr.hasNext()){
				importStr="import "+importsItr.next()+";\n";
			}
			fout.write(importStr.getBytes());
			fout.write(("public class " + name + " {\n\n").getBytes());
			fout.write((fieldsStr+"\n").getBytes());
			fout.write((getterStr).getBytes());
			fout.write((setterStr+"\n").getBytes());
			fout.write("}".getBytes());
		} catch (Exception e) {
			LOGGER.error(e);
			throw e;
		} finally {
			Utilities.closeStream(fout);
		}
	}

}
