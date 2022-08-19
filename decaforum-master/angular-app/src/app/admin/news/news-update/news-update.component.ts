import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { News } from '../../../models/news.model';
import { NewsService } from '../../../services/news.service';

@Component({
  selector: 'app-news-update',
  templateUrl: './news-update.component.html',
  styleUrls: ['./news-update.component.css']
})
export class NewsUpdateComponent implements OnInit {

  constructor(
    private newsService: NewsService,
    private router: Router,
    private activatedRoute: ActivatedRoute,

  ) { }


  fullTitle = '';
  contentHtml = '';
  newsPost: News;
  newPost;

  message: string;

  ngOnInit() {

    // Get the information that we want to edit
    // This method knows what post is interesting/selected
    this.activatedRoute.params.subscribe(params => {
      this.newsService.getNewsPost(params.id).subscribe(
        postData => {
          this.newsPost = postData;
          console.log(postData);
          this.contentHtml = this.newsPost.content.replace(/<br \/>/gi, '\n');
        },
        error => {
          console.log('There was an error getting the post');
          console.log(error);
        }
      );
    });
  }

  updateNewsPost(form: NgForm) {

    // To keep the formatting.
    const contentInput: string = form.value.enteredContent;
    const inputAsHtml: string = contentInput.replace(/\n/g, '<br />');

    this.newPost = {
      _id: this.newsPost._id,
      content: inputAsHtml,
    };
    this.newsService.updateNews(this.newPost).subscribe(
      data => {
        this.router.navigate(['/']);
      },
      error => {
        this.message = error.message;
      }
    );
    console.log('Message from the edit server request ' + this.message);
  }


}
