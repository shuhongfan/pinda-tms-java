INSERT INTO `pd_auth_menu` VALUES (101, '系统管理', '系统管理', b'0', '/user', 'pinda/user/Index', b'1', 0, 'el-icon-user-solid', '', 0, 1, '2019-07-25 15:35:12', 3, '2020-01-09 19:23:12');
INSERT INTO `pd_auth_menu` VALUES (104, '监控管理', '开发者', b'0', '/developer', 'pinda/developer/Index', b'1', 1, 'el-icon-user-solid', '', 0, 1, '2019-11-11 14:38:34', 3, '2020-04-02 15:21:18');
INSERT INTO `pd_auth_menu` VALUES (603976297063910529, '菜单配置', '', b'0', '/auth/menu', 'pinda/auth/menu/Index', b'1', 3, '', '', 101, 1, '2019-07-25 15:46:11', 3, '2019-11-11 14:31:52');
INSERT INTO `pd_auth_menu` VALUES (603981723864141121, '角色管理', '', b'0', '/auth/role', 'pinda/auth/role/Index', b'1', 4, '', '', 101, 1, '2019-07-25 16:07:45', 3, '2020-06-29 14:10:38');
INSERT INTO `pd_auth_menu` VALUES (603982542332235201, '组织管理', '', b'0', '/user/org', 'pinda/user/org/Index', b'1', 0, '', '', 101, 1, '2019-07-25 16:11:00', 3, '2020-01-11 08:51:22');
INSERT INTO `pd_auth_menu` VALUES (603982713849908801, '岗位管理', '', b'0', '/user/station', 'pinda/user/station/Index', b'1', 1, '', '', 101, 1, '2019-07-25 16:11:41', 3, '2019-11-11 14:28:43');
INSERT INTO `pd_auth_menu` VALUES (603983082961243905, '用户管理', '', b'0', '/user/user', 'pinda/user/user/Index', b'1', 2, '', '', 101, 1, '2019-07-25 16:13:09', 3, '2020-01-09 19:27:02');
INSERT INTO `pd_auth_menu` VALUES (605078672772170209, '操作日志', '', b'0', '/developer/optLog', 'pinda/developer/optLog/Index', b'1', 3, '', '', 104, 1, '2019-07-28 16:46:38', 3, '2020-04-27 11:05:10');
INSERT INTO `pd_auth_menu` VALUES (605078979149300257, '数据库监控', 'eeee', b'0', '/developer/db', 'pinda/developer/db/Index', b'1', 2, '', '', 104, 1, '2019-07-28 16:47:51', 3, '2020-06-29 14:10:53');
INSERT INTO `pd_auth_menu` VALUES (605079239015793249, '接口文档', '', b'0', 'http://68.79.7.219:8760/api/gate/doc.html', 'Layout', b'1', 5, '', '', 104, 1, '2019-07-28 16:48:53', 3, '2020-05-29 17:13:08');
INSERT INTO `pd_auth_menu` VALUES (605079411338773153, '注册配置中心', '', b'0', 'http://localhost:8848/nacos', 'Layout', b'0', 6, '', '', 104, 1, '2019-07-28 16:49:34', 3, '2020-01-10 10:40:47');
INSERT INTO `pd_auth_menu` VALUES (645215230518909025, '登录日志', '', b'0', '/developer/loginLog', 'pinda/developer/loginLog/Index', b'1', 4, '', '', 104, 3, '2019-11-16 10:54:59', 3, '2019-11-16 10:54:59');
INSERT INTO `pd_auth_menu` VALUES (667033750256747169, '文件管理', '', b'0', '/file/attachment', 'pinda/file/attachment/Index', b'1', 5, '', '', 101, 3, '2020-01-15 15:53:59', 3, '2020-04-03 08:20:56');
INSERT INTO `pd_auth_menu` VALUES (695291921186165953, '品达物流', '', b'0', '/tms', 'pinda/tms/Index', b'1', 3, 'el-icon-user', '', 0, 3, '2020-04-02 15:21:52', 3, '2020-04-02 15:21:52');
INSERT INTO `pd_auth_menu` VALUES (695292431045760449, '机构作业范围', '', b'0', '/jgzyfw', 'pinda/jgzyfw/Index', b'1', 0, 'el-icon-user', '', 695291921186165953, 3, '2020-04-02 15:23:54', 3, '2020-04-02 15:23:54');
INSERT INTO `pd_auth_menu` VALUES (695292612529100353, '机构作业范围列表', '', b'0', 'http://68.79.7.219:8080/tms/#/organization/institutions-jobs-area', 'Layout', b'1', 0, '', '', 695292431045760449, 3, '2020-04-02 15:24:37', 3, '2020-05-16 11:06:19');
INSERT INTO `pd_auth_menu` VALUES (695292762970396353, '订单管理', '', b'0', '/ddgz', 'pinda/ddgz/Index', b'1', 1, 'el-icon-user', '', 695291921186165953, 3, '2020-04-02 15:25:13', 3, '2020-04-21 11:33:00');
INSERT INTO `pd_auth_menu` VALUES (695292856260106017, '运单管理', '', b'0', '/ydgl', 'pinda/ydgl/Index', b'1', 2, 'el-icon-user', '', 695291921186165953, 3, '2020-04-02 15:25:35', 3, '2020-04-21 11:33:05');
INSERT INTO `pd_auth_menu` VALUES (695292983368488833, '订单列表', '', b'0', 'http://68.79.7.219:8080/tms/#/order-manage/list', 'Layout', b'1', 0, '', '', 695292762970396353, 3, '2020-04-02 15:26:05', 3, '2020-05-28 11:36:29');
INSERT INTO `pd_auth_menu` VALUES (695293106584557537, '运单列表', '', b'0', 'http://68.79.7.219:8080/tms/#/waybill/list', 'Layout', b'1', 0, '', '', 695292856260106017, 3, '2020-04-02 15:26:35', 3, '2020-05-16 11:07:00');
INSERT INTO `pd_auth_menu` VALUES (695293401356051297, '网点管理', '', b'0', '/wdgl', 'pinda/wdgl/Index', b'1', 3, 'el-icon-user', '', 695291921186165953, 3, '2020-04-02 15:27:45', 3, '2020-04-21 11:33:09');
INSERT INTO `pd_auth_menu` VALUES (695293667283313729, '转运中心', '', b'0', '/zyzx', 'pinda/zyzx/Index', b'1', 4, 'el-icon-user', '', 695291921186165953, 3, '2020-04-02 15:28:48', 3, '2020-04-21 11:33:13');
INSERT INTO `pd_auth_menu` VALUES (695293828101317793, '运输管理', '', b'0', '/ysgl', 'pinda/ysgl/Index', b'1', 5, 'el-icon-user', '', 695291921186165953, 3, '2020-04-02 15:29:27', 3, '2020-04-21 11:33:18');
INSERT INTO `pd_auth_menu` VALUES (695294080086716545, '快递作业管理', '', b'0', 'http://68.79.7.219:8080/tms/#/branches/operational', 'Layout', b'1', 0, '', '', 695293401356051297, 3, '2020-04-02 15:30:27', 3, '2020-05-16 11:07:06');
INSERT INTO `pd_auth_menu` VALUES (695294225880723681, '快递员作业范围管理', '', b'0', 'http://68.79.7.219:8080/tms/#/branches/operational-range', 'Layout', b'1', 1, '', '', 695293401356051297, 3, '2020-04-02 15:31:02', 3, '2020-05-16 11:07:11');
INSERT INTO `pd_auth_menu` VALUES (695294386598064481, '货品管理', '', b'0', 'http://68.79.7.219:8080/tms/#/branches/goods-type', 'Layout', b'1', 2, '', '', 695293401356051297, 3, '2020-04-02 15:31:40', 3, '2020-05-16 11:07:15');
INSERT INTO `pd_auth_menu` VALUES (695294615183438273, '车型管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transit/car-models', 'Layout', b'1', 0, '', '', 695293667283313729, 3, '2020-04-02 15:32:34', 3, '2020-05-16 11:07:22');
INSERT INTO `pd_auth_menu` VALUES (695294721781674529, '车队管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transit/motorcade', 'Layout', b'1', 1, '', '', 695293667283313729, 3, '2020-04-02 15:33:00', 3, '2020-05-16 15:26:26');
INSERT INTO `pd_auth_menu` VALUES (695294880338948737, '司机管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transit/driver', 'Layout', b'1', 3, '', '', 695293667283313729, 3, '2020-04-02 15:33:38', 3, '2020-05-16 11:07:37');
INSERT INTO `pd_auth_menu` VALUES (695295025063408385, '车辆管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transit/vehicle', 'Layout', b'1', 2, '', '', 695293667283313729, 3, '2020-04-02 15:34:12', 3, '2020-05-16 11:07:33');
INSERT INTO `pd_auth_menu` VALUES (695295182781821793, '线路管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transit/line-manage', 'Layout', b'1', 4, '', '', 695293667283313729, 3, '2020-04-02 15:34:50', 3, '2020-05-16 11:07:43');
INSERT INTO `pd_auth_menu` VALUES (695295324612211649, '线路类型管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transit/line-type', 'Layout', b'1', 5, '', '', 695293667283313729, 3, '2020-04-02 15:35:24', 3, '2020-05-16 11:07:56');
INSERT INTO `pd_auth_menu` VALUES (695295658923406497, '运输任务管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transport/transport-task', 'Layout', b'1', 0, '', '', 695293828101317793, 3, '2020-04-02 15:36:43', 3, '2020-06-29 11:32:12');
INSERT INTO `pd_auth_menu` VALUES (695295822920692993, '司机作业单管理', '', b'0', 'http://68.79.7.219:8080/tms/#/transport/driver-operation', 'Layout', b'1', 1, '', '', 695293828101317793, 3, '2020-04-02 15:37:22', 3, '2020-05-16 11:08:13');
INSERT INTO `pd_auth_menu` VALUES (695295955934655841, '机构数据看板', '', b'0', 'http://68.79.7.219:8080/tms/#/transport/agency-data-board', 'Layout', b'1', 2, '', '', 695293828101317793, 3, '2020-04-02 15:37:54', 3, '2020-05-16 11:08:19');
INSERT INTO `pd_auth_menu` VALUES (702189239864921505, '调度管理', '', b'0', '/scheduling', 'pinda/scheduling/Index', b'1', 6, 'el-icon-user', '', 695291921186165953, 3, '2020-04-21 16:09:21', 3, '2020-04-21 16:09:31');
INSERT INTO `pd_auth_menu` VALUES (702189623291416225, '智能调度', '', b'0', 'http://68.79.7.219:8080/tms/#/scheduling/scheduling-list', 'Layout', b'1', 0, '', '', 702189239864921505, 3, '2020-04-21 16:10:52', 3, '2020-06-23 20:10:29');
INSERT INTO `pd_auth_menu` VALUES (703303431078548513, '调度信息', '', b'0', 'http://68.79.7.219:8080/tms/#/scheduling/scheduling-log-list', 'Layout', b'1', 0, '', '', 702189239864921505, 3, '2020-04-24 17:56:45', 3, '2020-06-23 20:10:37');
