FROM eventuateio/eventuate-mysql:0.15.0.BUILD-SNAPSHOT


COPY 6.compile-schema-per-service.sh /docker-entrypoint-initdb.d/
COPY 7.intialize-database-uaa.sql /docker-entrypoint-initdb.d/
COPY template /docker-entrypoint-initdb.d/template

RUN touch /docker-entrypoint-initdb.d/6.schema-per-service.sql
RUN chown mysql -R docker-entrypoint-initdb.d