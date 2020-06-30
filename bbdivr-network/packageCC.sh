# Usage ./packageCC.sh 1

# Add fabric-samples /bin to path; workdir: fabric-samples
# export PATH=${PWD}/../bin:${PWD}:$PATH

# add fabric-samples /config; workdir: fabric-samples
# export FABRIC_CFG_PATH=$PWD/../config/

export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
rm -f *.tar.gz

# Some problems occures when jar is in /target directory ??
#
cp ../bbdivr-chaincode/target/*.jar .
peer lifecycle chaincode package bbdivr_$1.tar.gz --path . --lang java --label bbdivr_$1

rm -f *.jar
