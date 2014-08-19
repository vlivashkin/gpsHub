-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 19, 2014 at 05:27 PM
-- Server version: 5.5.38-0+wheezy1
-- PHP Version: 5.4.4-14+deb7u12

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `gpshub`
--

-- --------------------------------------------------------

--
-- Table structure for table `company`
--

CREATE TABLE IF NOT EXISTS `company` (
  `company_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_hash` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `account_type` varchar(200) DEFAULT NULL,
  `locversion` int(11) DEFAULT NULL,
  PRIMARY KEY (`company_id`),
  UNIQUE KEY `company_id` (`company_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `company`
--

INSERT INTO `company` (`company_id`, `company_hash`, `name`, `account_type`, `locversion`) VALUES
(1, 'qwerty', 'My favorite company', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `driver`
--

CREATE TABLE IF NOT EXISTS `driver` (
  `driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `confirmed` tinyint(1) NOT NULL,
  `rand` int(5) DEFAULT NULL,
  `name` varchar(70) DEFAULT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `vehicle_num` varchar(20) DEFAULT NULL,
  `vehicle_description` varchar(200) DEFAULT NULL,
  `lat` varchar(20) DEFAULT NULL,
  `lng` varchar(20) DEFAULT NULL,
  `busy` varchar(20) DEFAULT NULL,
  `last_activity` varchar(20) DEFAULT NULL,
  `properties` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `driver_id` (`driver_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=143 ;

--
-- Dumping data for table `driver`
--

INSERT INTO `driver` (`driver_id`, `confirmed`, `rand`, `name`, `alias`, `phone_number`, `vehicle_num`, `vehicle_description`, `lat`, `lng`, `busy`, `last_activity`, `properties`) VALUES
(101, 1, NULL, 'create new 136', '', '', 'В841ВС', '', '55.78493333333333', '38.452353333333335', 'free', '1408442016', NULL),
(122, 1, 5196, '', '', '+79859115292', 'А972ВС', '', '55.93092650215309', '37.520911415907754', 'free', '1407542808', NULL),
(123, 1, 6276, '', '', '+79859115386', 'Р297ХК', '', '55.775556666666674', '38.433315', 'free', '1408442015', NULL),
(124, 1, 1108, '', '', '+79859115436', 'Р298ХК', '', '55.802446666666675', '38.43470666666666', 'free', '1408356832', NULL),
(125, 1, 1604, '', '', '+79859115318', 'Р299ХК', '', '55.77694666666666', '38.45496', 'free', '1408384260', NULL),
(126, 1, 8057, '', '', '+79859115116', 'А449ВС', '', '55.77518666666666', '38.43078833333333', 'free', '1408379056', NULL),
(127, 1, 7831, '', '', '+79859115406', 'А325ВС', '', '55.84989666666667', '38.437848333333335', 'free', '1408438397', NULL),
(128, 1, 1108, '', '', '+79859115405', 'Р267УР', '', '55.79240166666667', '38.431808333333336', 'free', '1408442016', NULL),
(129, 1, 7531, '', '', '+79859115489', 'Р336УР', '', '55.777483333333336', '38.455391666666664', 'free', '1407325895', NULL),
(130, 1, 8517, '', '', '+79859115027', 'В849ВС', '', '55.79515500000001', '38.44009333333334', 'free', '1408441286', NULL),
(131, 1, 843, '', '', '+79859115398', 'В776ВС', '', '55.77641333333333', '38.455958333333335', 'free', '1408442017', NULL),
(132, 1, 7185, '', '', '+79859115471', 'В840ВС', '', '55.792318333333334', '38.457856666666665', 'free', '1408442016', NULL),
(133, 1, 1740, '', '', '+79859114958', 'Т321КА', '', '55.772483333333334', '38.447671666666665', 'free', '1408418030', NULL),
(134, 1, 147, '', '', '+79859114978', 'М624ЕА', '', '55.755945000000004', '38.44126333333334', 'free', '1408431327', NULL),
(135, 1, 9314, '', '', '+79859115027', 'С170ЕН', '', '55.77241333333333', '38.447805', 'free', '1408382283', NULL),
(136, 0, 8100, 'В', '', '+79859115502', 'В841ВС', '', '55.77475453333333', '38.43458356666667', 'free', '1407676344', NULL),
(137, 1, 107, '', '', '+79859114823', 'К561ЕК', '', '55.77694499999999', '38.45505', 'free', '1407323013', NULL),
(138, 1, 7406, '', '', '+79859114853', 'K567ЕК', '', '55.80504333333333', '38.435581666666664', 'free', '1408361757', NULL),
(139, 1, 9008, '', '', '+79859114839', 'K946EK', '', '55.768775000000005', '38.449245', 'free', '1408442015', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(300) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_activity` varchar(50) DEFAULT NULL,
  `user_type` varchar(50) DEFAULT NULL,
  `properties` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email` (`email`),
  KEY `company_id` (`company_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `company_id`, `email`, `password`, `name`, `last_activity`, `user_type`, `properties`) VALUES
(1, 1, 'admin@admin.com', '3d63a9433775737f702615e5e32d61ff28cdc7fd4cc4ad1bd1daf78aa84cfc55', 'admin', NULL, NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
