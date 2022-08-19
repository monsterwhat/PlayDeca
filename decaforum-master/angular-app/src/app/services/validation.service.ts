import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor() { }

  private _usernameRegExp = /^[a-zA-Z0-9_-]{3,16}$/i;
  // Upper, lower, digit, spec char, min=8;
  private _passwordRegExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$/;

  // tslint:disable-next-line: max-line-length
  private _emailRegExp: RegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;


  public matchUsername(username: string): boolean {
    return this.match(this._usernameRegExp, username);
  }

  public matchPassword(password: string): boolean {
    return this.match(this._passwordRegExp, password);
  }

  public matchEmail(email: string): boolean {
    return this.match(this._emailRegExp, email);
  }

  private match(regex, testString): boolean {
    return regex.test(testString);
  }

  public warningUsername(): string {
    return 'Incorrect username format.' + ' Name must be between 3 and 16 characters.' + 'Please use only the following: ' +
      'English uppercase and lowercase chaacters, numbers, _ and -';
  }

  public warningEmail(): string {
    return 'Incorrect email format, please check and try again';
  }

  public warningPassword(): string {
    return 'Incorrect password format, please ensure you password contains at least:\n' +
        'One uppercase character, one lowercase character, a number' +
        ' and the password is at minimum 8 character in length';

  }
}
