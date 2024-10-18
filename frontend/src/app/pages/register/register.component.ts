import { Component } from '@angular/core';
import { ParticipantRequest, ParticipantResponse } from '../../services/participant/models';
import { ParticipantControllerService } from '../../services/participant/services';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  participantResponse: ParticipantResponse = {};
  errorMsg: Array<string> = [];

  request: ParticipantRequest = {email: ''}

  success : boolean = false;  

  constructor(private service: ParticipantControllerService) {}

  async ngOnInit(): Promise<void> {}

  createParticipant() {
    this.success = false;
    this.errorMsg = [];
    this.participantResponse = {};

    this.service.register({
      body: this.request
    }).subscribe({
      next: (group : ParticipantResponse) => {
        this.participantResponse = group;
        this.request.email = '';
        this.success = true; 
        localStorage.setItem("clientId", group.id!.toString());
      },
      error: (err) => {
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        }
        else if (err.error.error) {
          this.errorMsg.push(err.error.error);
        }
        else {
          this.errorMsg.push(err.error);
        }
      }
    });
  }
}
