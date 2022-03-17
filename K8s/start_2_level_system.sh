#! /bin/sh

microk8s enable ingress
microk8s enable dns
microk8s kubectl create -f two_level_system.yml

