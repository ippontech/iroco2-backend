ALTER TABLE cloud_service_provider_service
ADD COLUMN IF NOT EXISTS availability VARCHAR;

UPDATE cloud_service_provider_service SET availability = 'AVAILABLE';

ALTER TABLE cloud_service_provider_service ALTER COLUMN availability SET NOT NULL;
