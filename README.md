## kube-backend

Providing unified API to manage mutiple Kubernetes clusters:

- RuntimeMirror: extract all objects during runtime by Kubernetes' 'watch' API, then write to a database and publish to a MQ using the JSON style.
- ApiMapper: define an unified APIs <clusterId, operator, json>, which include create, update, delete, list, get
  - create/update/delete: invoking Kubernetes apiserver directly
  - query/get: access to the database using SQL  

This project is based on the following softwares.

|               NAME            |   Website                       |      LICENSE              | 
|-------------------------------|---------------------------------|---------------------------|
|     datafrk                   |  https://github.com/kubesys/datafrk              |  Apache License 2.0 |
|     alibaba druid             |  https://github.com/alibaba/druid                |  Apache License 2.0 |



This project should work with the following components.

- [Kubernetes](https://github.com/kubernetes/kubernetes)
- [kube-frontend](https://github.com/kubesys/kube-frontend)


If these componets are not working, using ['kubeinit'](https://github.com/kubesys/kube-installer) tool.

```
kubeinst init-env
kubeinst init-kube
kubeinst init-backend
kubeinst init-frontend
````


## Authos

- wuheng@iscas.ac.cn

## Usage

### Quick start for single cluster

```
git clone https://github.com/kubesys/kube-backend.git
bash run.sh
```

### Config as you want (optional)

```
// both runtime-mirror and api-mapper
jdbcType=postgres // (postgres or mysql)
jdbcDriver=org.postgresql.Driver //optional, default is 'org.postgresql.Driver'
jdbcHost=127.0.0.1 // optional, default is 'kube-database.kube-system'
jdbcPort=5432  // optional, postgres is 5432, mysql is 3306
jdbcDB=kube  // optional, default is 'kube'
jdbcUser=postgres   // optional, postgres is 'postgres', mysql is 'root'
jdbcPassword=onceas // optional, default is 'onceas'

// both runtime-mirror and api-mapper
kubeToken=xxx     // see project kubernetes-client-java
kubeUrl=https://39.100.71.73:6443     // see project kubernetes-client-java

// region
region=local
```

## Architecture

If you have just a cluster, you should deploy all components in it. 

![avatar](/docs/arch-single.png)

Otherwise, you need deploy ApiMapper, Database and MessageQueue together,
and deploy a RuntimMirror component for each Kubernetes cluster.

![avatar](/docs/arch-mutiple.png)

## Roadmap

- 2.0.x: support MQ
  - 2.0.1: merge codes from [kubernetes-mirror](https://github.com/syswu/kubernetes-mirror) and [kubernetes-api-mapper](https://github.com/syswu/kubernetes-api-mapper) 
  - 2.0.2: support AMQP and generate default frontend
  - 2.0.4: using Secret replaces User
