resource "aws_subnet" "public-subnet" {
  vpc_id = aws_vpc.k8s-vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "eu-central-1b"
  map_public_ip_on_launch = true

  tags = {
    Name = "k8s-public-subnet"
  }

}