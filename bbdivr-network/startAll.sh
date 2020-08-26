# Usage ./startAll.sh

rm -r ../wallet

./start.sh

./createChannel.sh

./packageCC.sh 1

./installCC.sh 1

text="$(./installCC.sh 1 | tail -1)"

echo "$text"
hash=${text##*ID: }   # Remove the left part.
hash=${hash%%\,*}     # Remove the right part.
echo "$hash"

./approveCC.sh $hash 1.0 1

./commitCC.sh 1.0 1

./testCC.sh


