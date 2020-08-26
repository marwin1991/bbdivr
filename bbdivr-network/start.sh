# Usage ./start.sh

docker rm -f logspout
./network.sh down
./network.sh up -ca -s couchdb