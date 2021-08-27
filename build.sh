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
