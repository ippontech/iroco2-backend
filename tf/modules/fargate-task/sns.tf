resource "aws_sns_topic" "failover_email_topic" {
  name = "${var.namespace}-${var.project_name}-${var.environment}-failover-email-topic"
}

resource "aws_sns_topic_subscription" "email_target" {
  count     = length(var.failover_mailing_list)
  topic_arn = aws_sns_topic.failover_email_topic.arn
  protocol  = "email"
  endpoint  = var.failover_mailing_list[count.index]
}
