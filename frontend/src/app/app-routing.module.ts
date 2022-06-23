import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from "./customer/customer.component"
import {AccountComponent} from "./account/account.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {CustomerAccountsComponent} from "./customer-accounts/customer-accounts.component";

const routes: Routes = [
  { path: 'customers', component: CustomerComponent },
  { path: 'accounts', component: AccountComponent },
  { path: 'new-customers', component:  NewCustomerComponent},
  { path: 'customer-accounts/:id', component:  CustomerAccountsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
