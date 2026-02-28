resource "aws_security_group" "k8s-worker-sg" {
  name = "k8s-worker-sg"
  vpc_id = aws_vpc.k8s-vpc.id
  description = "Security group used for worker nodes"

  tags = {
    Name = "k8s-worker-sg"
  }
}

resource "aws_security_group_rule" "k8s-worker-calico-worker-worker-bgp" {
  type = "ingress"
  from_port = 179
  to_port = 179
  protocol = "tcp"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-worker-calico-master-worker-bgp" {
  type = "ingress"
  from_port = 179
  to_port = 179
  protocol = "tcp"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-worker-calico-worker-worker-ipip" {
  type = "ingress"
  from_port = -1
  to_port = -1
  protocol = "4"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-worker-calico-master-worker-ipip" {
  type = "ingress"
  from_port = -1
  to_port = -1
  protocol = "4"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-worker-calico-worker-worker" {
  type = "ingress"
  from_port = 4789
  to_port = 4789
  protocol = "udp"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-worker-calico-master-worker" {
  type = "ingress"
  from_port = 4789
  to_port = 4789
  protocol = "udp"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-worker-kubelet" {
  type = "ingress"
  from_port = 10250
  to_port = 10250
  protocol = "tcp"
  security_group_id = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-master-sg.id
}

resource "aws_security_group_rule" "k8s-worker-kubelet-self" {
  type                     = "ingress"
  from_port                = 10250
  to_port                  = 10250
  protocol                 = "tcp"
  security_group_id        = aws_security_group.k8s-worker-sg.id
  source_security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-worker-ssh" {
  type = "ingress"
  from_port = 22
  to_port = 22
  protocol = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-worker-egress" {
  type = "egress"
  from_port = 0
  to_port = 0
  protocol = "-1"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.k8s-worker-sg.id
}

resource "aws_security_group_rule" "k8s-worker-node-port" {
  type = "ingress"
  from_port = 30000
  to_port = 32767
  protocol = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.k8s-worker-sg.id
}