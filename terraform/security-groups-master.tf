resource "aws_security_group" "k8s-master-sg" {
  name = "k8s-master-sg"
  description = "Security group for k8s master node"
  vpc_id = aws_vpc.k8s-vpc.id

  tags = {
    Name = "k8s-master-sg"
  }
}

resource "aws_security_group_rule" "k8s-master-calico-worker-master-ipip" {
  type = "ingress"
  from_port = -1
  to_port = -1
  protocol = "4"
  security_group_id = aws_security_group.k8s-master-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-master-calico-worker-master-bgp" {
  type = "ingress"
  from_port = 179
  to_port = 179
  protocol = "tcp"
  security_group_id = aws_security_group.k8s-master-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-master-calico-master-master" {
  type = "ingress"
  from_port = 4789
  to_port = 4789
  protocol = "udp"
  security_group_id = aws_security_group.k8s-master-sg.id
  source_security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-master-calico-worker-master" {
  type = "ingress"
  from_port = 4789
  to_port = 4789
  protocol = "udp"
  security_group_id = aws_security_group.k8s-master-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "master-aspiserver" {
  type = "ingress"
  from_port = 6443
  to_port = 6443
  protocol = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-master-etcd" {
  type = "ingress"
  from_port = 2379
  to_port = 2379
  protocol = "tcp"
  source_security_group_id = aws_security_group.k8s-master-sg.id
  security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-master-kubelet" {
  type = "ingress"
  from_port = 10250
  to_port = 10250
  protocol = "tcp"
  source_security_group_id = aws_security_group.k8s-master-sg.id
  security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "master-ssh" {
  type = "ingress"
  from_port = 22
  to_port = 22
  protocol = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "master-egress" {
  type = "egress"
  from_port = 0
  to_port = 0
  protocol = "-1"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.k8s-master-sg.id
}