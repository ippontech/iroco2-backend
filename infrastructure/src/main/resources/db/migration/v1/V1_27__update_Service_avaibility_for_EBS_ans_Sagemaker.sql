-- EBS
UPDATE services SET availability = 'AVAILABLE' where services.name='Amazon Elastic Block Store (EBS)';

UPDATE services SET limitation = 'Il n''y a pas de différenciation entre les types de stockage (SSD, HDD, etc...)' where services.name='Amazon Elastic Block Store (EBS)';
UPDATE services SET lever = 'Mise en place d''une policy d''archivage des données vers d''autre stockages ou de suppression' where services.name='Amazon Elastic Block Store (EBS)';

INSERT INTO VARIABLE_SERVICES(VARIABLE_ID, SERVICES_NAME)
SELECT var.ID, 'Amazon Elastic Block Store (EBS)' FROM VARIABLE as var
    WHERE var.NAME = 'STORAGE';

-- Sagemaker

UPDATE services SET availability = 'AVAILABLE' where services.name='Amazon SageMaker';

UPDATE services SET limitation = 'Seulement le service SageMaker Notebooks est pris en compte pour le moment\nPas de stockage pris en compte' where services.name='Amazon SageMaker';
UPDATE services SET lever = 'Right Sizing pour choisir le type d''instance adapté à votre besoin' where services.name='Amazon SageMaker';

INSERT INTO VARIABLE_SERVICES(VARIABLE_ID, SERVICES_NAME)
SELECT var.ID, 'Amazon SageMaker' FROM VARIABLE as var
    WHERE var.NAME = 'MEMORY';

INSERT INTO VARIABLE_SERVICES(VARIABLE_ID, SERVICES_NAME)
SELECT var.ID, 'Amazon SageMaker' FROM VARIABLE as var
    WHERE var.NAME = 'CPU_COMPUTE';






