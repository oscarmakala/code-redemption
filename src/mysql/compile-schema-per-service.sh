#! /bin/bash -e

for schema in cr_voucher_service cr_order_service cr_account_service cr_notification_service; 
do
  user=${schema}_user
  password=${schema}_password
  cat >> schema-per-service.sql <<END
  CREATE USER '${user}'@'%' IDENTIFIED BY '$password';
  create database $schema;
  GRANT ALL PRIVILEGES ON $schema.* TO '${user}'@'%' WITH GRANT OPTION;
  USE $schema;
END
    cat template >> schema-per-service.sql
done


