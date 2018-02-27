package com.code.genreation;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.genreation.common.Utilities;

public class UiComponent {

	private static Logger LOGGER = Logger.getLogger(UiComponent.class);

	public static void main(String[] args) throws Exception {
		String str = "{\"location\":\"F:/\",\"name\":\"Discounts\",\"fields\":[{\"name\":\"id\",\"type\":\"String\"},{\"name\":\"name\",\"type\":\"String\"},{\"name\":\"description\",\"type\":\"String\"},{\"name\":\"type\",\"type\":\"String\"}]}";
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
			String tableName = Utilities.capitalizeFirstLetter(obj.optString("name"));
			if (StringUtils.isEmpty(tableName)) {
				throw new Exception("empty name received");
			}
			String capitalName = tableName.toUpperCase();
			String commaSeparatedFields = Utilities.getList(fields, "'");
			String objectName = Utilities.lowerFirstLetter(obj.optString("name"));
			String fileLocaiton = PropertyManager.getProperty("temp.file.location");
			String modelClassName = tableName+".component.ts";
			f = new File(fileLocaiton+modelClassName);
			System.out.println(f.getAbsolutePath());
			fout = new FileOutputStream(f);
			StringBuilder finalCode = new StringBuilder("\nimport {Component,ViewChild} from \"@angular/core\";\nimport {FormGroup, FormBuilder} from \"@angular/forms\";\nimport {SwitchBoard} from \"qCommon/app/services/SwitchBoard\";\nimport {Session} from \"qCommon/app/services/Session\";\nimport {ToastService} from \"qCommon/app/services/Toast.service\";\nimport {TOAST_TYPE} from \"qCommon/app/constants/Qount.constants\";\nimport {LoadingService} from \"qCommon/app/services/LoadingService\";\nimport {pageTitleService} from \"qCommon/app/services/PageTitle\";\nimport {Router} from \"@angular/router\";\n");
			finalCode.append("import {").append(tableName).append("Service} from \"../services/")
			.append(tableName).append("Service.service\";\n")
			.append("import {").append(tableName).append("Form} from \"../forms/").append(tableName).append(".form\";")
			.append("\n\ndeclare let jQuery:any;\ndeclare let _:any;\n\n@Component({\n  selector: '").append(objectName)
			.append("',\n  templateUrl: '../views/").append(objectName).append(".html',\n})\n\n")
			.append("export class ").append(tableName).append("Component{\n")
			.append("  ").append(objectName).append("Form: FormGroup;\n  ").append(objectName).append("s = [];\n")
			.append("  newFormActive:boolean = true;\n  tableData:any = {};\n  tableOptions:any = {};\n")
			.append("  editMode:boolean = false;\n  companyId:string;\n  ").append(objectName).append("Id:any;\n")
			.append("  row:any;\n  tableColumns:Array<string> = [").append(commaSeparatedFields).append("]\n")
			.append("  showFlyout:boolean = false;\n  confirmSubscription:any;\n  routeSubscribe:any;\n  has").append(objectName).append(":boolean;\n\n\n")
			.append("  constructor(private _fb: FormBuilder, private _").append(objectName).append("Form: ")
			.append(tableName).append("Form, private switchBoard: SwitchBoard,private _router: Router,\n")
			.append("              private ").append(objectName).append("Service: ").append(tableName).append("Service")
			.append(", private toastService: ToastService, private loadingService:LoadingService,\n              private titleService:pageTitleService){\n")
			.append("    this.").append(objectName).append("Form = this._fb.group(this._").append(objectName).append("Form.getForm());\n    this.titleService.setPageTitle(\"Late Fees\");\n\n    this.confirmSubscription = this.switchBoard.onToastConfirm.subscribe(toast => this.delete").append(tableName).append("(toast));\n    this.companyId = Session.getCurrentCompany();\n    this.loadingService.triggerLoadingEvent(true);\n    this.routeSubscribe = switchBoard.onClickPrev.subscribe(title => {\n      if(this.showFlyout){\n        this.hideFlyout();\n      }else {\n        this.toolsRedirect();\n      }\n    });\n    ")
			.append("this.").append(objectName).append("Service.").append(objectName).append("s(this.companyId)\n      .subscribe(").append(objectName).append("s => this.buildTableData(").append(objectName).append("s), error=> this.handleError(error));\n\n  }\n\n  toolsRedirect(){\n  //change below route\n    let link = ['tools'];\n    this._router.navigate(link);\n  }\n  ngOnDestroy(){\n    this.routeSubscribe.unsubscribe();\n    this.confirmSubscription.unsubscribe();\n  }\n\n  handleError(error){\n    this.loadingService.triggerLoadingEvent(false);\n    //this.row = {};\n    this.toastService.pop(TOAST_TYPE.error, \"Could not perform operation\");\n  }\n")
			.append("\n  showAdd").append(tableName).append("() {\n    this.titleService.setPageTitle(\"CREATE ").append(capitalName).append("\");\n    this.editMode = false;\n    this.newForm();\n    this.").append(objectName).append("Form = this._fb.group(this._").append(objectName).append("Form.getForm());\n    this.showFlyout = true;\n  }\n\n  showEdit").append(tableName).append("(row: any){\n    let base = this;\n    this.titleService.setPageTitle(\"UPDATE ").append(capitalName).append("\");\n    this.loadingService.triggerLoadingEvent(true);\n    this.").append(objectName).append("Service.get").append(tableName).append("(this.companyId, row.id)\n      .subscribe(").append(objectName).append(" => {\n        this.row=").append(objectName).append(";\n        this._").append(objectName).append("Form.updateForm(this.").append(objectName).append("Form, ").append(objectName).append(");\n        this.loadingService.triggerLoadingEvent(false);\n      }, error => this.handleError(error));\n    this.editMode = true;\n    this.newForm();\n    this.showFlyout = true;\n  }\n\n  delete").append(tableName).append("(toast){\n    this.loadingService.triggerLoadingEvent(true);\n    this.").append(objectName).append("Service.remove").append(tableName).append("(this.companyId, this.").append(objectName).append("Id)\n      .subscribe(coa => {\n        // this.loadingService.triggerLoadingEvent(false);\n        this.toastService.pop(TOAST_TYPE.success, \"").append(tableName).append(" deleted successfully\");\n        \n        this.").append(objectName).append("Service.").append(objectName).append("s(this.companyId)\n          .subscribe(").append(objectName).append("s => this.buildTableData(").append(objectName).append("s), error=> this.handleError(error));\n      }, error => this.handleError(error));\n  }\n  \n  remove").append(tableName).append("(row: any){\n    this.").append(objectName).append("Id = row.id;\n    this.toastService.pop(TOAST_TYPE.confirm, \"Are you sure you want to delete?\");\n  }\n\n  newForm(){\n    this.newFormActive = false;\n    setTimeout(()=> this.newFormActive=true, 0);\n  }\n\n  ngOnInit(){\n\n  }\n\n  handleAction($event){\n    let action = $event.action;\n    delete $event.action;\n    delete $event.actions;\n    if(action == 'edit') {\n      this.showEdit").append(tableName).append("($event);\n    } else if(action == 'delete'){\n      this.remove").append(tableName).append("($event);\n    }\n  }\n\n  submit($event){\n    let base = this;\n    $event && $event.preventDefault();\n    let data = this._").append(objectName).append("Form.getData(this.").append(objectName).append("Form);\n    \n    this.loadingService.triggerLoadingEvent(true);\n    if(this.editMode){\n      data.id = this.row.id;\n      this.").append(objectName).append("Service.update").append(tableName).append("(this.companyId, this.row.id,data)\n        .subscribe(").append(objectName).append(" => {\n          base.row = {};\n          base.toastService.pop(TOAST_TYPE.success, \"").append(tableName).append(" updated successfully\");\n          let index = _.findIndex(base.").append(objectName).append("s, {id: data.id});\n          base.").append(objectName).append("s[index] = ").append(objectName).append(";\n          base.buildTableData(base.").append(objectName).append("s);\n          this.showFlyout = false;\n        }, error => {\n            this.loadingService.triggerLoadingEvent(false);\n          if(error&&JSON.parse(error))\n            this.toastService.pop(TOAST_TYPE.error, JSON.parse(error).message);\n          else\n          this.toastService.pop(TOAST_TYPE.success, \"Failed to update the ").append(tableName).append("\");\n        });\n    } else{\n      this.").append(objectName).append("Service.add").append(objectName).append("(this.companyId, data)\n        .subscribe(new").append(tableName).append(" => {\n          this.handle").append(tableName).append("(new").append(tableName).append(");\n          this.showFlyout = false;\n        }, error => this.handleError(error));\n    }\n  }\n\n  handle").append(tableName).append("(new").append(tableName).append("){\n    this.toastService.pop(TOAST_TYPE.success, \"").append(tableName).append(" created successfully\");\n    this.").append(objectName).append("s.push(new").append(tableName).append(");\n    this.buildTableData(this.").append(objectName).append("s);\n  }\n")
			.append("\n  buildTableData(").append(objectName).append("s) {\n    this.has").append(objectName).append("s = false;\n    this.").append(objectName).append("s = ").append(objectName).append("s;\n    this.tableData.rows = [];\n    this.tableOptions.search = true;\n    this.tableOptions.pageSize = 9;\n    this.tableData.columns = ")
			.append(Utilities.getFooTableListJson(fields)).append(";\n")
		    .append("    let base = this;\n    ").append(objectName).append("s.forEach(function(").append(objectName).append(") {\n      let row:any = {};\n      _.each(base.tableColumns, function(key) {\n          row[key] = ").append(objectName).append("[key];\n        row['actions'] = \"<a class='action' data-action='edit' style='margin:0px 0px 0px 5px;'><i class='icon ion-edit'></i></a><a class='action' data-action='delete' style='margin:0px 0px 0px 5px;'><i class='icon ion-trash-b'></i></a>\";\n      });\n      base.tableData.rows.push(row);\n    });\n    setTimeout(function(){\n      base.has").append(objectName).append("s = true;\n    }, 0);\n\n    this.loadingService.triggerLoadingEvent(false);\n  }\n\n\n  hideFlyout(){\n    this.titleService.setPageTitle(\"").append(tableName).append("\");\n    this.row = {};\n    this.showFlyout = !this.showFlyout;\n  }\n\n\n}\n");
			
			
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
