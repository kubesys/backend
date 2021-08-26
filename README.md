## kube-backend
providing unified API to manage mutiple Kubernetes clusters, we plan to support

- SQL
  - Mysql/MariaDB
  - PostgresSQL
  - Sqlite
- NoSQL
  - Key-value
    - Redis
    - Memcached
  - Table-oriented
    - Cassandra
    - HBase
  - Document-oriented
    - MongDB
    - CouchDB
- NewSQL
  - CockroachDB

## Authos

- wuheng@iscas.ac.cn

## Architecture

If you have just a cluster, you should deploy all components in it. 

![avatar](/docs/arch-single.png)

Otherwise, you need deploy ApiMapper, Database and MessageQueue together,
and deploy a RuntimMirror component for each Kubernetes cluster.

![avatar](/docs/arch-mutiple.png)
