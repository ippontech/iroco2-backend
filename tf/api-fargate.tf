locals {
  database_name      = "irocalc"
  cur_s3_bucket_name = "cur-${var.namespace}-${var.environment}"
}
module "api-fargate" {
  source = "./modules/fargate-task"

  region                = var.region
  project_name          = var.project_name
  environment           = var.environment
  deploy_account        = var.deploy_account
  deploy_account_number = var.aws_deploy_account_number

  container_cpu           = var.cpu
  container_memory        = var.memory
  container_port          = var.port
  container_image         = "${var.image_repository}:${var.image_version}"
  container_desired_count = var.desired_count
  override_base_path      = "iroco2"
  route53_zone            = var.route53_zone

  kms_identity_key_arn = data.aws_kms_key.signing_key.arn

  task_container_environment = {
    DATABASE_NAME                      = local.database_name,
    IROCO2_CORS_ALLOWED_ORIGINS        = var.cors_allowed_origins
    IROCO2_AWS_ANALYZER_SQS_QUEUE_NAME = data.aws_sqs_queue.analyzer_sqs_cur.name
    IROCO2_AWS_SCANNER_SQS_QUEUE_NAME  = data.aws_sqs_queue.scanner_sqs_cur.name
    IROCO2_AWS_SQS_QUEUE_ENDPOINT      = trimsuffix(data.aws_sqs_queue.analyzer_sqs_cur.url, data.aws_sqs_queue.analyzer_sqs_cur.name)
    S3_BUCKET_NAME                     = local.cur_s3_bucket_name
    IROCO2_AWS_REGION_STATIC           = var.region
    IROCO2_CLERK_ISSUER                = data.aws_ssm_parameter.clerk_issuer.value
    IROCO2_CLERK_AUDIENCE              = data.aws_ssm_parameter.clerk_audience.value
    IROCO2_CLERK_PUBLIC_KEY            = data.aws_ssm_parameter.clerk_public_key.value
    IROCO2_KMS_IDENTITY_KEY_ID         = data.terraform_remote_state.data.outputs.iroco_identity_provider_key_id
    IROCO2_KMS_IDENTITY_PUBLIC_KEY     = data.aws_kms_public_key.by_id.public_key
    JWT_ISSUER                         = var.route53_zone[var.environment]
    JWT_AUDIENCE                       = var.route53_zone[var.environment]
  }
  task_container_secrets_arn = {}
  task_container_secrets_arn_with_key = {
    IROCO2_DATA_SOURCE_URL = {
      arn = data.aws_secretsmanager_secret.rds_credentials.arn
      key = "host"
    }
    IROCO2_DATA_SOURCE_USERNAME = {
      arn = data.aws_secretsmanager_secret.rds_credentials.arn
      key = "username"
    }
    IROCO2_DATA_SOURCE_PASSWORD = {
      arn = data.aws_secretsmanager_secret.rds_credentials.arn
      key = "password"
    }
    IROCO2_DATA_SOURCE_FLYWAY_USERNAME = {
      arn = data.aws_secretsmanager_secret.rds_credentials.arn
      key = "username"
    }
    IROCO2_DATA_SOURCE_FLYWAY_PASSWORD = {
      arn = data.aws_secretsmanager_secret.rds_credentials.arn
      key = "password"
    }
  }
}
