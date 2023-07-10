FROM openjdk:17.0.2-slim

RUN mkdir -p /home/kube-backend

ADD kube-backend.jar /home/kube-backend/
ADD config /home/kube-backend/

WORKDIR /home/kube-backend