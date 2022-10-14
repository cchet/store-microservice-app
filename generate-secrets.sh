#!/bin/sh

SECURITY="security"
TRUSTSTORE_PASSWORD="$(openssl rand -hex 12)"
rm -rf ${SECURITY}
mkdir ${SECURITY}

function generate_secret_env {
    mkdir ${SECURITY}/${1}
    touch ${SECURITY}/${1}/.env
    echo "KEY_STORE_PASSWORD=$(openssl rand -hex 12)" >> ${SECURITY}/${1}/.env
    echo "DB_PASSWORD=$(openssl rand -hex 12)" >> ${SECURITY}/${1}/.env
    echo "TRUST_STORE_PASSWORD=${TRUSTSTORE_PASSWORD}" >> ${SECURITY}/${1}/.env
}

function generate_certificates {
    echo "generating keyPair for service ${1}"
    source ${SECURITY}/${1}/.env
    openssl req -passout pass:"${KEY_STORE_PASSWORD}" -days 365 -subj /CN=${1}/  -newkey rsa:2048 -nodes -keyout ${SECURITY}/${1}.key -out ${SECURITY}/${1}.csr
    openssl x509 -signkey ${SECURITY}/${1}.key -in ${SECURITY}/${1}.csr -req -days 365 -out ${SECURITY}/${1}/${1}.crt
    openssl pkcs12 -password pass:"${KEY_STORE_PASSWORD}" -inkey ${SECURITY}/${1}.key -in ${SECURITY}/${1}/${1}.crt -export -out ${SECURITY}/${1}/${1}.p12
    keytool -noprompt -import -storetype PKCS12 -storepass ${TRUST_STORE_PASSWORD} -file ${SECURITY}/${1}/${1}.crt -alias ${1} -keystore ${SECURITY}/truststore.p12
}

function enrich_env {
    source ${SECURITY}/${1}/.env
    echo "${1}_DB_PASSWORD=${DB_PASSWORD}" >> ${SECURITY}/${2}/.env
}

generate_secret_env store
generate_secret_env order
generate_secret_env warehouse
generate_secret_env sso

generate_certificates store
generate_certificates order
generate_certificates warehouse
generate_certificates sso

enrich_env store sso
enrich_env order sso
enrich_env warehouse sso

cp -rf ${SECURITY}/truststore.p12 ${SECURITY}/store/ store/
cp -rf ${SECURITY}/truststore.p12 ${SECURITY}/order/ order/
cp -rf ${SECURITY}/truststore.p12 ${SECURITY}/warehouse/ warehouse/
cp -rf ${SECURITY}/truststore.p12 ${SECURITY}/sso/ infrastructure/compose
cp -rf ${SECURITY}/truststore.p12 ${SECURITY}/sso/ infrastructure/k8s