#!/usr/bin/bash
set -e

REGION="eu-central-1"

until aws ssm get-parameter \
  --region $REGION \
  --name "/k8s/join-command" \
  --query Parameter.Value \
  --output text 2>/dev/null; do
  sleep 15
done

JOIN_CMD=$(aws ssm get-parameter \
  --region $REGION \
  --name "/k8s/join-command" \
  --query Parameter.Value \
  --output text)

eval $JOIN_CMD