import { Component, OnInit } from '@angular/core';
import { News } from '../../models/news.model';
import { NewsService } from '../../services/news.service';
import { AdminSessionService } from '../../services/admin-session.service';

@Component({
  selector: 'app-news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.css']
})
export class NewsListComponent implements OnInit {

  allNews: News[] = [];
  adminAccess = false;

  constructor(
    private newsService: NewsService,
    private adminSessionService: AdminSessionService,

  ) { }


  ngOnInit() {
    this.getNews();
  }

  getNews() {

    this.newsService.getAllNews().subscribe(
      data => {
        this.allNews = data;

        for (const news of this.allNews) {
          // Localise and display date nicely
          news.date = new Date(news.date);
        }
      },
      error => {
        console.log('News failed to load');
      }
    );


  }



}
