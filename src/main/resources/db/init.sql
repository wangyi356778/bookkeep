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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

USE bookkeep_db;

CREATE TABLE IF NOT EXISTS record (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL COMMENT '用户ID',
                                      type VARCHAR(20) NOT NULL COMMENT '类型：income(收入)/expense(支出)',
                                      category VARCHAR(50) NOT NULL COMMENT '类目',
                                      amount DECIMAL(10,2) NOT NULL COMMENT '金额',
                                      date DATE NOT NULL COMMENT '日期',
                                      remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      INDEX idx_user_id (user_id),
                                      INDEX idx_user_date (user_id, date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记账记录表';

USE bookkeep_db;
CREATE TABLE IF NOT EXISTS record (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL COMMENT '用户ID',
                                      type VARCHAR(20) NOT NULL COMMENT '类型：income/expense',
                                      category VARCHAR(50) NOT NULL COMMENT '类目',
                                      amount DECIMAL(10,2) NOT NULL COMMENT '金额',
                                      date DATE NOT NULL COMMENT '日期',
                                      remark VARCHAR(200) DEFAULT NULL,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      INDEX idx_user_id (user_id),
                                      INDEX idx_user_date (user_id, date)
);