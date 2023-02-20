/*
 Navicat Premium Data Transfer

 Source Server         : 68.79.7.219
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 68.79.7.219:3306
 Source Schema         : pd_base

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 06/08/2020 09:25:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for d_global_user
-- ----------------------------
DROP TABLE IF EXISTS `d_global_user`;
CREATE TABLE `d_global_user`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `tenant_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
  `account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `readonly` bit(1) NULL DEFAULT b'0' COMMENT '是否内置',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '全局账号' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for d_tenant
-- ----------------------------
DROP TABLE IF EXISTS `d_tenant`;
CREATE TABLE `d_tenant`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '企业编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '企业名称',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'CREATE' COMMENT '类型\n#{CREATE:创建;REGISTER:注册}',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'NORMAL' COMMENT '状态\n#{NORMAL:正常;FORBIDDEN:禁用;WAITING:待审核;REFUSE:拒绝}',
  `readonly` bit(1) NULL DEFAULT b'0' COMMENT '是否内置',
  `duty` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '责任人',
  `expiration_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期\n为空表示永久',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'logo地址',
  `describe_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '企业简介',
  `password_expire` int(11) NULL DEFAULT 0 COMMENT '用户密码有效期\n单位：天 0表示永久有效\n',
  `is_multiple_login` bit(1) NULL DEFAULT b'1' COMMENT '是否多地登录',
  `password_error_num` int(11) NULL DEFAULT 10 COMMENT '密码输错次数\n密码输错锁定账号的次数\n单位：次\n',
  `password_error_lock_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '账号锁定时间\n密码输错${passwordErrorNum}次后，锁定账号的时间\n单位： h | d | w | m\n单位： 时 | 天 | 周 | 月\n如：0=当天晚上24点 2h = 2小时   2d = 2天  ',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UN_CODE`(`code`) USING BTREE COMMENT '租户唯一编码'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_agency_scope
-- ----------------------------
DROP TABLE IF EXISTS `pd_agency_scope`;
CREATE TABLE `pd_agency_scope`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机构id',
  `area_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行政区域id',
  `muti_points` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '机构作业范围',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '机构业务范围表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_area_boundaries
-- ----------------------------
DROP TABLE IF EXISTS `pd_area_boundaries`;
CREATE TABLE `pd_area_boundaries`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'is',
  `area_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行政区域id',
  `boundaries` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '边界-面坐标点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '行政区域临时表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_courier_scop
-- ----------------------------
DROP TABLE IF EXISTS `pd_courier_scop`;
CREATE TABLE `pd_courier_scop`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `area_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行政区域id',
  `muti_points` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '快递员作业范围',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '快递员业务范围表' ROW_FORMAT = Compact;

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
-- Table structure for pd_goods_type
-- ----------------------------
DROP TABLE IF EXISTS `pd_goods_type`;
CREATE TABLE `pd_goods_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货物类型名称',
  `default_weight` decimal(32, 2) NULL DEFAULT NULL COMMENT '默认重量，单位：千克',
  `default_volume` decimal(32, 2) NULL DEFAULT NULL COMMENT '默认体积，单位：方',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '说明',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0：禁用 1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '货物类型表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_transport_line
-- ----------------------------
DROP TABLE IF EXISTS `pd_transport_line`;
CREATE TABLE `pd_transport_line`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '线路名称',
  `line_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '线路编号',
  `agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属机构',
  `transport_line_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '线路类型',
  `start_agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起始地机构id',
  `end_agency_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地机构id',
  `distance` decimal(32, 2) NULL DEFAULT NULL COMMENT '距离',
  `cost` decimal(32, 2) NULL DEFAULT NULL COMMENT '成本',
  `estimated_time` decimal(32, 2) NULL DEFAULT NULL COMMENT '预计时间（分钟）',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0：禁用 1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '线路表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_transport_line_type
-- ----------------------------
DROP TABLE IF EXISTS `pd_transport_line_type`;
CREATE TABLE `pd_transport_line_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '线路类型名称',
  `type_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '线路类型编号',
  `start_agency_type` int(2) NULL DEFAULT NULL COMMENT '起始地机构类型',
  `end_agency_type` int(2) NULL DEFAULT NULL COMMENT '目的地机构类型',
  `last_update_time` datetime(0) NOT NULL COMMENT '最后更新时间',
  `updater` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '最后更新人',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态  0：禁用   1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '线路类型表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_transport_trips
-- ----------------------------
DROP TABLE IF EXISTS `pd_transport_trips`;
CREATE TABLE `pd_transport_trips`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车次名称',
  `departure_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发车时间',
  `transport_line_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属线路id',
  `period` tinyint(2) NOT NULL COMMENT '周期，1为天，2为周，3为月',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态  0：禁用   1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车次信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_transport_trips_truck_driver
-- ----------------------------
DROP TABLE IF EXISTS `pd_transport_trips_truck_driver`;
CREATE TABLE `pd_transport_trips_truck_driver`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `truck_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车辆id',
  `transport_trips_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车次id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '司机id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车次与车辆和司机关联表' ROW_FORMAT = Compact;

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

-- ----------------------------
-- Table structure for pd_truck_driver
-- ----------------------------
DROP TABLE IF EXISTS `pd_truck_driver`;
CREATE TABLE `pd_truck_driver`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id，来自用户表',
  `fleet_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属车队id',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `driving_age` int(11) NULL DEFAULT NULL COMMENT '驾龄',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '司机表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_truck_driver_license
-- ----------------------------
DROP TABLE IF EXISTS `pd_truck_driver_license`;
CREATE TABLE `pd_truck_driver_license`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `allowable_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '准驾车型',
  `initial_certificate_date` date NULL DEFAULT NULL COMMENT '初次领证日期',
  `valid_period` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效期限',
  `license_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驾驶证号',
  `driver_age` int(11) NULL DEFAULT NULL COMMENT '驾龄',
  `license_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驾驶证类型',
  `qualification_certificate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '从业资格证信息',
  `pass_certificate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入场证信息',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '司机驾驶证表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_truck_license
-- ----------------------------
DROP TABLE IF EXISTS `pd_truck_license`;
CREATE TABLE `pd_truck_license`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `truck_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车辆id',
  `engine_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发动机编号',
  `registration_date` date NULL DEFAULT NULL COMMENT '注册时间',
  `mandatory_scrap` date NULL DEFAULT NULL COMMENT '国家强制报废日期',
  `expiration_date` date NULL DEFAULT NULL COMMENT '检验有效期',
  `overall_quality` decimal(32, 2) NULL DEFAULT NULL COMMENT '整备质量',
  `allowable_weight` decimal(32, 2) NULL DEFAULT NULL COMMENT '核定载质量',
  `outside_dimensions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外廓尺寸',
  `validity_period` date NULL DEFAULT NULL COMMENT '行驶证有效期',
  `transport_certificate_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '道路运输证号',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆行驶证表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_truck_type
-- ----------------------------
DROP TABLE IF EXISTS `pd_truck_type`;
CREATE TABLE `pd_truck_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车辆类型名称',
  `allowable_load` decimal(32, 2) NULL DEFAULT NULL COMMENT '准载重量',
  `allowable_volume` decimal(32, 2) NULL DEFAULT NULL COMMENT '准载体积',
  `measure_long` decimal(32, 2) NULL DEFAULT NULL COMMENT '长',
  `measure_width` decimal(32, 2) NULL DEFAULT NULL COMMENT '宽',
  `measure_high` decimal(32, 2) NULL DEFAULT NULL COMMENT '高',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0：禁用 1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆类型表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pd_truck_type_goods_type
-- ----------------------------
DROP TABLE IF EXISTS `pd_truck_type_goods_type`;
CREATE TABLE `pd_truck_type_goods_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `truck_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车辆类型id',
  `goods_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货物类型id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆类型与货物类型关联表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
