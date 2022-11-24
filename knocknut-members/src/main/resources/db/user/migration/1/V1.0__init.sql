-- V1.0__init.sql
DROP TABLE IF EXISTS `kn_member`;
CREATE TABLE `kn_member`
(
    `id`               BIGINT(0)   NOT NULL AUTO_INCREMENT,
    `member_level_id`  BIGINT(0),
    `username`         VARCHAR(64) NOT NULL COMMENT '會員帳號',
    `password`         VARCHAR(64) NOT NULL COMMENT '會員密碼 (加密過後)',
    `nickname`         VARCHAR(64) COMMENT '會員別名',
    `email`            VARCHAR(200) COMMENT '會員聯絡信箱',
    `phone`            VARCHAR(64) COMMENT '會員聯絡電話',
    `status`           TINYINT COMMENT '帳號啟用狀態; [0]: 未啟用; [1]: 已啟用; [2]: 禁用',
    `avatar`           VARCHAR(500) COMMENT '會員頭像',
    `gender`           TINYINT COMMENT '性別; [0]: 女; [1]: 男',
    `birthday`         DATE COMMENT '生日',
    `source`           INT(0) COMMENT '會員來源',
    `credit`           INT(0) COMMENT '會員可用積分',
    `total_credits`    INT(0) COMMENT '會員歷史積分',
    `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `last_modified_at` TIMESTAMP DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_username` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5566
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

INSERT INTO `kn_member` (id, member_level_id, username, password, nickname,
                         email, phone, status,
                         avatar,
                         gender, birthday, source)
VALUES (1, NULL, 'clay', '$2a$10$qXZpxje2jFUFyDVXw.4e1.UM03UXTqtMpkfczavrxJipF2emmF/P2', 'cc',
        'clementcheng56@gmail.com', '0912345678', 1,
        'https://avataaars.io/?avatarStyle=Circle&topType=ShortHairShortCurly&accessoriesType=Blank&hairColor=BrownDark&facialHairType=BeardMajestic&facialHairColor=BrownDark&clotheType=BlazerSweater&eyeType=Surprised&eyebrowType=RaisedExcited&mouthType=Serious&skinColor=Brown',
        1, '1989-10-30', 666);