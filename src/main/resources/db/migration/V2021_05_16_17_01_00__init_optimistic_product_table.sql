CREATE TABLE `optimistic_product` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `product_name` varchar(64) NOT NULL DEFAULT '' COMMENT '产品名称',
    `product_count` int(11) NOT NULL DEFAULT 0 COMMENT '产品数量',
    `version` int(11) NOT NULL DEFAULT 0 COMMENT '产品版本',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品';
