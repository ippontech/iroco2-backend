terraform {
  required_version = ">= 1.0.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    key = "backend/irocalc-backend/eu-west-3/terraform.tfstate"
  }

}

provider "aws" {
  region = var.region

  default_tags {
    tags = {
      namespace    = var.namespace
      project_type = var.project_type
      project      = var.project_name
      env          = var.environment
    }
  }
}
