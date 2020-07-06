# Usage ./packageCC.sh 1

# Add fabric-samples /bin to path; workdir: fabric-samples
# export PATH=${PWD}/../bin:${PWD}:$PATH

# add fabric-samples /config; workdir: fabric-samples
export FABRIC_CFG_PATH=$PWD/../config/
rm -f -r bbdivr-chaincode-tmp || true
mkdir "bbdivr-chaincode-tmp"
export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp

# Some problems occures when jar is in /target directory ??
#
cp ../bbdivr-chaincode/target/*jar-with-dependencies.jar ./bbdivr-chaincode-tmp
peer lifecycle chaincode package bbdivr_$1.tar.gz --path ./bbdivr-chaincode-tmp --lang java --label bbdivr_$1
rm -f -r bbdivr-chaincode-tmp || true