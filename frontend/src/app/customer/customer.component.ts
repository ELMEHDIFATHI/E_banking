import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {error} from "@angular/compiler/src/util";
import {CustomerService} from "../services/customer.service"
import {catchError, filter, map, Observable, throwError} from "rxjs";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {
  errorMessage!:string;
  searchformGroup : FormGroup|undefined;


  constructor( private customerService:CustomerService,private fb:FormBuilder,private router:Router){}
  customers! :Observable<Array<Customer>>;


//ngOnInit() la methode qui sexecute au demarage
  ngOnInit(): void {
    this.searchformGroup=this.fb.group({
      keyword:this.fb.control("")
    })
    this.handleSearchCustomers();
  }

  handleSearchCustomers() {
    let kw = this.searchformGroup?.value.keyword;
    this.customers=this.customerService.searchCustemers(kw).pipe(
      catchError(err=>{
        this.errorMessage=err.message;
        return throwError(err)

      })
    );
  }

  handelDeleteCustomer(c: Customer) {
    let conf=confirm("Are you sure?");
    if (!conf)return;
    this.customerService.deleteCustemers(c.id).subscribe({
      next: (resp) => {
        this.customers=this.customers.pipe(
          map(data=>{
            let index= data.indexOf(c);
            data.splice(index,1)
            return data;
          })
        )
      },
      error:err=>{
        console.log(err);
      }
    })
  }

  handelCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl(`customer-accounts/${customer.id}`,{state:customer});
  }
}
