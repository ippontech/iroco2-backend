# ECS

data "aws_ecs_cluster" "cluster" {
  cluster_name = var.cluster_name[var.deploy_account]
}

# Network

data "aws_vpc" "vpc" {
  default = false
  tags = {
    Name = var.vpc_name[var.deploy_account]
  }
}
data "aws_subnets" "private_subnet_ids" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc.id]
  }
  tags = {
    Name = var.private_subnet_name_regex
  }
}

data "aws_lb" "alb" {
  name = var.alb_name[var.deploy_account]
}

data "aws_lb_listener" "alb_80" {
  load_balancer_arn = data.aws_lb.alb.arn
  port              = 443
}

data "aws_security_group" "alb" {
  name = var.alb_security_group_name[var.deploy_account]
}

data "aws_route53_zone" "main" {
  name = local.deploy_account_route53_zone
}

data "aws_kms_alias" "secrets_manager" {
  name = "alias/aws/secretsmanager"
}

# CUR

data "aws_s3_bucket" "cur_s3_bucket" {
  bucket = "cur-${var.namespace}-${var.environment}"
}