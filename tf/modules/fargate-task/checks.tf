# For the moment, we stick with checks.
# To use tests correctly, we should have them in a separate environment.

data "aws_regions" "current" {}

check "authorized_region" {
  assert {
    condition     = alltrue([for region in local.authorized_region : contains(data.aws_regions.current.names, region)])
    error_message = "One of the authorized region is not a valid or available one, please check.\nRegions list: ${join(", ", local.authorized_region)}"
  }
}
