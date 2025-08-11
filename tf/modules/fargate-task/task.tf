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

locals {
  task_environment = [
    for k, v in var.task_container_environment : {
      name  = k
      value = v
    }
  ]
  task_secrets_arn = [
    for k, v in var.task_container_secrets_arn : {
      name      = k
      valueFrom = v
    }
  ]
  task_secrets_arn_with_key = [
    for k, v in var.task_container_secrets_arn_with_key : {
      name      = k
      valueFrom = "${v.arn}:${v.key}::"
    }
  ]
}

resource "aws_ecs_task_definition" "api" {
  family                   = local.app_name
  network_mode             = "awsvpc"
  execution_role_arn       = aws_iam_role.service.arn
  task_role_arn            = aws_iam_role.task.arn
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  container_definitions = jsonencode([
    {
      name        = var.project_name
      image       = var.container_image
      networkMode = "awsvpc"

      essential   = true
      environment = local.task_environment
      secrets     = concat(local.task_secrets_arn, local.task_secrets_arn_with_key)
      portMappings = [
        {
          containerPort = var.container_port
          hostPort      = var.container_port
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = aws_cloudwatch_log_group.app-logs.name
          awslogs-region        = var.region
          awslogs-stream-prefix = "ecs"
        }
      }

    }
  ])
}

resource "aws_cloudwatch_log_group" "app-logs" {
  name              = "/ecs/service/${local.app_name}/app-logs"
  retention_in_days = 5
}
