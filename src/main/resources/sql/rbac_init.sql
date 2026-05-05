-- 创建数据库并使用
CREATE DATABASE IF NOT EXISTS `rbac_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `rbac_system`;

-- 1. 系统用户表
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '1' COMMENT '账号状态 (1正常 0停用)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志 (0正常 1删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 2. 系统角色表
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称 (如：管理员)',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串 (如：admin)',
  `status` tinyint(1) DEFAULT '1' COMMENT '角色状态 (1正常 0停用)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志 (0正常 1删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 3. 系统菜单/权限表
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单/权限ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型 (M目录 C菜单 F按钮)',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识 (如：sys:user:add)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 4. 用户和角色关联表
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和角色关联表';

-- 5. 角色和菜单关联表
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

-- 插入一条超管测试数据 (密码为明文 123456 加密后的 BCrypt 密文)
INSERT INTO `sys_user` (`id`, `username`, `password`, `create_by`) 
		VALUES (1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'system');


UPDATE sys_user 
SET password = '$2a$10$o3xJZdw6gaFH9i6TGBWB7.6d.xyr90XzM6Qwnsv1RqNuyhMl4hcHy' 
WHERE username = 'admin';

-- 1. 插入顶级目录：系统管理 (id = 1, parent_id = 0)
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`) 
VALUES (1, '系统管理', 0, 1, '/system', 'M', '', 'admin');

-- 2. 插入子菜单：用户管理 (id = 2, parent_id = 1, 归属于“系统管理”)
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`) 
VALUES (2, '用户管理', 1, 1, '/system/user', 'C', 'sys:user:list', 'admin');

-- 3. 插入子菜单：角色管理 (id = 3, parent_id = 1, 归属于“系统管理”)
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `menu_type`, `perms`, `create_by`) 
VALUES (3, '角色管理', 1, 2, '/system/role', 'C', 'sys:role:list', 'admin');

-- 1. 创建一个普通用户 (账号: testuser, 密码: 123456 的 BCrypt 密文)
INSERT INTO `sys_user` (`id`, `username`, `password`, `create_by`) 
VALUES (2, 'testuser', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'admin');

-- 2. 创建一个普通角色 (例如: 普通员工)
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `create_by`) 
VALUES (2, '普通员工', 'common', 'admin');

-- 3. 将 testuser(id=2) 和 普通员工角色(id=2) 关联起来
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (2, 2);

UPDATE sys_user 
SET password = '$2a$10$zFdTpFKp/dYlpC0Muo4.3Of/wGlbu.fHPWs0rZZ3UzS0qw0Pbobd2' 
WHERE username = 'testuser';










