#!/usr/bin/bash
set -e

REGION="eu-central-1"
aws ssm delete-parameter --region $REGION --name "/k8s/join-command" 2>/dev/null || true
PUBLIC_IP=$(curl -s https://checkip.amazonaws.com)
PRIVATE_IP=$(curl -s http://169.254.169.254/latest/meta-data/local-ipv4)


export KUBECONFIG=/etc/kubernetes/admin.conf

kubeadm init \
  --apiserver-advertise-address=$PRIVATE_IP \
  --apiserver-cert-extra-sans=$PUBLIC_IP \
  --pod-network-cidr=192.168.0.0/16

mkdir -p /root/.kube
cp /etc/kubernetes/admin.conf /root/.kube/config

mkdir -p /home/ubuntu/.kube
cp /etc/kubernetes/admin.conf /home/ubuntu/.kube/config
chown ubuntu:ubuntu /home/ubuntu/.kube/config

kubectl apply -f https://raw.githubusercontent.com/projectcalico/calico/v3.27.0/manifests/calico.yaml

JOIN_CMD=$(kubeadm token create --print-join-command)
aws ssm put-parameter \
  --region $REGION \
  --name "/k8s/join-command" \
  --value "$JOIN_CMD" \
  --type String \
  --overwrite