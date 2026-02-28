resource "aws_lb" "k8s-nlb" {
  name = "k8s-nlb"
  internal = false
  load_balancer_type = "network"
  subnets = [aws_subnet.public-subnet.id]

  tags = {
    Name = "k8s-nlb"
  }
}

resource "aws_lb_target_group" "k8s-nlb-tg-http" {
  name = "k8s-nlb-tg"
  port = 31000
  protocol = "TCP"
  vpc_id = aws_vpc.k8s-vpc.id
  health_check {
    protocol = "TCP"
    port = 31000
  }
}

resource "aws_lb_listener" "k8s-nlb-http-listener" {
  load_balancer_arn = aws_lb.k8s-nlb.arn
  port = 80
  protocol = "TCP"
  default_action {
    type = "forward"
    target_group_arn = aws_lb_target_group.k8s-nlb-tg-http.arn
  }
}

resource "aws_lb_target_group_attachment" "k8s-nlb-http-worker-small" {
  target_group_arn = aws_lb_target_group.k8s-nlb-tg-http.arn
  target_id = aws_instance.k8s-worker-node-small.id
  port = 31000
}

resource "aws_lb_target_group_attachment" "k8s-nlb-http-worker-big" {
  target_group_arn = aws_lb_target_group.k8s-nlb-tg-http.arn
  target_id = aws_instance.k8s-worker-node-big.id
  port = 31000
}

