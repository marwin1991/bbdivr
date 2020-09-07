# bbdivr (blockchain-based-docker-images-vulnerabilities-registry)
This is my master thesis project to keep source files, configurations and other important files that was produce during research


## installing hyperledger fabric

Pre requirements:
```
git
curl 
docker
```

Developers of this framework made it easy to istall all required dependecies, just do:


```
curl -sSL https://bit.ly/2ysbOFE | bash -s
```

and then add /bin to yor path

```
export PATH=<path to download location>/bin:$PATH
```

To test it locally add to /etc/hosts
```
127.0.0.1 peer0.org1.example.com
127.0.0.1 peer0.org2.example.com
```
