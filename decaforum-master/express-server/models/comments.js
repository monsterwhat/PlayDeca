const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var commentDataSchema = new Schema({
    content: { type: String, required: true },
    author: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    date_published: { type: Date, required: true },
});

module.exports = mongoose.model('CommentData', commentDataSchema);