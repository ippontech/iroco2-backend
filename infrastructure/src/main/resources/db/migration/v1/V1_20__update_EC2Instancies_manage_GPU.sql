ALTER TABLE EC2Instance
ADD GPU INTEGER,
ADD gpuType VARCHAR(15);

UPDATE EC2Instance
SET GPU = 4, gpuType = 'T4'
WHERE name='g4dn.12xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'T4'
WHERE name='g4dn.16xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'T4'
WHERE name='g4dn.2xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'T4'
WHERE name='g4dn.4xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'T4'
WHERE name='g4dn.8xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'T4'
WHERE name='g4dn.xlarge';

UPDATE EC2Instance
SET GPU = 8, gpuType = 'V100'
WHERE name='p3.16xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'V100'
WHERE name='p3.2xlarge';

UPDATE EC2Instance
SET GPU = 4, gpuType = 'V100'
WHERE name='p3.8xlarge';

UPDATE EC2Instance
SET GPU = 8, gpuType = 'A100'
WHERE name='p4de.24xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'INFERENTIA'
WHERE name='trn1.2xlarge';

UPDATE EC2Instance
SET GPU = 1, gpuType = 'INFERENTIA'
WHERE name='trn1.32xlarge';

UPDATE EC2Instance
SET GPU = 16, gpuType = 'INFERENTIA'
WHERE name='trn1n.32xlarge';
