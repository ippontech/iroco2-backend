## Networking
# Target Group
resource "aws_lb_target_group" "api" {
  name        = "${substr(local.app_name, 0, 15)}-${substr(uuid(), 0, 3)}"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.vpc.id
  target_type = "ip"

  health_check {
    enabled             = true
    path                = local.healthcheck_path
    matcher             = "200"
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 10
  }

  lifecycle {
    create_before_destroy = true
    ignore_changes        = [name]
  }
}

resource "aws_lb_listener_rule" "api" {
  listener_arn = data.aws_lb_listener.alb_80.arn

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api.arn
  }

  condition {
    path_pattern {
      values = ["/*"]
    }
  }

}
