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

data "aws_iam_policy_document" "task_assume_role" {
  statement {
    effect = "Allow"
    sid    = "1"

    principals {
      type = "Service"
      identifiers = [
        "ecs-tasks.amazonaws.com",
        "logs.amazonaws.com"
      ]
    }

    actions = ["sts:AssumeRole"]
  }
}

data "aws_iam_policy_document" "s3_access_doc" {
  statement {
    effect = "Allow"

    actions = [
      "s3:ListBucket",
      "s3:GetObject",
      "s3:PutObject"
    ]
    resources = ["${data.aws_s3_bucket.cur_s3_bucket.arn}",
    "${data.aws_s3_bucket.cur_s3_bucket.arn}/*"]
  }

  statement {
    effect = "Allow"

    actions = ["sqs:ListQueues",
      "sqs:ReceiveMessage",
      "sqs:GetQueueUrl",
      "sqs:DeleteMessage",
    "sqs:ChangeMessageVisibility"]

    resources = ["*"]
  }

  statement {
    effect = "Allow"

    actions = ["kms:Sign"]

    resources = [var.kms_identity_key_arn]
  }
}

resource "aws_iam_role_policy" "s3_access" {
  name   = "s3Access"
  role   = aws_iam_role.task.name
  policy = data.aws_iam_policy_document.s3_access_doc.json
}

resource "aws_iam_role" "task" {
  name = "${local.app_name}-task"

  assume_role_policy = data.aws_iam_policy_document.task_assume_role.json
}
