UPDATE AWSRELATIVIEREGIONPRICING
SET RelativePercentPricing = -3.69
WHERE AWSDataCenter = 'Europe (Ireland)';

UPDATE AWSRELATIVIEREGIONPRICING
SET RelativePercentPricing = 8.89
WHERE AWSDataCenter = 'Europe (Paris)';

UPDATE AWSRELATIVIEREGIONPRICING
SET RelativePercentPricing = 8.92
WHERE AWSDataCenter = 'Europe (Milan)';

UPDATE AWSRELATIVIEREGIONPRICING
SET RelativePercentPricing = 6.58
WHERE AWSDataCenter = 'Europe (London)';

UPDATE AWSRELATIVIEREGIONPRICING
SET RelativePercentPricing = 8.26
WHERE AWSDataCenter = 'Europe (Frankfurt)';

UPDATE AWSRELATIVIEREGIONPRICING
SET RelativePercentPricing = 19.32
WHERE AWSDataCenter = 'Europe (Zurich)';

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Europe (Spain)', 4.25);

ALTER TABLE AWSRELATIVIEREGIONPRICING ALTER COLUMN AWSDataCenter TYPE varchar(30);

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('US East (N. Virginia)', -6.91);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('US East (Ohio)', 6.48);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('US West (N. California)', 8.92);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('US West (Oregon)', -6.92);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Canada (Central)', 8.92);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Canada West (Calgary)', 6.58);

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Africa (Cap Town)', 19.11);

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Hong Kong)', 25.42);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Hyderabad)', 6.78);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Jakarta)', 18.14);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Melbourne)', 16.36);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Mumbai)', 7.10);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Osaka)', 18.90);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Seoul)', 14.37);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Singapore)', 18.15);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Sydney)', 16.64);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Asia Pacific (Tokyo)', 18.68);

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Israel (Tel Aviv)', 12.61);

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Middle East (Bahrain)', 14.57);
INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('Middle East (UAE)', 13.97);

INSERT INTO AWSRELATIVIEREGIONPRICING(AWSDataCenter,RelativePercentPricing) VALUES ('South America (Sao Paulo)', 41.11);