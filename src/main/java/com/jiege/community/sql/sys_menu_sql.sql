USE auth;

CREATE TABLE IF NOT EXISTS sys_menu
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '数据库主键',

    menu_id     CHAR(36)     NOT NULL COMMENT '菜单业务ID，UUID',

    parent_id   CHAR(36)              DEFAULT NULL COMMENT '父菜单业务ID，根菜单为NULL',

    menu_name   VARCHAR(50)  NOT NULL COMMENT '菜单名称',

    path        VARCHAR(200)          DEFAULT NULL COMMENT '路由地址',

    perms       VARCHAR(100)          DEFAULT NULL COMMENT '权限标识，如 user:add',

    type        TINYINT      NOT NULL COMMENT '菜单类型：1-目录，2-菜单，3-按钮',

    icon        VARCHAR(100)          DEFAULT NULL COMMENT '图标',

    sort        INT          NOT NULL DEFAULT 0 COMMENT '显示排序',

    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '菜单状态：0-禁用，1-正常',

    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),

    UNIQUE KEY uk_menu_id (menu_id),

    UNIQUE KEY uk_menu_name (menu_name),

    UNIQUE KEY uk_perms (perms)

) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
    COMMENT = '菜单权限表';

CREATE TABLE IF NOT EXISTS sys_role_menu
(
    id      BIGINT  NOT NULL AUTO_INCREMENT COMMENT '数据库主键',

    role_id CHAR(36) NOT NULL COMMENT '角色业务ID',

    menu_id CHAR(36) NOT NULL COMMENT '菜单业务ID',

    PRIMARY KEY (id),

    UNIQUE KEY uk_role_menu (role_id, menu_id)

) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
    COMMENT = '角色菜单关联表';
