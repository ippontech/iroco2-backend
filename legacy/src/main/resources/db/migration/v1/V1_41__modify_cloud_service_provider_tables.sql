ALTER TABLE cloud_service_provider_regions RENAME TO cloud_service_provider_region;
ALTER TABLE cloud_service_provider_services RENAME TO cloud_service_provider_service;

ALTER TABLE cloud_service_provider ADD CONSTRAINT csp__name__unique UNIQUE (name);
ALTER TABLE cloud_service_provider_region ADD CONSTRAINT csp_regions__name_csp__unique UNIQUE (name, csp);
ALTER TABLE cloud_service_provider_service ADD CONSTRAINT csp_services__name_csp__unique UNIQUE (name, csp);
