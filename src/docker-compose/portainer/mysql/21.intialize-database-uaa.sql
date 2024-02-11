drop database keycloak;
create database keycloak;
GRANT ALL PRIVILEGES ON keycloak.* TO 'mysqluser'@'%' WITH GRANT OPTION;