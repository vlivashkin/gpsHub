DROP TABLE IF EXISTS `companies`;
CREATE TABLE `companies` (
  `company_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`company_id`),
  UNIQUE KEY `company_id` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
INSERT INTO `companies` VALUES (1,'companyname');


DROP TABLE IF EXISTS `drivers`;
CREATE TABLE `drivers` (
  `driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `vehicle_num` varchar(20) DEFAULT NULL,
  `vehicle_description` varchar(200) DEFAULT NULL,
  `lat` varchar(20) DEFAULT NULL,
  `lng` varchar(20) DEFAULT NULL,
  `busy` varchar(20) DEFAULT NULL,
  `last_activity` varchar(20) DEFAULT NULL,
  `accuracy` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `driver_id` (`driver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;

INSERT INTO `drivers` VALUES 
	(101,'','','','В841ВС','','55.78493333333333','38.452353333333335','free','1408442016',NULL),
	(122,'','','','','','55.93092650215309','37.520911415907754','free','1407542808',NULL),
	(123,'','','+7911123456','E123KX','','55.775556666666674','38.433315','free','1408442015',NULL),
	(124,'','','+7912123456','E223KX','','55.802446666666675','38.43470666666666','free','1408356832',NULL),
	(125,'','','+7913123456','E323KX','','55.77694666666666','38.45496','free','1408384260',NULL),
	(126,'','','+7914123456','E423KX','','55.77518666666666','38.43078833333333','free','1408379056',NULL),
	(127,'','','+7915123456','E523KX','','55.84989666666667','38.437848333333335','free','1408438397',NULL),
	(128,'','','+7916123456','E623KX','','55.79240166666667','38.431808333333336','free','1408442016',NULL),
	(129,'','','+7917123456','E723KX','','55.777483333333336','38.455391666666664','free','1407325895',NULL),
	(130,'','','+7918123456','E823KX','','55.79515500000001','38.44009333333334','free','1408441286',NULL),
	(131,'','','+7919123456','E923KX','','55.77641333333333','38.455958333333335','free','1408442017',NULL),
	(132,'','','+7911123456','E133KX','','55.792318333333334','38.457856666666665','free','1408442016',NULL),
	(133,'','','+7911123456','E143KX','','55.772483333333334','38.447671666666665','free','1408418030',NULL),
	(134,'','','+7911123456','E153KX','','55.755945000000004','38.44126333333334','free','1408431327',NULL),
	(135,'','','+7911123456','E163KX','','55.77241333333333','38.447805','free','1408382283',NULL),
	(136,'В','','+711123456','E173KX','','55.77475453333333','38.43458356666667','free','1407676344',NULL),
	(137,'','','+7911123456','E183KX','','55.77694499999999','38.45505','free','1407323013',NULL),
	(138,'','','+7911123456','E193KX','','55.80504333333333','38.435581666666664','free','1408361757',NULL),
	(139,'','','+7911123456','E124KX','','55.768775000000005','38.449245','free','1408442015',NULL);


DROP TABLE IF EXISTS `drivers_groups`;
CREATE TABLE `drivers_groups` (
  `group_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `drivers_groups` VALUES (1,101),(1,123),(2,122);


DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
INSERT INTO `groups` VALUES (1,'user1_group'),(2,'user2_group');


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL,
  `login` varchar(30) NOT NULL,
  `password` varchar(300) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_activity` varchar(50) DEFAULT NULL,
  `isadmin` varchar(10) DEFAULT NULL,
  `group_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email` (`login`),
  KEY `company_id` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
INSERT INTO `users` VALUES 
	(1,1,'sysadm','ac201aa8860de931d1a670467a9222ba3c9c6ccdaa0c0500f1882e852c8e7238','admin',NULL,'true',0),
	(2,1,'administrator','ac201aa8860de931d1a670467a9222ba3c9c6ccdaa0c0500f1882e852c8e7238','admin',NULL,'true',0);
