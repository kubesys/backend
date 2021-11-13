#! /bin/bash
###############################################
##
##  Copyright (2020, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################

MAVEN="maven:3.6.3-openjdk-11"

###############################################
##
##  Source to Jar
##
###############################################
docker run -it --net host --rm -v /root/.m2:/root/.m2 -v "$(pwd)/kube-common":/usr/src/mymaven -w /usr/src/mymaven $MAVEN mvn clean install -Dmaven.test.skip

docker run -it --net host --rm -v /root/.m2:/root/.m2 -v "$(pwd)/kube-runtime-mirror":/usr/src/mymaven -w /usr/src/mymaven $MAVEN mvn clean install -Dmaven.test.skip

docker run -it --net host --rm -v /root/.m2:/root/.m2 -v "$(pwd)/kube-api-mapper":/usr/src/mymaven -w /usr/src/mymaven $MAVEN mvn clean package -Dmaven.test.skip spring-boot:repackage


###############################################
##
##  Jar to local image
##
###############################################

mirror="kube-runtime-mirror"
mapper="kube-api-mapper"
version=$(cat $mirror/pom.xml | grep version | head -1 | awk -F">" '{print$2}' | awk -F"<" '{print$1}')
withjar="jar-with-dependencies"

\cp $mirror/target/$mirror-$version-$withjar.jar docker/$mirror.jar
\cp $mapper/target/$mapper-$version.jar docker/$mapper.jar

repo="registry.cn-beijing.aliyuncs.com/doslab"

docker buildx create --name mybuilder --driver docker-container
docker buildx use mybuilder
docker run --privileged --rm tonistiigi/binfmt --install all

docker buildx build docker/ --platform linux/arm64,linux/amd64 -t $repo/$mirror:v$version --push -f docker/Dockerfile-$mirror
docker buildx build docker/ --platform linux/arm64,linux/amd64 -t $repo/$mapper:v$version --push -f docker/Dockerfile-$mapper

