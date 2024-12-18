import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GroupsComponent } from './pages/groups/groups.component';
import { RegisterComponent } from './pages/register/register.component';
import { StartComponent } from './pages/start/start.component';
import { AuthGuard } from './services/keycloak/auth-guard';
import { ModeratorComponent } from './pages/moderator/moderator.component';

const routes: Routes = [
  {
    path: '',
    component: StartComponent,
    data: { name: 'Start' }
  },
  {
    path: 'groups',
    component: GroupsComponent,
    data: { name: 'Gruppen' },
    canActivate: [AuthGuard]
  }
  ,
  {
    path: 'moderator',
    component: ModeratorComponent,
    data: { name: 'Moderator' }
  }
  /*
  {
    path: 'register',
    component: RegisterComponent,
    data: { name: 'Registrieren' }
  }
    */
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
