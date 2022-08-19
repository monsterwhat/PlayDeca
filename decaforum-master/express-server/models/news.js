const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Very like the forum, but only for news.
var newsSchema = new Schema({
    title: { type: String, required: true },
    content: { type: String, required: true },
    author: { type: String, required: true, default: 'Admin' },
    date: { type: Date, required: true }
});

module.exports = mongoose.model('NewsData', newsSchema);
