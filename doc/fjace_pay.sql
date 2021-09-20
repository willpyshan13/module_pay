/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : fjace_pay

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 12/09/2021 22:58:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_mch_app
-- ----------------------------
DROP TABLE IF EXISTS `t_mch_app`;
CREATE TABLE `t_mch_app` (
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用ID',
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '应用名称',
  `mch_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户号',
  `state` tinyint NOT NULL DEFAULT '1' COMMENT '应用状态: 0-停用, 1-正常',
  `app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用私钥',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `created_uid` bigint DEFAULT NULL COMMENT '创建者用户ID',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者姓名',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户应用表';

-- ----------------------------
-- Records of t_mch_app
-- ----------------------------
BEGIN;
INSERT INTO `t_mch_app` VALUES ('613249b2cc6c24960b9e7ce8', '默认应用', 'M1630685617', 1, 'ztibnv23k88zim5to54ofhqs6feai5de2c6ywaxnhnvgkxp0kaek9hoxs37zchn1x05bsugq6img3s23k10v1flhkbzloiyvk17dnkkytddy9y1y6e6n8722hf0yl6r9', NULL, 100001, 'ye', '2021-09-04 00:13:38.071652', '2021-09-04 00:13:38.071652');
COMMIT;

-- ----------------------------
-- Table structure for t_mch_info
-- ----------------------------
DROP TABLE IF EXISTS `t_mch_info`;
CREATE TABLE `t_mch_info` (
  `mch_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户号',
  `mch_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户名称',
  `mch_short_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户简称',
  `type` tinyint NOT NULL DEFAULT '1' COMMENT '类型: 1-普通商户, 2-特约商户(服务商模式)',
  `isv_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务商号',
  `contact_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人姓名',
  `contact_tel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人手机号',
  `contact_email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人邮箱',
  `state` tinyint NOT NULL DEFAULT '1' COMMENT '商户状态: 0-停用, 1-正常',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户备注',
  `init_user_id` bigint DEFAULT NULL COMMENT '初始用户ID（创建商户时，允许商户登录的用户）',
  `created_uid` bigint DEFAULT NULL COMMENT '创建者用户ID',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者姓名',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`mch_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户信息表';

-- ----------------------------
-- Records of t_mch_info
-- ----------------------------
BEGIN;
INSERT INTO `t_mch_info` VALUES ('M1630685617', '测试商户', '测试', 1, NULL, 'Test', '13888888888', NULL, 1, NULL, 100001, 801, '超管', '2021-09-04 00:13:37.911301', '2021-09-12 22:57:13.782580');
COMMIT;

-- ----------------------------
-- Table structure for t_mch_notify_record
-- ----------------------------
DROP TABLE IF EXISTS `t_mch_notify_record`;
CREATE TABLE `t_mch_notify_record` (
  `notify_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商户通知记录ID',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID',
  `order_type` tinyint NOT NULL COMMENT '订单类型:1-支付,2-退款',
  `mch_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户订单号',
  `mch_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户号',
  `isv_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务商号',
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用ID',
  `notify_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知地址',
  `res_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '通知响应结果',
  `notify_count` int NOT NULL DEFAULT '0' COMMENT '通知次数',
  `notify_count_limit` int NOT NULL DEFAULT '6' COMMENT '最大通知次数, 默认6次',
  `state` tinyint NOT NULL DEFAULT '1' COMMENT '通知状态,1-通知中,2-通知成功,3-通知失败',
  `last_notify_time` datetime DEFAULT NULL COMMENT '最后一次通知时间',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`notify_id`) USING BTREE,
  UNIQUE KEY `Uni_OrderId_Type` (`order_id`,`order_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1037 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户通知记录表';

-- ----------------------------
-- Records of t_mch_notify_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_pay_interface_config
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_interface_config`;
CREATE TABLE `t_pay_interface_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `info_type` tinyint NOT NULL COMMENT '账号类型:1-服务商 2-商户 3-商户应用',
  `info_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务商号/商户号/应用ID',
  `if_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付接口代码',
  `if_params` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口配置参数,json字符串',
  `if_rate` decimal(20,6) DEFAULT NULL COMMENT '支付接口费率',
  `state` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0-停用, 1-启用',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `created_uid` bigint DEFAULT NULL COMMENT '创建者用户ID',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者姓名',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `updated_uid` bigint DEFAULT NULL COMMENT '更新者用户ID',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者姓名',
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Uni_InfoType_InfoId_IfCode` (`info_type`,`info_id`,`if_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付接口配置参数表';

-- ----------------------------
-- Records of t_pay_interface_config
-- ----------------------------
BEGIN;
INSERT INTO `t_pay_interface_config` VALUES (2, 3, '613249b2cc6c24960b9e7ce8', 'wxpay', '{\"apiVersion\":\"V2\",\"mchId\":\"your wechat mpId\",\"appId\":\"your wechat appId\",\"appSecret\":\"your mp appSecret\",\"key\":\"your mp key\"}', NULL, 1, NULL, 100001, 'ye', '2021-09-10 23:12:49.334319', 100001, 'ye', '2021-09-11 11:10:15.950988');
COMMIT;

-- ----------------------------
-- Table structure for t_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_order`;
CREATE TABLE `t_pay_order`
(
    `pay_order_id`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '支付订单号',
    `mch_no`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '商户号',
    `isv_no`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '服务商号',
    `app_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '应用ID',
    `mch_name`         varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '商户名称',
    `mch_type`         tinyint                                                       NOT NULL COMMENT '类型: 1-普通商户, 2-特约商户(服务商模式)',
    `mch_order_no`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '商户订单号',
    `if_code`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '支付接口代码',
    `way_code`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '支付方式代码',
    `amount`           bigint                                                        NOT NULL COMMENT '支付金额,单位分',
    `currency`         varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
    `state`            tinyint                                                       NOT NULL DEFAULT '0' COMMENT '支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭',
    `notify_state`     tinyint                                                       NOT NULL DEFAULT '0' COMMENT '向下游回调状态, 0-未发送,  1-已发送',
    `client_ip`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '客户端IP',
    `subject`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '商品标题',
    `body`             varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品描述信息',
    `channel_extra`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '特定渠道发起额外参数',
    `channel_user`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '渠道用户标识,如微信openId,支付宝账号',
    `channel_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '渠道订单号',
    `refund_state`     tinyint                                                       NOT NULL DEFAULT '0' COMMENT '退款状态: 0-未发生实际退款, 1-部分退款, 2-全额退款',
    `refund_times`     int                                                           NOT NULL DEFAULT '0' COMMENT '退款次数',
    `refund_amount`    bigint                                                        NOT NULL DEFAULT '0' COMMENT '退款总金额,单位分',
    `division_flag`    tinyint                                                                DEFAULT '0' COMMENT '订单分账标志：0-否  1-是',
    `division_time`    datetime                                                               DEFAULT NULL COMMENT '预计分账发起时间',
    `err_code`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '渠道支付错误码',
    `err_msg`          varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '渠道支付错误描述',
    `ext_param`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '商户扩展参数',
    `notify_url`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '异步通知地址',
    `return_url`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT '' COMMENT '页面跳转地址',
    `expired_time`     datetime                                                               DEFAULT NULL COMMENT '订单失效时间',
    `success_time`     datetime                                                               DEFAULT NULL COMMENT '订单支付成功时间',
    `created_at`       timestamp(6)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    `updated_at`       timestamp(6)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP (6) COMMENT '更新时间',
    PRIMARY KEY (`pay_order_id`) USING BTREE,
    UNIQUE KEY `Uni_MchNo_MchOrderNo` (`mch_no`,`mch_order_no`) USING BTREE,
    KEY                `created_at` (`created_at`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付订单表';

-- ----------------------------
-- Records of t_pay_order
-- ----------------------------

DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`
(
    `id`               varchar(22) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '订单id',
    `user_id`          bigint                                                        NOT NULL COMMENT '用户id',
    `phone`            varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '联系电话',
    `identity_card`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '身份证号',
    `real_name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `email`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
    `course_id`        bigint                                                       DEFAULT NULL COMMENT '课程id',
    `course_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品名称（课程名称）',
    `course_type`      bigint                                                       DEFAULT NULL COMMENT '培训类别id',
    `course_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '课程类别名称',
    `origin_price`     decimal(10, 2)                                                NOT NULL COMMENT '原价',
    `pay_price`        decimal(10, 2)                                                NOT NULL COMMENT '实际支付价格',
    `pay_order_id`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付订单id',
    `create_time`      datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time`      datetime                                                     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_type`      tinyint(1) DEFAULT NULL COMMENT '创建方式 1:excel 2:web',
    `expire_time`      datetime                                                     DEFAULT NULL COMMENT '过期时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程订单表';

-- ----------------------------
-- Records of order_info
-- ----------------------------

BEGIN;
COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;
