DROP TABLE IF EXISTS link_user_ws_account;

CREATE TABLE link_user_aws_account(
    userId VARCHAR(255) NOT NULL DEFAULT '',
    awsAccountId VARCHAR(255) NOT NULL DEFAULT '',
    PRIMARY KEY (userId, awsAccountId),
    UNIQUE (awsAccountId)
);

INSERT INTO link_user_aws_account VALUES ('user_2rLdM5xtF9UeOCn0jAqb2R7FgLK', 'awsID');