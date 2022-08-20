const express = require( 'express' );
const dotenv = require( 'dotenv' );
dotenv.config();
const app = express();
const path = require( 'path' );
const http = require( 'http' );
const https = require( 'https' );
const fs = require( 'fs' );
const cors = require( 'cors' );
const mongoose = require( 'mongoose' );
const passport = require( 'passport' );
const helmet = require( 'helmet' );
const session = require( 'express-session' );
const MongoStore = require( 'connect-mongo' )( session );
const cookieparser = require( 'cookie-parser' );

const usersRoute = require( './routes/user' );
const adminRoute = require( './routes/admin' );
const forumRoute = require( './routes/forum' );
const newsRoute = require( './routes/news' );

const BACKEND_PORT = process.env.BACKEND_PORT || 8001;
const FRONTEND_PORT = process.env.FRONTEND_PORT || 4200;

const FRONTEND_DOMAIN = "http://" + process.env.DOMAIN + ":" + FRONTEND_PORT;
const BACKEND_DOMAIN = "http://" + process.env.DOMAIN + ":" + BACKEND_PORT;
const MONGODB_DOMAIN = process.env.DB_CONNECTION;
const MONGODB_DOMAIN_DOCKER = process.env.DB_CONNECTION_ALT;

app.use( cors( {
    origin: [FRONTEND_DOMAIN, "http://" + process.env.DOMAIN, 'https://playdeca.com' ],
    credentials: true,
} ) );

app.use( helmet() );
app.use( express.json() );
app.use( cookieparser() );
app.use( express.urlencoded( { extended: false } ) );

// DATABASE
var connectToDB = (connectionDomain) => {
    return mongoose.connect(connectionDomain, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useFindAndModify: false,
        user: process.env.MONGO_USER,
        pass: process.env.MONGO_PASS
    }, (error) => {
        if (error) {
            console.error('Failed to connect to mongo on startup ');
        }
        else {
            console.log("Successfully connected to DB");
        }
    });
};

app.use( session( {
    name: 'decaform.sid',
    resave: false,
    saveUninitialized: false,
    secret: process.env.SECRET,
    cookie: {
        httpOnly: false,
        secure: false
    },
    store: new MongoStore( { mongooseConnection: mongoose.connection } )
} ) );

// ROUTES
app.get( '/api', ( req, res ) => {
    console.log("API");
    res.send("something");
} );

require( './passport-config' );
app.use( passport.initialize() );
app.use( passport.session() );

// FORUM
app.use( '/api/forum', forumRoute );

// USERS
app.use( '/api/users', usersRoute );

// ADMIN
app.use( '/api/admin', adminRoute );
app.use( '/api/news', newsRoute );

// CERTIFICATION
var options = {
//     key: fs.readFileSync('/etc/letsencrypt/live/playdeca.com/privkey.pem'),
//     cert: fs.readFileSync('/etc/letsencrypt/live/playdeca.com/cert.pem'),
//     ca: fs.readFileSync('/etc/letsencrypt/live/playdeca.com/chain.pem')
};

http.createServer(options, app).listen(BACKEND_PORT, '0.0.0.0', () => {
    console.log('Server started!');
});
// connectToDB(MONGODB_DOMAIN);
connectToDB(MONGODB_DOMAIN_DOCKER)
