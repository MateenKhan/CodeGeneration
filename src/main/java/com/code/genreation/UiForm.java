package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class UiForm {

	private static Logger LOGGER = Logger.getLogger(UiForm.class);

	public static void main(String[] args) throws Exception {
		String str = "{\"location\":\"F:/\",\"name\":\"Discounts\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"description\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"}]}";
		JSONObject obj = Utilities.getJsonFromString(str);
		generateCode(obj);

	}

	@SuppressWarnings("resource")
	public static File generateCode(JSONObject obj) throws Exception {
		File f = null;
		FileOutputStream fout = null;
		try {
			JSONArray fields = obj.optJSONArray("fields");
			if (null == fields || fields.length() == 0) {
				throw new Exception("empty fields received");
			}
			String uiModelName = Utilities.capitalizeFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(uiModelName)) {
				throw new Exception("empty name received");
			}
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			String modelClassName = uiModelName+".form.ts";
			f = new File(fileLocaiton+modelClassName);
			System.out.println(f.getAbsolutePath());
			fout = new FileOutputStream(f);
			StringBuilder finalCode = new StringBuilder();
			StringBuilder importsStr = new StringBuilder("import {Injectable} from \"@angular/core\";\nimport {abstractForm} from \"qCommon/app/forms/abstractForm\";\nimport {Validators} from \"@angular/forms\";\n");
			StringBuilder fieldsStr = new StringBuilder("");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:");
				}
				fieldsStr.append("      \"").append(fieldName).append("\": [model?model.").append(fieldName).append(":'', Validators.required],\n");
			}
			fieldsStr.deleteCharAt(fieldsStr.length()-2);
			finalCode.append("\n").append(importsStr).append("\n@Injectable()\n")
			.append("export class ").append(uiModelName+"Form").append("  extends abstractForm{\n\n  getForm(model?:any) {\n    return {\n")
			.append(fieldsStr)
			.append("\n    }\n  }\n\n}");
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
