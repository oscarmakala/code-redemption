#!/bin/zsh

uaac target http://172.16.100.25:8080
uaac token client get admin -s azamPay@1235
uaac client add gateway -s azamclientsecret --name gateway --scope resource.read,resource.write,openid,profile,email,address,phone --authorized_grant_types authorization_code,refresh_token,client_credentials,password --authorities uaa.resource --redirect_uri http://localhost:4200/login/oauth2/code/gateway
uaac user add makalaoscar -p moscar981 --emails makalaoscar@gmail.com