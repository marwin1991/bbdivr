# Usage ./commitCC.sh 1.0 1
#

export FABRIC_CFG_PATH=$PWD/../config/

VERSION=$1
SEQ=$2

# check ready to commit
export CORE_PEER_TLS_ENABLED=true
export CORE_PEER_LOCALMSPID="Org2MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
export CORE_PEER_ADDRESS=localhost:9051

echo "Approve status: "
peer lifecycle chaincode checkcommitreadiness --channelID bbdivr-channel --name bbdivr --version $VERSION --sequence $SEQ --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem --output json

# A successful commit transaction will start the new chaincode right away.
# If the chaincode definition changed the endorsement policy, the new policy
# would be put in effect.

echo "Commit chaincode"
peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID bbdivr-channel --name bbdivr --version $VERSION --sequence $SEQ --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem --peerAddresses localhost:7051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt

# verify that the new chaincode has started on your peers:
docker ps