docker exec -it \
$(docker container ls --filter name=_charts -q) \
charts-cli add-user --first-name "<First>" --last-name "<Last>" \
--email "<user@example.com>" --password "<Password>" \
--role "<UserAdmin|User>"

docker exec -it \
$(docker container ls --filter name=_charts -q) \
charts-cli add-user --first-name "Oscar" --last-name "Makala" \
--email "oscar.makala@gmail.com" --password "password" \
--role "UserAdmin"