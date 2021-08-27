## kube-backend

Providing unified API to manage mutiple Kubernetes clusters:

- RuntimeMirror: extract all objects during runtime by Kubernetes' 'watch' API, then write to a database and publish to a MQ using the JSON style.
- ApiMapper: define an unified API <clusterId, operator, json>, the operators include create, update, delete, list, get
  - create/update/delete: invoking Kubernetes apiserver directly
  - query/get: access to the database using SQL  

## Authos

- wuheng@iscas.ac.cn

## Usage

### enviroment

```
jdbcType=postgres // (postgres or mysql)
jdbcDriver=org.postgresql.Driver //optional, default is 'org.postgresql.Driver'

// jdbc:postgresql://127.0.0.1:5432/kube
jdbcHost=127.0.0.1 // optional, default is 'kube-database.kube-system'
jdbcPort=5432  // optional, postgres is 5432, mysql is 3306
jdbcDB=kube  // optional, default is 'kube'

jdbcUser=postgres   // optional, postgres is 'postgres', mysql is 'root'
jdbcPassword=onceas // optional, default is 'onceas'

kubeToken=xxx     // see project kubernetes-client-java
kubeUrl=https://39.100.71.73:6443     // see project kubernetes-client-java
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
