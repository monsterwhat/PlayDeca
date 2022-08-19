import { Vote } from './vote.model';

export interface ForumPost {
  _id: number;
  title: string;
  content: string;
  author?: number;
  authorname?: string;
  vote_count?: number;
  vote_status?: Vote;
  date_published: Date;
  comment: [number];
}
