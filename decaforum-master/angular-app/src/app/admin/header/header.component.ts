import { Component, OnInit } from '@angular/core';
import { UserSessionService } from '../../services/user-session.service';

@Component({
  selector: 'app-admin-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class AdminHeaderComponent implements OnInit {

  constructor(
    private userService: UserSessionService,

  ) { }

  ngOnInit() {
  }

  logout() {
    this.userService.logout().subscribe(
      data => {
        location.reload();
        this.userService.isUserSignedIn = false;
        console.log(data.message);
      },
      error => {
        console.log(error);
      }
    );
  }
}
