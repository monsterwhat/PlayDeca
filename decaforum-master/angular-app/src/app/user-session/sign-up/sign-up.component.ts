import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserSessionService } from '../../services/user-session.service';
import { ValidationService } from '../../services/validation.service';
import { invalid } from '@angular/compiler/src/render3/view/util';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  constructor(
    private userSessionService: UserSessionService,
    private snackBar: MatSnackBar,
    private router: Router,
    private validationService: ValidationService,

  ) { }

  password: string;
  confirmPassword: string;
  warning = '';

  user = {
    username: '',
    email: '',
    password: ''
  };

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    this.password = form.value.enteredPassword;
    this.confirmPassword = form.value.enteredPassword2;

    if (this.password !== this.confirmPassword) {
      this.openSnackBar('Mismatched password. Make sure they are the same', 'Close');
      return;
    }
    if (!form.valid) {
      this.openSnackBar('Form error, check username and email', 'Close');
      return;
    }
    this.user = {
      username: form.value.enteredUsername,
      email: form.value.enteredEmail,
      password: form.value.enteredPassword
    };

    if ( this.validateSubmisson(this.user)) {
      console.log('Sent as valid');
      this.userSessionService.registerRequest(this.user).subscribe(
        data => {
          this.router.navigate(['/confirm']);
        },
        error => {
          this.openSnackBar('This user is already registered.', 'Close');
          console.log(error);
        }
      );
    } else {
      console.log('Failed Validation');
    }

  }

  validateSubmisson(information): boolean {
    this.warning = '';

    let valid = true;
    if (!this.validationService.matchUsername(information.username)) {
      this.warning = this.validationService.warningUsername();
      valid = false;
    }
    if (!this.validationService.matchEmail(information.email)) {
      this.warning = this.validationService.warningEmail();
      valid = false;
    }
    if (!this.validationService.matchPassword(information.password)) {
      this.warning = this.validationService.warningPassword();
      valid = false;
    }
    return valid;
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 5000,
    });
  }
}
