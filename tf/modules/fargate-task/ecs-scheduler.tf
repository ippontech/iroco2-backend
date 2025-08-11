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

resource "aws_scheduler_schedule" "turn_off_in_the_evening" {

  name = "${aws_ecs_service.main.name}-turn_off_in_the_evening"

  schedule_expression          = "cron(0 20 ? * MON-FRI *)"
  schedule_expression_timezone = "Europe/Paris"

  target {
    arn      = "arn:aws:scheduler:::aws-sdk:ecs:updateService"
    role_arn = aws_iam_role.scheduler.arn

    retry_policy {
      maximum_retry_attempts = 1
    }

    input = jsonencode({
      Cluster      = var.cluster_name[var.deploy_account]
      Service      = aws_ecs_service.main.name
      DesiredCount = 0
    })
  }

  flexible_time_window {
    mode = "OFF"
  }
}

resource "aws_scheduler_schedule" "turn_on_in_the_morning" {
  name = "${aws_ecs_service.main.name}-turn_on_in_the_morning"

  schedule_expression          = "cron(30 8 ? * MON-FRI *)"
  schedule_expression_timezone = "Europe/Paris"

  target {
    arn      = "arn:aws:scheduler:::aws-sdk:ecs:updateService"
    role_arn = aws_iam_role.scheduler.arn

    retry_policy {
      maximum_retry_attempts = 1
    }

    input = jsonencode({
      Cluster      = var.cluster_name[var.deploy_account]
      Service      = aws_ecs_service.main.name
      DesiredCount = var.container_desired_count
    })
  }

  flexible_time_window {
    mode = "OFF"
  }
}

resource "aws_iam_role" "scheduler" {
  name               = "${aws_ecs_service.main.name}-office-hours-scaling"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"

    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["scheduler.amazonaws.com"]
    }
  }
}

data "aws_iam_policy_document" "allow_update_service" {
  statement {
    actions = ["ecs:UpdateService"]

    resources = [aws_ecs_service.main.id]
  }
}

resource "aws_iam_role_policy" "allow_update_service" {
  role   = aws_iam_role.scheduler.name
  policy = data.aws_iam_policy_document.allow_update_service.json
}
