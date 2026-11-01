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

# Plans the module with a mocked AWS provider and checks that each existing
# resource is looked up by the name given for the selected deploy account.

# The mocked values below only need to pass the provider's format checks.
mock_provider "aws" {
  mock_data "aws_regions" {
    defaults = {
      names = ["eu-west-3"]
    }
  }

  mock_data "aws_iam_policy_document" {
    defaults = {
      json = "{\"Version\":\"2012-10-17\",\"Statement\":[]}"
    }
  }

  mock_data "aws_lb_listener" {
    defaults = {
      arn = "arn:aws:elasticloadbalancing:eu-west-3:123456789012:listener/app/alb/0123456789abcdef/0123456789abcdef"
    }
  }
}
mock_provider "null" {}

variables {
  project_name          = "iroco2"
  environment           = "main"
  region                = "eu-west-3"
  deploy_account_number = "123456789012"
  kms_identity_key_arn  = "arn:aws:kms:eu-west-3:123456789012:key/00000000-0000-0000-0000-000000000000"
  container_cpu         = 512
  container_memory      = 2048
  container_image       = "ghcr.io/ippontech/iroco2-backend:latest"
  failover_mailing_list = ["team@example.com"]

  route53_zone = {
    ppr  = "ppr.example.com"
    prod = "example.com"
  }

  cluster_name            = { ppr = "cluster-ppr", prod = "cluster-prod" }
  vpc_name                = { ppr = "vpc-ppr", prod = "vpc-prod" }
  alb_name                = { ppr = "alb-ppr", prod = "alb-prod" }
  alb_security_group_name = { ppr = "alb-group-ppr", prod = "alb-group-prod" }
}

run "looks_up_the_ppr_resources" {
  command = plan

  variables {
    deploy_account = "ppr"
  }

  assert {
    condition     = data.aws_ecs_cluster.cluster.cluster_name == "cluster-ppr"
    error_message = "The ECS cluster must be looked up by its ppr name."
  }

  assert {
    condition     = data.aws_vpc.vpc.tags.Name == "vpc-ppr"
    error_message = "The VPC must be looked up by its ppr Name tag."
  }

  assert {
    condition     = data.aws_lb.alb.name == "alb-ppr"
    error_message = "The load balancer must be looked up by its ppr name."
  }

  assert {
    condition     = data.aws_security_group.alb.name == "alb-group-ppr"
    error_message = "The load balancer group must be looked up by its ppr name."
  }
}

run "looks_up_the_prod_resources" {
  command = plan

  variables {
    deploy_account = "prod"
  }

  assert {
    condition     = data.aws_ecs_cluster.cluster.cluster_name == "cluster-prod"
    error_message = "The ECS cluster must be looked up by its prod name."
  }

  assert {
    condition     = data.aws_vpc.vpc.tags.Name == "vpc-prod"
    error_message = "The VPC must be looked up by its prod Name tag."
  }

  assert {
    condition     = data.aws_lb.alb.name == "alb-prod"
    error_message = "The load balancer must be looked up by its prod name."
  }

  assert {
    condition     = data.aws_security_group.alb.name == "alb-group-prod"
    error_message = "The load balancer group must be looked up by its prod name."
  }
}
