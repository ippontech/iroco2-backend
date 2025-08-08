resource "aws_cloudwatch_metric_alarm" "autoscale_alarm" {
  alarm_name          = "${var.namespace}-${var.project_name}-${var.environment}-failover-ALARM"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "1"
  namespace           = "AWS/ApplicationELB"
  metric_name         = "UnHealthyHostCount"
  period              = "60"
  statistic           = "Average"
  threshold           = "1"

  treat_missing_data = "ignore"

  dimensions = {
    TargetGroup  = aws_lb_target_group.api.arn_suffix
    LoadBalancer = data.aws_lb.alb.arn_suffix
  }

  alarm_description = "This alarm triggers if there is 1 or more unhealthy targets for more than 1 minute."
  alarm_actions = [
    aws_appautoscaling_policy.scale_out_policy.arn,
    aws_sns_topic.failover_email_topic.arn
  ]
}

resource "aws_appautoscaling_target" "ecs_target" {
  max_capacity       = 1
  min_capacity       = 0
  resource_id        = "service/${var.cluster_name[var.deploy_account]}/${aws_ecs_service.main.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  service_namespace  = "ecs"
}


resource "aws_appautoscaling_policy" "scale_out_policy" {
  name               = "scale-out"
  policy_type        = "StepScaling"
  resource_id        = aws_appautoscaling_target.ecs_target.resource_id
  scalable_dimension = aws_appautoscaling_target.ecs_target.scalable_dimension
  service_namespace  = aws_appautoscaling_target.ecs_target.service_namespace

  step_scaling_policy_configuration {
    adjustment_type         = "ExactCapacity"
    cooldown                = 60
    metric_aggregation_type = "Average"

    step_adjustment {
      scaling_adjustment          = 0
      metric_interval_lower_bound = 0
    }
  }
}
