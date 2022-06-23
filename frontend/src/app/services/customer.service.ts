import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, publish} from "rxjs";
import {Customer} from "../model/customer.model";
import {Environment} from "@angular/cli/lib/config/workspace-schema";
import {environment} from "../../environments/environment";
import {BankAccount} from "../model/bankaccount.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {


  constructor(private http:HttpClient) {
  }
  public getCustemers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(`${environment.backendHost}/customers`);
  }
  public searchCustemers(kw:string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(`${environment.backendHost}/customers/search?keyword=${kw}`);
  }
  public saveCustemers(custemer:Customer):Observable<Customer>{
    return this.http.post<Customer>(`${environment.backendHost}/customers`,custemer);
  }
  public deleteCustemers(id:number){
    return this.http.delete(`${environment.backendHost}/customers/${id}`);
  }
public getCustemerBankAccount(id:number):Observable<Array<BankAccount>>{
    return this.http.get<Array<BankAccount>>(`${environment.backendHost}/customers/${id}/accounts`);
}
  public getCustemerById(id:number):Observable<Customer>{
    return this.http.get<Customer>(`${environment.backendHost}/customers/${id}`);
  }


}
