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

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES (1, 'package rules;\r\n\r\nimport com.itheima.pinda.entity.AddressRule;\r\nimport com.itheima.pinda.entity.fact.AddressCheckResult;\r\nimport com.itheima.pinda.service.impl.DroolsRulesServiceImpl;\r\n\r\ndialect  \"java\"\r\n\r\nrule \"总重量在1kg以下\"\r\n    when\r\n        address : AddressRule(totalWeight != null, totalWeight<=1.00)\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        checkResult.setPostCodeResult(true);\r\n        checkResult.setResult(\"20\");\r\n		System.out.println(\"小于等于1kg价格为20元!\");\r\nend\r\n\r\nrule \"总重量大于1kg：距离在200公里以下\"\r\n    when\r\n        address : AddressRule(totalWeight != null && totalWeight>1.00 && distance<200.00)\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        address.setFirstFee(20.00);\r\n        address.setContinuedFee(6.00);\r\n        address.setFirstWeight(1.00);\r\n        DroolsRulesServiceImpl droolsRulesService = new DroolsRulesServiceImpl();\r\n        String fee = droolsRulesService.calcFee(address);\r\n        checkResult.setPostCodeResult(true);\r\n        checkResult.setResult(fee);\r\n		System.out.println(\"大于1kg,200公里内续重每1kg资费6元!\");\r\nend\r\n\r\nrule \"总重量大于1kg：距离在200-500公里\"\r\n    when\r\n        address : AddressRule(totalWeight != null && totalWeight>1.00 && distance>200.00 && distance<500.00)\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        address.setFirstFee(20.00);\r\n        address.setContinuedFee(9.00);\r\n        address.setFirstWeight(1.00);\r\n        DroolsRulesServiceImpl droolsRulesService = new DroolsRulesServiceImpl();\r\n        String fee = droolsRulesService.calcFee(address);\r\n        checkResult.setPostCodeResult(true);\r\n        checkResult.setResult(fee);\r\n		System.out.println(\"大于1kg,200至500公里续重每1kg资费9元!\");\r\nend\r\n\r\nrule \"总重量大于1kg：距离在500公里以上\"\r\n    when\r\n        address : AddressRule(totalWeight != null && totalWeight>1.00 && distance>500.00)\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        address.setFirstFee(20.00);\r\n        address.setContinuedFee(15.00);\r\n        address.setFirstWeight(1.00);\r\n        DroolsRulesServiceImpl droolsRulesService = new DroolsRulesServiceImpl();\r\n        String fee = droolsRulesService.calcFee(address);\r\n        checkResult.setPostCodeResult(true);\r\n        checkResult.setResult(fee);\r\n		System.out.println(\"大于1kg,500公里以上续重每1kg资费15元!\");\r\nend', '111', '111', 'score', '1');