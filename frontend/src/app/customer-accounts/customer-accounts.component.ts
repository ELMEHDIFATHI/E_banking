import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {catchError, Observable, throwError} from "rxjs";
import {BankAccount} from "../model/bankaccount.model";

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css']
})
export class CustomerAccountsComponent implements OnInit {
    customerId!:string;
    customer!:Customer;
    errorMessage!:string;
    customerBankAccounts! :Observable<Array<BankAccount>>;
    custemer$!:Observable<Customer>;

  constructor(private route:ActivatedRoute,private router:Router,private custemerService: CustomerService) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
  this.customerId= this.route.snapshot.params['id'];
  this.custemer$ = this.custemerService.getCustemerById(Number(this.customerId)).pipe(
    catchError(err=>{
      this.errorMessage=err.message;
      return throwError(err)

    })
  );

  this.customerBankAccounts=this.custemerService.getCustemerBankAccount(Number(this.customerId)).pipe(
      catchError(err=>{
        this.errorMessage=err.message;
        return throwError(err)

      })
    );

  }

}
