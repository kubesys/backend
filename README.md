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


## Authos

- wuheng@iscas.ac.cn
- xugang@iscas.ac.cn

## API

### 1. 登陆 /system/login

POST请求

```
{
    "kind": "User",
    "data": {
        "name": "admin",
        "password": "b25jZWFz"
    }
}
```

### 2. 登出 /system/logout

POST请求

Headers
```
authorization: bearer token (see response from login)
user: see name in login
```

### 3. 创建 /kube/createResource

POST请求

Headers
```
authorization: bearer token (see response from login)
user: see name in login
```

Body (see k8s json)
```
{
   "kind": ""
   others:
}
```

### 3. 更新 /kube/updateResource

POST请求

Headers
```
authorization: bearer token (see response from login)
user: see name in login
```

Body (see k8s json)
```
{
   "kind": ""
   others:
}
```

### 3. 更新 /kube/deleteResource

POST请求

Headers
```
authorization: bearer token (see response from login)
user: see name in login
```

Body (see k8s json)
```
{
   "kind": ""
   others:
}
```

## Roadmap

- 2.3.x: support single region
