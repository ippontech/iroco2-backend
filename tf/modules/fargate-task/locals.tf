locals {
  app_name = var.environment

  app_base_path = coalesce(var.override_base_path, var.project_name)
  healthcheck_path         = "/actuator/health"
  is_prod                  = var.deploy_account == "prod"
  is_ephemeral_environment = var.environment != "main"

  deploy_account_route53_zone = var.route53_zone[var.deploy_account]
  fqdn                        = "api.${local.deploy_account_route53_zone}"
}
