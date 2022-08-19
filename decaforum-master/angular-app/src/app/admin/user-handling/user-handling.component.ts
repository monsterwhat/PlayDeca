import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user.model';
import { UserSessionService } from '../../services/user-session.service';
import { UserHandlingService } from '../../services/user-handling.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-handling',
  templateUrl: './user-handling.component.html',
  styleUrls: ['./user-handling.component.css']
})
export class UserHandlingComponent implements OnInit {

  constructor(
    private userService: UserSessionService,
    private userHandling: UserHandlingService,
    private snackBar: MatSnackBar,
  ) { }

  userList: User[] = [];
  bannedUserList: User[] = [];

  ngOnInit() {

    // Filled the Lists
    this.userHandling.getUserList().subscribe(
      data => {
        this.userList = data.allUsers;
      },
      error => {
        this.openSnackBar(error.message);
      }
    );

    this.userHandling.getBannedUserList().subscribe(
      data => {
        this.bannedUserList = data.allUsers;
      },
      error => {
        this.openSnackBar(error.message);
      }
    );


  }

  banUser(user: User) {
    this.userHandling.banUser(user._id).subscribe(
      data => {
        this.openSnackBar(data.message);
      },
      error => {
        this.openSnackBar(error.message);
      }
    );
  }

  unbanUser(user: User) {
    this.userHandling.unbanUser(user._id).subscribe(
      data => {
        this.openSnackBar(data.message);
      },
      error => {
        this.openSnackBar(error.message);
      }
    );
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 9000,
    });
  }



}
