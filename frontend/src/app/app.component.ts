import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
//import { KeycloakService } from './services/keycloak/keycloak.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'frontend';

  constructor(private keycloakService: KeycloakService) {}

  async logout() {
    await this.keycloakService.logout();
  }
}
