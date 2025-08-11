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

# For the moment, we stick with checks.
# To use tests correctly, we should have them in a separate environment.

data "aws_regions" "current" {}

check "authorized_region" {
  assert {
    condition     = alltrue([for region in local.authorized_region : contains(data.aws_regions.current.names, region)])
    error_message = "One of the authorized region is not a valid or available one, please check.\nRegions list: ${join(", ", local.authorized_region)}"
  }
}
