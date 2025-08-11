# Common variables
variable "region" {
  default = "eu-west-3"
}
variable "project_name" {
  default = "irocalc-backend"
}
variable "project_type" {
  default = "backend"
}
variable "namespace" {
  default = "iroco2"
}

variable "environment" {}
variable "deploy_account" {}
variable "aws_deploy_account_number" {}
variable "cors_allowed_origins" {}

# App specifics

variable "cpu" {}
variable "memory" {}
variable "image_repository" {}
variable "image_version" {}
variable "port" {
  default = 8080
}
variable "desired_count" {
  default = 1
}

# APP VARS

variable "route53_zone" {
  default = {
    # TODO: Add route53 zones ex. ppr = "subdomain.domain.fr"
  }
}