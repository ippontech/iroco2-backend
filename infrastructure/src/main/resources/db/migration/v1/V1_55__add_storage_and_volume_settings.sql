-- configuration_setting
INSERT INTO configuration_setting (id, name) VALUES
('253b4084-f112-465d-aada-043b37432c61', 'VOLUME_NUMBER');

-- service_configuration_setting
INSERT INTO service_configuration_setting (id, service_id, configuration_setting_id, default_value) VALUES

-- EBS 80a1bc13-51b9-4d76-bb41-22fd955f3d7c
('bdc48f06-65a6-415e-b445-74b0e591bc84','80a1bc13-51b9-4d76-bb41-22fd955f3d7c', '253b4084-f112-465d-aada-043b37432c61', null), -- VOLUME_NUMBER 253b4084-f112-465d-aada-043b37432c61

-- Aurora a7a944e8-7a55-419e-99d3-4518f7222c1f
('ba438de3-0955-4c1f-a5da-bcf34f2c6961','a7a944e8-7a55-419e-99d3-4518f7222c1f', 'ac175a69-f9b3-4485-b2cd-bb777ea29ba9', null), -- STORAGE_IN_MEGA_BYTE ac175a69-f9b3-4485-b2cd-bb777ea29ba9

-- Redshift 7e1930c6-ca46-4713-bd02-c8228f1f8586
('5230d7d8-64f6-4534-aa52-1a913f959511','7e1930c6-ca46-4713-bd02-c8228f1f8586', 'ac175a69-f9b3-4485-b2cd-bb777ea29ba9', null) -- STORAGE_IN_MEGA_BYTE ac175a69-f9b3-4485-b2cd-bb777ea29ba9
;
