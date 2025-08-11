# Copyright 2025 Ippon Technologies
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0

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
