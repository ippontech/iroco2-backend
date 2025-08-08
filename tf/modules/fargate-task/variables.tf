# Those locals are here to help validate the variables.
locals {
  authorized_region = ["eu-west-3"]
}


##### COMMON ####
# Required
variable "project_name" {
  type = string

  validation {
    condition     = length(var.project_name) > 0 && length(var.project_name) <= 32
    error_message = "Project name length should be > 0 and <= 32."
  }
}

variable "environment" {
  type = string
}

variable "deploy_account" {
  type = string
}

# Not required
variable "region" {
  type        = string
  description = "AWS's region"
}

variable "deploy_account_number" {
  type        = string
  description = "AWS account ID"

  validation {
    condition     = length(regexall("^\\d{12}$", var.deploy_account_number)) > 0
    error_message = "The aws account id is invalid, it should follow this regex: ^\\d{12}$."
  }
}

variable "failover_mailing_list" {
  type = list(string)

  default = [
    # TODO: Add developers email after testing
  ]
}

#### ECS ####
# Required

# Not Required
variable "cluster_name" {
  type = object({
    ppr  = string
    prod = string
  })

  default = {
    # TODO: Add cluster names
  }
}

#### NETWORK ####
# Required

# Not required
variable "vpc_name" {
  type = object({
    ppr  = string
    prod = string
  })

  default = {
    # TODO: Add vpc names
  }
}

variable "private_subnet_name_regex" {
  type    = string
  default = "*-private-eu-west-3a*"
}

variable "alb_name" {
  type = object({
    ppr  = string
    prod = string
  })

  default = {
    # TODO: Add alb names
  }
}

variable "alb_security_group_name" {
  type = object({
    ppr  = string
    prod = string
  })

  default = {
    # TODO: Add alb security group names
  }
}

variable "route53_zone" {
  type = object({
    ppr  = string
    prod = string
  })

  validation {
    condition = alltrue([
      for _, value in tomap(var.route53_zone) :
      (length(regexall("^([a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]$", value)) > 0)
    ])
    error_message = "One or more provided domain are invalid, they should follow this regex: ^([a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]$"
  }
}

variable "kms_identity_key_arn" {
  type        = string
  description = "The key arn of the kms key used for identity"
}

#### CONTAINER ####
# Required
variable "container_cpu" {
  type = number
}

variable "container_memory" {
  type = number
}

variable "container_image" {
  type = string
}

# Not required
variable "container_port" {
  type    = number
  default = 8080

  validation {
    condition     = var.container_port > 0 && var.container_port <= 65536
    error_message = "The port should be between 0 and 65536."
  }
}

variable "container_desired_count" {
  type    = number
  default = 1

  validation {
    condition     = var.container_desired_count >= 1
    error_message = "You should have at least one instance running."
  }
}

#### APP SPECIFICS ####
# Required

# Not required
variable "task_container_environment" {
  type    = map(string)
  default = {}
}

variable "task_container_secrets_arn" {
  type    = map(string)
  default = {}
}

variable "task_container_secrets_arn_with_key" {
  type = map(object({
    arn = string
    key = string
  }))
  default = {}
}

variable "override_base_path" {
  type    = string
  default = null
}

variable "override_healthcheck_path" {
  type    = string
  default = null
}

variable "namespace" {
  type    = string
  default = "iroco2"
}

#### VALIDATION ####

# Validation that could not be made in the validation block of a variable.
# For some validation, we need to use information from elsewhere. We can't put other variables in the validation block
# then the one we're testing.
# So for those tests, we use a precondition in the lifecycle of a null resource.

resource "null_resource" "region_validation" {
  lifecycle {
    precondition {
      condition     = contains(local.authorized_region, var.region)
      error_message = "${var.region} is not an authorized region, should be on of those: ${join(", ", local.authorized_region)}"
    }
  }
}
