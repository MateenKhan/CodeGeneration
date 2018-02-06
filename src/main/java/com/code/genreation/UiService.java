package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class UiService {

	private static Logger LOGGER = Logger.getLogger(UiService.class);

	public static void main(String[] args) throws Exception {
		String str = "{\"location\":\"F:/\",\"name\":\"BillingCompany\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"company_id\",\"type\":\"String\"},{\"name\":\"created_by\",\"type\":\"String\"},{\"name\":\"created_at\",\"type\":\"long\"},{\"name\":\"last_updated_by\",\"type\":\"String\"},{\"name\":\"last_updated_at\",\"type\":\"long\"},{\"name\":\"plan_id\",\"type\":\"String\"},{\"name\":\"state\",\"type\":\"String\"}]}";
		JSONObject obj = Utilities.getJsonFromString(str);
		generateCode(obj);

	}

	public static File generateCode(JSONObject obj) throws Exception {
		File f = null;
		FileOutputStream fout = null;
		try {
			JSONArray fields = obj.optJSONArray("fields");
			if (null == fields || fields.length() == 0) {
				throw new Exception("empty fields received");
			}
			String uiModelName = Utilities.capitalizeFirstLetter(obj.optString("name"));
			String fieldName = Utilities.lowerFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(uiModelName)) {
				throw new Exception("empty name received");
			}
			String fieldId = fieldName+"Id";
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			String modelClassName = uiModelName+"Service.service.ts";
			f = new File(fileLocaiton+modelClassName);
			System.out.println(f.getAbsolutePath());
			fout = new FileOutputStream(f);
			String constantVariableName = (uiModelName+"Service").toUpperCase();
			StringBuilder finalCode = new StringBuilder();
			StringBuilder constructor = new StringBuilder("\n    constructor(http: Http) {\n        super(http);\n    }\n");
			StringBuilder error = new StringBuilder("\n    private handleError (error: Response) {\n        return Observable.throw(error.text());\n    }\n");
			StringBuilder getAllMethod = new StringBuilder();
			StringBuilder addMethod = new StringBuilder();
			StringBuilder updateMethod = new StringBuilder();
			StringBuilder removeMethod = new StringBuilder();
			StringBuilder getMethod = new StringBuilder();
			getAllMethod.append("\n    getAll(companyId): Observable<any> {\n")
			.append("        var url = this.interpolateUrl(PATH.").append(constantVariableName).append(", null, {id: Session.getUser().id, companyId: companyId});\n")
			.append("        return this.query(url, SOURCE_TYPE.JAVA).map(res => <any> res.json())\n            .catch(this.handleError)\n    }\n");
			
			addMethod.append("\n    add").append(fieldName).append("(companyId, ").append(fieldName).append(": Observable<any> {\n")
			.append("        var url = this.interpolateUrl(PATH.").append(constantVariableName).append(", null, {id: Session.getUser().id, companyId: companyId});\n")
			.append("        return this.create(url, ").append(fieldName).append(", SOURCE_TYPE.JAVA).map(res => <any> res.json())\n")
			.append("            .catch(this.handleError)\n")
			.append("    }\n");
			
			updateMethod.append("\n    update").append(fieldName).append("(companyId, ").append(fieldId).append(", ").append(fieldName).append(": Observable<any> {\n")
			.append("        var url = this.interpolateUrl(PATH.").append(constantVariableName).append(", null, {id: Session.getUser().id, companyId: companyId, ").append(fieldId).append(":").append(fieldId).append(")});\n")
			.append("        return this.update(url, ").append(fieldName).append(", SOURCE_TYPE.JAVA).map(res => <any> res.json())\n")
			.append("            .catch(this.handleError)\n")
			.append("    }\n");
			
			
			removeMethod.append("\n    remove").append(fieldName).append("(companyId, ").append(fieldId).append(": Observable<any> {\n")
			.append("        var url = this.interpolateUrl(PATH.").append(constantVariableName).append(", null, {id: Session.getUser().id, companyId: companyId, ").append(fieldId).append(":").append(fieldId).append(")});\n")
			.append("        return this.delete(url, ").append(fieldName).append(", SOURCE_TYPE.JAVA).map(res => <any> res.json())\n")
			.append("            .catch(this.handleError)\n")
			.append("    }\n");
			
			getMethod.append("\n    get").append(fieldName).append("(companyId, ").append(fieldId).append(": Observable<any> {\n")
			.append("        var url = this.interpolateUrl(PATH.").append(constantVariableName).append(", null, {id: Session.getUser().id, companyId: companyId, ").append(fieldId).append(":").append(fieldId).append(")});\n")
			.append("        return this.query(url, ").append(fieldName).append(", SOURCE_TYPE.JAVA).map(res => <any> res.json())\n")
			.append("            .catch(this.handleError)\n")
			.append("    }\n");
			
			StringBuilder importsStr = new StringBuilder("import {Injectable} from \"@angular/core\";\nimport {QountServices} from \"./QountServices\";\nimport {Response, Http} from \"@angular/http\";\nimport {Observable} from \"rxjs/Rx\";\nimport {PATH, SOURCE_TYPE} from \"../constants/Qount.constants\";\nimport {Session} from \"./Session\";\nimport {VendorModel} from \"../models/Vendor.model\";\nimport {CompanyModel} from \"../models/Company.model\";\n");
			finalCode.append("\n").append(importsStr).append("\n\n").append("@Injectable()\n")
			.append("export class ").append(uiModelName+"Service").append(" extends  QountServices{\n")
			.append(constructor)
			.append(getAllMethod)
			.append(addMethod)
			.append(updateMethod)
			.append(removeMethod)
			.append(getMethod)
			.append(error)
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
