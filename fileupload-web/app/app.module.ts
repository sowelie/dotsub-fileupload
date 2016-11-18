import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { FileService } from './file.service';

import './rxjs-extensions';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule
    ],
    declarations: [
        AppComponent
    ],
    bootstrap: [ AppComponent ],
    providers: [ FileService ],
})
export class AppModule { }
