USE auth;

CREATE TABLE IF NOT EXISTS sys_role
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '数据库主键',

    role_id     CHAR(36)     NOT NULL COMMENT '角色业务ID，UUID',

    role_name   VARCHAR(50)  NOT NULL COMMENT '角色名称',

    role_key    VARCHAR(50)  NOT NULL COMMENT '角色标识',

    description VARCHAR(255)          DEFAULT NULL COMMENT '角色描述',

    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '角色状态：0-禁用，1-正常',

    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),

    UNIQUE KEY uk_role_id (role_id),
    UNIQUE KEY uk_role_name (role_name),
    UNIQUE KEY uk_role_key (role_key)

) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
    COMMENT = '角色表';

CREATE TABLE IF NOT EXISTS sys_user_role
(
    id      BIGINT  NOT NULL AUTO_INCREMENT COMMENT '数据库主键',

    user_id CHAR(36) NOT NULL COMMENT '用户业务ID',

    role_id CHAR(36) NOT NULL COMMENT '角色业务ID',

    PRIMARY KEY (id),

    UNIQUE KEY uk_user_role (user_id, role_id)

) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
    COMMENT = '用户角色关联表';
