rm -f report.json
docker rm -f clair
docker rm -f db
docker network create scanning || true
docker run -p 5432:5432 -d --net=scanning --name db arminc/clair-db
docker run -p 6060:6060  --net=scanning --link db:postgres -d --name clair arminc/clair-local-scan:v2.0.6
#docker run --net=scanning --name=scanner --link=clair:clair -v '//var/run/docker.sock:/var/run/docker.sock'  objectiflibre/clair-scanner --clair="http://clair:6060" --ip="scanner" -r report.json libamtrack/libamtrackforjs:latest
#docker container cp scanner:report.json ./report.json
#docker container rm scanner