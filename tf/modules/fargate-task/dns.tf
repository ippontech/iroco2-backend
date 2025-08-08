resource "aws_route53_record" "alb_public" {
  zone_id = data.aws_route53_zone.main.id
  name    = local.fqdn
  type    = "A"
  alias {
    name                   = data.aws_lb.alb.dns_name
    zone_id                = data.aws_lb.alb.zone_id
    evaluate_target_health = false
  }
}
