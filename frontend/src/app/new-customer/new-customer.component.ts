import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validator, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
  newCustemerFromGroup! : FormGroup;


  constructor(private fb : FormBuilder,private custemerService:CustomerService,private router:Router) { }

  ngOnInit(): void {

    this.newCustemerFromGroup= this.fb.group({
      name:this.fb.control(null,[Validators.required,Validators.minLength(4)]),
      email:this.fb.control(null,[Validators.required,Validators.email])

    })
  }

  handelSaveCustomer() {
    let custemer:Customer= this.newCustemerFromGroup.value;
    this.custemerService.saveCustemers(custemer).subscribe({
      next:data=>{
        alert("Customer has been successfully saved !");
       // this.newCustemerFromGroup.reset();
        this.router.navigateByUrl("/customers")
      },
      error:err=>{
        console.log(err);
      }
    });


  }
}
