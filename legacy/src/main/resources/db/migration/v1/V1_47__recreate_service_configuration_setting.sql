DROP TABLE service_configuration_setting;

CREATE TABLE IF NOT EXISTS service_configuration_setting
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    service_id UUID NOT NULL,
    configuration_setting_id UUID NOT NULL,
    default_value varchar(255)
);
