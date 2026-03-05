data "aws_vpc" "rds-vpc" {
  id = "vpc-07d9cb5c867588697"
}

data "aws_route_table" "rds-rt" {
  vpc_id = data.aws_vpc.rds-vpc.id
}

resource "aws_vpc_peering_connection" "k8s-rs-peering" {
  vpc_id = aws_vpc.k8s-vpc.id
  peer_vpc_id = data.aws_vpc.rds-vpc.id
  auto_accept = true
}

resource "aws_route" "k8s-to-rds" {
  route_table_id = aws_route_table.k8s-public-rt.id
  destination_cidr_block = data.aws_vpc.rds-vpc.cidr_block
  vpc_peering_connection_id = aws_vpc_peering_connection.k8s-rs-peering.id
}

resource "aws_route" "rds-to-k8s" {
  route_table_id = data.aws_route_table.rds-rt.id
  destination_cidr_block = aws_vpc.k8s-vpc.cidr_block
  vpc_peering_connection_id = aws_vpc_peering_connection.k8s-rs-peering.id
}