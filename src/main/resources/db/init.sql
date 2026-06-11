CREATE DATABASE IF NOT EXISTS bookkeep_db;

USE bookkeep_db;
DROP TABLE IF EXISTS user;
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
                      password VARCHAR(100) NOT NULL COMMENT '密码',
                      name VARCHAR(50) DEFAULT NULL COMMENT '姓名',
                      age INT DEFAULT NULL COMMENT '年龄',
                      occupation VARCHAR(50) DEFAULT NULL COMMENT '职业',
                      gender VARCHAR(10) DEFAULT NULL COMMENT '性别：男/女',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表'; user ADD COLUMN gender VARCHAR(10) DEFAULT NULL COMMENT '性别' AFTER occupation;