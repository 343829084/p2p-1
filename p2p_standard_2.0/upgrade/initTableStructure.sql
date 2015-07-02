-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (`id` varchar(32) NOT NULL, `key` varchar(200) DEFAULT NULL, `description` text, `seq_num` int(11) DEFAULT NULL, `status` varchar(100) DEFAULT NULL, `value` varchar(500) DEFAULT NULL, `p_id` varchar(32) DEFAULT NULL, PRIMARY KEY (`id`), KEY `FK2F0BB6BF66BF45` (`p_id`) USING BTREE, CONSTRAINT `dict_ibfk_1` FOREIGN KEY (`p_id`) REFERENCES `dict` (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for fee_configuration
-- ----------------------------
DROP TABLE IF EXISTS `fee_configuration`;
CREATE TABLE `fee_configuration` ( `id` varchar(32) NOT NULL, `fee_point` varchar(400) DEFAULT NULL, `fee_type` varchar(400) DEFAULT NULL, `factor` varchar(400) DEFAULT NULL, `factor_value` varchar(400) DEFAULT NULL, `fee` double DEFAULT NULL, `fee_upper_limit` double DEFAULT NULL, `interval_lower_limit` double DEFAULT NULL, `interval_upper_limit` double DEFAULT NULL, `description` varchar(200) DEFAULT NULL, `operate_mode` varchar(255) DEFAULT NULL, PRIMARY KEY (`id`), UNIQUE KEY `id` (`id`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for loan_type
-- ----------------------------
DROP TABLE IF EXISTS `loan_type`;
CREATE TABLE `loan_type` ( `id` varchar(64) NOT NULL, `interest_point` varchar(200) DEFAULT NULL, `interest_type` varchar(200) DEFAULT NULL, `name` varchar(32) DEFAULT NULL, `repay_time_period` int(11) DEFAULT NULL, `repay_time_unit` varchar(200) DEFAULT NULL, `repay_type` varchar(200) DEFAULT NULL, `description` text, PRIMARY KEY (`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE `user_login_log` MODIFY COLUMN `user_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `is_success`;
