const nodemailer = require( 'nodemailer' );
const randomstring = require( 'randomstring' );
const dotenv = require( 'dotenv' );
dotenv.config();

const User = require( './models/user' );

const emailHeader = 'Hello player,'; // We could get the username if we wanted.
const emailFooter = 'Good luck and have fun, PlayDeca Administration'
const tokenText = 'The link will lead you to a reset password prompt, but you will need to enter the passcode provided. Passcode: '
const linkStart = 'http://' + process.env.DOMAIN;
const fromAddress = '"' + process.env.EMAIL_USERNAME + '" <' + process.env.EMAIL_ACCOUNT + '>';

const transporter = nodemailer.createTransport( {
    service: 'gmail',
    auth: {
        user: process.env.EMAIL_ACCOUNT,
        pass: process.env.EMAIL_PASS,
        // TODO: ENSURE THIS WORKS ON DEPLOYMENT
        // type: 'OAuth2',
        // user: process.env.EMAIL_ACCOUNT,
        // clientId: process.env.EMAIL_CLIENT_ID,
        // clientSecret: process.env.EMAIL_SECRET,
        // refreshToken: process.env.EMAIL_REFRESH_TOKEN,
        // accessToken: process.env.EMAIL_ACCESS_TOKEN,
    }
} );

const sendEmail = async ( targetAddress, subject, textPlain, textHTML ) => {

    const message = {
        from: fromAddress,
        to: targetAddress,
        subject: subject,
        text: textPlain, // plain text body
        html: textHTML, // html body
    };

    // let testAccount = await nodemailer.createTestAccount();

    let sent = false;
    await transporter.sendMail( message, ( error, info ) => {
        if ( error ) {
            console.log( "Mail error: " + error );
            sent = false;
        } else {
            console.log( "Mail info: " + info );
            sent = true;
        }
    } );

    return sent; // Bugged atm

};

const sendConfirmEmail = async ( targetAddress, id ) => {

    // Just adding some noise to the link.
    const clickLink = linkStart + '/confirm-account/' + id + '/' + randomstring.generate();


    const confirmContentHTML = emailHeader + '<br><br><b>In order to confirm your email address for Playdeca, click the link below</b><br>' +
        'If you did not sign up to Playdeca.com, then you can simply ignore this email.<br><br>' +
        '<a href="' + clickLink + '">Confirmation Link</a><br><br>' + 'Good luck and have fun, <br>PlayDeca Administration';

    const confirmContentPlain = emailHeader + '\n\nIn order to confirm your email address for Playdeca, click the link below\n' +
        'If you did not sign up to Playdeca.com, then you can simply ignore this email.\n\n' + clickLink + '\n\n' + 'Good luck and have fun,\nPlayDeca Administration';

    const subject = 'Account Confirmation ✔';

    await sendEmail( targetAddress, subject, confirmContentPlain, confirmContentHTML );
    return true; // Boolean responce for sent
};

const sendResetEmail = async ( targetAddress, id ) => {
    const tempPassword = randomstring.generate( 10 );
    let tempSetCorrectly = setTempPass( id, tempPassword );

    if ( !tempSetCorrectly ) { return false; }
    else {

        // I could just put the tempPassword in the URL, but I'll rather place it in the email body.
        const clickLink = linkStart + '/reset-pass/' + id + '/' + randomstring.generate(); //Noise in the URL
        // The user will then be asked to place the token in a abox on the site and enter a new password. 

        const resetContentHTML = emailHeader + '<br><br><b>To reset the password of your account for Playdeca, click the link below</b><br>' +
            'If you did not request to reset your password, then you can ignore this email.<br>' +
            'In case, you have any trouble with this and are not able to log in after changing your password, content Random6894 or Monster_what on the Discord server. The Discord link is available on Playdeca.com<br><br>' +
            '<a href="' + clickLink + '">Reset Password Link</a><br><br>' +
            tokenText + '<b>' + tempPassword + '</b>' + '<br><br>' + 'Good luck and have fun, <br>PlayDeca Administration';

        const resetContentPlain = emailHeader + 'To reset the password of your account for Playdeca, click the link below\n' +
            'If you did not request to reset your password, then you can ignore this email.\n\nIn case, you have any trouble with this and are not able to log in after changing your password, content Random6894 or Monster_what on the Discord server. The Discord link is available on Playdeca.com\n\n Link to Reset Password: ' + clickLink + '\n\n' +
            tokenText + '\t' + tempPassword + '\n\n' + 'Good luck and have fun,\nPlayDeca Administration';

        const subject = 'Account Password Reset';

        await sendEmail( targetAddress, subject, resetContentPlain, resetContentHTML );
        return true; // Boolean responce for sent

    }
};

const setTempPass = async ( id, tempPass ) => {
    let tempPassSetSuccess = false;
    await User.findByIdAndUpdate( id, { extra_info: tempPass }, ( error, document, result ) => {
        if ( error ) { tempPassSetSuccess = false; console.log( 'Cannot set temp pass for user' + id ) }
        else { tempPassSetSuccess = true; }
    } );
    return tempPassSetSuccess;
}

module.exports.sendConfirmEmail = sendConfirmEmail;
module.exports.sendResetEmail = sendResetEmail;
