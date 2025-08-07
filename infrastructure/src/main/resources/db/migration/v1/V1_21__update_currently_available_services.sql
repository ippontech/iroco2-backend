UPDATE services SET limitation = 'Ne prend pas en compte le stockage (EBS ou Instance Store)\nPas de différentiation entre Graviton 1/2/3/4\n Le TDP provient du dernier modèle de l''architecture concernée (x86,arm,epyx)\nLe mode de consommation SPOT n''est pas prit en compte pour le moment' where services.name='Amazon Elastic Compute Cloud (EC2)';
UPDATE services SET lever = 'Utilisation de EC2 Right Sizing pour choisir le type d''instance adapté à votre besoin\nUtilisation Amazon EC2 Scheduler pour le démarrage/arrêt des instances non critiques\nUtilisation de EC2 Auto-scaling group pour provisionner le bon nombre d''instance selon le besoin\nEst ce que EC2 est vraiment le service qu''il vous faut ?
' where services.name='Amazon Elastic Compute Cloud (EC2)';

UPDATE services SET lever = 'Right-sizing des tâches Fargate (monitoring Cloudwatch)\nPlanification des tâches dans les périodes d''utilisation\nECS Fargate Autoscaling pour provisionner le bon nombre de tâche selon le besoin
' where services.name='AWS Fargate';

UPDATE services SET limitation = 'Seulement S3 Standard est prit en compte pour le moment\nLe transfert de données (inbound/outbound) n''est pas pris en compte pour le moment' where services.name='Amazon Simple Storage Service (S3)';
UPDATE services SET lever = 'Mise en place d''une policy d''archivage des données vers d''autre Tier S3 (Glacier, etc...)' where services.name='Amazon Simple Storage Service (S3)';

UPDATE services SET limitation = 'Aurora Serverless ne prend en compte que le stockage pour le moment (et pas les ACUs)
' where services.name='Amazon Aurora';
UPDATE services SET lever = 'Right Sizing pour choisir le type d''instance adapté à votre besoin\nDéfinir des policy de gestion des backup\nAuto-scaling des Read replicas
' where services.name='Amazon Aurora';

UPDATE services SET limitation = 'Le transfert de données (inbound/outbound) n''est pas pris en compte pour le moment\nSeulement DynamoDB On-Demand est prit en charge pour le moment' where services.name='Amazon DynamoDB';
UPDATE services SET lever = 'Utilisation d''Auto scaling pour la provisioned capacity\nChoisir le bonne classe de stockage (standard, infrequent, etc...)' where services.name='Amazon DynamoDB';

UPDATE services SET limitation = 'Le transfert de données (inbound/outbound) n''est pas pris en compte pour le moment\nRedshift Serverless n''est pas prit en compte pour le moment' where services.name='Amazon Redshift';
UPDATE services SET lever = 'Utilisation d''Auto scaling pour la provisioned capacity\nRight Sizing pour choisir le type d''instance adapté à votre besoin' where services.name='Amazon Redshift';

UPDATE services SET lever = 'Optimisation du code source pour réduire un maximum le temps d''execution\nRight Sizing des ressources allouées aux Lambda' where services.name='AWS Lambda';
