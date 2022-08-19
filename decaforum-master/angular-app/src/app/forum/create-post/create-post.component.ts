import { Component, OnInit } from '@angular/core';
import { ForumService } from '../../services/forum.service';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserSessionService } from '../../services/user-session.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  constructor(
    private forumService: ForumService,
    private router: Router,
    private userService: UserSessionService,
    private snackBar: MatSnackBar,
  ) { }

  newPost;
  contentHtml = '';
  fullTitle = '';
  successfullyPosted: boolean;
  currentUser;
  userMessage: string;

  ngOnInit() {
    this.userService.checkUser().subscribe(user => {
      this.currentUser = user;
    });
  }

  submitPost(form: NgForm) {

    if (this.currentUser) {

      const contentInput: string = form.value.enteredContent;
      this.contentHtml = contentInput.replace(/\n/g, '<br />');

      this.fullTitle = form.value.enteredTitle;

      this.newPost = {
        title: this.fullTitle,
        content: this.contentHtml,
        date_published: new Date(),
      };

      this.forumService.addNewForumPost(this.newPost).subscribe(
        data => {
          // Send a message to the user saying that the message was a success.
          console.log(data);
          this.successfullyPosted = data.sent;
          this.sendMessageForSuccessOrFailure(data.message);
        },
        error => {
          // Send a message that it failed
          console.log(error);
          this.successfullyPosted = error.sent;
          this.sendMessageForSuccessOrFailure(error.message);
        }
      );
      this.redirectUserToList();

    } else {
      this.successfullyPosted = false;
      this.sendMessageForSuccessOrFailure('You need to sign in');
    }
  }

  sendMessageForSuccessOrFailure(message: string) {
    console.log(message);
    this.openSnackBar(message, 'Okay');
  }

  getCurrentUser() {
    console.log(this.currentUser);
    return this.currentUser._id;
  }

  redirectUserToList() {
    setTimeout(() => {
      this.router.navigate(['/forums']);
    }, 3000);
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 3000,
    });
  }

}
