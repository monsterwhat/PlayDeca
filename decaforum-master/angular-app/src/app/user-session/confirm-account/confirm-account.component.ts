import { Component, OnInit } from '@angular/core';
import { UserSessionService } from '../../services/user-session.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['./confirm-account.component.css']
})
export class ConfirmAccountComponent implements OnInit {

  constructor(
    private userService: UserSessionService,
    private router: Router,
    private activatedRoute: ActivatedRoute,

  ) { }

  message = '';
  confirmed: boolean;

  ngOnInit() {

    this.activatedRoute.params.subscribe(params => {
      this.sendImportantInformation(params.id);
    });
  }

  // Thinking of adding a username into the link to make it look more authentic.
  sendImportantInformation(id: number/*, username:string */ ) {
    this.userService.confirmUser(id).subscribe(
      data => {
        // Data.sent is a boolean response
        this.confirmed = data.accepted;
        if (data.accepted) {
          console.log(data.reply);
          // Questionable for UX
          setTimeout(() => {
              this.router.navigate(['/sign-in']);
          }, 5000);
        } else {
          this.displayError(data.reply);
        }
      },
      error => {
        this.displayError('Problem confirming data with server.');
      }
    );
  }

  displayError(message: string) {
    this.message = message;
  }


}
