/*
 Navicat Premium Data Transfer

 Source Server         : 68.79.7.219
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 68.79.7.219:3306
 Source Schema         : pd_work

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 06/08/2020 09:27:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pd_driver_job
-- ----------------------------
DROP TABLE IF EXISTS `pd_driver_job`;
CREATE TABLE `pd_driver_job`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `start_agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '起始机构id',
  `end_agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目的机构id',
  `status` tinyint(4) NOT NULL COMMENT '作业状态，1为待执行（对应 待提货）、2为进行中（对应在途）、3为改派（对应 已交付）、4为已完成（对应 已交付）、5为已作废',
  `driver_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '司机id',
  `task_transport_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运输任务id',
  `start_handover` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货对接人',
  `finish_handover` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交付对接人',
  `plan_departure_time` datetime(0) NULL DEFAULT NULL COMMENT '计划发车时间',
  `actual_departure_time` datetime(0) NULL DEFAULT NULL COMMENT '实际发车时间',
  `plan_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '计划到达时间',
  `actual_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '实际到达时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '司机作业单' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_task_pickup_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `pd_task_pickup_dispatch`;
CREATE TABLE `pd_task_pickup_dispatch`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联订单id',
  `task_type` tinyint(4) NULL DEFAULT NULL COMMENT '任务类型，1为取件任务，2为派件任务',
  `status` int(11) NULL DEFAULT NULL COMMENT '任务状态，1为待执行（对应 待上门和须交接）、2为进行中（该状态暂不使用，属于保留状态）、3为待确认（对应 待妥投和须交件）、4为已完成、5为已取消',
  `sign_status` tinyint(4) NULL DEFAULT NULL COMMENT '签收状态(1为已签收，2为拒收)',
  `agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网点ID',
  `courier_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递员ID',
  `estimated_start_time` datetime(0) NULL DEFAULT NULL COMMENT '预计开始时间',
  `actual_start_time` datetime(0) NULL DEFAULT NULL COMMENT '实际开始时间',
  `estimated_end_time` datetime(0) NULL DEFAULT NULL COMMENT '预计完成时间',
  `actual_end_time` datetime(0) NULL DEFAULT NULL COMMENT '实际完成时间',
  `confirm_time` datetime(0) NULL DEFAULT NULL COMMENT '确认时间',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
  `assigned_status` tinyint(4) NOT NULL COMMENT '任务分配状态(1未分配2已分配3待人工分配)',
  `mark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL COMMENT '任务创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '取件、派件任务信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_task_transport
-- ----------------------------
DROP TABLE IF EXISTS `pd_task_transport`;
CREATE TABLE `pd_task_transport`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `transport_trips_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车次id',
  `start_agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '起始机构id',
  `end_agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目的机构id',
  `status` int(11) NOT NULL COMMENT '任务状态，1为待执行（对应 未发车）、2为进行中（对应在途）、3为待确认（保留状态）、4为已完成（对应 已交付）、5为已取消',
  `assigned_status` tinyint(4) NOT NULL COMMENT '任务分配状态(1未分配2已分配3待人工分配)',
  `loading_status` int(11) NOT NULL COMMENT '满载状态(1.半载2.满载3.空载)',
  `truck_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆id',
  `cargo_pick_up_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货凭证',
  `cargo_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货物照片',
  `transport_certificate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运回单凭证',
  `deliver_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交付货物照片',
  `delivery_latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货纬度值',
  `delivery_longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货经度值',
  `deliver_latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交付纬度值',
  `deliver_longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交付经度值',
  `plan_departure_time` datetime(0) NULL DEFAULT NULL COMMENT '计划发车时间',
  `actual_departure_time` datetime(0) NULL DEFAULT NULL COMMENT '实际发车时间',
  `plan_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '计划到达时间',
  `actual_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '实际到达时间',
  `plan_pick_up_goods_time` datetime(0) NULL DEFAULT NULL COMMENT '计划提货时间',
  `actual_pick_up_goods_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '实际提货时间',
  `plan_delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '计划交付时间',
  `actual_delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '实际交付时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运输任务表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_transport_order
-- ----------------------------
DROP TABLE IF EXISTS `pd_transport_order`;
CREATE TABLE `pd_transport_order`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单ID',
  `status` int(11) NULL DEFAULT NULL COMMENT '运单状态(1.新建 2.已装车 3.到达 4.到达终端网点 5.已签收 6.拒收)',
  `scheduling_status` int(11) NULL DEFAULT NULL COMMENT '调度状态(1.待调度2.未匹配线路3.已调度)',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_transport_order_task
-- ----------------------------
DROP TABLE IF EXISTS `pd_transport_order_task`;
CREATE TABLE `pd_transport_order_task`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `transport_order_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运单id',
  `transport_task_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运输任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运单与运输任务关联表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
