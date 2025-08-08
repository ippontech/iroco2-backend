resource "aws_iam_role" "service" {
  name = "${local.app_name}-service-execution"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": [
          "ecs.amazonaws.com",
          "ecs-tasks.amazonaws.com",
          "ecs.application-autoscaling.amazonaws.com",
          "logs.amazonaws.com"
        ]
      },
      "Effect": "Allow",
      "Sid": "1"
    }
  ]
}
EOF
}

resource "aws_iam_policy" "service" {
  name        = "${local.app_name}-service-execution"
  description = "Execution role for ${local.app_name}"
  path        = "/"
  policy      = data.aws_iam_policy_document.service.json
}

data "aws_iam_policy_document" "service" {
  statement {
    actions = [
      "ecr:BatchGetImage",
      "ecr:GetAuthorizationToken",
      "ecr:DescribeImages",
      "ecr:GetDownloadUrlForLayer",
      "ecr:ListImages",
      "logs:*"
    ]
    resources = [
      "*"
    ]
  }
  statement {
    sid = "KMSDecrypt"
    actions = [
      "kms:Decrypt"
    ]
    resources = [
      data.aws_kms_alias.secrets_manager.target_key_arn
    ]
  }
  statement {
    sid = "GetSecrets"
    actions = [
      "secretsmanager:DescribeSecret",
      "secretsmanager:GetSecretValue",
    ]
    resources = concat(
      values(var.task_container_secrets_arn),
      distinct([for secret in values(var.task_container_secrets_arn_with_key) : secret.arn])
    )
  }
}

resource "aws_iam_role_policy_attachment" "service" {
  role       = aws_iam_role.service.name
  policy_arn = aws_iam_policy.service.arn
}
