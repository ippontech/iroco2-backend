TRUNCATE TABLE infrastructure CASCADE;
TRUNCATE TABLE cloud_service_provider_region CASCADE;
TRUNCATE TABLE cloud_service_provider_service CASCADE;
TRUNCATE TABLE cloud_service_provider CASCADE;

INSERT INTO cloud_service_provider (id, name) VALUES ('f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'AWS');

INSERT INTO cloud_service_provider_region (id, csp, name, area, shortname)
VALUES ('b9f79cac-d423-4ff8-b378-1aa86cdd9ac7', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','US East (N. Virginia)', 'NORTH_AMERICA', 'us-east-1'),
       ('b4d6da1b-cf07-4911-af79-20d3734610cb', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','US East (Ohio)', 'NORTH_AMERICA', 'us-east-2'),
       ('18f0f080-fb37-45b8-9cd5-b2fb5bed1620', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','US West (N. California)', 'NORTH_AMERICA', 'us-west-1'),
       ('988cce7e-bc2a-462a-b3b7-ec53340e6dbe', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','US West (Oregon)', 'NORTH_AMERICA', 'us-west-2'),
       ('6c6444d7-af3a-4328-ba59-d35ba9600ebf', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Canada (Central)', 'NORTH_AMERICA', 'ca-central-1'),
       ('9e24da63-f487-44e9-90ab-1ecdba5d7244', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Canada West (Calgary)', 'NORTH_AMERICA', 'ca-west-1'),
       ('d9495b45-b55b-4b9c-9c41-0f3414a960b0', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Africa (Cape Town)', 'AFRICA', 'af-south-1'),
       ('7deaea71-3fe3-47c8-a646-0ba2a6656103', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Hong Kong)', 'ASIA', 'ap-east-1'),
       ('5e0e6776-a9cc-4cb2-bf5a-a1727d7646e1', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Hyderabad)', 'ASIA', 'ap-south-2'),
       ('fb933c61-0e19-4b0a-98b7-699a140d9cb5', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Jakarta)', 'ASIA', 'ap-southeast-3'),
       ('4020796f-ea99-4fd6-934c-9f3080bd6bc0', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Melbourne)', 'ASIA', 'ap-southeast-4'),
       ('c74bb554-e727-44c5-81b4-3aea4367951e', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Mumbai)', 'ASIA', 'ap-south-1'),
       ('53721306-5d35-47f5-b14b-5c7c950aaac9', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Osaka)', 'ASIA', 'ap-northeast-3'),
       ('06502616-d63d-4c27-9e86-cf183df1f416', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Seoul)', 'ASIA', 'ap-northeast-2'),
       ('a85727f7-9d72-464d-81cb-d4f1c920709e', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Singapore)', 'ASIA', 'ap-southeast-1'),
       ('b68bec3e-9617-4d74-8cdf-836cb88313db', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Sydney)', 'ASIA', 'ap-southeast-2'),
       ('bddf6f48-fb5a-4caf-97b0-97a42bd43bf5', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Asia Pacific (Tokyo)', 'ASIA', 'ap-northeast-1'),
       ('e249da64-6060-49c4-866d-06a3e0305c81', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Frankfurt)', 'EUROPE', 'eu-central-1'),
       ('7aff1ad2-8933-406e-825b-5da45e580f99', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Ireland)', 'EUROPE', 'eu-west-1'),
       ('fa609e8a-544a-485c-9dde-233fe5e258c6', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (London)', 'EUROPE', 'eu-west-2'),
       ('bc762e98-8342-421d-8db2-7e51ad6c3313', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Milan)', 'EUROPE', 'eu-south-1'),
       ('cd035d2c-9281-4db8-bb1d-adeaa6ff8832', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Paris)', 'EUROPE', 'eu-west-3'),
       ('98ee1236-e08f-4a6f-8e6b-7f6871a643c1', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Spain)', 'EUROPE', 'eu-south-2'),
       ('627012f1-7188-47b2-9502-90d3b0dd78c8', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Stockholm)', 'EUROPE', 'eu-north-1'),
       ('278c89c3-3343-4450-b981-6e8ba01c141e', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Europe (Zurich)', 'EUROPE', 'eu-central-1'),
       ('2a5cb2ee-b4fb-47cc-a8a9-b669933fb689', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Israel (Tel Aviv)', 'ISRAEL', 'il-central-1'),
       ('8cfb2db3-a56e-4c0d-9931-d28fc6f9fa2c', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Middle East (Bahrain)', 'MIDDLE_EAST', 'me-south-1'),
       ('d7cdcd9a-c54c-44f2-92c1-5d7729855d30', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','Middle East (UAE)', 'MIDDLE_EAST', 'me-central-1'),
       ('a9b49013-2cb7-4f0d-a8dc-194354334a99', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f','South America (Sao Paulo)', 'SOUTH_AMERICA', 'sa-east-1');


INSERT INTO cloud_service_provider_service (id, csp, name, description)
VALUES ('2e9c3b1b-13fc-4984-8139-93af07ad74df', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon SageMaker', 'Avec Amazon SageMaker, vous ne payez que ce que vous utilisez. La création, la formation et le déploiement de modèles de ML sont facturés à la seconde, sans frais minimum et sans engagement initial. La tarification dans Amazon SageMaker est ventilée par instances ML à la demande, stockage ML et frais de traitement des données dans les instances d''hébergement.'),
       ('80a1bc13-51b9-4d76-bb41-22fd955f3d7c', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Elastic Block Store (EBS)', 'Amazon Elastic Block Storage (EBS) vous permet de créer des volumes de stockage persistant en mode bloc, puis de les attacher à des instances Amazon EC2.'),
       ('b46fb976-ae0f-47e1-8978-993bf6fb1afd', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Elastic Compute Cloud (EC2)', 'Amazon Elastic Compute Cloud ou EC2 est un service proposé par Amazon permettant à des tiers de louer des serveurs sur lesquels exécuter leurs propres applications web.'),
       ('64bfca71-c7f4-4e93-aadc-ebc522ce121f', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Relational Database Service (RDS)', 'Amazon Relational Database Service (Amazon RDS) Custom est un service de base de données géré pour les applications héritées, personnalisées et intégrées qui nécessitent un accès au système d''exploitation sous-jacent et à l''environnement de base de données.'),
       ('5a7b23b0-d3d8-4ee5-a7e1-91100e6935f1', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'AWS Fargate', 'AWS Fargate est un moteur de calcul sans serveur pour conteneurs qui fonctionne à la fois avec Amazon Elastic Container Service (ECS) et Amazon Elastic Kubernetes Service (EKS). Fargate vous permet de vous concentrer facilement sur la création de vos applications. Il élimine le besoin de mettre en service et de gérer des serveurs, vous permet de spécifier et de payer les ressources par application, et améliore la sécurité grâce à l''isolation des applications par leur conception.'),
       ('f01997e6-2a86-456c-a67e-4671b7cccc8f', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Elastic File System (EFS)', 'Amazon Elastic File System (EFS) fournit un système de fichiers élastique sans serveur simple qui demande très peu de suivi une fois configuré. Il s''utilise avec les solutions AWS Cloud Services et les ressources sur site.'),
       ('e16af23a-dece-4135-9333-c3dfce5fc8dc', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Simple Storage Service (S3)', 'Amazon Simple Storage Service (Amazon S3) est une solution de stockage sur Internet. Elle permet de stocker et récupérer n''importe quel volume de données à tout moment et depuis n''importe où sur le Web.'),
       ('a7a944e8-7a55-419e-99d3-4518f7222c1f', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Aurora', 'La base de données relationnelle Amazon Aurora MySQL Compatible est conçue pour le cloud et combine les performances et la disponibilité des bases de données traditionnelles à la simplicité et à la rentabilité des bases de données open source.'),
       ('512a5e2c-1130-441b-88c9-b0fe8e3dccd8', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon DynamoDB', 'Amazon DynamoDB est une base de données de valeurs-clés et de documents qui offre des performances de l''ordre de la milliseconde, quelle que soit sa taille. Il s''agit d''une base de données durable, multi-région, multimaître, entièrement gérée, avec sécurité, sauvegarde et restauration intégrées, et mise en cache en mémoire pour les applications Internet.'),
       ('7e1930c6-ca46-4713-bd02-c8228f1f8586', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'Amazon Redshift', 'Amazon Redshift est un service d''entreposage de données dans le cloud entièrement géré et d''une capacité de plusieurs pétaoctets. Amazon Redshift étend également les requêtes d''entrepôt de données à votre lac de données, sans chargement requis. Vous pouvez exécuter des requêtes analytiques sur des pétaoctets de données stockées localement dans Redshift, et directement sur des exaoctets de données stockées dans Amazon S3.'),
       ('cece60a9-8e50-4d00-b91e-7a93c0c1229f', 'f109f0b2-340e-437e-8d0d-9f465ad03c2f', 'AWS Lambda', 'AWS Lambda vous permet d''exécuter du code sans avoir à mettre en service ou gérer des serveurs. Vous payez uniquement pour le temps de calcul consommé, aucuns frais ne s''appliquent lorsque votre code n''est pas exécuté.');


-- configuration_setting
insert into configuration_setting (id, name) values
('0d281333-7ad6-4920-bb74-3c9f75c9b16d', 'INSTANCE_NUMBER'),
('3580bd2a-b351-4964-9fc1-582e48be0c85', 'INSTANCE_TYPE');

-- service_configuration_setting
insert into service_configuration_setting (id, service_id, configuration_setting_id, default_value) values
('db5c25e6-afc5-4035-9759-8ee8d0924c36', 'b46fb976-ae0f-47e1-8978-993bf6fb1afd', '0d281333-7ad6-4920-bb74-3c9f75c9b16d', null),
('901be926-2d85-475b-b5f2-1eff4dc2b3ca', 'b46fb976-ae0f-47e1-8978-993bf6fb1afd', '3580bd2a-b351-4964-9fc1-582e48be0c85', null);