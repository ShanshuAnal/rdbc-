CREATE DATABASE IF NOT EXISTS auth
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
USE auth;

DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
    user_id     CHAR(36)     NOT NULL COMMENT '用户业务ID，UUID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码哈希',
    email       VARCHAR(100) NOT NULL COMMENT '邮箱',
    phone       VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常，2-删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email),
    UNIQUE KEY uk_phone (phone)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
    COMMENT = '用户表';

