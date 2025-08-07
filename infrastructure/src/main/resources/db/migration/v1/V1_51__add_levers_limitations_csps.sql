ALTER TABLE cloud_service_provider_service
    ADD COLUMN IF NOT EXISTS levers      VARCHAR[] DEFAULT array []::varchar[],
    ADD COLUMN IF NOT EXISTS limitations VARCHAR[] DEFAULT array []::varchar[];

UPDATE cloud_service_provider_service
SET levers      = '{"Right Sizing pour choisir le type d''instance adapté à votre besoin", "Définir des policy de gestion des backup", "Auto-scaling des Read replicas"}',
    limitations = '{"Aurora Serverless ne prend en compte que le stockage pour le moment (et pas les ACUs)"}'
WHERE id = 'a7a944e8-7a55-419e-99d3-4518f7222c1f';

UPDATE cloud_service_provider_service
SET levers      = '{"Mise en place d''une policy d''archivage des données vers d''autre stockages ou de suppression"}',
    limitations = '{"Il n''y a pas de différenciation entre les types de stockage (SSD, HDD, etc...)"}'
WHERE id = '80a1bc13-51b9-4d76-bb41-22fd955f3d7c';

UPDATE cloud_service_provider_service
SET levers      = '{"Pour les environnements de dev uniquement : Utilisation de SSM pour auto-shutdown/start les instances en dehors des horaires de travail", "Mise en place de policy de suppression de backup"}',
    limitations = '{"Impossible de configurer la réplication multi-azs pour les cluster RDS (une seule instance prise en compte)"}'
WHERE id = '64bfca71-c7f4-4e93-aadc-ebc522ce121f';

UPDATE cloud_service_provider_service
SET levers      = '{"Mise en place d''une policy d''archivage des données vers d''autre Tier (Infrequent Access, etc...)"}',
    limitations = '{"Le transfert de données (inbound/outbound) n''est pas pris en compte pour le moment"}'
WHERE id = 'f01997e6-2a86-456c-a67e-4671b7cccc8f';

UPDATE cloud_service_provider_service
SET levers      = '{"Utilisation d''Auto scaling pour la provisioned capacity", "Right Sizing pour choisir le type d''instance adapté à votre besoin"}',
    limitations = '{"Le transfert de données (inbound/outbound) n''est pas pris en compte pour le moment", "Redshift Serverless n''est pas pris en compte pour le moment"}'
WHERE id = '7e1930c6-ca46-4713-bd02-c8228f1f8586';

UPDATE cloud_service_provider_service
SET levers      = '{"Utilisation d''Auto scaling pour la provisioned capacity", "Choisir la bonne classe de stockage (standard, infrequent, etc...)"}',
    limitations = '{"Le transfert de données (inbound/outbound) n''est pas pris en compte pour le moment", "Seulement DynamoDB On-Demand est pris en charge pour le moment"}'
WHERE id = '512a5e2c-1130-441b-88c9-b0fe8e3dccd8';

UPDATE cloud_service_provider_service
SET levers      = '{"Utilisation de EC2 Right Sizing pour choisir le type d''instance adapté à votre besoin", "Utilisation Amazon EC2 Scheduler pour le démarrage/arrêt des instances non critiques", "Utilisation de EC2 Auto-scaling group pour provisionner le bon nombre d''instance selon le besoin", "Est ce que EC2 est vraiment le service qu''il vous faut ?"}',
    limitations = '{"Ne prend pas en compte le stockage (EBS ou Instance Store)", "Pas de différentiation entre Graviton 1/2/3/4", "Le TDP provient du dernier modèle de l''architecture concernée (x86,arm,epyx)", "Le mode de consommation SPOT n''est pas pris en compte pour le moment"}'
WHERE id = 'b46fb976-ae0f-47e1-8978-993bf6fb1afd';

UPDATE cloud_service_provider_service
SET levers      = '{"Mise en place d''une policy d''archivage des données vers d''autre Tier S3 (Glacier, etc...)"}',
    limitations = '{"Seulement S3 Standard est pris en compte pour le moment", "Le transfert de données (inbound/outbound) n''est pas pris en compte pour le moment"}'
WHERE id = 'e16af23a-dece-4135-9333-c3dfce5fc8dc';

UPDATE cloud_service_provider_service
SET levers      = '{"Right Sizing pour choisir le type d''instance adapté à votre besoin"}',
    limitations = '{"Seulement le service SageMaker Notebooks est pris en compte pour le moment", "Pas de stockage pris en compte"}'
WHERE id = '2e9c3b1b-13fc-4984-8139-93af07ad74df';

UPDATE cloud_service_provider_service
SET levers      = '{"Right-sizing des tâches Fargate (monitoring Cloudwatch)", "Planification des tâches dans les périodes d''utilisation", "ECS Fargate Autoscaling pour provisionner le bon nombre de tâche selon le besoin"}',
    limitations = '{}'
WHERE id = '5a7b23b0-d3d8-4ee5-a7e1-91100e6935f1';

UPDATE cloud_service_provider_service
SET levers      = '{"Optimisation du code source pour réduire un maximum le temps d''éxécution", "Right Sizing des ressources allouées aux Lambda"}',
    limitations = '{}'
WHERE id = 'cece60a9-8e50-4d00-b91e-7a93c0c1229f';