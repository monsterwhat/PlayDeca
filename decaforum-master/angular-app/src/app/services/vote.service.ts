import { Injectable } from '@angular/core';
import { UserSessionService } from './user-session.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vote } from '../models/vote.model';
import { BackendConnectionService } from './backend-connection.service';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  constructor(
    private userService: UserSessionService,
    private http: HttpClient,
    private backend: BackendConnectionService,
  ) { }

  private userVoteInfoUrl: string = this.backend.getFullAddress() + '/api/forum/user-voting-info';
  private incForumVoteUrl: string = this.backend.getFullAddress() + '/api/forum/inc-forum-vote';
  private decForumVoteUrl: string = this.backend.getFullAddress() + '/api/forum/dec-forum-vote';


  private getForumVoteStatusFromBackend(forumId: number): Observable<any> {
    const forum = { _id: forumId }; // User is from the JWT.
    return this.http.post<any>(this.userVoteInfoUrl, { forum }, this.backend.getHttpOptions());
  }

  increaseForumVote(forumId: number, alreadyVoted: boolean) {
    const forum = { _id: forumId };
    return this.http.post<any>(this.incForumVoteUrl, { forum, voted: alreadyVoted }, this.backend.getHttpOptions());
  }

  decreaseForumVote(forumId: number, alreadyVoted: boolean) {
    const forum = { _id: forumId };
    return this.http.post<any>(this.decForumVoteUrl, { forum, voted: alreadyVoted }, this.backend.getHttpOptions());
  }

  getUserForumVoteStatus(forumId: number): Vote {
    let currentVoteStatus: Vote;

    // Sets default position
    currentVoteStatus = {
      forum_id: forumId,
      voted_up: false,
      voted_down: false,
      author_id: 0,
    };

    if (this.userService.currentUser) {

      currentVoteStatus.author_id = this.userService.currentUser._id;

      this.getForumVoteStatusFromBackend(forumId).subscribe(
        data => {
          if (data.vote.voted_up != null) {
            currentVoteStatus.voted_up = data.vote.voted_up;
          }

          if (data.vote.voted_down != null) {
            currentVoteStatus.voted_down = data.vote.voted_down;
          }
        },
        error => {
          console.log(error);
        }
      );
    }

    return currentVoteStatus;
  }


}
