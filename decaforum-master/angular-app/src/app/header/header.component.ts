import { Component, OnInit } from '@angular/core';
import { UserSessionService } from '../services/user-session.service';
import { AdminSessionService } from '../services/admin-session.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  user = {
    _id: 0,
    username: '',
  };
  mobileMenu = false;

  constructor(
    private userService: UserSessionService,
    private adminSession: AdminSessionService,
  ) {
  }

  ngOnInit() {
    this.userService.checkUser().subscribe(
      data => {
        this.userService.currentUser = data;
        this.userService.isUserSignedIn = true;
        this.user._id = this.userService.currentUser._id;
        this.user.username = this.userService.currentUser.username;
      },
      error => {
        // This assumes the user is signed-out
        // user.getDefaultUser();
        this.userService.isUserSignedIn = false;
      }
    );

    this.adminSession.role().subscribe(
      data => {
        this.adminSession.loggedIn = data.admin;
      },
      error => {
        this.adminSession.loggedIn = false;
      }
    );
  }

  getIsAdmin(): boolean {
    return this.adminSession.loggedIn;
  }

  getSignedIn(): boolean {
    return this.userService.isUserSignedIn;
  }

  logout() {
    this.userService.logout().subscribe(
      data => {
        location.reload();
        this.userService.isUserSignedIn = false;
        this.adminSession.loggedIn = false;
        console.log(data.message);
      },
      error => {
        console.log(error);
      }
    );
  }

  dynmapLink() {
    // return window.location.protocol + '//' + window.location.hostname + ':8123';
    // This is the actual IP that Dynmap hosts itself on in-game
    return 'http://' + '91.121.91.94' + ':8123';
  }

  toggleMobileMenu() {
    this.mobileMenu = !this.mobileMenu;
  }

}
