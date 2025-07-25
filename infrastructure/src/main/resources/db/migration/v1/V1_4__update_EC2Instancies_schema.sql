CREATE TABLE IF NOT EXISTS EC2instance(
   Id SERIAL PRIMARY KEY,
   Name  VARCHAR(58) UNIQUE NOT NULL,
   Memory NUMERIC(8,3) NOT NULL,
   vCPUs  INTEGER  NOT NULL,
   cpuType VARCHAR(30) NOT NULL
);

INSERT INTO EC2instance(Name,Memory,vCPUs,cpuType) VALUES ('dc2.8xlarge',244.0,32,'XEON');
INSERT INTO EC2instance(Name,Memory,vCPUs,cpuType) VALUES ('dc2.large',15.0,2,'XEON');
INSERT INTO EC2instance(Name,Memory,vCPUs,cpuType) VALUES ('ra3.16xlarge',384.0,48,'ETSY');
INSERT INTO EC2instance(Name,Memory,vCPUs,cpuType) VALUES ('ra3.4xlarge',96.0,12,'ETSY');
INSERT INTO EC2instance(Name,Memory,vCPUs,cpuType) VALUES ('ra3.xlplus',32.0,4,'ETSY');