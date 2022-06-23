import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";
import {catchError, Observable, throwError} from "rxjs";
import {AccountDetails} from "../model/account.model";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  accountFormgroup!: FormGroup;
  curentPage:number=0
  pageSize:number=5
  accountObservable!: Observable<AccountDetails>;
  operationFormGroup!:FormGroup;
  errorMessage!:string

  constructor(private fb:FormBuilder,private accountService:AccountsService) { }

  ngOnInit(): void {
    this.accountFormgroup= this.fb.group({
      accountID:this.fb.control('')
    })
    this.operationFormGroup=this.fb.group({
      operationType: this.fb.control(null),
      amount: this.fb.control(0),
      description:this.fb.control(null),
      accountDestination:this.fb.control(null)

    })
  }

  handelSearchAccount() {
    let accountId:string= this.accountFormgroup.value.accountID;
   this.accountObservable= this.accountService.getAccount(accountId,this.curentPage,this.pageSize).pipe(
     catchError(err=>{
       this.errorMessage= err.message;
       return throwError(err);
     })
   );
  }

  gotoPage(page: number) {
    this.curentPage=page;
    this.handelSearchAccount()
  }

  handelAccountOperation() {
  let accountId:string = this.accountFormgroup.value.accountID;
  let opertaionType:string = this.operationFormGroup.value.operationType;
  let amount:number= this.operationFormGroup.value.amount;
  let description:string= this.operationFormGroup.value.description;
  let accountDestination:string=this.operationFormGroup.value.accountDestination;
  if (opertaionType=='DEBIT'){
    this.accountService.debit(accountId,amount,description).subscribe({
      next:(data)=>{
        alert("Success Debit");
        this.operationFormGroup.reset();
        this.handelSearchAccount()
      },
      error: (err)=>{
        console.log(err);
      }
    })
  }else if (opertaionType='CREDIT'){
    this.accountService.credit(accountId,amount,description).subscribe({
      next:(data)=>{
        alert("Success Credit");
        this.operationFormGroup.reset();
        this.handelSearchAccount()
      },
      error: (err)=>{
        console.log(err);
      }
    })

  }else if(opertaionType=='TRANSFER'){
    this.accountService.transfer(accountId,accountDestination,amount,description).subscribe({
      next:(data)=>{
        alert("Success Transfer");
        this.operationFormGroup.reset();
        this.handelSearchAccount()
      },
      error: (err)=>{
        console.log(err);
      }
    })
  }

  }
}
