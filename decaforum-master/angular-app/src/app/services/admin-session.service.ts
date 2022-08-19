import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BackendConnectionService } from './backend-connection.service';

@Injectable({
  providedIn: 'root'
})
export class AdminSessionService {

  constructor(
    private http: HttpClient,
    private backend: BackendConnectionService,

  ) { }

  signInUrl = this.backend.getFullAddress() + '/api/admin/login';
  signOutUrl = this.backend.getFullAddress() + '/api/admin/logout';
  userUrl = this.backend.getFullAddress() + '/api/admin/user';
  registerUrl = this.backend.getFullAddress() + '/api/users/default-admin';
  roleUrl = this.backend.getFullAddress() + '/api/admin/role';
  checkUrl = this.backend.getFullAddress() + '/api/admin/check';

  private _loggedIn: boolean;
  public get loggedIn() {
    return this._loggedIn;
  }
  public set loggedIn(value) {
    this._loggedIn = value;
  }

  register(): Observable<any> {
    // User is null
    return this.http.post<any>(this.registerUrl, this.backend.getHttpOptions());
  }

  role(): Observable<any> {
    // User is null
    return this.http.get<any>(this.roleUrl, this.backend.getHttpOptions());
  }

  // Checks if current user is admin
  check(): Observable<any> {
    return this.http.get<any>(this.checkUrl, this.backend.getHttpOptions());
  }

  checkUser(): Observable<any> {
    return this.http.get<any>(this.userUrl);
  }

}
