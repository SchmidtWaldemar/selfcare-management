import { Component } from '@angular/core';
import { ModeratorRequest, ModeratorResponse } from '../../services/moderator/models';
import { ApplicationStarterService } from '../../services/moderator/services';

@Component({
  selector: 'app-moderator',
  templateUrl: './moderator.component.html',
  styleUrl: './moderator.component.scss'
})
export class ModeratorComponent {
  moderatorResponse: ModeratorResponse = {};
  moderatorRequest: ModeratorRequest = {firstName: '', lastName: ''}

  errorMsg: Array<string> = [];
  success : boolean = false;

  constructor(private service: ApplicationStarterService) {}

  createModerator() {
    this.errorMsg = [];
    this.success = false;
    this.service.apiModeratorsCreatePost({
      body: this.moderatorRequest
    }).subscribe({
      next: (moderator : ModeratorResponse) => {
          this.moderatorResponse = moderator;
          this.success = true; 
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