#! /bin/bash
###############################################
##
##  Copyright (2020, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################

# global
mirror="kube-runtime-mirror"
mapper="kube-api-mapper"
type="postgres"
database="kube-database-"$type
dir="/opt/yamls"

## token
kubectl apply -f yamls/kube-token.yaml
token=$(kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep kubernetes-client | awk '{print $1}') | grep "token:" | awk -F":" '{print$2}' | sed 's/ //g')


## arch
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

## version
version=$(cat $mirror/pom.xml | grep version | head -1 | awk -F">" '{print$2}' | awk -F"<" '{print$1}')

# host and url
name=$(hostname | tr '[A-Z]' '[a-z]')
host=$(kubectl get no $name -o yaml | grep "\- address:" | head -1 | awk -F":" '{print$2}' | sed  's/^[ \t]*//g')
url="https://"$host":6443"

#@ port
port="30306"


## dir
mkdir -p $dir
\cp yamls/$mirror.yaml $dir/$mirror.yaml
\cp yamls/$mapper.yaml $dir/$mapper.yaml
\cp yamls/$database.yaml $dir/$database.yaml

sed -i "s/#type#/$type/g" $dir/$mirror.yaml
sed -i "s/#host#/$host/g" $dir/$mirror.yaml
sed -i "s/#port#/$port/g" $dir/$mirror.yaml
sed -i "s/#url#/$url/g" $dir/$mirror.yaml
sed -i "s/#token#/$token/g" $dir/$mirror.yaml
sed -i "s/#version#/$version/g" $dir/$mirror.yaml
sed -i "s/#arch#/$arch/g" $dir/$mirror.yaml

sed -i "s/#type#/$type/g" $dir/$mapper.yaml
sed -i "s/#host#/$host/g" $dir/$mapper.yaml
sed -i "s/#port#/$port/g" $dir/$mapper.yaml
sed -i "s/#url#/$url/g" $dir/$mapper.yaml
sed -i "s/#token#/$token/g" $dir/$mapper.yaml
sed -i "s/#version#/$version/g" $dir/$mapper.yaml
sed -i "s/#arch#/$arch/g" $dir/$mapper.yaml

kubectl apply -f $dir/$database.yaml
kubectl apply -f $dir/$mirror.yaml
kubectl apply -f $dir/$mapper.yaml
