import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GroupsComponent } from './pages/groups/groups.component';
import { RegisterComponent } from './pages/register/register.component';
import { HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { StartComponent } from './pages/start/start.component';
import { NavbarComponent } from './navbar/navbar.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { TokenInterceptor } from './interceptor/token-interceptor';
import { ModeratorComponent } from './pages/moderator/moderator.component';

export function initializeKeycloak(keycloakService: KeycloakService) {
  return () => keycloakService.init({
    config: {
      //url: 'http://localhost:7080',
      url: 'http://192.168.178.157:7080',
      realm: 'selfcare-management',
      clientId: 'selfcare'
    },
    initOptions: {
      onLoad: 'check-sso',
      //onLoad: 'login-required',
      checkLoginIframe: false,
      silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
    },
    bearerExcludedUrls: ['', 'assets/']
  });
}

@NgModule({
  declarations: [
    AppComponent,
    GroupsComponent,
    RegisterComponent,
    StartComponent,
    NavbarComponent,
    ModeratorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    KeycloakAngularModule,
  ],
  providers: [
    provideHttpClient(),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      deps: [KeycloakService],
      useFactory: initializeKeycloak,
      multi: true
    },
    KeycloakService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
