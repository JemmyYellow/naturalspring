DROP TABLE IF EXISTS `jemmy_loginuser`;
CREATE TABLE `business`.`jemmy_loginuser`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `status` int(11) NOT NULL COMMENT '0-登录 1-注销',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1176 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `jemmy_user`;
CREATE TABLE `business`.`jemmy_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '用户密码，md5加密',
  `phone` varchar(255) NOT NULL COMMENT '手机',
  `role` int(4) NOT NULL COMMENT '角色:0-管理员，1-普通用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `jemmy_product`;
CREATE TABLE `business`.`jemmy_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `detail` text COMMENT '商品详情',
  `main_image` varchar(500) DEFAULT NULL COMMENT '产品主图',
  `price` decimal(20,2) NOT NULL COMMENT '价格，单位元，保留两位小数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=522 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `jemmy_cart`;
CREATE TABLE `business`.`jemmy_cart`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL COMMENT '商品数量',
  `checked` int(11) DEFAULT NULL COMMENT '是否选择 1=已勾选，0=未勾选',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `jemmy_order`;
CREATE TABLE `business`.`jemmy_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` int(11) DEFAULT NULL,
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `unit_price` decimal(20,2) DEFAULT NULL COMMENT '单价',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `total_price` decimal(20,2) DEFAULT NULL COMMENT '实际付款金额',
  `status` int(11) DEFAULT NULL COMMENT '订单状态: 20-未付款 30-已付款 40-交易成功 50-交易关闭',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_no_index` (`order_no`) USING BTREE,
  KEY `order_no_user_id_index` (`user_id`,`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `jemmy_payinfo`;
CREATE TABLE `business`.`jemmy_payinfo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `platform_number` varchar(255) DEFAULT NULL COMMENT '支付流水号',
  `platform_status` varchar(255) DEFAULT NULL COMMENT '支付状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
