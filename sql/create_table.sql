/*
 * 电商系统数据库表创建脚本
 */

-- 删除已存在的表（如果存在）
create database shop;
use shop;
DROP TABLE IF EXISTS orderdetail;
DROP TABLE IF EXISTS orderbasetable;
DROP TABLE IF EXISTS carttable;
DROP TABLE IF EXISTS focustable;
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
    isRecommend INT DEFAULT 0,
    isAdvertisement INT DEFAULT 0,
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

-- 创建收藏表
CREATE TABLE focustable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    goodstable_id INT,
    busertable_id INT,
    focustime DATETIME,
    FOREIGN KEY (goodstable_id) REFERENCES goodstable(id),
    FOREIGN KEY (busertable_id) REFERENCES busertable(id)
);

-- 插入示例数据
INSERT INTO ausertable VALUES ('admin', 'admin');

INSERT INTO goodstype VALUES (1, '服装'), (2, '电子产品'), (3, '家居用品');

INSERT INTO busertable (bemail, bpwd) VALUES ('user@example.com', '123456');

ALTER TABLE goodstable
    ADD COLUMN status INT DEFAULT 1 COMMENT '商品状态：1-上架，0-下架';


INSERT INTO goodstable (gname, goprice, grprice, gstore, gpicture, isRecommend, isAdvertisement, status, goodstype_id) 
VALUES 
('T恤', 50.0, 39.9, 100, '201910274135150096.jpg', 1, 0, 1, 1),
('手机', 2000.0, 1800.0, 50, '201910280135503341.jpg', 1, 1, 1, 2),
('沙发', 3000.0, 2500.0, 20, '201910274135059473.jpg', 0, 1, 1, 3);