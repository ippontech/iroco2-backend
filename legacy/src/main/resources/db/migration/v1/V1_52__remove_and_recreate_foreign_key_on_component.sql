ALTER TABLE component DROP CONSTRAINT component__csp_region__fk;
ALTER TABLE component ADD CONSTRAINT component__csp_region__fk FOREIGN KEY (csp_region) REFERENCES cloud_service_provider_region (id);
