
import {Component,ViewChild} from "@angular/core";
import {FormGroup, FormBuilder} from "@angular/forms";
import {SwitchBoard} from "qCommon/app/services/SwitchBoard";
import {Session} from "qCommon/app/services/Session";
import {ToastService} from "qCommon/app/services/Toast.service";
import {TOAST_TYPE} from "qCommon/app/constants/Qount.constants";
import {LoadingService} from "qCommon/app/services/LoadingService";
import {pageTitleService} from "qCommon/app/services/PageTitle";
import {Router} from "@angular/router";
import {BillingCompanyService} from "qCommon/app/services/BillingCompanyService.service";
import {BillingCompanyForm} from "../forms/BillingCompany.form";

declare let jQuery:any;
declare let _:any;

@Component({
  selector: 'billingCompany',
  templateUrl: '../views/billingCompany.html',
})

export class BillingCompanyComponent{
  billingCompanyForm: FormGroup;
  billingCompanys = [];
  newFormActive:boolean = true;
  haslateFees: boolean = false;
  tableData:any = {};
  tableOptions:any = {};
  editMode:boolean = false;
  companyId:string;
  billingCompanyId:any;
  row:any;
  tableColumns:Array<string> = ['id','company_id','created_by','created_at','last_updated_by','last_updated_at','plan_id','state']
  showFlyout:boolean = false;
  confirmSubscription:any;
  routeSubscribe:any;


  constructor(private _fb: FormBuilder, private _billingCompanyForm: BillingCompanyForm, private switchBoard: SwitchBoard,private _router: Router,
              private billingCompanyService: BillingCompanyService, private toastService: ToastService, private loadingService:LoadingService,
              private titleService:pageTitleService){
    this.billingCompanyForm = this._fb.group(this._billingCompanyForm.getForm());
    this.titleService.setPageTitle("Late Fees");

    this.confirmSubscription = this.switchBoard.onToastConfirm.subscribe(toast => this.deleteBillingCompany(toast));
    this.companyId = Session.getCurrentCompany();
    this.loadingService.triggerLoadingEvent(true);
    this.routeSubscribe = switchBoard.onClickPrev.subscribe(title => {
      if(this.showFlyout){
        this.hideFlyout();
      }else {
        this.toolsRedirect();
      }
    });
    this.billingCompanysService.billingCompanys(this.companyId)
      .subscribe(billingCompanys => this.buildTableData(billingCompanys), error=> this.handleError(error));

  }

  toolsRedirect(){
  //change below route
    let link = ['tools'];
    this._router.navigate(link);
  }
  ngOnDestroy(){
    this.routeSubscribe.unsubscribe();
    this.confirmSubscription.unsubscribe();
  }

  handleError(error){
    this.loadingService.triggerLoadingEvent(false);
    //this.row = {};
    this.toastService.pop(TOAST_TYPE.error, "Could not perform operation");
  }

  showAddBillingCompany() {
    this.titleService.setPageTitle("CREATE BILLINGCOMPANY");
    this.editMode = false;
    this.newForm();
    this.billingCompanyForm = this._fb.group(this._billingCompanyForm.getForm());
    this.showFlyout = true;
  }

  showEditBillingCompany(row: any){
    let base = this;
    this.titleService.setPageTitle("UPDATE BILLINGCOMPANY");
    this.loadingService.triggerLoadingEvent(true);
    this.billingCompanysService.getBillingCompany(this.companyId, row.id)
      .subscribe(billingCompany => {
        this.row=billingCompany;
        this._billingCompanyForm.updateForm(this.billingCompanyForm, billingCompany);
        this.loadingService.triggerLoadingEvent(false);
      }, error => this.handleError(error));
    this.editMode = true;
    this.newForm();
    this.showFlyout = true;
  }

