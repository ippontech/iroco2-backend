INSERT INTO infrastructure (id, owner, name, cloud_service_provider, default_region)
VALUES ('ebda6c8b-3c28-4046-8f91-ac112a7e24c2', 'charles@ippon.fr', 'MonInfra',
        'a4f9e914-4a9c-4551-9717-66359a9298df', '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO component (id, name, last_modification_date, infrastructure_id, csp_region, service_id)
VALUES ('0ccf663c-19a4-4a34-8b4b-9dba60725b42', 'MyEC2', '2024-10-08', 'ebda6c8b-3c28-4046-8f91-ac112a7e24c2',
        '123e4567-e89b-12d3-a456-426614174001', '089a0a6c-bdd5-42f1-a683-ea885711fb81');

INSERT INTO configured_setting (component_id, configuration_setting_id, value)
VALUES ('0ccf663c-19a4-4a34-8b4b-9dba60725b42', '3580bd2a-b351-4964-9fc1-582e48be0c85', 't3a.nano'),
       ('0ccf663c-19a4-4a34-8b4b-9dba60725b42', '0d281333-7ad6-4920-bb74-3c9f75c9b16d', '2');
