FROM node:latest AS builder
COPY . /backend-application
WORKDIR /backend-application

RUN rm package-lock.json
RUN npm i
RUN npm rebuild bcrypt --update-binary
RUN npm audit fix
EXPOSE 8001
ENTRYPOINT ["node", "server.js"]