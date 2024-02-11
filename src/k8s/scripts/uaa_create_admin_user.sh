gem install uaac
#configure uaac to point to our running instance of UAA
uaac target http://192.168.65.3
#configure uaac to point to our running instance of UAA
uaac token client get admin -s azamPay@1235

#create client
uaac client add azamclient -s azamclientsecret --name AzamClient --scope resource.read,resource.write,openid,profile,email,address,phone --authorized_grant_types authorization_code,refresh_token,client_credentials,password --authorities uaa.resource --redirect_uri http://192.168.65.3/login/oauth2/code/uaa


uaac client add azamclient -s azamclientsecret --name AzamClient --scope resource.read,resource.write,openid,profile,email,address,phone --authorized_grant_types authorization_code,refresh_token,client_credentials,password --authorities uaa.resource --redirect_uri http://localhost:4200/login/oauth2/code/uaa
#add users
uaac user add makalaoscar -p moscar981 --emails makalaoscar@gmail.com


uaac target http://161.97.125.175:8080


uaac target http://172.16.100.25:8080
uaac token client get admin -s azamPay@1235
uaac client add gateway -s azamclientsecret --name gateway --scope resource.read,resource.write,openid,profile,email,address,phone --authorized_grant_types authorization_code,refresh_token,client_credentials,password --authorities uaa.resource --redirect_uri http://172.16.100.25:7070/login/oauth2/code/gateway
uaac user add makalaoscar -p moscar981 --emails makalaoscar@gmail.com
uaac user add denis@azampay.com -p denis321 --emails denis@azampay.com
uaac user add beatrice@azampay.com -p beatrice321 --emails beatrice@azampay.com
uaac user add makongoro@azampay.com -p makongoro124 --emails makongoro@azampay.com

uaac password set makongoro@azampay.com -p makongoro124


uaac user add eugenia@azampay.com -p eugenia --emails eugenia@azampay.com
uaac member add uaa.admin eugenia@azampay.com

uaac member add role.manage makongoro@azampay.com
uaac


