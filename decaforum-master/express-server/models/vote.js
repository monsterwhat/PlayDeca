const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// This tracks who voted on what.
var voteDataSchema = new Schema({
    forum_id: { type: mongoose.Schema.Types.ObjectId, ref: 'ForumData', required: true },
    voted_up: { type: Boolean, required: true },
    voted_down: { type: Boolean, required: true },
    user_id: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
});

module.exports = mongoose.model('VoteData', voteDataSchema);
