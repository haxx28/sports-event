import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { SponsorRegisterComponent } from './components/sponsor-register/sponsor-register.component';
import { SponsorRequestComponent } from './components/sponsor-request/sponsor-request.component';
import { SponsorStatusComponent } from './components/sponsor-status/sponsor-status.component';
import { EventMasterComponent } from './components/event-master/event-master.component';
import { SponsorshipService } from './services/sponsorship.service';

@NgModule({
  declarations: [
    AppComponent,
    SponsorRegisterComponent,
    SponsorRequestComponent,
    SponsorStatusComponent,
    EventMasterComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    FormsModule
  ],
  providers: [SponsorshipService],
  bootstrap: [AppComponent]
})
export class AppModule { }