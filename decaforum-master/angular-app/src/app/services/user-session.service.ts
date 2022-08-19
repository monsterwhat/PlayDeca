import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { BackendConnectionService } from './backend-connection.service';

@Injectable({
  providedIn: 'root'
})
export class UserSessionService {

  constructor(
    private http: HttpClient,
    private backend: BackendConnectionService,

  ) { }

  private _isUserSignedIn = false;
  private _user: User;

  private signInUrl = this.backend.getFullAddress() + '/api/users/login';
  private registerUrl = this.backend.getFullAddress() + '/api/users/register';
  private userUrl = this.backend.getFullAddress() + '/api/users/user';
  private userInfoUrl = this.backend.getFullAddress() + '/api/users/user-info';
  private checkForEmailUrl = this.backend.getFullAddress() + '/api/users/check-email';
  private logoutUrl = this.backend.getFullAddress() + '/api/users/logout';
  private allUsersUrl = this.backend.getFullAddress() + '/api/users/all-users';
  private confirmEmailUrl = this.backend.getFullAddress() + '/api/users/confirm-email';
  private resetPasswordUrl = this.backend.getFullAddress() + '/api/users/reset-pass';
  private resetPassSubmissionUrl = this.backend.getFullAddress() + '/api/users/reset-email';

  loginRequest(user): Observable<any> {
    return this.http.post<any>(this.signInUrl, user, this.backend.getHttpOptions());
  }

  registerRequest(body): Observable<any> {
    return this.http.post(this.registerUrl, body, this.backend.getHttpOptions());
  }

  getUsernameFromID(id): Observable<any> {
    return this.http.post<any>(this.userInfoUrl, { id }, this.backend.getHttpOptions());
  }

  // All Info
  checkUser(): Observable<any> {
    return this.http.get<any>(this.userUrl, this.backend.getHttpOptions());
  }

  logout(): Observable<any> {
    return this.http.get(this.logoutUrl, this.backend.getHttpOptions());
  }

  getAllUsers(): Observable<any> {
    return this.http.get(this.allUsersUrl, this.backend.getHttpOptions());
  }

  checkEmail(email: string): Observable<any> {
    const payload = { email };
    return this.http.post<any>(this.checkForEmailUrl, { payload }, this.backend.getHttpOptions());
  }

  confirmUser(id: number): Observable<any> {
    const payload = { id };
    return this.http.post<any>(this.confirmEmailUrl, { payload }, this.backend.getHttpOptions());
  }

  resetPassword(id: number, information: string, password: string): Observable<any> {
    const payload = { id, information, password };
    return this.http.post<any>(this.resetPasswordUrl, { payload }, this.backend.getHttpOptions());
  }

  // This sends the email address for the request to change the pass
  resetSubmission(email: string): Observable<any> {
    const payload = { email };
    return this.http.post<any>(this.resetPassSubmissionUrl, { payload }, this.backend.getHttpOptions());
  }

  public get isUserSignedIn(): boolean {
    return this._isUserSignedIn;
  }
  public set isUserSignedIn(value: boolean) {
    this._isUserSignedIn = value;
  }

  public get currentUser(): User {
    return this._user;
  }
  public set currentUser(value: User) {
    this._user = value;
  }


}
