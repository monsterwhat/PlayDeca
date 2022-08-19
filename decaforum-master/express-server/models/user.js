const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const dotenv = require('dotenv');
dotenv.config();
const bcrypt = require('bcrypt');

// JWT will not store the role, only the id
var schema = new Schema({
    email: { type: String, require: true },
    username: { type: String, require: true },
    password: { type: String, require: true },
    creation_date: { type: Date, require: true },

    extra_info: { type: String, required: false },
    allow_reset: { type: Boolean, required: true, default: false },
    confirmed_game: { type: Boolean, required: true, default: false },
    confirmed_email: { type: Boolean, required: true, default: false },
    role: { type: String, required: false, default: 'user' },
    banned: { type: Boolean, required: true, default: false },
});

schema.statics.hashPassword = function hashPassword(password) {
    var saltOrRounds = +process.env.SALTORROUNDS;
    return bcrypt.hashSync(password, saltOrRounds);
}

schema.methods.isValid = function (hashedPassword) {
    return bcrypt.compareSync(hashedPassword, this.password);
}

module.exports = mongoose.model('User', schema);

/*
On the frontend we have a User model too.
FE User will have name, email, creation_date, and I think thats all.
Exposing role, confirmed or anything else would be a risk.
*/