const express = require( 'express' );
const router = express.Router();
const passport = require( 'passport' );

const NewsData = require( '../models/news' );

const isUserValid = passport.authenticate( 'jwt', { session: false } );

const isUserAdmin = ( req, res, next ) => {
    // JWT belongs to a normal user
    if ( req.user.role === 'user' ) {
        res.status( 400 ).json( { admin: false, message: "User is not an admin" } );
    }
    // JWT belongs to an admin
    else if ( req.user.role === 'admin' ) {
        next();
    }
    // JWT belongs to something else that isn't normal
    else {
        res.status( 400 ).json( { admin: false, message: "User is not an admin" } );
    }
}

// 'http://127.0.0.1:8000/api/news/create'
router.post( '/create', isUserValid, isUserAdmin, function ( req, res, next ) {

    let newsInformation = new NewsData( req.body.news );

    try {
        newsInformation.save();
        res.status( 200 ).json( { sent: true, message: "Submited, thank you" } );
    } catch ( reason ) {
        console.log( "Saving failed for reason: " + reason );
        res.status( 200 ).json( { sent: false, message: 'There was an error saving the news, try again later' } );
    }
    next();
} );

// 'http://127.0.0.1:8000/api/news/delete-post'
router.post( '/delete', isUserValid, isUserAdmin, function ( req, res, next ) {

    NewsData.findByIdAndDelete( req.body.news._id ).then(
        ( doc, err ) => {
            if ( err ) {
                res.json( { sent: false, message: err } )
            }
            res.status( 200 ).json( { sent: true, message: "News deleted" } );
        }
    );
} );

// 'http://127.0.0.1:8000/api/news/edit-post'
router.post( '/edit', isUserValid, isUserAdmin, function ( req, res, next ) {

    NewsData.findByIdAndUpdate( req.body.news._id, { content: req.body.news.content },
        () => {
            res.json( { message: "News post updated!" } );
        }
    );
} );

// 'http://127.0.0.1:8000/api/news/list'
router.post( '/get-post', function ( req, res, next ) {
    NewsData.findById( req.body.news._id ).then(
        doc => {
            res.json( doc );
        }
    ).catch(
        reason => {
            console.log( "Rejected for reason: " + reason );
            res.status( 404 ).json( reason );
        }
    );
} );

// 'http://127.0.0.1:8000/api/news/list'
router.get( '/list', function ( req, res, next ) {
    NewsData.find( {} ).sort( '-date' ).then(
        doc => {
            res.json( doc );
        }
    ).catch(
        reason => {
            console.log( "Rejected for reason: " + reason );
            res.status( 404 ).json( reason );
        }
    );
} );

module.exports = router;