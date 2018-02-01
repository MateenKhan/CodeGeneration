package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class UiModel {

	private static Logger LOGGER = Logger.getLogger(UiModel.class);

	public static void main(String[] args) throws Exception {
		String str = "{\"location\":\"F:/\",\"name\":\"BillingCompany\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"company_id\",\"type\":\"String\"},{\"name\":\"created_by\",\"type\":\"String\"},{\"name\":\"created_at\",\"type\":\"long\"},{\"name\":\"last_updated_by\",\"type\":\"String\"},{\"name\":\"last_updated_at\",\"type\":\"long\"},{\"name\":\"plan_id\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"}]}";
		JSONObject obj = Utilities.getJsonFromString(str);
		createUiModel(obj);

	}

	@SuppressWarnings("resource")
	public static File createUiModel(JSONObject obj) throws Exception {
		File f = null;
		FileOutputStream fout = null;
		try {
			JSONArray fields = obj.optJSONArray("fields");
			if (null == fields || fields.length() == 0) {
				throw new Exception("empty fields received");
			}
			String uiModelFileName = Utilities.capitalizeFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(uiModelFileName)) {
				throw new Exception("empty name received");
			}
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			String modelClassName = uiModelFileName+".model.ts";
			f = new File(fileLocaiton+modelClassName);
			System.out.println(f.getAbsolutePath());
			fout = new FileOutputStream(f);
			StringBuilder finalCode = new StringBuilder();
			StringBuilder fieldsStr = new StringBuilder("");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = fieldObj.optString("name");
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:");
				}
				String fieldType = fieldObj.optString("type");
				fieldType = Utilities.getUiTypeString(fieldType);
				fieldsStr.append("  ").append(fieldName).append(":").append(fieldType).append(";\n");
			}
			finalCode.append("\n\n")
			.append("export class ").append(uiModelFileName+"Model").append(" {\n")
			.append(fieldsStr)
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
