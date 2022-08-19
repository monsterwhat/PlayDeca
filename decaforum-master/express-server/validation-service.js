// This service handles all of the validation for the backend

const _usernameRegExp = /^[a-z0-9_-]{3,16}$/ig;
// Upper, lower, digit, spec char, min=8;
const _passwordRegExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;

// tslint:disable-next-line: max-line-length
const _emailRegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;


const matchUsername = (username) => {
    return !_usernameRegExp.test(username);
}

const matchPassword = (password) => {
    return !_passwordRegExp.test(password);
}

const matchEmail = (email) => {
    return !_emailRegExp.test(email);
}

const validateNewUser = (username, email, password) => {

    if (matchUsername(username)) {
        return false;
    }
    if (matchEmail(email)) {
        return false;
    }
    if (matchPassword(password)) {
        return false;
    }
    return true;

}

const validateSignIn = (email, password) => {
    if (matchEmail(email)) {
        return false;
    }
    if (matchPassword(password)) {
        return false;
    }
    return true;
}

module.exports.matchEmail = matchEmail;
module.exports.matchUsername = matchUsername;
module.exports.matchPassword = matchPassword;
module.exports.validateNewUser = validateNewUser;
module.exports.validateSignIn = validateSignIn;