#! /bin/sh

microk8s enable ingress
microk8s enable dns
microk8s kubectl create -f four_level_system.yml

