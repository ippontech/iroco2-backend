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