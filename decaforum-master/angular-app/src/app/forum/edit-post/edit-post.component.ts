import { Component, OnInit } from '@angular/core';
import { ForumService } from '../../services/forum.service';
import { ForumPost } from '../../models/forum.model';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { UserSessionService } from '../../services/user-session.service';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {

  constructor(
    private forumService: ForumService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private userService: UserSessionService,

  ) { }

  fullTitle = '';
  contentHtml = '';
  forumPost: ForumPost;
  newPost;

  message: string;

  ngOnInit() {
    // Get the information that we want to edit
    // This method knows what post is interesting/selected
    this.activatedRoute.params.subscribe(params => {
      this.forumService.showForumPost(params.id).subscribe(
        postData => {
          this.forumPost = postData;
          this.userService.getUsernameFromID(this.forumPost.author).subscribe(
            data => {
              this.forumPost.authorname = data.user.username;
            },
            error => {
              console.log('Error');
            }
          );
          this.contentHtml = this.forumPost.content.replace(/<br \/>/gi, '\n');
        },
        error => {
          console.log('There was an error getting the post');
          console.log(error);
        }
      );
    });
  }

  publishNewInformation(form: NgForm) {
    const contentInput: string = form.value.enteredContent;
    const inputAsHtml: string = contentInput.replace(/\n/g, '<br />');

    this.newPost = {
      _id: this.forumPost._id,
      content: inputAsHtml,
    };
    this.forumService.editForumPost(this.newPost).subscribe(
      data => {
        this.message = data.message;
        this.router.navigate(['/forums/' + this.forumPost._id]);
      },
      error => {
        this.message = error.message;
      }
    );
    console.log('Message from the edit server request ' + this.message);
  }

}
