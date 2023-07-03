## kube-backend

Providing unified API to manage mutiple Kubernetes clusters:

- RuntimeMirror: extract all objects during runtime by Kubernetes' 'watch' API, then write to a database and publish to a MQ using the JSON style.
- ApiMapper: define an unified APIs <clusterId, operator, json>, which include create, update, delete, list, get
  - create/update/delete: invoking Kubernetes apiserver directly
  - query/get: access to the database using SQL  

This project is based on the following softwares.

|               NAME            |   Website                       |      LICENSE              | 
|-------------------------------|---------------------------------|---------------------------|
|     client-java               |  https://github.com/kubesys/client-java              |  Apache License 2.0 |
|     devfrk-java               |  https://github.com/kubesys/devfrk-java              |  Apache License 2.0 |



This project should work with the following components.

- [Kubernetes](https://github.com/kubernetes/kubernetes)
- [frontend](https://github.com/kubesys/frontend)


If these componets are not working, using ['kubeinit'](https://github.com/kubesys/kube-installer) tool.

```
kubeinst init-env
kubeinst init-kube
kubeinst 
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
jdbcUrl=jdbc:postgresql://localhost:5432/mydatabase // (postgres or mysql)
jdbcUser=postgres   // optional, postgres is 'postgres', mysql is 'root'
jdbcPassword=xxx // optional, default is 'onceas'
jdbcDriver=org.postgresql.Driver //optional, default is 'org.postgresql.Driver'
// both runtime-mirror and api-mapper
kubeToken=xxx     // see project kubernetes-client-java
kubeUrl=https://39.100.71.73:6443     // see project kubernetes-client-java
// region
kubeRegion=local
```

## Architecture

If you have just a cluster, you should deploy all components in it. 

![avatar](/docs/arch-single.png)

Otherwise, you need deploy ApiMapper, Database and MessageQueue together,
and deploy a RuntimMirror component for each Kubernetes cluster.

![avatar](/docs/arch-mutiple.png)

## Roadmap

- 2.3.x: support single region
