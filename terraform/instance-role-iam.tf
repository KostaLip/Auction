resource "aws_iam_role" "k8s-nodes-role" {
  name = "k8s-node-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy" "k8s-ssm-policy" {
  name = "k8s-ssm-policy"
  role = aws_iam_role.k8s-nodes-role.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ssm:PutParameter",
          "ssm:GetParameter",
          "ssm:DeleteParameter"
        ]
        Resource = "arn:aws:ssm:*:*:parameter/k8s/*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "k8s-s3-attachment" {
  role = aws_iam_role.k8s-nodes-role.name
  policy_arn = "arn:aws:iam::093410165669:policy/auction-logs-put-policy"
}

resource "aws_iam_instance_profile" "k8s-node-profile" {
  name = "k8s-node-profile"
  role = aws_iam_role.k8s-nodes-role.name
}