resource "aws_vpc_endpoint" "s3" {
  vpc_id = aws_vpc.k8s-vpc.id
  service_name = "com.amazonaws.eu-central-1.s3"
  route_table_ids = [aws_route_table.k8s-public-rt.id]
}