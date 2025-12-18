-- 创建库
create database if not exists disruptordemo;

-- 切换库
use disruptordemo;


-- 用户表
create table if not exists user
(
    userId           bigint auto_increment comment 'userId' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    UNIQUE KEY uk_userAccount (userAccount),
    INDEX idx_userName (userName)
) comment '用户' ;

-- 图片表
create table if not exists picture
(
    pictureId           bigint auto_increment comment 'pictureId' primary key
) comment '图片' ;
