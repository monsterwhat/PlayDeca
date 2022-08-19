const express = require( 'express' );
const router = express.Router();
const passport = require( 'passport' );
const mongoose = require( 'mongoose' );

const CommentData = require( '../models/comments' );
const ForumData = require( '../models/forum' );
const VoteData = require( '../models/vote' );

const isUserValid = passport.authenticate( 'jwt', { session: false } );

const getCommentAuthorId = function ( req, res, next ) {
    CommentData.findById( req.body.comment._id ).then(
        ( doc, err ) => {
            if ( err ) {
                res.status( 404 ).send();
            } else {
                req.originalAuthorId = doc.author;
                next();
            }
        }
    );
};

const getPostAuthorId = function ( req, res, next ) {
    ForumData.findById( req.body._id ).then(
        ( doc, err ) => {
            if ( err ) {
                res.status( 404 ).send();
            } else {
                req.originalAuthorId = doc.author;
                next();
            }
        }
    );
};
//  Replies are Comments
// The was a decision to either make comments to the post or allow replies to the post and other replies.

// 'http://127.0.0.1:8000/api/forum/create'
router.post( '/create', isUserValid, function ( req, res, next ) {
    let rawPostData = req.body;
    rawPostData.author = req.user._id;
    rawPostData.vote_count = 0;

    let postInformation = new ForumData( rawPostData );

    try {
        postInformation.save();
        res.status( 200 ).json( { sent: true, message: "Post was submited, thank you" } );
    } catch ( reason ) {
        console.log( "Saving failed for reason: " + reason );
        res.status( 200 ).json( { sent: false, message: 'There was an error sending the post, try again later' } );
    }

} );

// 'http://127.0.0.1:8000/api/forum/delete-post'
router.post( '/delete-post', isUserValid, getPostAuthorId, function ( req, res, next ) {
    // Make sure we have an admin user set up
    if ( req.user._id.equals( req.originalAuthorId ) || req.user.role === 'admin' ) {

        ForumData.findById( req.body._id ).then(
            function ( doc, err ) {
                if ( err ) {
                    res.json( { sent: false, message: err } ).catch();
                }

                doc.comment.forEach( comId => {
                    CommentData.findByIdAndDelete( comId ).then(
                        function ( doc, err ) {
                            if ( err ) {
                                res.json( { CommentMessage: err } );
                            }
                        }
                    ).catch( rejection => {
                        console.log( rejection );
                    } );
                } );
            }
        );

        ForumData.findByIdAndDelete( req.body._id ).then(
            function ( doc, err ) {
                if ( err ) {
                    res.json( { sent: false, message: err } )
                }
                res.json( { sent: true, message: "Post deleted" } );
            }
        );
    } else {
        res.json( { sent: false, message: "You are not the author of this post" } );
    }
} );

// 'http://127.0.0.1:8000/api/forum/edit-post'
router.post( '/edit-post', isUserValid, getPostAuthorId, function ( req, res, next ) {
    if ( req.user._id.equals( req.originalAuthorId ) || req.user.role === 'admin' ) {

        ForumData.findByIdAndUpdate( req.body._id, { content: req.body.content },
            () => {
                res.json( { message: "Post updated!" } );
            }
        );
    }
} );

// 'http://127.0.0.1:8000/api/forum/list'
router.get( '/list', function ( req, res, next ) {
    ForumData.find( {} ).sort( '-date_published' ).exec(
        ( reason, doc ) => {
            if ( reason ) {
                console.log( "Rejected for reason: " + reason );
                res.status( 404 ).json( reason );
            } else {
                res.json( doc );
            }
        }
    );
} );

// 'http://127.0.0.1:8000/api/forum/delete-reply'
router.post( '/delete-reply', isUserValid, getCommentAuthorId, function ( req, res, next ) {

    if ( !req.user || req.user._id.equals( req.originalAuthorId ) || req.user.role === 'admin' ) {

        CommentData.findByIdAndDelete( req.body.comment._id )
            .then(
                function ( doc, err ) {
                    if ( err ) {
                        res.status( 400 ).json( { sent: false, message: "Error deleting comment -- " + err } );
                    };
                }
            ).then(
                ForumData.findByIdAndUpdate( req.body.postId, { '$pull': { 'comment': req.body.comment._id } } ).then(
                    function ( doc, err ) {
                        if ( err ) {
                            res.status( 400 ).json( { sent: false, message: "Error updating forum post comments -- " + err } )
                        };
                    }
                )
            );
        res.json( { sent: true, message: "Comment/Reply was deleted" } );
    } else {
        res.json( { sent: false, message: "You may not delete others' comments" } );
    }
} );

// 'http://127.0.0.1:8000/api/forum/add-reply'
router.post( '/add-reply', isUserValid, function ( req, res, next ) {

    let postId = req.body.postId;
    let commentPost = req.body.comment;
    commentPost.author = req.user._id;
    let commentInformation = new CommentData( commentPost );

    commentInformation.save().then(
        com => {
            return ( ForumData.findByIdAndUpdate( postId, { '$push': { 'comment': com._id } } )
                .catch( () => {
                    console.log( "Error on saving comment" );
                    res.json( { message: "Error on saving comment" } );
                } )
            );
        }
    );

    res.json( { message: "Comment added! Thank you, " + req.user.username } );
} );

