import { Component, OnInit } from '@angular/core';
import { ForumPost } from '../../models/forum.model';
import { ForumService } from '../../services/forum.service';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { UserSessionService } from '../../services/user-session.service';
import { Vote } from '../../models/vote.model';
import { VoteService } from '../../services/vote.service';
import { AdminSessionService } from '../../services/admin-session.service';

@Component({
  selector: 'app-show-post',
  templateUrl: './show-post.component.html',
  styleUrls: ['./show-post.component.css']
})
export class ShowPostComponent implements OnInit {

  constructor(
    private forumService: ForumService,
    private userService: UserSessionService,
    private adminService: AdminSessionService,
    private voteService: VoteService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private snackBar: MatSnackBar,
  ) { }

  commentButtonClicked = false;
  message: string;
  wasDeleted = false;
  forumPost: ForumPost;
  postAuthor: string;
  voteStatus: Vote;

  ngOnInit() {
    let postId;

    this.activatedRoute.params.subscribe(params => {
      postId = params.id;
    });

    this.getPost(postId);

    this.voteStatus = {
      author_id: null,
      forum_id: postId,
      voted_up: false,
      voted_down: false,
    };

    this.userService.checkUser().subscribe(
      user => {
        this.userService.currentUser = user;
        this.voteStatus = this.checkVoteStatus(postId);
      },
      error => {
        console.log(error.message);
        this.userService.currentUser = null;
      }
    );

    this.adminService.role().subscribe(
      data => {
        this.adminService.loggedIn = data.admin;
      },
      error => {
        this.adminService.loggedIn = false;
      }
    );

  }

  // VOTING SECTION
  upVote(alreadyVotedUp: boolean, alreadyVotedDown: boolean) {

    // Check if user voted up before.
    // if voted down, make the vote up
    // if unvoted, make the vote up
    // if voted up, make the vote unvoted
    this.voteService.increaseForumVote(this.forumPost._id, alreadyVotedUp).subscribe(
      data => {
        console.log('I voted up');
      },
      error => {
        console.log('error voting');
      }
    );

    this.voteStatus.voted_up = !alreadyVotedUp;
    (alreadyVotedUp) ? this.forumPost.vote_count -= 1 : this.forumPost.vote_count += 1;

    if (alreadyVotedDown) {
      this.forumPost.vote_count += 1;
      this.voteStatus.voted_down = false;
    }

  }

  downVote(alreadyVotedUp: boolean, alreadyVotedDown: boolean) {
    // Check if user voted down before.
    // if voted up, make the vote down
    // if unvoted, make the vote down
    // if voted down, make the vote unvoted
    this.voteService.decreaseForumVote(this.forumPost._id, alreadyVotedDown).subscribe(
      data => {
        console.log('I voted down');
      },
      error => {
        console.log('error voting');
      }
    );

    this.voteStatus.voted_down = !alreadyVotedDown;
    (alreadyVotedDown) ? this.forumPost.vote_count += 1 : this.forumPost.vote_count -= 1;

    if (alreadyVotedUp) {
      this.forumPost.vote_count -= 1;
      this.voteStatus.voted_up = false;
    }
  }


  checkVoteStatus(postId): Vote {
    return this.voteService.getUserForumVoteStatus(postId);

  }

  // POST SECTION
  deletePost(post: ForumPost) {
    this.forumService.deleteForumPost(post._id).subscribe(
      data => {
        if (data.sent) {
          this.wasDeleted = true;
          post.title = 'Deleted';
          post.content = data.message;
        } else {
          this.openSnackBar(data.message, 'Okay');
        }
      },
      error => {
        this.message = error.message;
      }
    );
    setTimeout(() => {
      this.router.navigate(['/forums/']);
    }, 2000);
  }

  getPost(idOfThePost) {
    this.forumService.showForumPost(idOfThePost).subscribe(
      data => {
        // Loads the right post in the service.
        this.forumService.interestedPost = data;
        // Can also use
        // this.forumPost = this.forumService.interestedPost();
        this.forumPost = data;

        this.forumPost.date_published = new Date(data.date_published);

        this.userService.getUsernameFromID(this.forumPost.author).subscribe(
          userdata => {
            this.forumPost.authorname = userdata.user.username;
          },
          error => {
            this.openSnackBar('Error getting username', 'Close');
            this.forumPost.authorname = error.error.message;
          }
        );
      },
      error => {
        console.log('There was an error getting the post');
        console.log(error);
      }
    );
  }

  showPostControls(): boolean {
    if (this.userService.currentUser && this.forumService.interestedPost) {
      if (this.userService.currentUser._id === this.forumService.interestedPost.author) {
        return true;
      }
      if (this.adminService.loggedIn) {
        return true;
      }
    }
    return false;
  }

  checkSignedOut(): boolean {
    return !this.userService.isUserSignedIn;
  }

  // COMMENT SECTION
  createComment(postId: number, form: NgForm) {

    if (this.userService.currentUser) {

      this.commentButtonClicked = true;

      const commentItem = {
        content: form.value.enteredComment,
        date_published: new Date(),
      };

      this.forumService.addReplyToForumPost(postId, commentItem).subscribe(
        data => {
          this.openSnackBar(data.message, 'Close');
          this.reloadPage();
        },
        error => {
          // Make a message
          this.openSnackBar(error.message, 'Close');
        }
      );
    } else {
      this.openSnackBar('You need to log in to post a comment', 'Close');
    }
  }

  // MISC
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  reloadPage() {
    setTimeout(() => {
      location.reload();
    }, 3000);
  }

}
