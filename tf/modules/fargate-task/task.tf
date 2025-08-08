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