// 'http://127.0.0.1:8000/api/forum/edit-reply'
router.post( '/edit-reply', isUserValid, getCommentAuthorId, function ( req, res, next ) {

    if ( req.user._id.equals( req.originalAuthorId ) || req.user.role === 'admin' ) {
        CommentData.findByIdAndUpdate( req.body.comment._id, {
            content: req.body.comment.content
        } ).catch(
            ( reason ) => {
                console.log( reason );
                res.status( 400 ).json( { message: "Comment failed to update" } )
            }
        );
        res.status( 200 ).json( { message: "Comment updated" } );

    } else {
        res.json( { message: "You don't own this comment." } );
    }
} );


// 'http://127.0.0.1:8000/api/forum/get-reply'
router.post( '/get-reply', function ( req, res, next ) {
    // This is getting one at a time from the list in the original post.
    CommentData.findById( req.body._id ).sort().exec(
        ( error, document ) => {
            if ( error ) {
                res.status( 404 ).json( { message: 'Comments failed to load' } );
            } else {
                res.json( document );
            }
        }
    );
} );

// 'http://127.0.0.1:8000/api/forum/get-post'
router.post( '/get-post', function ( req, res, next ) {
    ForumData.findOne( req.body ).then(
        ( doc ) => {
            res.json( doc );
            next();
        }
    );
} );


// VOTING 
// 'http://127.0.0.1:8000/api/forum/inc-forum-vote'
router.post( '/inc-forum-vote', isUserValid, function ( req, res, next ) {

    let postId = req.body.forum._id;
    let userId = req.user._id;
    voteAction( postId, userId, true )
} );

// 'http://127.0.0.1:8000/api/forum/dec-forum-vote'
router.post( '/dec-forum-vote', isUserValid, function ( req, res, next ) {

    let postId = req.body.forum._id;
    let userId = req.user._id;
    voteAction( postId, userId, false )
} );

function voteAction( postId, userId, isActionVoteUp ) {
    let voteIncrease = 1;
    if ( !isActionVoteUp ) voteIncrease *= -1;
    // Get Forum Post Votes Array
    // Get Vote on Id and 

    // Forum has Vote ID
    // Vote has User and Forum

    VoteData.findOne( { user_id: userId, forum_id: postId }, ( err, vote ) => {
        if ( err ) {
            console.log( "Voting error: " + err );
        }
        else if ( vote === null ) {
            // User hasn't voted, add them
            let newVoteData = {
                forum_id: postId,
                voted_up: isActionVoteUp,
                voted_down: !isActionVoteUp,
                user_id: userId,
            };

            let newVote = new VoteData( newVoteData );

            try {
                newVote.save().then(
                    ( voteSaved ) => ( ForumData.findByIdAndUpdate( postId, {
                        $push: { votes: voteSaved._id },
                        $inc: { vote_count: voteIncrease }
                    } ) ).catch( () => { res.json( { message: "Error upvoting" } ); } )
                );
            }
            catch ( error ) { console.log( "Voting creation error: " + error ); }

        }
        else {
            // Vote is not null, changing vote 
            if ( vote.voted_up && isActionVoteUp || vote.voted_down && !isActionVoteUp ) {

                if ( isActionVoteUp ) {  // Change upvote to no vote
                    changeForumVote( postId, vote._id, false, false, -1 );
                }

                else {  // Change downvote to no vote
                    changeForumVote( postId, vote._id, false, false, 1 );
                }

            }
            else if ( !vote.voted_up && !vote.voted_down ) {  // up or down by 1
                changeForumVote( postId, vote._id, isActionVoteUp, !isActionVoteUp, voteIncrease );
            }
            else { // up or down by 2
                // Updating Forum Vote and Vote Data 
                changeForumVote( postId, vote._id, isActionVoteUp, !isActionVoteUp, voteIncrease * 2 );
            }
        }
    } )
}

function changeForumVote( postId, voteId, upVote, downVote, voteIncrease ) {

    ForumData.findByIdAndUpdate( postId, { '$inc': { vote_count: voteIncrease } }, ( err, res ) => {
        if ( err ) { console.log( "Error: " + err ); }
        else { /*Might use this*/ }
    } );
    VoteData.findByIdAndUpdate( voteId, { '$set': { voted_up: upVote, voted_down: downVote } }, ( err, res ) => {
        if ( err ) { console.log( "Error: " + err ); }
        else { }
    } );
}


// 'http://127.0.0.1:8000/api/forum/user-voting-info'
router.post( '/user-voting-info', isUserValid, function ( req, res, next ) {
    // VoteData.find({}, (error, vote) => {
    VoteData.findOne( { 'user_id': req.user._id, 'forum_id': req.body.forum._id }, ( error, vote ) => {
        if ( error ) {
            return res.status( 400 );
        }
        else {
            res.status( 200 ).json( { vote } );
        }
    } );
} );

// 'http://127.0.0.1:8000/api/forum/inc-comment-vote'
// router.post('/inc-comment-vote', function (req, res, next) {
//     CommentData.findByIdAndUpdate(req.body.comment._id, { $inc: { vote_count: 1 } });
// });

// 'http://127.0.0.1:8000/api/forum/dec-comment-vote'
// router.post('/dec-comment-vote', function (req, res, next) {
//     CommentData.findByIdAndUpdate(req.body.comment._id, { $inc: { vote_count: -1 } });
// });

// 'http://127.0.0.1:8000/api/forum/get-post'

module.exports = router;