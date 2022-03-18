#! /bin/sh

microk8s enable ingress
microk8s enable dns
microk8s kubectl create -f k8s_3_levels.yml

