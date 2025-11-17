/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50740
Source Host           : localhost:3306
Source Database       : shop

Target Server Type    : MYSQL
Target Server Version : 50740
File Encoding         : 65001

Date: 2025-11-17 14:47:25
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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';

-- ----------------------------
-- Records of admin_operation_log
-- ----------------------------
INSERT INTO `admin_operation_log` VALUES ('1', '1', 'admin', '系统管理员', 'ADD', '商品', '添加商品：测试商品', 'Goods', '1', null, null, '127.0.0.1', null, '2025-11-16 16:39:56', null, 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('36', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', 'redirect:/goods/selectAllGoodsByPage?currentPage=1&act=updateSelect', 'addGoods', '参数序列化失败', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 19:38:24', '9', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('37', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', 'redirect:/goods/selectAllGoodsByPage?currentPage=1&act=updateSelect', 'addGoods', '参数序列化失败', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 19:39:12', '5', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('38', '1', 'admin', '系统管理员', 'ADD', '商品类型管理', '添加商品类型', 'GoodsType', 'redirect:/type/selectAllTypeByPage?currentPage=1', 'addType', '[{\"id\":0,\"typename\":\"衣物\"}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 19:44:03', '20', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('39', '1', 'admin', '系统管理员', 'DELETE', '商品管理', '删除商品', 'Goods', null, 'delete', '[5]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:20:47', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('40', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:4, 名称:电脑, 状态:下架]', 'Goods', '4', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:43:55', '7', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('41', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:4, 名称:电脑, 状态:上架]', 'Goods', '4', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:44:35', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('42', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', null, 'delete', '[1]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:45:58', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('43', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', null, 'delete', '[1]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:46:05', '4', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('44', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', null, 'delete', '[4]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:46:15', '8', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('45', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品 [名称:xx]', 'Goods', '0', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 21:55:42', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('46', '1', 'admin', '系统管理员', 'ADD', '商品类型管理', '添加商品类型', 'GoodsType', '5', 'addType', '[{\"id\":5,\"typename\":\"衣物\"}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:03:49', '17', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('47', '1', 'admin', '系统管理员', 'DELETE', '商品类型管理', '删除商品类型', 'GoodsType', '5', 'delete', '[5]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:03:52', '11', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('48', '1', 'admin', '系统管理员', 'ADD', '商品管理', '添加商品', 'Goods', '7', 'addGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:04:29', '13', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('49', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:6, 名称:xx, 状态:下架]', 'Goods', '6', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:04:50', '8', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('50', '1', 'admin', '系统管理员', 'UPDATE', '商品管理', '修改商品 [ID:7, 名称:xxnn, 状态:上架]', 'Goods', '7', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:05:00', '6', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('51', '1', 'admin', '系统管理员', 'DELETE', '商品管理', '删除商品', 'Goods', '7', 'delete', '[7]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:05:16', '19', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('52', '1', 'admin', '系统管理员', 'ADD', '管理员管理', '添加管理员', 'AUser', '2', 'addAdmin', '[{\"id\":2,\"aname\":\"root\",\"apwd\":\"$2a$10$4Oc0c3EvzujaFQTbfH/UGejLdLR07aXJLpenNKd6diR.Q6Dt5jqr.\",\"aemail\":\"11@qq.com\",\"arealname\":\"系统管理员2\",\"aphone\":\"11111111111\",\"astatus\":1,\"createTime\":null,\"lastLoginTime\":null}]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:07:55', '131', 'SUCCESS', null);
INSERT INTO `admin_operation_log` VALUES ('53', '2', 'root', '系统管理员2', 'UPDATE', '商品管理', '修改商品 [ID:6, 名称:xxm, 状态:上架]', 'Goods', '6', 'updateGoods', '[\"Goods 对象\"]', '0:0:0:0:0:0:0:1', 'Chrome', '2025-11-16 22:08:51', '4', 'SUCCESS', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ----------------------------
-- Records of ausertable
-- ----------------------------
INSERT INTO `ausertable` VALUES ('1', 'admin', '$2a$10$B0mMb0sbJcdlBomYuW3wNeMZ73ACtRkN0k3Fg5VgEgd4vBKCqt51q', 'admin@ebusiness.com', '系统管理员', null, '1', '2025-11-16 16:39:56', '2025-11-17 14:43:41');
INSERT INTO `ausertable` VALUES ('2', 'root', '$2a$10$4Oc0c3EvzujaFQTbfH/UGejLdLR07aXJLpenNKd6diR.Q6Dt5jqr.', '11@qq.com', '系统管理员2', '11111111111', '1', '2025-11-16 22:07:55', '2025-11-16 22:08:34');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of busertable
-- ----------------------------
INSERT INTO `busertable` VALUES ('1', 'user@example.com', '$2a$10$TiFsNa7uu5Lap70.jikcB.ytUyRY2EuIvvImacXNP3XsI3f3u1YKy', '2025-11-17 14:41:22');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of carttable
-- ----------------------------
INSERT INTO `carttable` VALUES ('7', '1', '6', '1');

-- ----------------------------
-- Table structure for goodstable
-- ----------------------------
DROP TABLE IF EXISTS `goodstable`;
CREATE TABLE `goodstable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gname` varchar(100) NOT NULL,
  `goprice` double NOT NULL,
  `grprice` double NOT NULL,
  `stock` int(11) NOT NULL,
  `gpicture` varchar(100) DEFAULT NULL,
  `goodstype_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '商品状态：1-上架，0-下架',
  PRIMARY KEY (`id`),
  KEY `goodstype_id` (`goodstype_id`),
  CONSTRAINT `goodstable_ibfk_1` FOREIGN KEY (`goodstype_id`) REFERENCES `goodstype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goodstable
-- ----------------------------
INSERT INTO `goodstable` VALUES ('1', 'T恤', '50', '39.9', '88', '201910274135150096.jpg', '1', '1');
INSERT INTO `goodstable` VALUES ('2', '手机', '2000', '1800', '49', '201910280135503341.jpg', '2', '1');
INSERT INTO `goodstable` VALUES ('3', '沙发', '3000', '2500', '19', '201910274135059473.jpg', '3', '1');
INSERT INTO `goodstable` VALUES ('4', '电脑', '1800', '1700', '28', '202511315203635221.jpg', '1', '1');
INSERT INTO `goodstable` VALUES ('6', 'xxm', '111', '100', '11', '202511320215541868.webp', '1', '1');

-- ----------------------------
-- Table structure for goodstype
-- ----------------------------
DROP TABLE IF EXISTS `goodstype`;
CREATE TABLE `goodstype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typename` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

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
  `amount` double NOT NULL,
  `status` int(11) DEFAULT '0',
  `orderdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `busertable_id` (`busertable_id`),
  CONSTRAINT `orderbasetable_ibfk_1` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderbasetable
-- ----------------------------
INSERT INTO `orderbasetable` VALUES ('1', '1', '1800', '1', '2025-11-11 20:40:15');
INSERT INTO `orderbasetable` VALUES ('2', '1', '2500', '1', '2025-11-13 18:06:48');
INSERT INTO `orderbasetable` VALUES ('3', '1', '17000', '1', '2025-11-13 19:46:16');
INSERT INTO `orderbasetable` VALUES ('4', '1', '1578.8', '1', '2025-11-16 22:10:11');

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderdetail
-- ----------------------------
INSERT INTO `orderdetail` VALUES ('1', '1', '2', '1');
INSERT INTO `orderdetail` VALUES ('2', '2', '3', '1');
INSERT INTO `orderdetail` VALUES ('3', '3', '4', '10');
INSERT INTO `orderdetail` VALUES ('4', '4', '1', '12');
INSERT INTO `orderdetail` VALUES ('5', '4', '6', '11');
