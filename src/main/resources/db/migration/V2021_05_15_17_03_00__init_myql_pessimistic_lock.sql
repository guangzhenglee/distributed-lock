CREATE TABLE `pessimistic_lock` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `resource_name` varchar(64) NOT NULL DEFAULT '' COMMENT '锁定的资源名',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uidx_resource_name` (`resource_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='锁定中的资源';
