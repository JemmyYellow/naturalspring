DROP TABLE IF EXISTS `jemmy_loginuser`;
CREATE TABLE `business`.`jemmy_loginuser`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `status` int(11) NOT NULL COMMENT '0-登录 1-注销',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `jemmy_user`;
CREATE TABLE `business`.`jemmy_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '用户密码，md5加密',
  `phone` varchar(255) NOT NULL COMMENT '手机',
  `role` int(4) NOT NULL COMMENT '角色:0-管理员，1-普通用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `jemmy_product`;
CREATE TABLE `business`.`jemmy_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `detail` text COMMENT '商品详情',
  `main_image` varchar(500) DEFAULT NULL COMMENT '产品主图',
  `price` decimal(20,2) NOT NULL COMMENT '价格，单位元，保留两位小数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (1, 'SUV_1', '最大功率(kW):110 发动机:1.4T 150马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591196947040&di=ee7a04f0134cf1c0da34084f1a1f76bf&imgtype=0&src=http%3A%2F%2Fe0.ifengimg.com%2F11%2F2019%2F0127%2F3E34553B53A421C9D140C86CC623E5DD307B1843_size73_w1024_h768.jpeg', 99999.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (2, 'SUV_2', '最大功率(kW):137 发动机:1.4T 186马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591196987998&di=68163fa2607423cb0a43698b67f72e06&imgtype=0&src=http%3A%2F%2F01imgmini.eastday.com%2Fmobile%2F20190104%2F20190104180128_6e8def8e96ee59dbd5d2cbc6de54e6d1_1.jpeg', 88888.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (3, 'SUV_3', '最大功率(kW):110 发动机:2.0T 220马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197012671&di=18cfa18a595d145769d1e0881b2f0bdd&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ftranslate%2F20171031%2F9wRk-fynhhaz0181942.jpg', 108888.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (4, '轿车A', '最大功率(kW):140 发动机:2.0T 190马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197054238&di=11b75431e32d649cbf6aef359b4de761&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ftranslate%2F20170508%2FHR6O-fyeyqem1620037.jpg', 80000.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (5, '轿车B', '最大功率(kW):165 发动机:2.0T 224马力', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2625151105,3499070948&fm=15&gp=0.jpg', 90000.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (6, '轿车C', '最大功率(kW):250 发动机:3.0T 340马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197038113&di=030f1caf0904096cc0a910b048c9cd95&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn20115%2F141%2Fw1024h717%2F20181226%2Fa725-hqtwzec7036011.jpg', 100000.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (7, 'MVP_1', '最大功率(kW):158 发动机:2.0T 146马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197109361&di=031e82129de15e5fb415b26fc5c00d31&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9vo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2F9922720e0cf3d7ca725d380ef41fbe096b63a91a.jpg', 100000.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (8, 'MVP_2', '最大功率(kW):137 发动机:2.0T 186马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197110820&di=c31baa9348e973efc6f07cb1a76a7431&imgtype=0&src=http%3A%2F%2F05imgmini.eastday.com%2Fmobile%2F20190227%2F20190227084121_e61542de14eb301e743ecee12a92cbcb_1.jpeg', 120000.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (9, 'MVP_3', '最大功率(kW):174 发动机:2.0T 237马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197111936&di=6b76b51cd21e0626af878e43db09109a&imgtype=0&src=http%3A%2F%2Fdingyue.nosdn.127.net%2FoxmoB%3DbPbHvojLnCkQqX5wTGrDxQaRqoEAuMNkb7mtOLe1527057183739.jpg', 160000.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (10, '跑车X', '最大功率(kW):145 发动机:2.0T 197马力', 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2947445803,1457145895&fm=26&gp=0.jpg', 888888.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (11, '跑车Y', '最大功率(kW):184 发动机:2.0T 250马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197174267&di=b68cf7161d1cda07767fa5381ae5b560&imgtype=0&src=http%3A%2F%2Fpic29.nipic.com%2F20130513%2F11822880_144916202180_2.jpg', 1888888.00);
INSERT INTO `business`.`jemmy_product`(`id`, `name`, `detail`, `main_image`, `price`) VALUES (12, '跑车Z', '最大功率(kW):283 发动机:3.0T 385马力', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591197179987&di=77ec804ad74abf58a9373be5f0139e90&imgtype=0&src=http%3A%2F%2F01.imgmini.eastday.com%2Fmobile%2F20180610%2F20180610003004_5fc14c7d7090e8d3f07a41b8ded71bb8_3.jpeg', 2888888.00);

