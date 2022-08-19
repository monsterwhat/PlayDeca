import { Component, OnInit } from '@angular/core';
import { News } from '../../../models/news.model';
import { NgForm } from '@angular/forms';
import { NewsService } from '../../../services/news.service';
import { Router } from '@angular/router';
import { UserSessionService } from '../../../services/user-session.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-news-create',
  templateUrl: './news-create.component.html',
  styleUrls: ['./news-create.component.css']
})
export class NewsCreateComponent implements OnInit {

  newsPost: News;
  constructor(
    private newsService: NewsService,
    private router: Router,
    private userService: UserSessionService,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit() {

  }

  onSubmit(form: NgForm) {

    const contentInput: string = form.value.enteredContent;
    const contentHtml = contentInput.replace(/\n/g, '<br />');

    this.newsPost = {
      title: form.value.enteredTitle,
      content: contentHtml,
      author: 'Admin',
      date: new Date(),
    };

    let sent: boolean;
    this.newsService.saveNews(this.newsPost).subscribe(
      data => {
        console.log(data);
        sent = data.sent;
        this.openSnackBar(data.message);
      },
      error => {
        console.log(error);
        sent = error.sent;
        this.openSnackBar(error.message);
      }
    );

    if (sent) {
      // Redirect away from here.
      this.router.navigate(['/']);
    }
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 2000,
    });
  }

}
