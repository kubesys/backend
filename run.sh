#! /bin/bash
###############################################
##
##  Copyright (2020, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################


kubectl apply -f yamls/kube-token.yaml
token=$(kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep kubernetes-client | awk '{print $1}') | grep "token:" | awk -F":" '{print$2}' | sed 's/ //g')


arch=""

if [[ $(arch) == "x86_64" ]]
then
  arch="amd64"
elif [[ $(arch) == "aarch64" ]]
then
  arch="arm64"
else
  echo "only support x86_64(amd64) and aarch64(arm64)"
  exit 1
fi

mirror="kube-runtime-mirror"
mapper="kube-api-mapper"
version=$(cat $mirror/pom.xml | grep version | head -1 | awk -F">" '{print$2}' | awk -F"<" '{print$1}')

type="postgres"
name=$(hostname)
host=$(kubectl get no $name -o yaml | grep "\- address:" | head -1 | awk -F":" '{print$2}' | sed  's/^[ \t]*//g')
port="30306"
url="https://"$host":6443"

sed -i "s/#type#/$type/g" yamls/$mirror.yaml
sed -i "s/#host#/$host/g" yamls/$mirror.yaml
sed -i "s/#port#/$port/g" yamls/$mirror.yaml
sed -i "s/#url#/$url/g" yamls/$mirror.yaml
sed -i "s/#token#/$token/g" yamls/$mirror.yaml
sed -i "s/#version#/$version/g" yamls/$mirror.yaml
sed -i "s/#arch#/$arch/g" yamls/$mirror.yaml

sed -i "s/#type#/$type/g" yamls/$mapper.yaml
sed -i "s/#host#/$host/g" yamls/$mapper.yaml
sed -i "s/#port#/$port/g" yamls/$mapper.yaml
sed -i "s/#url#/$url/g" yamls/$mapper.yaml
sed -i "s/#token#/$token/g" yamls/$mapper.yaml
sed -i "s/#version#/$version/g" yamls/$mapper.yaml
sed -i "s/#arch#/$arch/g" yamls/$mapper.yaml

kubectl apply -f yamls/$mirror.yaml
kubectl apply -f yamls/$mapper.yaml
