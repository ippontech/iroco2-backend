#!/bin/bash

# Variables
BUCKET_NAME="my-bucket"
QUEUE_NAME="my-queue"
PROFILE_NAME="localstack"
ENDPOINT_URL="http://localhost:4566"
AWS_REGION="us-east-1"
AWS_ACCESS_KEY_ID="test"
AWS_SECRET_ACCESS_KEY="test"
OUTPUT="json"

# Configure the localstack profile
if ! aws configure list --profile $PROFILE_NAME >/dev/null 2>&1; then
aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID --profile $PROFILE_NAME
aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY --profile $PROFILE_NAME
aws configure set region $AWS_REGION --profile $PROFILE_NAME
aws configure set output $OUTPUT --profile $PROFILE_NAME
 echo "AWS CLI profile '$PROFILE_NAME' created."
else
  echo "AWS CLI profile '$PROFILE_NAME' is already configured."
fi

# Create the S3 bucket if it doesn't exist
if ! aws --endpoint-url=$ENDPOINT_URL --profile $PROFILE_NAME s3api head-bucket --bucket $BUCKET_NAME 2>/dev/null; then
  aws --endpoint-url=$ENDPOINT_URL --profile $PROFILE_NAME s3api create-bucket --bucket $BUCKET_NAME
  aws --endpoint-url=$ENDPOINT_URL s3api put-bucket-cors --bucket my-bucket --profile $PROFILE_NAME --cors-configuration '{
      "CORSRules": [
          {
              "AllowedHeaders": ["*"],
              "AllowedMethods": ["GET", "PUT", "POST", "DELETE", "HEAD"],
              "AllowedOrigins": ["*"],
              "ExposeHeaders": []
          }
      ]
  }'
  echo "Bucket $BUCKET_NAME created."
else
  echo "Bucket $BUCKET_NAME already exists."
fi

# Create the SQS queue if it doesn't exist
if ! aws --endpoint-url=$ENDPOINT_URL --profile $PROFILE_NAME sqs get-queue-url --queue-name $QUEUE_NAME 2>/dev/null; then
  aws --endpoint-url=$ENDPOINT_URL --profile $PROFILE_NAME sqs create-queue --queue-name $QUEUE_NAME
  echo "Queue $QUEUE_NAME created."
else
  echo "Queue $QUEUE_NAME already exists."
fi

