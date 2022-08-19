export interface ForumComment {
  _id: number;
  content: string;
  author?: number;
  authorname?: string;
  vote_count?: number;
  date_published: Date;
  show_controls?: boolean;
}
