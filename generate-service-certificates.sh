#!/bin/sh

SECURITY="security"
PASSWORD="changeit"
rm -rf ${SECURITY}
mkdir ${SECURITY}

function generate {
    echo "generating keyPair for service ${1}"
    openssl req -passout pass:${PASSWORD} -days 365 -subj /CN=${1}/  -newkey rsa:2048 -nodes -keyout ${SECURITY}/${1}.key -out ${SECURITY}/${1}.csr
    openssl x509 -signkey ${SECURITY}/${1}.key -in ${SECURITY}/${1}.csr -req -days 365 -out ${SECURITY}/${1}.crt
    openssl pkcs12 -password pass:${PASSWORD} -inkey ${SECURITY}/${1}.key -in ${SECURITY}/${1}.crt -export -out ${1}/${1}.p12
    keytool -noprompt -import -storetype PKCS12 -storepass ${PASSWORD} -file ${SECURITY}/${1}.crt -alias ${1} -keystore ${SECURITY}/truststore.p12
}

generate store
generate order
generate warehouse

cp -f ${SECURITY}/truststore.p12 store/
cp -f ${SECURITY}/truststore.p12 order/
cp -f ${SECURITY}/truststore.p12 warehouse/