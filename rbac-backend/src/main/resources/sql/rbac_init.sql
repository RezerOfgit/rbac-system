-- ============================================================
-- RBAC 权限管理系统 - 数据库初始化脚本
-- 说明：首次部署时执行此脚本，创建表结构并插入初始测试数据。
-- ============================================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `rbac_system`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `rbac_system`;

-- ============================================================
-- 2. 核心表结构
-- ============================================================

-- 2.1 系统用户表
CREATE TABLE `sys_user` (
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    varchar(64)  NOT NULL                COMMENT '用户名',
    `password`    varchar(100) NOT NULL                COMMENT 'BCrypt 加密后的密码',
    `email`       varchar(100) DEFAULT NULL            COMMENT '邮箱',
    `status`      tinyint(1)   DEFAULT '1'             COMMENT '账号状态 (1正常 0停用)',
    `create_by`   varchar(64)  DEFAULT NULL            COMMENT '创建人',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`    tinyint(1)   DEFAULT '0'             COMMENT '逻辑删除标志 (0正常 1删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 2.2 系统角色表
CREATE TABLE `sys_role` (
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`   varchar(30)  NOT NULL                COMMENT '角色名称',
    `role_key`    varchar(100) NOT NULL                COMMENT '角色标识 (如 admin、user)',
    `status`      tinyint(1)   DEFAULT '1'             COMMENT '角色状态 (1正常 0停用)',
    `create_by`   varchar(64)  DEFAULT NULL            COMMENT '创建人',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`    tinyint(1)   DEFAULT '0'             COMMENT '逻辑删除标志',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 2.3 系统菜单/权限表
CREATE TABLE `sys_menu` (
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '菜单/权限ID',
    `menu_name`   varchar(50)  NOT NULL                COMMENT '菜单名称',
    `parent_id`   bigint       DEFAULT '0'             COMMENT '父菜单ID (0表示顶级)',
    `order_num`   int          DEFAULT '0'             COMMENT '同级排序',
    `path`        varchar(200) DEFAULT ''              COMMENT '前端路由地址',
    `menu_type`   char(1)      DEFAULT ''              COMMENT '菜单类型 (M目录 C菜单 F按钮)',
    `perms`       varchar(100) DEFAULT NULL            COMMENT '权限标识 (如 sys:user:add)',
    `create_by`   varchar(64)  DEFAULT NULL            COMMENT '创建人',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 2.4 用户-角色关联表（多对多中间表）
CREATE TABLE `sys_user_role` (
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户与角色关联表';

-- 2.5 角色-菜单关联表（多对多中间表）
CREATE TABLE `sys_role_menu` (
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色与菜单关联表';


-- ============================================================
-- 3. 初始测试数据
-- 说明：admin 和 testuser 的密码均为 "123456"，使用 BCrypt 加密
-- ============================================================

-- 3.1 初始化账号
INSERT INTO `sys_user` (`id`, `username`, `password`, `create_by`)
VALUES (1, 'admin', '$2a$10$o3xJZdw6gaFH9i6TGBWB7.6d.xyr90XzM6Qwnsv1RqNuyhMl4hcHy', 'system');

-- 3.2 初始化权限树（菜单与按钮）
-- 顶级目录
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`)
VALUES (1, '系统管理', 0, 1, '/system', 'M', '', 'admin');

-- 子菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`)
VALUES (2, '用户管理', 1, 1, '/system/user', 'C', 'sys:user:list', 'admin');

INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`)
VALUES (3, '角色管理', 1, 2, '/system/role', 'C', 'sys:role:list', 'admin');

-- 按钮级权限（归属“用户管理”）
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`)
VALUES (4, '用户修改', 2, 3, '', 'F', 'sys:user:update', 'admin');

INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`)
VALUES (5, '用户删除', 2, 4, '', 'F', 'sys:user:delete', 'admin');

INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`)
VALUES (6, '用户查询', 2, 5, '', 'F', 'sys:user:list', 'admin');

-- 3.3 初始化角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `create_by`)
VALUES (2, '普通员工', 'common', 'admin');

-- 3.4 为角色批量授予权限（关联菜单）
-- 分配菜单权限和按钮权限，让普通角色拥有完整的“用户管理”能力
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 2);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 3);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 4);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 5);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 6);

-- 3.5 初始化普通测试用户并绑定角色
INSERT INTO `sys_user` (`id`, `username`, `password`, `create_by`)
VALUES (2, 'testuser', '$2a$10$zFdTpFKp/dYlpC0Muo4.3Of/wGlbu.fHPWs0rZZ3UzS0qw0Pbobd2', 'admin');

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (2, 2);

-- ============================================================
-- 4. 【可选】数据验证 SQL（执行后可在 DBeaver 中查看结果）
-- ============================================================
-- SELECT * FROM sys_user WHERE del_flag = 0;
-- SELECT * FROM sys_role_menu WHERE role_id = 2;