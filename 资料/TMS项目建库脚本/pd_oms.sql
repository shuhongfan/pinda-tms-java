/*
 Navicat Premium Data Transfer

 Source Server         : 68.79.7.219
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 68.79.7.219:3306
 Source Schema         : pd_oms

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 06/08/2020 09:26:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pd_order
-- ----------------------------
DROP TABLE IF EXISTS `pd_order`;
CREATE TABLE `pd_order`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `order_type` tinyint(2) NOT NULL COMMENT '订单类型，1为同城订单，2为城际订单',
  `pickup_type` tinyint(2) NOT NULL COMMENT '取件类型，1为网点自寄，2为上门取件',
  `create_time` datetime(0) NOT NULL COMMENT '下单时间',
  `member_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户id',
  `receiver_province_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人省份id',
  `receiver_city_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人城市id',
  `receiver_county_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人区县id',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人详细地址',
  `receiver_address_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人地址id',
  `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人姓名',
  `receiver_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人电话',
  `sender_province_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人省份id',
  `sender_city_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人城市id',
  `sender_county_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人区县id',
  `sender_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人详细地址',
  `sender_address_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人地址id',
  `sender_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人姓名',
  `sender_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人电话',
  `current_agency_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单当前所属网点',
  `payment_method` tinyint(2) NULL DEFAULT NULL COMMENT '付款方式,1.预结2到付',
  `payment_status` tinyint(2) NULL DEFAULT NULL COMMENT '付款状态,1.未付2已付',
  `amount` decimal(32, 2) NULL DEFAULT NULL COMMENT '金额',
  `estimated_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '预计到达时间',
  `distance` decimal(10, 6) NULL DEFAULT NULL COMMENT '距离，单位：公里',
  `status` int(20) NULL DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_order_cargo
-- ----------------------------
DROP TABLE IF EXISTS `pd_order_cargo`;
CREATE TABLE `pd_order_cargo`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `tran_order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运单id',
  `goods_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货物类型id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货物名称',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货物单位',
  `cargo_value` decimal(32, 8) NULL DEFAULT NULL COMMENT '货品货值',
  `cargo_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货品条码',
  `quantity` int(11) NULL DEFAULT NULL COMMENT '货品数量',
  `volume` decimal(32, 10) NULL DEFAULT NULL COMMENT '货品体积',
  `weight` decimal(32, 10) NULL DEFAULT NULL COMMENT '货品重量',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '货品备注',
  `total_volume` decimal(32, 10) NULL DEFAULT NULL COMMENT '货品总体积',
  `total_weight` decimal(32, 10) NULL DEFAULT NULL COMMENT '货品总重量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '货品总重量 ' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_order_location
-- ----------------------------
DROP TABLE IF EXISTS `pd_order_location`;
CREATE TABLE `pd_order_location`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `send_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送地址坐标',
  `receive_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址坐标',
  `send_agent_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送起始网点计算结果',
  `receive_agent_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接受终止网点的计算结果',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '记录状态 0：无效，1有效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单位置信息 ' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_modify_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rule_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_9yepjak9olg92holwkr8p3l0f`(`rule_key`) USING BTREE,
  UNIQUE INDEX `UK_ilmbp99kyt6gy10224pc9bl6n`(`version`) USING BTREE,
  UNIQUE INDEX `UK_ei48upwykmhx9r5p7p4ndxvgn`(`last_modify_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
