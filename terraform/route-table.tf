resource "aws_route_table" "k8s-public-rt" {
  vpc_id = aws_vpc.k8s-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.k8s-igw.id
  }

  tags = {
    Name = "k8s-public-rt"
  }
  
}

resource "aws_route_table_association" "k8s-public-rt-ass" {
  subnet_id = aws_subnet.public-subnet.id
  route_table_id = aws_route_table.k8s-public-rt.id
}