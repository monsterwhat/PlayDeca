# PlayDeca website project

This project is the current website for playdeca.com.
It has launched and will be getting updates overtime.

The websites goal is to provide the users with helpful information.
Connect players with the Forum and a link for the discord channel, which is only availible in-game at the moment.
Allow the processing of donations

### To get this started:
Clone the repo.

- In both Angular-App and Express-Server you will need to run 'npm install' to install the required packages.

You will need to have MongoDB installed on your machine and have it running.
If you are using Systemd run
'systemctl start mongod'
I highly recommend use docker for mongo as it makes installing and deploying much easier.
If you have MongoDB running then you  may skip to the Express Server step. Otherwise, you can launch it like with this. `docker run -d -p 27017:27017 -v data:/data/db mongo`

- Run `./setup.sh`
- Optional run './startservices.sh' to skip the next 2 steps (Requires the use of Screens)
- In the angular-app folder, run `ng serve`,
- In the express-server folder run `npm run dev` or `nodemon server.js`
- NOTE - If you get any errors on compilation regarding modules, remove them and re-install npm.
- Open localhost:4200 or 127.0.0.1:4200 to access the website.

### But what about the .env

You are expected you write your own .env in Express-Server.
These are the keywords you'll need to set, more may be added in due course.
For Example: BACKEND_PORT=8000

SECRET

BACKEND_PORT

FRONTEND_PORT

DOMAIN

SALTORROUNDS

DB_CONNECTION

EMAIL_ACCOUNT

EMAIL_PASS

EMAIL_USERNAME

EMAIL_SECRET

EMAIL_ACCESS_TOKEN

EMAIL_CLIENT_ID

EMAIL_REFRESH_TOKEN

ADMIN_USERNAME

ADMIN_EMAIL

ADMIN_PASS

MONGO_USER

MONGO_PASS
