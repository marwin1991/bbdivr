## Running the bbdivr network

```
rm -r ../wallet

./start.sh

./createChannel.sh

./packageCC.sh 1

./installCC.sh 1

./approveCC.sh bbdivr_1:70c31f7996cf0709f4f8daea367f3239cd09446040e6d9fa10fbb6940b35f0f7 1.0 1

./commitCC.sh 1.0 1

./testCC.sh

``` 

or

```
./startAll.sh
```