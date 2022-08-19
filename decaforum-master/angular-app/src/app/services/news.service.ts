import { Injectable } from '@angular/core';
import { News } from '../models/news.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { BackendConnectionService } from './backend-connection.service';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(
    private http: HttpClient,
    private backend: BackendConnectionService,

  ) { }

  private getNewsUrl = this.backend.getFullAddress() + '/api/news/list';
  private getNewsPostUrl = this.backend.getFullAddress() + '/api/news/get-post';
  private saveNewsUrl = this.backend.getFullAddress() + '/api/news/create';
  private editNewsUrl = this.backend.getFullAddress() + '/api/news/edit';
  private deleteNewsUrl = this.backend.getFullAddress() + '/api/news/delete';


  saveNews(news: News): Observable<any> {
    console.log(news);
    return this.http.post<any>(this.saveNewsUrl, { news }, this.backend.getHttpOptions());
  }

  updateNews(news: News) {
    return this.http.post(this.editNewsUrl, { news }, this.backend.getHttpOptions());
  }

  deleteNews(newsId: number) {
    const news = { _id: newsId };
    return this.http.post(this.deleteNewsUrl, { news }, this.backend.getHttpOptions());
  }

  getNewsPost(newsId: number): Observable<any> {
    const news = { _id: newsId };
    return this.http.post<any>(this.getNewsPostUrl, { news }, this.backend.getHttpOptions());
  }

  getAllNews(): Observable<any> {
    return this.http.get<any>(this.getNewsUrl, this.backend.getHttpOptions());
  }


}
