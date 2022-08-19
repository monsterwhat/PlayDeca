import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserSessionService } from '../services/user-session.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(
    private userService: UserSessionService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) { }

  user = {
    username: '',
    email: '',
    creation_date: null,
    confirmed: null,
  };
  allUsers: [];

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.getUserProfile(params.id);

    });
    this.getAllUsers();

  }

  getAllUsers() {

    this.userService.getAllUsers().subscribe(
      data => {
        this.allUsers = data.allUsers;
      },
      error => {
        console.log(error);
      }
    );
  }

  getUserProfile(id: number) {

    // Others public info
    this.userService.getUsernameFromID(id).subscribe(
      userData => {
        this.user = userData.user;
      },
      error => {
        console.log(error.error.message);
      }
    );

    this.userService.checkUser().subscribe(
      user => {
        this.userService.currentUser = user;

        // Users own private info
        if (this.userService.currentUser._id === id) {
          this.user = user;
          this.user.creation_date = new Date(this.user.creation_date);

        }
      },
      error => {
        console.log(error);
      }
    );



  }

}
