import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BackendConnectionService } from './backend-connection.service';

@Injectable({
  providedIn: 'root'
})
export class UserHandlingService {

  constructor(
    private http: HttpClient,
    private backend: BackendConnectionService,

  ) { }

  listAllUsersUrl = this.backend.getFullAddress() + '/api/users/all-users';
  listBannedUsersUrl = this.backend.getFullAddress() + '/api/admin/all-banned-users';
  banUserUrl = this.backend.getFullAddress() + '/api/admin/ban-user';
  unbanUserUrl = this.backend.getFullAddress() + '/api/admin/unban-user';

  banUser(userId: number): Observable<any> {
    const ban = { _id: userId };
    return this.http.post(this.banUserUrl, { ban }, this.backend.getHttpOptions());
  }

  unbanUser(userId: number): Observable<any> {
    const unban = { _id: userId };
    return this.http.post(this.unbanUserUrl, { unban }, this.backend.getHttpOptions());
  }

  getUserList(): Observable<any> {
    return this.http.get(this.listAllUsersUrl, this.backend.getHttpOptions());
  }

  getBannedUserList(): Observable<any> {
    return this.http.get(this.listBannedUsersUrl, this.backend.getHttpOptions());
  }




}
