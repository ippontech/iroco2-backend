TRUNCATE TABLE cloud_service_provider_service CASCADE;
TRUNCATE TABLE cloud_service_provider_region CASCADE;
TRUNCATE TABLE cloud_service_provider CASCADE;

INSERT INTO cloud_service_provider (id, name)
VALUES ('a4f9e914-4a9c-4551-9717-66359a9298df', 'AWS');

INSERT INTO cloud_service_provider_region (id, csp, name, area, shortname)
VALUES ('027311d5-9892-41f1-9ad1-8e45e6f0d374', 'a4f9e914-4a9c-4551-9717-66359a9298df','US East (N. Virginia)', 'NORTH_AMERICA', 'us-east-1'),
       ('123e4567-e89b-12d3-a456-426614174001', 'a4f9e914-4a9c-4551-9717-66359a9298df', 'Europe (Paris)', 'EUROPE', 'eu-west-3');

INSERT INTO cloud_service_provider_service (id, csp, name, description, short_name, availability, levers, limitations)
VALUES ('089a0a6c-bdd5-42f1-a683-ea885711fb81', 'a4f9e914-4a9c-4551-9717-66359a9298df',
        'Amazon Elastic Compute Cloud (EC2)',
        'Amazon Elastic Compute Cloud ou EC2 est un service proposé par Amazon permettant à des tiers de louer des serveurs sur lesquels exécuter leurs propres applications web.',
        'EC2',
        'AVAILABLE',
        '{"lever 1", "lever 2"}',
        '{"limit 1", "limit 2", "limit 3"}');
