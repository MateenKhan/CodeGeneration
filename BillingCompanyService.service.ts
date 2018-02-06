
import {Injectable} from "@angular/core";
import {QountServices} from "./QountServices";
import {Response, Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {PATH, SOURCE_TYPE} from "../constants/Qount.constants";
import {Session} from "./Session";
import {VendorModel} from "../models/Vendor.model";
import {CompanyModel} from "../models/Company.model";


@Injectable()
export class BillingCompanyService extends  QountServices{

    constructor(http: Http) {
        super(http);
    }

    getAll(companyId): Observable<any> {
        var url = this.interpolateUrl(PATH.BILLINGCOMPANYSERVICE, null, {id: Session.getUser().id, companyId: companyId});
        return this.query(url, SOURCE_TYPE.JAVA).map(res => <any> res.json())
            .catch(this.handleError)
    }

    addbillingCompany(companyId, billingCompany: Observable<any> {
        var url = this.interpolateUrl(PATH.BILLINGCOMPANYSERVICE, null, {id: Session.getUser().id, companyId: companyId});
        return this.create(url, billingCompany, SOURCE_TYPE.JAVA).map(res => <any> res.json())
            .catch(this.handleError)
    }

    updatebillingCompany(companyId, billingCompanyId, billingCompany: Observable<any> {
        var url = this.interpolateUrl(PATH.BILLINGCOMPANYSERVICE, null, {id: Session.getUser().id, companyId: companyId, billingCompanyId:billingCompanyId)});
        return this.update(url, billingCompany, SOURCE_TYPE.JAVA).map(res => <any> res.json())
            .catch(this.handleError)
    }

    removebillingCompany(companyId, billingCompanyId: Observable<any> {
        var url = this.interpolateUrl(PATH.BILLINGCOMPANYSERVICE, null, {id: Session.getUser().id, companyId: companyId, billingCompanyId:billingCompanyId)});
        return this.delete(url, billingCompany, SOURCE_TYPE.JAVA).map(res => <any> res.json())
            .catch(this.handleError)
    }

    getbillingCompany(companyId, billingCompanyId: Observable<any> {
        var url = this.interpolateUrl(PATH.BILLINGCOMPANYSERVICE, null, {id: Session.getUser().id, companyId: companyId, billingCompanyId:billingCompanyId)});
        return this.query(url, billingCompany, SOURCE_TYPE.JAVA).map(res => <any> res.json())
            .catch(this.handleError)
    }

    private handleError (error: Response) {
        return Observable.throw(error.text());
    }
}