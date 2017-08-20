DROP TABLE IF EXISTS `user`;

CREATE TABLE `xiaoxin_user` (
  `user_id`      INT(11)      NOT NULL AUTO_INCREMENT
  COMMENT '用户id',
  `email`        VARCHAR(100) NOT NULL
  COMMENT '邮箱',
  `password`     VARCHAR(100) NOT NULL
  COMMENT '用户密码',
  `secret_key`   VARCHAR(60)  NOT NULL
  COMMENT '盐值',
  `created_time` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `update_time`  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
  COMMENT '修改时间',
  `last_login`   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
  COMMENT '上次登录时间',
  `status`       TINYINT               DEFAULT 1
  COMMENT '状态 0：未激活 1：激活',
  PRIMARY KEY (`user_id`),
  UNIQUE (`email`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `xiaoxin_user_info` (
  `info_id`      INT(11)      NOT NULL AUTO_INCREMENT
  COMMENT '用户信息id',
  `user_id`      INT(11)      NOT NULL
  COMMENT '用户id',
  `display_name` VARCHAR(100) NOT NULL
  COMMENT '用户名',
  `email`        VARCHAR(100) NOT NULL
  COMMENT '邮箱',
  `gender`       VARCHAR(1)   NOT NULL
  COMMENT '性别M:F',
  `age`          INT(3)       NOT NULL
  COMMENT '年龄',
  `created_time` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `update_time`  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
  COMMENT '修改时间',
  `status`       TINYINT(1)            DEFAULT 1
  COMMENT '状态 0：未激活 1：激活',
  PRIMARY KEY (`info_id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;