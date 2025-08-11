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

locals {
  app_name = var.environment

  app_base_path = coalesce(var.override_base_path, var.project_name)
  healthcheck_path         = "/actuator/health"
  is_prod                  = var.deploy_account == "prod"
  is_ephemeral_environment = var.environment != "main"

  deploy_account_route53_zone = var.route53_zone[var.deploy_account]
  fqdn                        = "api.${local.deploy_account_route53_zone}"
}
