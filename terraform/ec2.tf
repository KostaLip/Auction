resource "aws_instance" "k8s-master-node" {
  ami = var.ami_id
  instance_type = "m7i-flex.large"
  subnet_id = aws_subnet.public-subnet.id
  vpc_security_group_ids = [aws_security_group.k8s-master-sg.id]
  iam_instance_profile = aws_iam_instance_profile.k8s-node-profile.name
  user_data = file("scripts/master.sh")

  root_block_device {
    volume_size = 20
    volume_type = "gp3"
  }

  tags = {
    Name = "k8s-master-node"
  }
}

resource "aws_instance" "k8s-worker-node-small" {
  ami = var.ami_id
  instance_type = "c7i-flex.large"
  subnet_id = aws_subnet.public-subnet.id
  vpc_security_group_ids = [aws_security_group.k8s-worker-sg.id]
  iam_instance_profile = aws_iam_instance_profile.k8s-node-profile.name
  user_data = file("scripts/worker.sh")

  root_block_device {
    volume_size = 20
    volume_type = "gp3"
  }

  tags = {
    Name = "k8s-worker-node-small"
  }
}

resource "aws_instance" "k8s-worker-node-big" {
  ami = var.ami_id
  instance_type = "m7i-flex.large"
  subnet_id = aws_subnet.public-subnet.id
  vpc_security_group_ids = [aws_security_group.k8s-worker-sg.id]
  iam_instance_profile = aws_iam_instance_profile.k8s-node-profile.name
  user_data = file("scripts/worker.sh")
  
  root_block_device {
    volume_size = 20
    volume_type = "gp3"
  }

  tags = {
    Name = "k8s-worker-node-big"
  }
}