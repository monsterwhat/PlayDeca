const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var forumDataSchema = new Schema({
    title: { type: String, required: true },
    content: { type: String, required: true },
    author: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    date_published: { type: Date, required: true },
    vote_count: { type: Number, required: true },
    votes: [{type: mongoose.Schema.Types.ObjectId, ref: 'VoteData' }],
    comment: [{ type: mongoose.Schema.Types.ObjectId, ref: 'CommentData' }],
});

module.exports = mongoose.model('ForumData', forumDataSchema);
