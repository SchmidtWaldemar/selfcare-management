import { Injectable } from '@angular/core';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { ClientProfile } from '../../models/clientProfile';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard extends KeycloakAuthGuard {
  
  private client: ClientProfile | undefined;

  constructor(protected override readonly router: Router,
    private readonly keycloak: KeycloakService
  ) {
    super(router, keycloak)
  }

  async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> {
      if (!this.authenticated) {
        await this.keycloak.login({
            redirectUri: window.location.origin + state.url
        })
      } else {
        localStorage.setItem('token', await this.keycloak.getToken());
        
        this.client = (await this.keycloak.loadUserProfile()) as ClientProfile;
        localStorage.setItem('client', JSON.stringify(this.client));
      }
      return this.authenticated;
  }
}