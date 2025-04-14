import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { HeaderComponent } from './components/header/header.component';

@NgModule({
  declarations: [],
  imports: [CommonModule, MaterialModule, HeaderComponent],
  exports: [HeaderComponent, MaterialModule]
})
export class SharedModule {}