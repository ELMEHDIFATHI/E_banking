import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";
import {environment} from "../../environments/environment";
import {AccountDetails} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private http:HttpClient) { }
  public getAccount(accountId:string,page:number,size:number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(`${environment.backendHost}/accounts/${accountId}/Pageoperations?page=${page}&size=${size}`);
  }

  public debit(accountID:string,amount:number,description:string){
    let data={accountID,amount,description}
    console.log(data)
    return this.http.post(`${environment.backendHost}/accounts/debit/`,data);
  }

  public credit(accountID:string,amount:number,description:string){
    let data={accountID,amount,description}
    console.log(data)
    return this.http.post(`${environment.backendHost}/accounts/credit/`,data);
  }

  public transfer(accountSource:string,accountDestination:string,amount:number,description:string){

    let data={accountSource,accountDestination,amount,description}
    console.log(data)
    return this.http.post(`${environment.backendHost}/accounts/transfer/`,data);
  }
}
