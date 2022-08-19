import { Injectable } from '@angular/core';
import { ForumPost } from '../models/forum.model';
import { ForumComment } from '../models/comment.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { BackendConnectionService } from './backend-connection.service';

@Injectable({
  providedIn: 'root'
})
export class ForumService {

  // Hold the ID of the Post that we want to checkout if the user is on 'edit' or 'show'
  private _interestedPost: ForumPost;

  constructor(
    private http: HttpClient,
    private backend: BackendConnectionService,
  ) { }

  // URLs
  private newPostUrl = this.backend.getFullAddress() + '/api/forum/create';
  private editPostUrl = this.backend.getFullAddress() + '/api/forum/edit-post';
  private deletePostUrl = this.backend.getFullAddress() + '/api/forum/delete-post';
  private listPostUrl = this.backend.getFullAddress() + '/api/forum/list';
  private listACommentUrl = this.backend.getFullAddress() + '/api/forum/get-reply';
  private listCommentsUrl = this.backend.getFullAddress() + '/api/forum/get-replies';
  private deletePostReplyUrl = this.backend.getFullAddress() + '/api/forum/delete-reply';
  private addReplyUrl = this.backend.getFullAddress() + '/api/forum/add-reply';
  private editCommentUrl = this.backend.getFullAddress() + '/api/forum/edit-reply';
  private showPostUrl = this.backend.getFullAddress() + '/api/forum/get-post';


  addNewForumPost(forumPost: ForumPost): Observable<any> {
    return this.http.post(this.newPostUrl, forumPost, this.backend.getHttpOptions());
  }

  deleteForumPost(id: number): Observable<any> {
    return this.http.post<any>(this.deletePostUrl, { _id: id }, this.backend.getHttpOptions());
  }

  editForumPost(forumPost: ForumPost): Observable<any> {
    return this.http.post(this.editPostUrl, forumPost, this.backend.getHttpOptions());
  }

  addReplyToForumPost(postId: number, comment): Observable<any> {
    return this.http.post(this.addReplyUrl, { postId, comment }, this.backend.getHttpOptions());
  }

  removeReplyFromForumPost(postId, commentId): Observable<any> {
    // Admin and author of reply only
    const comment = { _id: commentId };
    return this.http.post(this.deletePostReplyUrl, { postId, comment }, this.backend.getHttpOptions());
  }

  editReplyOfForumPost(comment: ForumComment): Observable<any> {
    // Observable will have a message for success or failure and maybe something else
    return this.http.post<any>(this.editCommentUrl, { comment }, this.backend.getHttpOptions());
  }

  listAComment(commentId): Observable<any> {
    return this.http.post<any>(this.listACommentUrl, { _id: commentId }, this.backend.getHttpOptions());
  }

  listAllComments(commentIdArray): Observable<any> {
    return this.http.post<any>(this.listCommentsUrl, { ids: commentIdArray }, this.backend.getHttpOptions());
  }

  listAllForumPost(): Observable<any> {
    // TODO: Invert this list. Currently getting newest last
    return this.listSomeForumPosts(100);
  }
  // TODO: Make a post request with the amount we want
  listSomeForumPosts(amountToList): Observable<any> {
    return this.http.get<any>(this.listPostUrl, this.backend.getHttpOptions());
  }

  showForumPost(postId): Observable<any> {
    // If the post is clicked it will show just the post and the details like the reply

    return this.http.post<any>(this.showPostUrl, { _id: postId }, this.backend.getHttpOptions());
  }

  public get interestedPost(): ForumPost {
    return this._interestedPost;
  }
  public set interestedPost(value: ForumPost) {
    this._interestedPost = value;
  }
}
