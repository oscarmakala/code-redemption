##! /bin/bash -e
#
#for schema in cr_voucher_service cr_order_service cr_account_service cr_notification_service;
#do
#  user=${schema}_user
#  password=${schema}_password
#  cat >> /docker-entrypoint-initdb.d/6.schema-per-service.sql <<END
#  CREATE USER '${user}'@'%' IDENTIFIED BY '$password';
#  create database $schema;
#  GRANT ALL PRIVILEGES ON $schema.* TO '${user}'@'%' WITH GRANT OPTION;
#  USE $schema;
#END
#    cat /docker-entrypoint-initdb.d/template >> /docker-entrypoint-initdb.d/6.schema-per-service.sql
#done
#
#
#! /bin/bash -e

for schema in cr_voucher_service cr_order_service cr_account_service cr_notification_service;
do
  user=${schema}_user
  password=${schema}_password
  cat >> 6.schema-per-service.sql <<END
  CREATE USER '${user}'@'%' IDENTIFIED BY '$password';
  create database $schema;
  GRANT ALL PRIVILEGES ON $schema.* TO '${user}'@'%' WITH GRANT OPTION;
  USE $schema;
END
    cat template >> 6.schema-per-service.sql
done


