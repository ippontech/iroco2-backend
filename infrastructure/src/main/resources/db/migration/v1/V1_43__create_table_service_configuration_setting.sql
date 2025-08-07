CREATE TABLE IF NOT EXISTS service_configuration_setting
(
    service_id UUID NOT NULL,
    configuration_setting_id UUID NOT NULL,
    PRIMARY KEY (service_id, configuration_setting_id)
);