# Secrets
data "aws_secretsmanager_secret" "rds_credentials" {
  name = "/RDS/MASTER_PASSWORD_IROCALC_DB_SECRET"
}

# SQS

data "terraform_remote_state" "cur_service" {
  backend = "s3"

  config = {
    region = "eu-west-3"
    bucket = "iroco-tfstates-store-${var.environment}"
    key    = "cur/eu-west-3/terraform.tfstate"
  }
}

data "aws_sqs_queue" "analyzer_sqs_cur" {
  name = data.terraform_remote_state.cur_service.outputs.analyzer_sqs_cur_name
}

data "aws_sqs_queue" "scanner_sqs_cur" {
  name = data.terraform_remote_state.cur_service.outputs.scanner_sqs_cur_name
}

# CLERK PARAMETERS

data "aws_ssm_parameter" "clerk_issuer" {
  name = "/IROCO2/PARAMETERS/CLERK_ISSUER"
}

data "aws_ssm_parameter" "clerk_audience" {
  name = "/IROCO2/PARAMETERS/CLERK_AUDIENCE"
}

data "aws_ssm_parameter" "clerk_public_key" {
  name = "/IROCO2/PARAMETERS/CLERK_PUBLIC_KEY"
}

data "terraform_remote_state" "data" {
  backend = "s3"

  config = {
    region = "eu-west-3"
    bucket = "iroco-tfstates-store-${var.environment}"
    key    = "infrastructure/eu-west-3/data/terraform.tfstate"
  }
}

data "aws_kms_public_key" "by_id" {
  key_id = data.terraform_remote_state.data.outputs.iroco_identity_provider_key_id
}

data "aws_kms_key" "signing_key" {
  key_id = data.terraform_remote_state.data.outputs.iroco_identity_provider_key_id
}
