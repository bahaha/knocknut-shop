-- V1.0__init.sql
CREATE TABLE `oauth_client_details`
(
    `client_id`               VARCHAR(256) NOT NULL COMMENT '信任的服務id',
    `client_secret`           VARCHAR(256) COMMENT '服務的密鑰',
    `resource_ids`            VARCHAR(256),
    `scope`                   VARCHAR(256) COMMENT '授權請求的範圍',
    `authorized_grant_types`  VARCHAR(256) COMMENT '授權的類型種類; authorization_code: 授權碼; password: 密碼模式; refresh_token: 允許取得刷新token',
    `web_server_redirect_uri` VARCHAR(256) COMMENT '授權完成後的目標頁面',
    `authorities`             VARCHAR(256),
    `access_token_validity`   int(0),
    `refresh_token_validity`  int(0),
    `additional_information`  VARCHAR(4096),
    `autoapprove`             VARCHAR(256),
    `created_at`              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `last_modified_at`        TIMESTAMP DEFAULT NULL,
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- knocknut#member
INSERT INTO `oauth_client_details` (client_id, client_secret,
                                    scope, authorized_grant_types)
VALUES ('member-service', '$2a$10$mANMs3XwZmc8qMDDmhbqGemK5YKtGZHCMvTSGtof.9ygbsrctI2GW',
        'read,write', 'password');