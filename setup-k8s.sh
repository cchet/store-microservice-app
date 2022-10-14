#!/bin/sh
set -e
SECRETS_DIR=secrets
INFRA_DIR=infrastructure
SERVICES_DIR=${INFRA_DIR}/k8s
KEYCLOAK_IMPORT_DIR=${INFRA_DIR}/keycloak/import
GENERATED_DIR=${SERVICES_DIR}/generated-yaml
GENERATED_CONFIG_DIR=${SERVICES_DIR}/generated-configs
TEMPLATES_DIR=${SERVICES_DIR}/templates
rm -rf ${GENERATED_DIR} ${GENERATED_CONFIG_DIR}
mkdir ${GENERATED_DIR} 
mkdir ${GENERATED_CONFIG_DIR}

function clean {
    kubectl delete all --all --now=true
    kubectl delete secret --all --now=true
    kubectl delete configmap --all --now=true
    kubectl delete pvc --all --now=true
}

function create_service_secret {
    source ${SECRETS_DIR}/${1}.env
    kubectl create secret generic ${1} \
        --from-file=keystore.p12=${SECRETS_DIR}/${1}.p12 \
        --from-file=truststore.p12=${SECRETS_DIR}/truststore.p12 \
        --from-literal=keystore_password=${KEY_STORE_PASSWORD} \
        --from-literal=truststore_password=${TRUST_STORE_PASSWORD} \
        --from-literal=db_password=${DB_PASSWORD} \
        --from-literal=client_secret=${CLIENT_SECRET} \
        --dry-run=client -o yaml > ${GENERATED_DIR}/${1}-secret.yaml
}

function create_sso_secret {
    source ${SECRETS_DIR}/sso.env
    kubectl create secret generic sso \
        --from-file=keystore.p12=${SECRETS_DIR}/sso.p12 \
        --from-file=truststore.p12=${SECRETS_DIR}/truststore.p12 \
        --from-literal=keystore_password=${KEY_STORE_PASSWORD} \
        --from-literal=truststore_password=${TRUST_STORE_PASSWORD} \
        --from-literal=admin_password=${ADMIN_PASSWORD} \
        --from-literal=db_password=${DB_PASSWORD} \
        --dry-run=client -o yaml > ${GENERATED_DIR}/sso-secret.yaml
}

function create_sso_config {
    source ${SECRETS_DIR}/sso.env
    sed -e "s/client-secret-store/${CLIENT_SECRET_STORE}/g" \
        -e "s/client-secret-order/${CLIENT_SECRET_ORDER}/g" \
        -e "s/client-secret-warehouse/${CLIENT_SECRET_WAREHOUSE}/g" \
        ${KEYCLOAK_IMPORT_DIR}/store-realm.json > ${GENERATED_CONFIG_DIR}/store-realm.json
    kubectl create configmap sso --from-file=store-realm.json=${GENERATED_CONFIG_DIR}/store-realm.json
}

function create_postgres_yaml {
    sed -e "s/{NAME}/${1}/g" ${TEMPLATES_DIR}/postgresql.yaml > ${GENERATED_DIR}/${1}-postgresql.yaml
}

# delete/create namespace
clean

# Create secrets yaml files
create_service_secret store
create_service_secret order
create_service_secret warehouse
create_sso_secret
create_sso_config

# prepare postgresql yamls
create_postgres_yaml store
create_postgres_yaml order
create_postgres_yaml warehouse
create_postgres_yaml sso

kubectl create -f ${GENERATED_DIR}
kubectl create -f ${SERVICES_DIR}
