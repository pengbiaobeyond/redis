/*
Navicat MySQL Data Transfer

Source Server         : 用户管理
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : user-redis

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2021-01-19 23:12:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_number
-- ----------------------------
DROP TABLE IF EXISTS `order_number`;
CREATE TABLE `order_number` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_name` varchar(255) DEFAULT NULL,
  `order_status` int(11) DEFAULT NULL,
  `order_token` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
