#!/bin/sh

function generate {
    echo "generating keyPair for service ${1}"
    openssl genrsa -out ${1}/${1}RSAPrivateKey.pem 2048
    openssl rsa -pubout -in ${1}/${1}RSAPrivateKey.pem -out ${1}/${1}PublicKey.pem
    openssl pkcs8 -topk8 -nocrypt -inform pem -in ./${1}/${1}RSAPrivateKey.pem -outform pem -out ./${1}/${1}PKCS8PrivateKey.pem
}

generate store
#generate order
#generatecd warehouse