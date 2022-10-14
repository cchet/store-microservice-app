#!/bin/sh
set -x
SECURITY_DIR=security
SERVICES_DIR=infrastructure/k8s
GENERATED=${SERVICES_DIR}/generated
TEMPLATES=${SERVICES_DIR}/templates
rm -rf ${GENERATED}
mkdir ${GENERATED}

function clean {
    kubectl delete all --all --now=true
    kubectl delete secret --all --now=true
    kubectl delete pvc --all --now=true
}

function create_secret {
    source ${SECURITY_DIR}/${1}/.env
    kubectl create secret generic ${1} \
        --from-file=${1}.p12=${SECURITY_DIR}/${1}/${1}.p12 \
        --from-file=truststore.p12=${SECURITY_DIR}/truststore.p12 \
        --from-literal=keystore.password=${KEY_STORE_PASSWORD} \
        --from-literal=truststore.password=${TRUST_STORE_PASSWORD} \
        --from-literal=db.password=${DB_PASSWORD} \
        --dry-run=client -o yaml > ${GENERATED}/${1}-secret.yaml
}

function create_postgres_yaml {
    sed -e "s/{NAME}/${1}/g" ${TEMPLATES}/postgresql.yaml > ${GENERATED}/${1}-postgresql.yaml
}

# delete/create namespace
clean

# Create secrets yaml files
create_secret store
create_secret order
create_secret warehouse
create_secret sso
kubectl create secret generic ${1} 

# prepare postgresql yamls
create_postgres_yaml store
create_postgres_yaml order
create_postgres_yaml warehouse
create_postgres_yaml sso

kubectl create -f ${GENERATED}
kubectl create -f ${SERVICES_DIR}
