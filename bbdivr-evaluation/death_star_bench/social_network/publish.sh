#!/bin/bash
# in future change marwin1991 to organisation

docker login
docker tag defreitas/dns-proxy-server:15-11-2020 marwin1991/dns-proxy-server:15-11-2020
docker push marwin1991/dns-proxy-server:15-11-2020

docker tag yg397/social-network-microservices:15-11-2020 marwin1991/social-network-microservices:15-11-2020
docker push marwin1991/social-network-microservices:15-11-2020

docker tag yg397/openresty-thrift:xenial-15-11-2020 marwin1991/openresty-thrift:xenial-15-11-2020
docker push marwin1991/openresty-thrift:xenial-15-11-2020

docker tag yg397/media-frontend:xenial-15-11-2020 marwin1991/media-frontend:xenial-15-11-2020
docker push marwin1991/media-frontend:xenial-15-11-2020