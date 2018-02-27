package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class UiHtml {

	private static Logger LOGGER = Logger.getLogger(UiHtml.class);

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
			String tableName = Utilities.capitalizeFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(tableName)) {
				throw new Exception("empty name received");
			}
			String objectName = Utilities.lowerFirstLetter(obj.optString("name"));
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			String modelClassName = objectName + ".html";
			f = new File(fileLocaiton + modelClassName);
			System.out.println(f.getAbsolutePath());
			fout = new FileOutputStream(f);
			StringBuilder finalCode = new StringBuilder();
			finalCode
					.append("<div class=\"row\" foundation>\n  <div class=\"columns small-6 small-offset-6 right\">\n\n  </div>\n</div>\n<div class=\"row chart-of-accounts\" foundation>\n  <div class=\"column large-12 medium-12 small-12 foo-container\">\n    <div class=\"row add-share-buttons add-share-adjust\">\n      <div class=\"column medium-12 text-right\">\n            <span class=\"pointer\" (click)=\"showAdd")
					.append(tableName).append("()\">\n                <i class=\"icon ion-ios-plus-outline\"></i>\n                <span>").append(tableName)
					.append("</span>\n            </span>\n\n      </div>\n    </div>\n    <foo-table #fooTableDir=\"fooTable\" class=\"bighalf-footable\" *ngIf=\"has")
					.append(objectName).append("\" [data]=\"tableData\" [options]=\"tableOptions\" (onRowAction)=\"handleAction($event)\"></foo-table>\n  </div>\n\n")
					.append("<div class=\"qount-off-canvas-menu\">\n    <div class=\"flyout expanded\" [ngClass]=\"{'expanded':showFlyout}\">\n      <section class=\"flyout-placeholder\">\n        <div class=\"flyout-body flyout-shadow-effect\">\n          <div id=\"add")
					.append(tableName).append("\" #add").append(tableName)
					.append(" class=\"create-vendor\">\n            <form *ngIf=\"showFlyout\" (ngSubmit)=\"submit($event)\" [formGroup]=\"").append(objectName)
					.append("Form\">\n              <section class=\"form-section clearfix\">\n                <div class=\"row\">\n                  <div class=\"small-12 medium-12 columns\">\n                    <div class=\"form-section-left\">\n                      <header class=\"form-section-header\">Basic Info</header>\n");
			StringBuilder fieldsStr = new StringBuilder("");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject fieldObj = fields.optJSONObject(i);
				String fieldName = Utilities.capitalizeFirstLetter(fieldObj.optString("name"));
				String fieldVariable = fieldName.toLowerCase();
				if (StringUtils.isEmpty(fieldName)) {
					throw new Exception("empty fieldName received:");
				}
				fieldsStr.append("                      <div class=\"row\">\n                        <div class=\"small-4 columns\">\n                          <label class=\"text-right\">")
				.append(fieldName).append("</label>\n                        </div>\n                        <div class=\"small-6 columns\">\n                          <input type=\"text\" formControlName=\"").append(fieldVariable).append("\" placeholder=\"")
				.append(fieldName).append("*\"/>\n                        </div>\n                      </div>\n					  \n");
			}
			finalCode.append(fieldsStr);
			finalCode.append("                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"small-12 medium-12 columns\">\n                    <button class=\"button small float-right\" [disabled]=\"!isValid(").append(objectName).append("Form)\" (click)=\"submit($event)\">Save</button>\n                  </div>\n                </div>\n              </section>\n            </form>\n          </div>\n        </div>\n      </section>\n    </div>\n  </div>\n</div>\n");
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
