import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SponsorshipComponent } from './components/sponsorship/sponsorship.component'; // Import the component

const routes: Routes = [
  { path: '', component: SponsorshipComponent } // Set SponsorshipComponent as the only route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
