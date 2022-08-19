import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { NewsCreateComponent } from './admin/news/news-create/news-create.component';
import { NewsListComponent } from './news/news-list/news-list.component';
import { NewsUpdateComponent } from './admin/news/news-update/news-update.component';
import { BackgroundComponent } from './news/background/background.component';
import { NewsComponent } from './news/news.component';
import { InfoComponent } from './info/info.component';
import { VoteComponent } from './vote/vote.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { SignInComponent } from './user-session/sign-in/sign-in.component';
import { SignUpComponent } from './user-session/sign-up/sign-up.component';
import { AdminHeaderComponent } from './admin/header/header.component';
import { AdminHomeComponent } from './admin/admin-home/admin-home.component';
import { AdminGuardGuard } from './services/admin-guard.guard';
import { ForumComponent } from './forum/forum.component';
import { CreatePostComponent } from './forum/create-post/create-post.component';
import { ListPostsComponent } from './forum/list-posts/list-posts.component';
import { EditPostComponent } from './forum/edit-post/edit-post.component';
import { ShowPostComponent } from './forum/show-post/show-post.component';
import { CommentComponent } from './forum/comment/comment.component';
import { ProfileComponent } from './profile/profile.component';
import { BasicListComponent } from './admin/news/basic-list/basic-list.component';
import { DonateComponent } from './donate/donate.component';
import { UserHandlingComponent } from './admin/user-handling/user-handling.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { ConfirmEmailComponent } from './user-session/confirm-email/confirm-email.component';
import { ResetPasswordComponent } from './user-session/reset-password/reset-password.component';
import { ConfirmAccountComponent } from './user-session/confirm-account/confirm-account.component';
import { ResetSubmissionComponent } from './user-session/reset-submission/reset-submission.component';
import { PathLocationStrategy, LocationStrategy } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';


const appRoutes: Routes = [
  {
    path: 'home',
    component: NewsComponent,
  },
  {
    path: '', redirectTo: '/home', pathMatch: 'full'
  },
  {
    path: 'info',
    component: InfoComponent,
  },
  {
    path: 'vote',
    component: VoteComponent,
  },
  {
    path: 'news',
    component: NewsComponent,
  },
  {
    path: 'sign-in',
    component: SignInComponent,
  },
  {
    path: 'forums',
    component: ForumComponent,
    children: [
      {
        path: '',
        component: ListPostsComponent,
      },
      {
        path: ':id',
        component: ShowPostComponent,
      },
      {
        path: 'edit/:id',
        component: EditPostComponent,
      },
    ]
  },
  {
    path: 'create',
    component: CreatePostComponent,
  },
  {
    path: 'admin',
    component: AdminHomeComponent,
    canActivate: [AdminGuardGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      {
        path: 'news-list',
        component: BasicListComponent,
      },
      {
        path: 'create-news',
        component: NewsCreateComponent,
      },
      {
        path: 'edit-news/:id',
        component: NewsUpdateComponent,
      },
      {
        path: 'user-handling',
        component: UserHandlingComponent,
      }
    ]
  },
  {
    path: 'profile/:id',
    component: ProfileComponent,
  },
  {
    path: 'reset-submission',
    component: ResetSubmissionComponent,
  },
  {
    path: 'reset-pass/:id/:info',
    component: ResetPasswordComponent,
  },
  {
    path: 'confirm',
    component: ConfirmEmailComponent,
  },
  {
    path: 'confirm-account/:id/:info',
    component: ConfirmAccountComponent,
  },
  {
    path: 'donate',
    component: DonateComponent,
  },
  {
    path: '**', component: PageNotFoundComponent,
  },

];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    NewsCreateComponent,
    NewsListComponent,
    NewsUpdateComponent,
    BackgroundComponent,
    NewsComponent,
    InfoComponent,
    VoteComponent,
    PageNotFoundComponent,
    SignInComponent,
    SignUpComponent,
    AdminHeaderComponent,
    AdminHomeComponent,
    ForumComponent,
    CreatePostComponent,
    ListPostsComponent,
    EditPostComponent,
    ShowPostComponent,
    CommentComponent,
    ProfileComponent,
    BasicListComponent,
    DonateComponent,
    UserHandlingComponent,
    DashboardComponent,
    ConfirmEmailComponent,
    ResetPasswordComponent,
    ConfirmAccountComponent,
    ResetSubmissionComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatExpansionModule,
    MatTabsModule,
    MatGridListModule,
    MatSnackBarModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, {
      onSameUrlNavigation: 'reload'
    }),
    FontAwesomeModule,
  ],
  providers: [{ provide: LocationStrategy, useClass: PathLocationStrategy }],
  bootstrap: [AppComponent]
})
export class AppModule { }
