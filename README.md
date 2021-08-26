## kube-backend

Providing unified API to manage mutiple Kubernetes clusters:

- RuntimeMirror: extract all objects during runtime by Kubernetes' 'watch' API, then write to a database and publish to a MQ using the JSON style.
- ApiMapper: define an unified API <clusterId, operator, json>, the operators include create, update, delete, list, get
  - create/update/delete: invoking Kubernetes apiserver directly
  - query/get: access to the database using SQL  

## Authos

- wuheng@iscas.ac.cn

## Architecture

If you have just a cluster, you should deploy all components in it. 

![avatar](/docs/arch-single.png)

Otherwise, you need deploy ApiMapper, Database and MessageQueue together,
and deploy a RuntimMirror component for each Kubernetes cluster.

![avatar](/docs/arch-mutiple.png)
