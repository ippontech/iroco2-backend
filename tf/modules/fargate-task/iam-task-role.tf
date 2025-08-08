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
