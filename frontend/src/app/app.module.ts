import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GroupsComponent } from './pages/groups/groups.component';
import { RegisterComponent } from './pages/register/register.component';
import { provideHttpClient} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { StartComponent } from './pages/start/start.component';
import { NavbarComponent } from './navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    GroupsComponent,
    RegisterComponent,
    StartComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