  deleteBillingCompany(toast){
    this.loadingService.triggerLoadingEvent(true);
    this.billingCompanysService.removeBillingCompany(this.companyId, this.billingCompanyId)
      .subscribe(coa => {
        // this.loadingService.triggerLoadingEvent(false);
        this.toastService.pop(TOAST_TYPE.success, "BillingCompany deleted successfully");
        
        this.billingCompanysService.billingCompanys(this.companyId)
          .subscribe(billingCompanys => this.buildTableData(billingCompanys), error=> this.handleError(error));
      }, error => this.handleError(error));
  }
  
  removeBillingCompany(row: any){
    this.billingCompanyId = row.id;
    this.toastService.pop(TOAST_TYPE.confirm, "Are you sure you want to delete?");
  }

  newForm(){
    this.newFormActive = false;
    setTimeout(()=> this.newFormActive=true, 0);
  }

  ngOnInit(){

  }

  handleAction($event){
    let action = $event.action;
    delete $event.action;
    delete $event.actions;
    if(action == 'edit') {
      this.showEditBillingCompany($event);
    } else if(action == 'delete'){
      this.removeBillingCompany($event);
    }
  }

  submit($event){
    let base = this;
    $event && $event.preventDefault();
    let data = this._billingCompanyForm.getData(this.billingCompanyForm);
    
    this.loadingService.triggerLoadingEvent(true);
    if(this.editMode){
      data.id = this.row.id;
      this.billingCompanysService.updateBillingCompany(this.companyId, this.row.id,data)
        .subscribe(billingCompany => {
          base.row = {};
          base.toastService.pop(TOAST_TYPE.success, "BillingCompany updated successfully");
          let index = _.findIndex(base.billingCompanys, {id: data.id});
          base.billingCompanys[index] = billingCompany;
          base.buildTableData(base.billingCompanys);
          this.showFlyout = false;
        }, error => {
            this.loadingService.triggerLoadingEvent(false);
          if(error&&JSON.parse(error))
            this.toastService.pop(TOAST_TYPE.error, JSON.parse(error).message);
          else
          this.toastService.pop(TOAST_TYPE.success, "Failed to update the BillingCompany");
        });
    } else{
      this.billingCompanysService.addbillingCompany(this.companyId, data)
        .subscribe(newBillingCompany => {
          this.handleBillingCompany(newBillingCompany);
          this.showFlyout = false;
        }, error => this.handleError(error));
    }
  }

  handleBillingCompany(newBillingCompany){
    this.toastService.pop(TOAST_TYPE.success, "BillingCompany created successfully");
    this.billingCompanys.push(newBillingCompany);
    this.buildTableData(this.billingCompanys);
  }

  buildTableData(billingCompanys) {
    this.hasbillingCompanys = false;
    this.billingCompanys = billingCompanys;
    this.tableData.rows = [];
    this.tableOptions.search = true;
    this.tableOptions.pageSize = 9;
    this.tableData.columns = [{"name":"id","type":"String","title":"Id"},{"name":"company_id","type":"String","title":"Company_id"},{"name":"created_by","type":"String","title":"Created_by"},{"name":"created_at","type":"long","title":"Created_at"},{"name":"last_updated_by","type":"String","title":"Last_updated_by"},{"name":"last_updated_at","type":"long","title":"Last_updated_at"},{"name":"plan_id","type":"String","title":"Plan_id"},{"name":"state","type":"String","title":"State"}];
    let base = this;
    billingCompanys.forEach(function(billingCompany) {
      let row:any = {};
      _.each(base.tableColumns, function(key) {
          row[key] = billingCompany[key];
        row['actions'] = "<a class='action' data-action='edit' style='margin:0px 0px 0px 5px;'><i class='icon ion-edit'></i></a><a class='action' data-action='delete' style='margin:0px 0px 0px 5px;'><i class='icon ion-trash-b'></i></a>";
      });
      base.tableData.rows.push(row);
    });
    setTimeout(function(){
      base.hasbillingCompanys = true;
    }, 0);

    this.loadingService.triggerLoadingEvent(false);
  }


  hideFlyout(){
    this.titleService.setPageTitle("BillingCompany");
    this.row = {};
    this.showFlyout = !this.showFlyout;
  }


}
