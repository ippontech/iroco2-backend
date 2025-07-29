-- Lier par clé étrangere le cloud service provider à l'infrastructure
ALTER TABLE infrastructure
ADD CONSTRAINT infrastructure__cloud_service_provider__fk FOREIGN KEY (cloud_service_provider) REFERENCES cloud_service_provider(id);