 CREATE TABLE `device` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` varchar(100) NOT NULL ,
  `comment` varchar(300) DEFAULT NULL ,
  `name` varchar(100) NOT NULL ,
  `uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_device_deviceid` (`device_id`),
  KEY `IDX_device_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;