SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE IF NOT EXISTS `organization` (
  `organization_id` INT(11)     NOT NULL AUTO_INCREMENT,
  `name`            VARCHAR(50) NOT NULL,
  `rate`            VARCHAR(200),
  `owner_id`        INT(11),
  PRIMARY KEY (`organization_id`),
  UNIQUE KEY `organization_id` (`organization_id`),
  KEY `owner_id` (`owner_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =8;

INSERT INTO `organization` (`organization_id`, `name`, `rate`, `owner_id`) VALUES
  (1, 'My loved company', NULL, NULL);

CREATE TABLE IF NOT EXISTS `users` (
  `user_id`         INT(11)     NOT NULL AUTO_INCREMENT,
  `email`           VARCHAR(50) NOT NULL,
  `password`        VARCHAR(50) NOT NULL,
  `name`            VARCHAR(50) NOT NULL,
  `organization_id` INT(11),
  `last_activity`   VARCHAR(50),
  `properties`      VARCHAR(200),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email` (`email`),
  KEY `organization_id` (`organization_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =8;

INSERT INTO `users` (`user_id`, `email`, `password`, `name`, `organization_id`) VALUES
  (1, 'illusionww@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b', 'Vladimir Ivashkin', 1),
  (2, 'snape@liceum8.ru', '5c924fd71a5cce07b5d332f4666e737b', 'Sergey Shilin', 1),
  (2, 'admin', 'admin', 'admin', 1);

CREATE TABLE IF NOT EXISTS `driver` (
  `driver_id`       INT(11)     NOT NULL AUTO_INCREMENT,
  `imei`            VARCHAR(50) NOT NULL,
  `name`            VARCHAR(50) NOT NULL,
  `phone_number`    VARCHAR(50) NOT NULL,
  `organization_id` INT(11)     NOT NULL,
  `last_activity`   VARCHAR(50),
  `properties`      VARCHAR(200),
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `driver_id` (`driver_id`),
  UNIQUE KEY `imei` (`imei`),
  KEY `organization_id` (`organization_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =8;

INSERT INTO `driver` (`driver_id`, `imei`, `name`, `phone_number`, `organization_id`) VALUES
  (1, '12314232341323', 'Vasya', '+7 (920) 232-32-34', 1),
  (2, '12312312131231', 'Pasha', '233-23-42', 1);

ALTER TABLE `users`
ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`organization_id`);

ALTER TABLE `driver`
ADD CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`organization_id`);