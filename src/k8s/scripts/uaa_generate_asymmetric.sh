#! /bin/bash

mkdir temp_uaa_certs
cd temp_uaa_certs
openssl genrsa -out privkey.pem 2048
openssl rsa -pubout -in privkey.pem -out pubkey.pem