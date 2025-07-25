ALTER TABLE cloud_service_provider_service ADD COLUMN short_name VARCHAR(255);

-- Mise à jour des valeurs de shortname pour chaque ligne
UPDATE cloud_service_provider_service
SET short_name = CASE
                    WHEN id = '2e9c3b1b-13fc-4984-8139-93af07ad74df' THEN 'SageMaker'
                    WHEN id = '80a1bc13-51b9-4d76-bb41-22fd955f3d7c' THEN 'EBS'
                    WHEN id = 'b46fb976-ae0f-47e1-8978-993bf6fb1afd' THEN 'EC2'
                    WHEN id = '64bfca71-c7f4-4e93-aadc-ebc522ce121f' THEN 'RDS'
                    WHEN id = '5a7b23b0-d3d8-4ee5-a7e1-91100e6935f1' THEN 'Fargate'
                    WHEN id = 'f01997e6-2a86-456c-a67e-4671b7cccc8f' THEN 'EFS'
                    WHEN id = 'e16af23a-dece-4135-9333-c3dfce5fc8dc' THEN 'S3'
                    WHEN id = 'a7a944e8-7a55-419e-99d3-4518f7222c1f' THEN 'Aurora'
                    WHEN id = '512a5e2c-1130-441b-88c9-b0fe8e3dccd8' THEN 'DynamoDB'
                    WHEN id = '7e1930c6-ca46-4713-bd02-c8228f1f8586' THEN 'Redshift'
                    WHEN id = 'cece60a9-8e50-4d00-b91e-7a93c0c1229f' THEN 'Lambda'
    END;