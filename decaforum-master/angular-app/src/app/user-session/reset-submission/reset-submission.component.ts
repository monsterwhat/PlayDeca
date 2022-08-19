import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserSessionService } from '../../services/user-session.service';
import { ValidationService } from '../../services/validation.service';

@Component({
  selector: 'app-reset-submission',
  templateUrl: './reset-submission.component.html',
  styleUrls: ['./reset-submission.component.css']
})
export class ResetSubmissionComponent implements OnInit {

  constructor(
    private userService: UserSessionService,
    private validationService: ValidationService,

  ) { }

  warning = '';
  information = '';
  disabled = false;

  ngOnInit() {
  }

  async onSubmit(form: NgForm) {

    const email = form.value.enteredEmail;
    if (form.invalid) {
      this.displayWarning('Please enter your email address ');

    } else if (!this.validationService.matchEmail(email)) {
      this.displayWarning('This email address isn\'t correctly formatted.Please check it and try again.');

    } else {
      this.userService.checkEmail(email).subscribe(
        (data) => {

          if (data.found) {
            this.userService.resetSubmission(email).subscribe(
              result => {
                this.displaySuccess(email);
              },
              err => {
                this.displayWarning(err.reply);
                // Email might not be found. Email might have tried already. Email might have other problems
              }
            );
          } else {
            this.displayWarning('Cannot find an account with that email address, are you sure its correct?');
          }
        },
        error => {
          console.log(error);
        }
      );

      this.disabled = true;
    }
  }

  displayWarning(message: string) {
    this.warning = message;
  }

  displaySuccess(email: string) {
    this.information = 'An email has been sent to ' + email + ' with a link you change your password.';
  }

}
