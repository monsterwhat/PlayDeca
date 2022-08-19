FROM node:latest AS builder
COPY ./ /frontend-application
WORKDIR /frontend-application
RUN npm i
RUN npm audit fix
RUN $(npm bin)/ng build --prod
# RUN $(npm bin)/ng build --aot

FROM httpd:latest
RUN rm -r /usr/local/apache2/htdocs/*

RUN echo "ServerName localhost" >> /usr/local/apache2/conf/httpd.conf

# COPY ./.htaccess ./dist/.htaccess
COPY --from=builder /frontend-application/dist /usr/local/apache2/htdocs/
RUN sed -i \
  's/#LoadModule rewrite_module modules\/mod_rewrite.so/LoadModule rewrite_module modules\/mod_rewrite.so/g' \
  /usr/local/apache2/conf/httpd.conf
RUN sed -i '/<Directory "\/usr\/local\/apache2\/htdocs">/a### Rewrite rule was written from the Dockerfile when building the image ###\n\
    DirectoryIndex index.html\n\
    RewriteEngine on\n\
    RewriteCond %{REQUEST_FILENAME} -s [OR]\n\
    RewriteCond %{REQUEST_FILENAME} -d\n\
    RewriteRule ^.*$ - [NC,L]\n\
    RewriteRule ^(.*) index.html [NC,L]\n' \
  /usr/local/apache2/conf/httpd.conf
RUN sed -i '/<Files "\.ht\*">/,/<\/Files>/c# This was commented out from the Dockerfile\n# <Files ".ht*">\n#     Require all denied\n# <\/Files>' \
  /usr/local/apache2/conf/httpd.conf
RUN chown -R root:daemon \
  /usr/local/apache2/htdocs/*
RUN chmod -R 555 \
  /usr/local/apache2/htdocs/*
