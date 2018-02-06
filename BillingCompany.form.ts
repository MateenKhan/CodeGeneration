
import {Injectable} from "@angular/core";
import {abstractForm} from "qCommon/app/forms/abstractForm";
import {Validators} from "@angular/forms";

@Injectable()
export class BillingCompanyForm  extends abstractForm{

  getForm(model?:any) {
    return {
      "id": [model?model.id:'', Validators.required],
      "company_id": [model?model.company_id:'', Validators.required],
      "created_by": [model?model.created_by:'', Validators.required],
      "created_at": [model?model.created_at:'', Validators.required],
      "last_updated_by": [model?model.last_updated_by:'', Validators.required],
      "last_updated_at": [model?model.last_updated_at:'', Validators.required],
      "plan_id": [model?model.plan_id:'', Validators.required],
      "state": [model?model.state:'', Validators.required]

    }
  }

}