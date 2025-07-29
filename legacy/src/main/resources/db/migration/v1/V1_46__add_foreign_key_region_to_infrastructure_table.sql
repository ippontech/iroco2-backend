ALTER TABLE infrastructure
ADD CONSTRAINT infrastructure__default_region__fk FOREIGN KEY (default_region) REFERENCES cloud_service_provider_region(id);
