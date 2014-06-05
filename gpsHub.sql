SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE IF NOT EXISTS `company` (
  `company_id` 		INT(11)     NOT NULL AUTO_INCREMENT,
  `name`       		VARCHAR(50) NOT NULL,
  `account_type`	VARCHAR(200),
  PRIMARY KEY (`company_id`),
  UNIQUE KEY `company_id` (`company_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =8;

INSERT INTO `company` (`company_id`, `name`) VALUES
  (1, 'My loved company');

CREATE TABLE IF NOT EXISTS `user` (
  `user_id`         INT(11)     NOT NULL AUTO_INCREMENT,
  `email`           VARCHAR(50) NOT NULL,
  `password`        VARCHAR(50) NOT NULL,
  `company_id` 		INT(11)		NOT NULL,
  `name`            VARCHAR(50),
  `last_activity`   VARCHAR(50),
  `user_type`      	VARCHAR(50),
  `properties`      VARCHAR(200),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email` (`email`),
  KEY `company_id` (`company_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =8;

INSERT INTO `user` (`user_id`, `email`, `password`, `name`, `company_id`) VALUES
  (1, 'illusionww@gmail.com', '21232f297a57a5a743894a0e4a801fc3', 'Vladimir Ivashkin', 1),
  (2, 'snape@liceum8.ru', '5c924fd71a5cce07b5d332f4666e737b', 'Sergey Shilin', 1);

CREATE TABLE IF NOT EXISTS `driver` (
  `driver_id`       	INT(11)     NOT NULL AUTO_INCREMENT,
  `hash`            	VARCHAR(50) NOT NULL,
  `status`				VARCHAR(50),
  `name`            	VARCHAR(50) NOT NULL,
  `alias`				VARCHAR(50),
  `phone_number`    	VARCHAR(50),
  `vehile_num`			VARCHAR(50),
  `vehile_description`	VARCHAR(200),
  `company_id` 			INT(11)     NOT NULL,
  `last_activity`   	VARCHAR(50),
  `properties`      	VARCHAR(200),
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `driver_id` (`driver_id`),
  UNIQUE KEY `hash` (`hash`),
  KEY `company_id` (`company_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =8;

INSERT INTO `driver` (`driver_id`, `hash`, `name`, `phone_number`, `company_id`) VALUES
  (1, '12314232341323', 'Vasily Pavlov', '+7 (920) 232-32-34', 1),
  (2, '12312312131231', 'Sergey Ivanov', '+7 (965) 453-23-42', 1);

ALTER TABLE `user`
ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`);

ALTER TABLE `driver`
ADD CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`);