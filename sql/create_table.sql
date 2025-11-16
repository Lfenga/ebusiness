/*
 * 电商系统数据库表创建脚本
 */

-- 删除已存在的表（如果存在）
create database shop;
use shop;
DROP TABLE IF EXISTS orderdetail;
DROP TABLE IF EXISTS orderbasetable;
DROP TABLE IF EXISTS carttable;
DROP TABLE IF EXISTS goodstable;
DROP TABLE IF EXISTS goodstype;
DROP TABLE IF EXISTS busertable;
DROP TABLE IF EXISTS ausertable;

-- 创建管理员表
CREATE TABLE ausertable
(
    aname VARCHAR(50) NOT NULL PRIMARY KEY,
    apwd  VARCHAR(50) NOT NULL
);

-- 创建用户表
CREATE TABLE busertable
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    bemail VARCHAR(100) NOT NULL UNIQUE,
    bpwd   VARCHAR(50)  NOT NULL
);

-- 创建商品类型表
CREATE TABLE goodstype
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(50) NOT NULL
);

drop table if exists goodstable;
-- 创建商品表
CREATE TABLE goodstable
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    gname        VARCHAR(100) NOT NULL,
    goprice      DOUBLE       NOT NULL,
    grprice      DOUBLE       NOT NULL,
    gstore       INT          NOT NULL,
    gpicture     VARCHAR(100),
-- 商品状态：1-上架，0-下架
    status       INT DEFAULT 1 COMMENT '商品状态：1-上架，0-下架',
    goodstype_id INT,
    FOREIGN KEY (goodstype_id) REFERENCES goodstype (id)
);

-- 创建订单基础表
CREATE TABLE orderbasetable
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    busertable_id INT,
    amount        DOUBLE NOT NULL,
    status        INT DEFAULT 0,
    orderdate     DATETIME,
    FOREIGN KEY (busertable_id) REFERENCES busertable (id)
);

-- 创建订单详情表
CREATE TABLE orderdetail
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    orderbasetable_id INT,
    goodstable_id     INT,
    shoppingnum       INT,
    FOREIGN KEY (orderbasetable_id) REFERENCES orderbasetable (id),
    FOREIGN KEY (goodstable_id) REFERENCES goodstable (id)
);

-- 创建购物车表
CREATE TABLE carttable
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    busertable_id INT,
    goodstable_id INT,
    shoppingnum   INT,
    FOREIGN KEY (busertable_id) REFERENCES busertable (id),
    FOREIGN KEY (goodstable_id) REFERENCES goodstable (id)
);

-- 插入示例数据
INSERT INTO ausertable
VALUES ('admin', 'admin');

INSERT INTO goodstype
VALUES (1, '服装'),
       (2, '电子产品'),
       (3, '家居用品');

INSERT INTO busertable (bemail, bpwd)
VALUES ('user@example.com', '123456');

-- 1. 插入商品数据 用于测试和上下架功能检测
INSERT INTO goodstable (gname, goprice, grprice, gstore, gpicture, status, goodstype_id)
VALUES ('T恤', 50.0, 39.9, 100, '201910274135150096.jpg', 1, 1),
       ('手机', 2000.0, 1800.0, 50, '201910280135503341.jpg', 1, 2),
       ('沙发', 3000.0, 2500.0, 20, '201910274135059473.jpg', 1, 3);


-- 2.数据库升级脚本：删除收藏、推荐、广告功能，添加商品状态字段
USE shop;

-- 删除收藏表
DROP TABLE IF EXISTS focustable;

-- 删除商品表中的 isRecommend 和 isAdvertisement 字段
ALTER TABLE goodstable
    DROP COLUMN isRecommend;

ALTER TABLE goodstable
    DROP COLUMN isAdvertisement;


-- 增加 stock 字段表示库存，并添加非负约束

ALTER TABLE goodstable
    CHANGE COLUMN gstore stock INT NOT NULL;

ALTER TABLE goodstable
    ADD CONSTRAINT chk_stock_non_negative CHECK (stock >= 0);

-- 增加密码长度  以适应BCrypt密码加密长度
ALTER TABLE busertable
    MODIFY COLUMN bpwd VARCHAR(100) NOT NULL;

ALTER table ausertable
    MODIFY COLUMN apwd VARCHAR(100) NOT NULL;

-- 增加系统日志

DROP TABLE IF EXISTS ausertable;

-- 1.3 创建新的管理员表
CREATE TABLE ausertable
(
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    aname           VARCHAR(50)  NOT NULL UNIQUE COMMENT '管理员用户名',
    apwd            VARCHAR(100) NOT NULL COMMENT '管理员密码（BCrypt加密）',
    aemail          VARCHAR(100) UNIQUE COMMENT '管理员邮箱',
    arealname       VARCHAR(50) COMMENT '管理员真实姓名',
    aphone          VARCHAR(20) COMMENT '管理员手机号',
    astatus         INT      DEFAULT 1 COMMENT '账号状态：1-启用，0-禁用',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    INDEX idx_aname (aname),
    INDEX idx_aemail (aemail)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='管理员表';

-- 1.4 插入默认管理员账号（密码：admin，需要程序首次登录后自动转BCrypt）
-- 注意：这里使用MD5作为临时密码，首次登录后会自动升级为BCrypt
INSERT INTO ausertable (aname, apwd, aemail, arealname, astatus)
VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@ebusiness.com', '系统管理员', 1);

-- ============================================
-- 2. 管理员操作日志表
-- ============================================

DROP TABLE IF EXISTS admin_operation_log;

CREATE TABLE admin_operation_log
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    admin_id       INT         NOT NULL COMMENT '管理员ID',
    admin_name     VARCHAR(50) NOT NULL COMMENT '管理员用户名（冗余）',
    admin_realname VARCHAR(50) COMMENT '管理员真实姓名（冗余）',
    operation_type VARCHAR(20) NOT NULL COMMENT '操作类型：ADD/UPDATE/DELETE/QUERY',
    module         VARCHAR(50) NOT NULL COMMENT '操作模块：商品/分类/用户/订单/管理员',
    operation_desc VARCHAR(500) COMMENT '操作描述',
    target_type    VARCHAR(50) COMMENT '目标对象类型：Goods/GoodsType/User/Order等',
    target_id      VARCHAR(100) COMMENT '目标对象ID（多个用逗号分隔）',
    request_method VARCHAR(200) COMMENT '请求方法名',
    request_params TEXT COMMENT '请求参数（JSON格式）',
    ip_address     VARCHAR(50) COMMENT 'IP地址',
    browser_info   VARCHAR(200) COMMENT '浏览器信息',
    operation_time DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    execution_time BIGINT COMMENT '执行耗时（毫秒）',
    status         VARCHAR(20) DEFAULT 'SUCCESS' COMMENT '执行状态：SUCCESS/FAIL',
    error_msg      TEXT COMMENT '错误信息（失败时记录）',

    INDEX idx_admin_id (admin_id),
    INDEX idx_operation_time (operation_time),
    INDEX idx_module (module),
    INDEX idx_operation_type (operation_type),
    INDEX idx_status (status),
    FOREIGN KEY (admin_id) REFERENCES ausertable (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='管理员操作日志表';

-- ============================================
-- 3. 插入测试日志数据（可选）
-- ============================================

INSERT INTO admin_operation_log (admin_id, admin_name, admin_realname, operation_type, module,
                                 operation_desc, target_type, target_id, ip_address, status)
VALUES (1, 'admin', '系统管理员', 'ADD', '商品',
        '添加商品：测试商品', 'Goods', '1', '127.0.0.1', 'SUCCESS');