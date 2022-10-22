#!/bin/sh

set -e

SECRETS_DIR="secrets"
INFRA_DIR="infrastructure"
COMPOSE_DIR="${INFRA_DIR}/compose"
TRUST_STORE_PASSWORD="$(openssl rand -hex 12)"
rm -rf ${SECRETS_DIR}
mkdir ${SECRETS_DIR}

function generate_certificate {
    openssl req -passout pass:"${2}" -days 365 -subj "/CN=${1}.store.mk8s.local/O=${1}.store.mk8s.local"  -newkey rsa:2048 -nodes -keyout ${SECRETS_DIR}/${1}.key -out ${SECRETS_DIR}/${1}.csr
    openssl x509 -signkey ${SECRETS_DIR}/${1}.key -in ${SECRETS_DIR}/${1}.csr -req -days 365 -out ${SECRETS_DIR}/${1}.crt
    openssl pkcs12 -password pass:"${2}" -inkey ${SECRETS_DIR}/${1}.key -in ${SECRETS_DIR}/${1}.crt -export -out ${SECRETS_DIR}/${1}.p12
    keytool -noprompt -import -storetype PKCS12 -storepass ${TRUST_STORE_PASSWORD} -file ${SECRETS_DIR}/${1}.crt -alias ${1} -keystore ${SECRETS_DIR}/truststore.p12
}

function generate_env_file {
    touch ${SECRETS_DIR}/${1}.env
    echo "KEY_STORE_PASSWORD=${2}" >> ${SECRETS_DIR}/${1}.env
    echo "TRUST_STORE_PASSWORD=${TRUST_STORE_PASSWORD}" >> ${SECRETS_DIR}/${1}.env
    echo "DB_PASSWORD=$(openssl rand -hex 12) " >> ${SECRETS_DIR}/${1}.env
}

function generate_service_secrets {
    local KEY_STORE_PASSWORD=$(openssl rand -hex 12) 
    local CLIENT_SECRET=$(openssl rand -hex 12) 
    
    generate_certificate ${1} ${KEY_STORE_PASSWORD}
    generate_env_file ${1} ${KEY_STORE_PASSWORD}

    cp -f ${SECRETS_DIR}/${1}.env ${1}/.env
    cp -f ${SECRETS_DIR}/${1}.p12 ${1}/keystore.p12
}

function generate_sso_secrets {
    local KEY_STORE_PASSWORD=$(openssl rand -hex 12) 
    local CLIENT_SECRET_STORE=$(openssl rand -hex 12)
    local CLIENT_SECRET_ORDER=$(openssl rand -hex 12)
    local CLIENT_SECRET_WAREHOUSE=$(openssl rand -hex 12)

    generate_certificate sso ${KEY_STORE_PASSWORD}
    generate_env_file sso ${KEY_STORE_PASSWORD}
    echo "ADMIN_PASSWORD=$(openssl rand -hex 12)" >> ${SECRETS_DIR}/sso.env
    echo "CLIENT_SECRET_STORE=${CLIENT_SECRET_STORE}" >> ${SECRETS_DIR}/sso.env
    echo "CLIENT_SECRET_ORDER=${CLIENT_SECRET_ORDER}" >> ${SECRETS_DIR}/sso.env
    echo "CLIENT_SECRET_WAREHOUSE=${CLIENT_SECRET_WAREHOUSE}" >> ${SECRETS_DIR}/sso.env

    # Set client_secrets on service .env files
    echo "CLIENT_SECRET=${CLIENT_SECRET_STORE}" >> ${SECRETS_DIR}/store.env
    echo "CLIENT_SECRET=${CLIENT_SECRET_ORDER}" >> ${SECRETS_DIR}/order.env
    echo "CLIENT_SECRET=${CLIENT_SECRET_WAREHOUSE}" >> ${SECRETS_DIR}/warehouse.env

    cp -f ${SECRETS_DIR}/sso.env ${COMPOSE_DIR}/.env
}

generate_service_secrets store
generate_service_secrets order
generate_service_secrets warehouse
generate_sso_secrets

cp -f ${SECRETS_DIR}/truststore.p12 store/truststore.p12
cp -f ${SECRETS_DIR}/truststore.p12 order/truststore.p12
cp -f ${SECRETS_DIR}/truststore.p12 warehouse/truststore.p12