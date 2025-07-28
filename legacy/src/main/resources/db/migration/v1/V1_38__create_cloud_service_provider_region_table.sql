CREATE TABLE IF NOT EXISTS cloud_service_provider_regions
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    csp       UUID    NOT NULL,
    name      VARCHAR NOT NULL,
    area      VARCHAR NOT NULL,
    shortname VARCHAR NOT NULL
);

WITH aws_id AS (SELECT id FROM cloud_service_provider WHERE name = 'AWS')
INSERT INTO cloud_service_provider_regions (csp, name, area, shortname)
SELECT aws_id.id, region.name, region.area, region.shortname
FROM aws_id
         CROSS JOIN (VALUES ('US East (N. Virginia)', 'NORTH_AMERICA', 'us-east-1'),
                            ('US East (Ohio)', 'NORTH_AMERICA', 'us-east-2'),
                            ('US West (N. California)', 'NORTH_AMERICA', 'us-west-1'),
                            ('US West (Oregon)', 'NORTH_AMERICA', 'us-west-2'),
                            ('Canada (Central)', 'NORTH_AMERICA', 'ca-central-1'),
                            ('Canada West (Calgary)', 'NORTH_AMERICA', 'ca-west-1'),
                            ('Africa (Cape Town)', 'AFRICA', 'af-south-1'),
                            ('Asia Pacific (Hong Kong)', 'ASIA', 'ap-east-1'),
                            ('Asia Pacific (Hyderabad)', 'ASIA', 'ap-south-2'),
                            ('Asia Pacific (Jakarta)', 'ASIA', 'ap-southeast-3'),
                            ('Asia Pacific (Melbourne)', 'ASIA', 'ap-southeast-4'),
                            ('Asia Pacific (Mumbai)', 'ASIA', 'ap-south-1'),
                            ('Asia Pacific (Osaka)', 'ASIA', 'ap-northeast-3'),
                            ('Asia Pacific (Seoul)', 'ASIA', 'ap-northeast-2'),
                            ('Asia Pacific (Singapore)', 'ASIA', 'ap-southeast-1'),
                            ('Asia Pacific (Sydney)', 'ASIA', 'ap-southeast-2'),
                            ('Asia Pacific (Tokyo)', 'ASIA', 'ap-northeast-1'),
                            ('Europe (Frankfurt)', 'EUROPE', 'eu-central-1'),
                            ('Europe (Ireland)', 'EUROPE', 'eu-west-1'),
                            ('Europe (London)', 'EUROPE', 'eu-west-2'),
                            ('Europe (Milan)', 'EUROPE', 'eu-south-1'),
                            ('Europe (Paris)', 'EUROPE', 'eu-west-3'),
                            ('Europe (Spain)', 'EUROPE', 'eu-south-2'),
                            ('Europe (Stockholm)', 'EUROPE', 'eu-north-1'),
                            ('Europe (Zurich)', 'EUROPE', 'eu-central-1'),
                            ('Israel (Tel Aviv)', 'ISRAEL', 'il-central-1'),
                            ('Middle East (Bahrain)', 'MIDDLE_EAST', 'me-south-1'),
                            ('Middle East (UAE)', 'MIDDLE_EAST', 'me-central-1'),
                            ('South America (Sao Paulo)', 'SOUTH_AMERICA', 'sa-east-1'))
    AS region(name, area, shortname);
