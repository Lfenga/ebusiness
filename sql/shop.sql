/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50740
Source Host           : localhost:3306
Source Database       : shop

Target Server Type    : MYSQL
Target Server Version : 50740
File Encoding         : 65001

Date: 2025-11-24 18:17:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_operation_log`;
CREATE TABLE `admin_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` int(11) NOT NULL COMMENT '管理员ID',
  `admin_name` varchar(50) NOT NULL COMMENT '管理员用户名（冗余）',
  `admin_realname` varchar(50) DEFAULT NULL COMMENT '管理员真实姓名（冗余）',
  `operation_type` varchar(20) NOT NULL COMMENT '操作类型：ADD/UPDATE/DELETE/QUERY',
  `module` varchar(50) NOT NULL COMMENT '操作模块：商品/分类/用户/订单/管理员',
  `operation_desc` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `target_type` varchar(50) DEFAULT NULL COMMENT '目标对象类型：Goods/GoodsType/User/Order等',
  `target_id` varchar(100) DEFAULT NULL COMMENT '目标对象ID（多个用逗号分隔）',
  `request_method` varchar(200) DEFAULT NULL COMMENT '请求方法名',
  `request_params` text COMMENT '请求参数（JSON格式）',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `browser_info` varchar(200) DEFAULT NULL COMMENT '浏览器信息',
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `execution_time` bigint(20) DEFAULT NULL COMMENT '执行耗时（毫秒）',
  `status` varchar(20) DEFAULT 'SUCCESS' COMMENT '执行状态：SUCCESS/FAIL',
  `error_msg` text COMMENT '错误信息（失败时记录）',
  PRIMARY KEY (`id`),
  KEY `idx_admin_id` (`admin_id`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_module` (`module`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_status` (`status`),
  CONSTRAINT `admin_operation_log_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `ausertable` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';

-- ----------------------------
-- Records of admin_operation_log
-- ----------------------------
INSERT INTO `admin_operation_log` VALUES ('1', '1', 'admin', '系统管理员', 'ADD', '商品', '添加商品：测试商品', 'Goods', '1', null, null, '127.0.0.1', null, '2025-11-16 16:39:56', null, 'FAIL', null);
INSERT INTO `admin_operation_log` VALUES ('36', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '2', 'addGoods', '参数序列化失败', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 19:38:24', '9', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('37', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '3', 'addGoods', '参数序列化失败', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 19:39:12', '5', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('38', '1', 'admin', '系统管理员', 'ADD', '商品类型管理', '添加商品类型', 'GoodsType', '2', 'addType', '[{\"id\":0,\"typename\":\"衣物\"}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 19:44:03', '20', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('39', '1', 'admin', '系统管理员', 'DELETE', '商品管理', '删除商品', 'Goods', '2', 'delete', '[5]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:20:47', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('40', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:4, 名称:电脑, 状态:下架]', 'Goods', '4', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:43:55', '7', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('41', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:4, 名称:电脑, 状态:上架]', 'Goods', '4', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:44:35', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('42', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', '3', 'delete', '[1]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:45:58', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('43', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', '1', 'delete', '[1]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:46:05', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('44', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', '1', 'delete', '[4]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:46:15', '8', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('45', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品 [名称:xx]', 'Goods', '0', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:55:42', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('46', '1', 'admin', '系统管理员', 'ADD', '商品类型管理', '添加商品类型', 'GoodsType', '5', 'addType', '[{\"id\":5,\"typename\":\"衣物\"}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:03:49', '17', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('47', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', '5', 'delete', '[5]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:03:52', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('48', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '7', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:04:29', '13', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('49', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:6, 名称:xx, 状态:下架]', 'Goods', '6', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:04:50', '8', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('50', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:7, 名称:xxnn, 状态:上架]', 'Goods', '7', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:05:00', '6', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('51', '1', 'admin', '系统管理员', 'DELETE', '商品管理', '删除商品', 'Goods', '7', 'delete', '[7]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:05:16', '19', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('52', '1', 'admin', '系统管理员', 'ADD', '管理员管理', '添加管理员', 'AUser', '2', 'addAdmin', '[{\"id\":2,\"aname\":\"root\",\"apwd\":\"$2a$10$4Oc0c3EvzujaFQTbfH/UGejLdLR07aXJLpenNKd6diR.Q6Dt5jqr.\",\"aemail\":\"11@qq.com\",\"arealname\":\"系统管理员2\",\"aphone\":\"11111111111\",\"astatus\":1,\"createTime\":null,\"lastLoginTime\":null}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:07:55', '131', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('53', '2', 'root', '系统管理员2', 'UPDATE', '商品管理', '修改商品 [ID:6, 名称:xxm, 状态:上架]', 'Goods', '6', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:08:51', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('54', '1', 'admin', '系统管理员', 'ADD', '商品类型管理', '添加商品类型', 'GoodsType', '6', 'addType', '[{\"id\":6,\"typename\":\"衣物\"}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-17 14:50:13', '8', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('55', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', '6', 'delete', '[6]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-17 14:50:15', '8', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('56', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:2, 名称:手机, 状态:上架]', 'Goods', '2', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-18 22:12:02', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('57', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '8', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-18 23:00:33', '75', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('58', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:8, 名称:二手飞机, 状态:上架]', 'Goods', '8', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 00:00:57', '9', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('59', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:8, 名称:二手飞机, 状态:上架]', 'Goods', '8', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 00:08:14', '16', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('60', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:8, 名称:二手飞机, 状态:上架]', 'Goods', '8', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 01:45:07', '19', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('61', '2', 'root', '系统管理员2', 'UPDATE', '商品管理', '修改商品 [ID:6, 名称:xxm, 状态:上架]', 'Goods', '6', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 19:52:28', '18', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('62', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:6, 名称:xmll, 状态:上架]', 'Goods', '6', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 20:10:55', '6', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('63', '1', 'admin', '系统管理员', 'ADD', '管理员管理', '添加管理员', 'AUser', 'admin/addAdmin', 'addAdmin', '[{\"id\":null,\"aname\":\"111\",\"apwd\":\"$2a$10$v2oNWzfDCDoUKm8jbbCIO.lmu07oiLoqlajTAD405mCxYxkGHKE82\",\"aemail\":\"11@qq.com\",\"arealname\":\"111111111\",\"aphone\":\"11111111111\",\"astatus\":1,\"createTime\":null,\"lastLoginTime\":null}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 20:12:11', '120', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('64', '1', 'admin', '系统管理员', 'ADD', '管理员管理', '添加管理员', 'AUser', 'admin/addAdmin', 'addAdmin', '[{\"id\":null,\"aname\":\"111\",\"apwd\":\"$2a$10$IneEOWEjvMaQEr0fEuHY7uff.OrmWVGIUUhsgY/AO5KrNtoGEdvJS\",\"aemail\":\"11@qq.com\",\"arealname\":\"111111111\",\"aphone\":\"11111111111\",\"astatus\":1,\"createTime\":null,\"lastLoginTime\":null}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 20:12:31', '76', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('65', '1', 'admin', '系统管理员', 'ADD', '管理员管理', '添加管理员', 'AUser', 'admin/addAdmin', 'addAdmin', '[{\"id\":null,\"aname\":\"111\",\"apwd\":\"$2a$10$2xhuinLfOwN/efOtthDORuGSsKqVjhWG1wFQKJ7kfe8RPHivbLvpW\",\"aemail\":\"11@qq.com\",\"arealname\":\"111111111\",\"aphone\":\"11111111111\",\"astatus\":1,\"createTime\":null,\"lastLoginTime\":null}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 20:12:48', '201', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('66', '1', 'admin', '系统管理员', 'ADD', '管理员管理', '添加管理员', 'AUser', '6', 'addAdmin', '[{\"id\":6,\"aname\":\"2222\",\"apwd\":\"$2a$10$Cy06A/z5sNa3GZHqAretoOx2jiCu35mMauhc/ZUe3720vw3lvvJ86\",\"aemail\":\"2222@qq.com\",\"arealname\":\"系统管理员22\",\"aphone\":\"22222222222\",\"astatus\":1,\"createTime\":null,\"lastLoginTime\":null}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-19 20:13:17', '139', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('81', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '快捷清除缓存', '', null, 'quickClearCache', '[\"goods_types\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-21 11:53:08', '1', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('82', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '清除指定缓存键', '', null, 'evictCache', '[\"goods:detail\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-21 11:54:49', '1', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('83', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '清除指定缓存键', '', null, 'evictCache', '[\"goods:detail\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-21 11:55:08', '1', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('84', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '清除指定缓存键', '', null, 'evictCache', '[\"goods:detail:8\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-21 11:55:54', '2', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('85', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '清除搜索结果缓存 [模式: index:search:*]', '', null, 'quickClearSearch', '[]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-21 12:04:14', '5', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('86', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '清除商品类型缓存 [键: index:goods_types]', '', null, 'quickClearGoodsTypes', '[]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-21 12:04:33', '3', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('87', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:2, 名称:手机, 状态:上架]', 'Goods', '2', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 11:36:08', '30', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('88', '1', 'admin', '系统管理员', 'DELETE', '缓存管理', '清空所有缓存', '', null, 'evictAllCache', '[]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 11:41:41', '3', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('89', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '9', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 15:26:50', '38', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('90', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '10', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 15:37:23', '54', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('91', '2', 'root', '系统管理员2', 'ADD', '商品管理', '添加商品', 'Goods', '11', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 16:46:54', '83', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('92', '2', 'root', '系统管理员2', 'ADD', '商品管理', '添加商品', 'Goods', '12', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 17:43:55', '74', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('93', '2', 'root', '系统管理员2', 'ADD', '商品管理', '添加商品', 'Goods', '13', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 17:45:48', '19', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('94', '2', 'root', '系统管理员2', 'ADD', '商品管理', '添加商品', 'Goods', '14', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 18:00:26', '46', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('95', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '15', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 18:03:42', '55', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('96', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '16', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-24 18:14:06', '14', 'SUCCESS', null);

-- ----------------------------
-- Table structure for ausertable
-- ----------------------------
DROP TABLE IF EXISTS `ausertable`;
CREATE TABLE `ausertable` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `aname` varchar(50) NOT NULL COMMENT '管理员用户名',
  `apwd` varchar(100) NOT NULL COMMENT '管理员密码（BCrypt加密）',
  `aemail` varchar(100) DEFAULT NULL COMMENT '管理员邮箱',
  `arealname` varchar(50) DEFAULT NULL COMMENT '管理员真实姓名',
  `aphone` varchar(20) DEFAULT NULL COMMENT '管理员手机号',
  `astatus` int(11) DEFAULT '1' COMMENT '账号状态：1-启用，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `aname` (`aname`),
  UNIQUE KEY `aemail` (`aemail`),
  KEY `idx_aname` (`aname`),
  KEY `idx_aemail` (`aemail`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ----------------------------
-- Records of ausertable
-- ----------------------------
INSERT INTO `ausertable` VALUES ('1', 'admin', '$2a$10$B0mMb0sbJcdlBomYuW3wNeMZ73ACtRkN0k3Fg5VgEgd4vBKCqt51q', 'admin@ebusiness.com', '系统管理员', null, '1', '2025-11-16 16:39:56', '2025-11-24 18:02:14');
INSERT INTO `ausertable` VALUES ('2', 'root', '$2a$10$4Oc0c3EvzujaFQTbfH/UGejLdLR07aXJLpenNKd6diR.Q6Dt5jqr.', '11@qq.com', '系统管理员2', '11111111111', '1', '2025-11-16 22:07:55', '2025-11-24 17:43:23');
INSERT INTO `ausertable` VALUES ('6', '2222', '$2a$10$Cy06A/z5sNa3GZHqAretoOx2jiCu35mMauhc/ZUe3720vw3lvvJ86', '2222@qq.com', '系统管理员22', '22222222222', '1', '2025-11-19 20:13:17', null);

-- ----------------------------
-- Table structure for busertable
-- ----------------------------
DROP TABLE IF EXISTS `busertable`;
CREATE TABLE `busertable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bemail` varchar(100) NOT NULL,
  `bpwd` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `bemail` (`bemail`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of busertable
-- ----------------------------
INSERT INTO `busertable` VALUES ('1', 'user@example.com', '$2a$10$SCrlTn5YAqQyGsS5J8zdUOeVjFvVMqMOpR6JXnwqJ9o0/yV/8OG7q', '2025-11-17 14:41:22');
INSERT INTO `busertable` VALUES ('2', 'user2@example.com', '$2a$10$hqZ0pkKU2m5brIDy03dhC.oLRAynW/EEhLqvKTag6BH3GYORvn1zG', '2025-11-17 15:10:02');

-- ----------------------------
-- Table structure for carttable
-- ----------------------------
DROP TABLE IF EXISTS `carttable`;
CREATE TABLE `carttable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `busertable_id` int(11) DEFAULT NULL,
  `goodstable_id` int(11) DEFAULT NULL,
  `shoppingnum` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `busertable_id` (`busertable_id`),
  KEY `goodstable_id` (`goodstable_id`),
  CONSTRAINT `carttable_ibfk_1` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`),
  CONSTRAINT `carttable_ibfk_2` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of carttable
-- ----------------------------
INSERT INTO `carttable` VALUES ('7', '1', '6', '1');
INSERT INTO `carttable` VALUES ('16', '2', '2', '1');

-- ----------------------------
-- Table structure for goodstable
-- ----------------------------
DROP TABLE IF EXISTS `goodstable`;
CREATE TABLE `goodstable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gname` varchar(100) NOT NULL,
  `goprice` decimal(10,2) NOT NULL COMMENT '商品原价',
  `grprice` decimal(10,2) NOT NULL COMMENT '团购价格',
  `stock` int(11) NOT NULL,
  `gpicture` varchar(100) DEFAULT NULL,
  `goodstype_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '商品状态：1-上架，0-下架',
  PRIMARY KEY (`id`),
  KEY `goodstype_id` (`goodstype_id`),
  CONSTRAINT `goodstable_ibfk_1` FOREIGN KEY (`goodstype_id`) REFERENCES `goodstype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goodstable
-- ----------------------------
INSERT INTO `goodstable` VALUES ('1', 'T恤', '50.00', '39.90', '54', '201910274135150096.jpg', '1', '1');
INSERT INTO `goodstable` VALUES ('2', '手机', '2000.00', '1800.00', '48', '201910280135503341.jpg', '2', '1');
INSERT INTO `goodstable` VALUES ('3', '沙发', '3000.00', '2500.00', '19', '201910274135059473.jpg', '3', '1');
INSERT INTO `goodstable` VALUES ('4', '电脑', '1800.00', '1700.00', '28', '202511315203635221.jpg', '1', '1');
INSERT INTO `goodstable` VALUES ('6', 'xmll', '111.00', '100.00', '11', '202511320215541868.webp', '1', '1');
INSERT INTO `goodstable` VALUES ('8', '二手飞机', '121.00', '102.00', '94', '202511323000814118.png', '3', '1');
INSERT INTO `goodstable` VALUES ('9', '男士英伦风帅气商务西装套装新郎结婚韩版修身型男高端双排扣西服', '836.07', '635.75', '23', '202511328152649471.png', '1', '1');
INSERT INTO `goodstable` VALUES ('10', '你好卡农 毛毛立领插肩袖棉服女冬季2025新款设计感休闲百搭外套', '368.00', '268.00', '43', '202511328153722658.png', '1', '1');
INSERT INTO `goodstable` VALUES ('11', 'Apple/苹果 iPhone 17 Pro Max', '9999.90', '8888.70', '66', '202511328164653698.png', '2', '1');
INSERT INTO `goodstable` VALUES ('12', '2025新款英特尔i9级处理器笔记本电脑4K超轻薄办公设计游戏电竞本', '970.00', '705.00', '34', '202511328174355358.png', '2', '1');
INSERT INTO `goodstable` VALUES ('13', '电热水器空心墙专用支架挂架龙门型架子墙壁承重调节挂板通用配件', '52.58', '50.00', '23', '202511328174547704.png', '3', '1');
INSERT INTO `goodstable` VALUES ('14', '短款羽绒服女冬季2025年新款加厚棉服面包服外套宽松时尚洋气学生', '125.00', '95.00', '36', '202511328180025662.png', '1', '1');
INSERT INTO `goodstable` VALUES ('15', '【柜版直发】MON*90白鸭绒连帽加厚羽绒服女雾面哑光情侣保暖外套', '697.00', '583.00', '75', '202511328180342105.png', '1', '1');
INSERT INTO `goodstable` VALUES ('16', '智能手表16GB+256GB插卡通话手表运动学习办公S10promaxa全网通5G', '149.00', '120.00', '22', '202511328181406411.png', '2', '1');

-- ----------------------------
-- Table structure for goodstype
-- ----------------------------
DROP TABLE IF EXISTS `goodstype`;
CREATE TABLE `goodstype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typename` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goodstype
-- ----------------------------
INSERT INTO `goodstype` VALUES ('1', '服装');
INSERT INTO `goodstype` VALUES ('2', '电子产品');
INSERT INTO `goodstype` VALUES ('3', '家居用品');

-- ----------------------------
-- Table structure for orderbasetable
-- ----------------------------
DROP TABLE IF EXISTS `orderbasetable`;
CREATE TABLE `orderbasetable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `busertable_id` int(11) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` int(11) DEFAULT '0',
  `orderdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `busertable_id` (`busertable_id`),
  CONSTRAINT `orderbasetable_ibfk_1` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderbasetable
-- ----------------------------
INSERT INTO `orderbasetable` VALUES ('1', '1', '1800.00', '1', '2025-11-11 20:40:15');
INSERT INTO `orderbasetable` VALUES ('2', '1', '2500.00', '1', '2025-11-13 18:06:48');
INSERT INTO `orderbasetable` VALUES ('3', '1', '17000.00', '1', '2025-11-13 19:46:16');
INSERT INTO `orderbasetable` VALUES ('4', '1', '1578.80', '1', '2025-11-16 22:10:11');
INSERT INTO `orderbasetable` VALUES ('5', '1', '91.87', '0', '2025-11-11 18:47:47');
INSERT INTO `orderbasetable` VALUES ('6', '1', '194.46', '2', '2025-10-31 18:47:47');
INSERT INTO `orderbasetable` VALUES ('7', '1', '206.72', '2', '2025-10-25 16:47:47');
INSERT INTO `orderbasetable` VALUES ('8', '1', '104.80', '0', '2025-11-17 19:47:47');
INSERT INTO `orderbasetable` VALUES ('9', '1', '151.82', '0', '2025-10-25 15:47:47');
INSERT INTO `orderbasetable` VALUES ('10', '1', '535.32', '2', '2025-11-05 17:47:47');
INSERT INTO `orderbasetable` VALUES ('11', '1', '268.79', '2', '2025-11-05 12:47:47');
INSERT INTO `orderbasetable` VALUES ('12', '1', '282.43', '1', '2025-10-24 11:47:47');
INSERT INTO `orderbasetable` VALUES ('13', '1', '50.73', '0', '2025-11-08 13:47:47');
INSERT INTO `orderbasetable` VALUES ('14', '1', '474.41', '0', '2025-10-23 02:47:47');
INSERT INTO `orderbasetable` VALUES ('15', '1', '63.34', '1', '2025-10-31 00:47:47');
INSERT INTO `orderbasetable` VALUES ('16', '1', '247.73', '1', '2025-11-17 16:47:47');
INSERT INTO `orderbasetable` VALUES ('17', '1', '291.66', '0', '2025-11-14 00:47:47');
INSERT INTO `orderbasetable` VALUES ('18', '1', '422.02', '1', '2025-10-28 19:47:47');
INSERT INTO `orderbasetable` VALUES ('19', '1', '78.85', '1', '2025-11-16 22:47:47');
INSERT INTO `orderbasetable` VALUES ('20', '1', '311.44', '1', '2025-11-04 23:47:47');
INSERT INTO `orderbasetable` VALUES ('21', '1', '388.45', '0', '2025-10-23 11:47:47');
INSERT INTO `orderbasetable` VALUES ('22', '1', '460.57', '1', '2025-11-01 07:47:47');
INSERT INTO `orderbasetable` VALUES ('23', '1', '461.28', '2', '2025-11-09 07:47:47');
INSERT INTO `orderbasetable` VALUES ('24', '1', '363.73', '2', '2025-11-16 22:47:47');
INSERT INTO `orderbasetable` VALUES ('25', '1', '345.54', '2', '2025-10-30 03:47:47');
INSERT INTO `orderbasetable` VALUES ('26', '1', '509.55', '2', '2025-10-30 06:47:47');
INSERT INTO `orderbasetable` VALUES ('27', '1', '238.04', '2', '2025-11-13 18:47:47');
INSERT INTO `orderbasetable` VALUES ('28', '1', '325.09', '0', '2025-11-17 11:47:47');
INSERT INTO `orderbasetable` VALUES ('29', '1', '173.21', '1', '2025-11-15 10:47:47');
INSERT INTO `orderbasetable` VALUES ('36', '2', '306.00', '1', '2025-11-18 23:41:13');
INSERT INTO `orderbasetable` VALUES ('37', '2', '159.60', '1', '2025-11-24 10:49:14');
INSERT INTO `orderbasetable` VALUES ('38', '2', '79.80', '1', '2025-11-24 10:52:16');
INSERT INTO `orderbasetable` VALUES ('39', '2', '1800.00', '1', '2025-11-24 10:52:52');
INSERT INTO `orderbasetable` VALUES ('40', '2', '100.00', '1', '2025-11-24 10:54:58');
INSERT INTO `orderbasetable` VALUES ('41', '2', '1117.20', '1', '2025-11-24 11:17:40');

-- ----------------------------
-- Table structure for orderdetail
-- ----------------------------
DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE `orderdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderbasetable_id` int(11) DEFAULT NULL,
  `goodstable_id` int(11) DEFAULT NULL,
  `shoppingnum` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orderbasetable_id` (`orderbasetable_id`),
  KEY `goodstable_id` (`goodstable_id`),
  CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`orderbasetable_id`) REFERENCES `orderbasetable` (`id`),
  CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderdetail
-- ----------------------------
INSERT INTO `orderdetail` VALUES ('1', '1', '2', '1');
INSERT INTO `orderdetail` VALUES ('2', '2', '3', '1');
INSERT INTO `orderdetail` VALUES ('3', '3', '4', '10');
INSERT INTO `orderdetail` VALUES ('4', '4', '1', '12');
INSERT INTO `orderdetail` VALUES ('5', '4', '6', '11');
INSERT INTO `orderdetail` VALUES ('7', '5', '3', '2');
INSERT INTO `orderdetail` VALUES ('8', '6', '1', '2');
INSERT INTO `orderdetail` VALUES ('9', '7', '2', '2');
INSERT INTO `orderdetail` VALUES ('10', '8', '2', '5');
INSERT INTO `orderdetail` VALUES ('11', '9', '1', '3');
INSERT INTO `orderdetail` VALUES ('12', '10', '2', '4');
INSERT INTO `orderdetail` VALUES ('13', '11', '1', '4');
INSERT INTO `orderdetail` VALUES ('14', '12', '3', '1');
INSERT INTO `orderdetail` VALUES ('15', '13', '3', '4');
INSERT INTO `orderdetail` VALUES ('16', '14', '2', '2');
INSERT INTO `orderdetail` VALUES ('17', '15', '3', '3');
INSERT INTO `orderdetail` VALUES ('18', '16', '1', '3');
INSERT INTO `orderdetail` VALUES ('19', '17', '3', '1');
INSERT INTO `orderdetail` VALUES ('20', '18', '2', '3');
INSERT INTO `orderdetail` VALUES ('21', '19', '1', '1');
INSERT INTO `orderdetail` VALUES ('22', '20', '1', '5');
INSERT INTO `orderdetail` VALUES ('23', '21', '3', '1');
INSERT INTO `orderdetail` VALUES ('24', '22', '1', '1');
INSERT INTO `orderdetail` VALUES ('25', '23', '2', '5');
INSERT INTO `orderdetail` VALUES ('26', '24', '3', '5');
INSERT INTO `orderdetail` VALUES ('27', '25', '1', '4');
INSERT INTO `orderdetail` VALUES ('28', '26', '2', '5');
INSERT INTO `orderdetail` VALUES ('29', '27', '1', '4');
INSERT INTO `orderdetail` VALUES ('30', '28', '3', '5');
INSERT INTO `orderdetail` VALUES ('31', '29', '3', '4');
INSERT INTO `orderdetail` VALUES ('38', '36', '8', '3');
INSERT INTO `orderdetail` VALUES ('39', '37', '1', '4');
INSERT INTO `orderdetail` VALUES ('40', '38', '1', '2');
INSERT INTO `orderdetail` VALUES ('41', '39', '2', '1');
INSERT INTO `orderdetail` VALUES ('42', '40', '6', '1');
INSERT INTO `orderdetail` VALUES ('43', '41', '1', '28');
