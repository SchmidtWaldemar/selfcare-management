import { Component, OnInit } from '@angular/core';
import { GroupRequest, GroupResponse, MemberRequest, MemberResponse } from '../../services/group/models';
import { GroupControllerService } from '../../services/group/services';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrl: './groups.component.scss'
})
export class GroupsComponent implements OnInit {

  groupResponse: GroupResponse = {};
  groupResponseList: Array<GroupResponse> = [];
  memberResponse: MemberResponse = {};

  groupRequest: GroupRequest = {id: 0, name: ''}
  memberRequest: MemberRequest = {clientNumber: "", nickname: "", groupId: 0}

  errorMsg: Array<string> = [];
  success : boolean = false;
  successMember : boolean = false;

  constructor(private service: GroupControllerService) {}

  async ngOnInit(): Promise<void> {
    this.service.findAll().subscribe({
      next: (groups: Array<GroupResponse>) => {
        this.groupResponseList = groups;
      }
    });
  }

  setAsMember(event : any) {
    this.successMember = false; 
    this.success = false;
    let nickname = "derGroeste";
    let clientId = localStorage.getItem("clientId");
    if (!clientId) {
      this.errorMsg.push("Sie sind noch nicht registriert!");
      return;
    }
    
    this.memberRequest.groupId = event.currentTarget.value;
    this.memberRequest.clientNumber = clientId;
    this.memberRequest.nickname = nickname;

    this.service.createMembership({
      body: this.memberRequest
    }).subscribe({
      next: (member : MemberResponse) => {
        this.memberResponse = member;
        this.successMember = true;
        event.currentTarget.disable = true;
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

  createGroup() {
    this.errorMsg = [];
    this.success = false;
    this.service.createGroup({
      body: this.groupRequest
    }).subscribe({
      next: (group : GroupResponse) => {
          this.groupResponseList.push(group);
          this.groupRequest.name = '';
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
