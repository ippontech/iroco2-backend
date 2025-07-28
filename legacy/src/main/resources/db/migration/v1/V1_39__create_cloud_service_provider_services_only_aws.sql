CREATE TABLE IF NOT EXISTS cloud_service_provider_services
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    csp         UUID    NOT NULL,
    name        VARCHAR NOT NULL,
    description TEXT    NOT NULL,
    CONSTRAINT csp_fk FOREIGN KEY (csp) REFERENCES cloud_service_provider(id)
);

WITH aws_id AS (SELECT id FROM cloud_service_provider WHERE name = 'AWS')
INSERT
INTO cloud_service_provider_services (csp, name, description)
SELECT aws_id.id, service.name, service.description
FROM aws_id
         CROSS JOIN (VALUES ('Amazon SageMaker', 'Avec Amazon SageMaker, vous ne payez que ce que vous utilisez. La création, la formation et le déploiement de modèles de ML sont facturés à la seconde, sans frais minimum et sans engagement initial. La tarification dans Amazon SageMaker est ventilée par instances ML à la demande, stockage ML et frais de traitement des données dans les instances d''hébergement.'),
                            ('Amazon Elastic Block Store (EBS)', 'Amazon Elastic Block Storage (EBS) vous permet de créer des volumes de stockage persistant en mode bloc, puis de les attacher à des instances Amazon EC2.'),
                            ('Amazon Elastic Compute Cloud (EC2)', 'Amazon Elastic Compute Cloud ou EC2 est un service proposé par Amazon permettant à des tiers de louer des serveurs sur lesquels exécuter leurs propres applications web.'),
                            ('Amazon Relational Database Service (RDS)', 'Amazon Relational Database Service (Amazon RDS) Custom est un service de base de données géré pour les applications héritées, personnalisées et intégrées qui nécessitent un accès au système d''exploitation sous-jacent et à l''environnement de base de données.'),
                            ('AWS Fargate', 'AWS Fargate est un moteur de calcul sans serveur pour conteneurs qui fonctionne à la fois avec Amazon Elastic Container Service (ECS) et Amazon Elastic Kubernetes Service (EKS). Fargate vous permet de vous concentrer facilement sur la création de vos applications. Il élimine le besoin de mettre en service et de gérer des serveurs, vous permet de spécifier et de payer les ressources par application, et améliore la sécurité grâce à l''isolation des applications par leur conception.'),
                            ('Amazon Elastic File System (EFS)', 'Amazon Elastic File System (EFS) fournit un système de fichiers élastique sans serveur simple qui demande très peu de suivi une fois configuré. Il s''utilise avec les solutions AWS Cloud Services et les ressources sur site.'),
                            ('Amazon Simple Storage Service (S3)', 'Amazon Simple Storage Service (Amazon S3) est une solution de stockage sur Internet. Elle permet de stocker et récupérer n''importe quel volume de données à tout moment et depuis n''importe où sur le Web.'),
                            ('Amazon Aurora', 'La base de données relationnelle Amazon Aurora MySQL Compatible est conçue pour le cloud et combine les performances et la disponibilité des bases de données traditionnelles à la simplicité et à la rentabilité des bases de données open source.'),
                            ('Amazon DynamoDB', 'Amazon DynamoDB est une base de données de valeurs-clés et de documents qui offre des performances de l''ordre de la milliseconde, quelle que soit sa taille. Il s''agit d''une base de données durable, multi-région, multimaître, entièrement gérée, avec sécurité, sauvegarde et restauration intégrées, et mise en cache en mémoire pour les applications Internet.'),
                            ('Amazon Redshift', 'Amazon Redshift est un service d''entreposage de données dans le cloud entièrement géré et d''une capacité de plusieurs pétaoctets. Amazon Redshift étend également les requêtes d''entrepôt de données à votre lac de données, sans chargement requis. Vous pouvez exécuter des requêtes analytiques sur des pétaoctets de données stockées localement dans Redshift, et directement sur des exaoctets de données stockées dans Amazon S3.'),
                            ('AWS Lambda', 'AWS Lambda vous permet d''exécuter du code sans avoir à mettre en service ou gérer des serveurs. Vous payez uniquement pour le temps de calcul consommé, aucuns frais ne s''appliquent lorsque votre code n''est pas exécuté.'))
    AS service(name, description);

ALTER TABLE cloud_service_provider_regions
ADD CONSTRAINT csp_fk FOREIGN KEY (csp) REFERENCES cloud_service_provider(id);