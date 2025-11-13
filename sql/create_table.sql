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
CREATE TABLE ausertable (
    aname VARCHAR(50) NOT NULL PRIMARY KEY,
    apwd VARCHAR(50) NOT NULL
);

-- 创建用户表
CREATE TABLE busertable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bemail VARCHAR(100) NOT NULL UNIQUE,
    bpwd VARCHAR(50) NOT NULL
);

-- 创建商品类型表
CREATE TABLE goodstype (
    id INT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(50) NOT NULL
);

drop table if exists goodstable;
-- 创建商品表
CREATE TABLE goodstable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    gname VARCHAR(100) NOT NULL,
    goprice DOUBLE NOT NULL,
    grprice DOUBLE NOT NULL,
    gstore INT NOT NULL,
    gpicture VARCHAR(100),
-- 商品状态：1-上架，0-下架
    status INT DEFAULT 1 COMMENT '商品状态：1-上架，0-下架',
    goodstype_id INT,
    FOREIGN KEY (goodstype_id) REFERENCES goodstype(id)
);

-- 创建订单基础表
CREATE TABLE orderbasetable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    busertable_id INT,
    amount DOUBLE NOT NULL,
    status INT DEFAULT 0,
    orderdate DATETIME,
    FOREIGN KEY (busertable_id) REFERENCES busertable(id)
);

-- 创建订单详情表
CREATE TABLE orderdetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderbasetable_id INT,
    goodstable_id INT,
    shoppingnum INT,
    FOREIGN KEY (orderbasetable_id) REFERENCES orderbasetable(id),
    FOREIGN KEY (goodstable_id) REFERENCES goodstable(id)
);

-- 创建购物车表
CREATE TABLE carttable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    busertable_id INT,
    goodstable_id INT,
    shoppingnum INT,
    FOREIGN KEY (busertable_id) REFERENCES busertable(id),
    FOREIGN KEY (goodstable_id) REFERENCES goodstable(id)
);

-- 插入示例数据
INSERT INTO ausertable VALUES ('admin', 'admin');

INSERT INTO goodstype VALUES (1, '服装'), (2, '电子产品'), (3, '家居用品');

INSERT INTO busertable (bemail, bpwd) VALUES ('user@example.com', '123456');

-- 1. 插入商品数据 用于测试和上下架功能检测
INSERT INTO goodstable (gname, goprice, grprice, gstore, gpicture, status, goodstype_id) 
VALUES 
('T恤', 50.0, 39.9, 100, '201910274135150096.jpg', 1, 1),
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