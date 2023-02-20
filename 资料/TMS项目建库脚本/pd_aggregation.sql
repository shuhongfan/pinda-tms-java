/*
 Navicat Premium Data Transfer

 Source Server         : 68.79.7.219
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 68.79.7.219:3306
 Source Schema         : pd_aggregation

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 06/08/2020 09:24:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pd_area
-- ----------------------------
DROP TABLE IF EXISTS `pd_area`;
CREATE TABLE `pd_area`  (
  `id` int(11) NOT NULL COMMENT '行政id',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级行政',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行政名称',
  `area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merger_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `short_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zip_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` tinyint(2) NULL DEFAULT 0 COMMENT '行政区域等级（0: 省级 1:市级 2:县级 3:镇级 4:乡村级）',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pinyin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音',
  `first` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '首字母',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_user` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `pd_auth_user`;
CREATE TABLE `pd_auth_user`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '组织ID\n#c_core_org',
  `station_id` bigint(20) NULL DEFAULT NULL COMMENT '岗位ID\n#c_core_station',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机',
  `sex` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '性别\n#Sex{W:女;M:男;N:未知}',
  `status` bit(1) NULL DEFAULT b'0' COMMENT '启用状态 1启用 0禁用',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像',
  `work_describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '工作描述\r\n比如：  市长、管理员、局长等等   用于登陆展示',
  `password_error_last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次输错密码时间',
  `password_error_num` int(11) NULL DEFAULT 0 COMMENT '密码错误次数',
  `password_expire_time` datetime(0) NULL DEFAULT NULL COMMENT '密码过期时间',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_user` bigint(20) NULL DEFAULT 0 COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) NULL DEFAULT 0 COMMENT '更新人id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UN_ACCOUNT`(`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_core_org
-- ----------------------------
DROP TABLE IF EXISTS `pd_core_org`;
CREATE TABLE `pd_core_org`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `abbreviation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '简称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父ID',
  `org_type` tinyint(1) NULL DEFAULT NULL COMMENT '部门类型 1为分公司，2为一级转运中心 3为二级转运中心 4为网点',
  `province_id` bigint(20) NULL DEFAULT NULL COMMENT '省',
  `city_id` bigint(20) NULL DEFAULT NULL COMMENT '市',
  `county_id` bigint(20) NULL DEFAULT NULL COMMENT '区',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `contract_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `manager_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人id',
  `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT ',' COMMENT '树结构',
  `sort_value` int(11) NULL DEFAULT 1 COMMENT '排序',
  `status` bit(1) NULL DEFAULT b'1' COMMENT '状态',
  `describe_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '描述',
  `latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `business_hours` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业时间',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_user` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  FULLTEXT INDEX `FU_PATH`(`tree_path`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_core_station
-- ----------------------------
DROP TABLE IF EXISTS `pd_core_station`;
CREATE TABLE `pd_core_station`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `org_id` bigint(20) NULL DEFAULT 0 COMMENT '组织ID\n#c_core_org',
  `status` bit(1) NULL DEFAULT b'1' COMMENT '状态',
  `describe_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_user` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位' ROW_FORMAT = Compact;

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
-- Table structure for pd_fleet
-- ----------------------------
DROP TABLE IF EXISTS `pd_fleet`;
CREATE TABLE `pd_fleet`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车队名称',
  `fleet_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属机构',
  `manager` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0：禁用 1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车队表' ROW_FORMAT = Compact;

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

-- ----------------------------
-- Table structure for pd_truck
-- ----------------------------
DROP TABLE IF EXISTS `pd_truck`;
CREATE TABLE `pd_truck`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `truck_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆类型id',
  `fleet_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属车队id',
  `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `license_plate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号码',
  `device_gps_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'GPS设备id',
  `allowable_load` decimal(32, 2) NULL DEFAULT NULL COMMENT '准载重量',
  `allowable_volume` decimal(32, 2) NULL DEFAULT NULL COMMENT '准载体积',
  `truck_license_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆行驶证信息id',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0：禁用 1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆信息表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
