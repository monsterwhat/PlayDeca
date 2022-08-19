import { Component, OnInit } from '@angular/core';
import { UserSessionService } from '../../services/user-session.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { ValidationService } from '../../services/validation.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  constructor(
    private userService: UserSessionService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private validationService: ValidationService,

  ) { }

  id: number;
  success: string;
  displayWarning: string;
  username: string;

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.id = params.id;
    });

    this.userService.getUsernameFromID(this.id).subscribe(
      data => {
        this.username = data.user.username;
      },
      error => {
        console.log(error);
      }
    );

  }

  sendNewPassword(form: NgForm) {

    if (form.value.enteredPassword1 === form.value.enteredPassword2 && form.value.enteredPassword1 !== '') {
      if (this.validateSubmission(form.value.enteredPassword1)) {

        this.userService.resetPassword(this.id, form.value.enteredTempCode, form.value.enteredPassword1).subscribe(
          data => {
            if (data.accepted) {
              this.success = 'Password reset successfully. Redirecting to log in';
              this.displayError('');
              setTimeout(() => {
                this.router.navigate(['/sign-in']);
              }, 4000);
            } else {
              this.displayError(data.message);
            }
          },
          error => {
            this.displayError(error.error.message);
          }
        );
      }

    } else {
      this.displayError('Passwords don\'t match');
    }
  }

  validateSubmission(information): boolean {

    if (!this.validationService.matchPassword(information)) {
      this.displayError(this.validationService.warningPassword());
      return false;
    }
    return true;
  }

  displayError(message: string) {
    this.displayWarning = message;
  }

}
