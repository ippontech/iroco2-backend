resource "aws_ecs_service" "main" {
  name = local.app_name

  cluster                = data.aws_ecs_cluster.cluster.id
  launch_type            = "FARGATE"
  platform_version       = "1.4.0"
  task_definition        = aws_ecs_task_definition.api.arn
  desired_count          = var.container_desired_count
  enable_execute_command = true

  load_balancer {
    target_group_arn = aws_lb_target_group.api.arn
    container_name   = var.project_name
    container_port   = var.container_port
  }
  network_configuration {
    subnets         = data.aws_subnets.private_subnet_ids.ids
    security_groups = [aws_security_group.service.id]
  }

  depends_on = [
    aws_iam_policy.service,
    aws_cloudwatch_log_group.app-logs
  ]
}

resource "aws_security_group" "service" {
  name        = "${local.app_name}-service"
  description = "Allow HTTP inbound traffic"
  vpc_id      = data.aws_vpc.vpc.id

  ingress {
    from_port = var.container_port
    to_port   = var.container_port
    protocol  = "tcp"
    security_groups = [
      data.aws_security_group.alb.id
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
