#!/usr/bin/bash
kubectl apply -f ../../base/mutating-webhook-namespace.yaml
kubectl apply -f ../../base/secrets/docker-hub-secret-mutating.yaml
kubectl apply -f ../../base/secrets/webhook-tls-secret.yaml
kubectl apply -f ../../base/admission-controller-service.yaml
kubectl apply -f ../../base/admission-controller-deployment.yaml
kubectl apply -f ../../base/mutating-webhook-configuration.yaml
