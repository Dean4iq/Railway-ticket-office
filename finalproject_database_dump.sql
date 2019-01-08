-- MySQL dump 10.13  Distrib 5.7.23, for Win64 (x86_64)
--
-- Host: localhost    Database: finalproject
-- ------------------------------------------------------
-- Server version	5.7.23-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE SCHEMA IF NOT EXISTS `finalproject`;

USE `finalproject`;

CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'localhost' WITH GRANT OPTION;

--
-- Table structure for table `route`
--

DROP TABLE IF EXISTS `route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route` (
  `Train_t_id` int(11) NOT NULL,
  `Station_st_id` int(11) NOT NULL,
  `arrival` datetime NOT NULL,
  `departure` datetime NOT NULL,
  PRIMARY KEY (`Train_t_id`,`Station_st_id`),
  KEY `fk_Route_Train2_idx` (`Train_t_id`),
  KEY `fk_Route_Station2_idx` (`Station_st_id`),
  CONSTRAINT `fk_Route_Station2` FOREIGN KEY (`Station_st_id`) REFERENCES `station` (`st_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Route_Train2` FOREIGN KEY (`Train_t_id`) REFERENCES `train` (`t_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route`
--

LOCK TABLES `route` WRITE;
/*!40000 ALTER TABLE `route` DISABLE KEYS */;
INSERT INTO `route` VALUES (1,1,'2018-01-01 03:12:00','2018-01-01 03:12:00'),(1,2,'2018-01-01 16:11:00','2018-01-01 16:23:00'),(1,7,'2018-01-01 18:10:00','2018-01-01 18:10:00'),(1,8,'2018-01-01 13:34:00','2018-01-01 13:48:00'),(1,10,'2018-01-01 08:05:00','2018-01-01 08:20:00'),(1,12,'2018-01-01 06:42:00','2018-01-01 06:55:00'),(2,6,'2018-01-02 00:02:00','2018-01-01 00:15:00'),(2,7,'2018-01-01 19:45:00','2018-01-01 19:45:00'),(2,9,'2018-01-01 23:40:00','2018-01-01 23:50:00'),(2,14,'2018-01-02 06:01:00','2018-01-02 06:01:00'),(2,15,'2018-01-02 02:50:00','2018-01-02 03:05:00'),(2,16,'2018-01-02 04:40:00','2018-01-02 04:53:00'),(2,22,'2018-01-01 21:15:00','2018-01-01 21:26:00'),(3,1,'2018-01-01 16:25:00','2018-01-01 16:35:00'),(3,4,'2018-01-01 22:00:00','2018-01-01 22:00:00'),(3,15,'2018-01-01 18:10:00','2018-01-01 18:21:00'),(3,17,'2018-01-01 20:02:00','2018-01-01 20:15:00'),(3,21,'2018-01-01 15:41:00','2018-01-01 15:41:00'),(4,1,'2018-01-01 08:12:00','2018-01-01 08:20:00'),(4,2,'2018-01-01 16:00:00','2018-01-01 16:00:00'),(4,3,'2018-01-01 04:30:00','2018-01-01 04:30:00'),(4,6,'2018-01-01 10:04:00','2018-01-01 10:10:00'),(4,11,'2018-01-01 12:20:00','2018-01-01 12:34:00'),(4,20,'2018-01-01 05:50:00','2018-01-01 06:05:00'),(4,22,'2018-01-01 13:56:00','2018-01-01 14:05:00'),(5,2,'2018-01-01 23:58:00','2018-01-01 23:58:00'),(5,6,'2018-01-02 03:45:00','2018-01-02 03:55:00'),(5,9,'2018-01-02 02:12:00','2018-01-02 02:25:00'),(5,14,'2018-01-02 07:09:00','2018-01-02 07:09:00'),(5,15,'2018-01-02 04:33:00','2018-01-02 04:40:00'),(5,16,'2018-01-02 05:07:00','2018-01-02 05:18:00'),(5,23,'2018-01-02 01:04:00','2018-01-02 01:10:00'),(6,3,'2018-01-01 09:20:00','2018-01-01 09:25:00'),(6,5,'2018-01-01 13:20:00','2018-01-01 13:28:00'),(6,16,'2018-01-01 14:01:00','2018-01-01 14:08:00'),(6,17,'2018-01-01 15:55:00','2018-01-01 16:03:00'),(6,18,'2018-01-01 17:10:00','2018-01-01 17:10:00'),(6,19,'2018-01-01 08:10:00','2018-01-01 08:10:00'),(6,20,'2018-01-01 11:03:00','2018-01-01 11:15:00'),(12,1,'2018-01-02 02:32:00','2018-01-02 02:32:00'),(12,4,'2018-01-01 16:34:00','2018-01-01 16:34:00'),(12,13,'2018-01-02 00:38:00','2018-01-02 00:49:00'),(12,15,'2018-01-01 21:55:00','2018-01-01 22:07:00'),(12,16,'2018-01-01 20:40:00','2018-01-01 20:56:00'),(12,17,'2018-01-01 18:22:00','2018-01-01 18:35:00'),(13,1,'2018-01-01 21:32:00','2018-01-01 21:50:00'),(13,6,'2018-01-01 23:10:00','2018-01-01 23:21:00'),(13,15,'2018-01-02 01:07:00','2018-01-02 01:18:00'),(13,17,'2018-01-02 02:59:00','2018-01-02 03:06:00'),(13,18,'2018-01-02 04:44:00','2018-01-02 04:44:00'),(13,19,'2018-01-01 18:45:00','2018-01-01 18:45:00'),(13,21,'2018-01-01 20:05:00','2018-01-01 20:13:00'),(65,1,'2018-01-01 13:29:00','2018-01-01 13:29:00'),(65,4,'2018-01-02 03:06:00','2018-01-02 03:06:00'),(65,5,'2018-01-01 21:01:00','2018-01-01 21:15:00'),(65,13,'2018-01-01 15:11:00','2018-01-01 15:16:00'),(65,14,'2018-01-01 22:35:00','2018-01-01 22:43:00'),(65,15,'2018-01-01 18:25:00','2018-01-01 18:38:00'),(65,16,'2018-01-01 19:47:00','2018-01-01 20:00:00'),(65,17,'2018-01-02 01:12:00','2018-01-02 01:20:00'),(65,18,'2018-01-01 23:57:00','2018-01-02 00:05:00'),(65,24,'2018-01-01 16:55:00','2018-01-01 17:04:00');
/*!40000 ALTER TABLE `route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seat` (
  `s_id` int(11) NOT NULL AUTO_INCREMENT,
  `Wagon_w_id` int(11) NOT NULL,
  PRIMARY KEY (`s_id`),
  KEY `fk_Seat_Wagon1_idx` (`Wagon_w_id`),
  CONSTRAINT `fk_Seat_Wagon1` FOREIGN KEY (`Wagon_w_id`) REFERENCES `wagon` (`w_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,2),(8,2),(9,2),(10,2),(11,2),(12,2),(13,3),(14,3),(15,3),(16,3),(17,3),(18,3),(19,4),(20,4),(21,4),(22,4),(23,4),(24,5),(25,5),(26,5),(27,5),(28,5),(29,6),(30,6),(31,6),(32,6),(33,6),(34,6),(35,6),(36,7),(37,7),(38,7),(39,7),(40,7),(41,7),(42,8),(43,8),(44,8),(45,8),(46,8),(47,8),(48,8),(49,9),(50,9),(51,9),(52,9),(53,9),(54,9),(55,9),(56,9),(57,10),(58,10),(59,10),(60,10),(61,10),(62,11),(63,11),(64,11),(65,11),(66,11),(67,11),(68,11),(69,11),(70,12),(71,12),(72,12),(73,12),(74,12),(75,13),(76,13),(77,13),(78,13),(79,13),(80,13),(81,14),(82,14),(83,14),(84,14),(85,14),(86,15),(87,15),(88,15),(89,15),(90,15),(91,16),(92,16),(93,16),(94,16),(95,16),(96,16),(97,17),(98,17),(99,17),(100,17),(101,17),(102,18),(103,18),(104,18),(105,18),(106,18),(107,19),(108,19),(109,19),(110,19),(111,19),(112,20),(113,20),(114,20),(115,20),(116,20),(117,21),(118,21),(119,21),(120,21),(121,21),(122,22),(123,22),(124,22),(125,22),(126,22),(127,23),(128,23),(129,23),(130,23),(131,23),(132,23),(133,24),(134,24),(135,24),(136,24),(137,24),(138,24),(139,24),(140,25),(141,25),(142,25),(143,25),(144,25),(145,26),(146,26),(147,26),(148,26),(149,26),(150,27),(151,27),(152,27),(153,27),(154,27),(155,28),(156,28),(157,28),(158,28),(159,29),(160,29),(161,29),(162,29),(163,29),(164,30),(165,30),(166,30),(167,30),(168,30),(169,30),(170,31),(171,31),(172,31),(173,31),(174,31),(175,32),(176,32),(177,32),(178,32),(179,32),(180,32),(181,33),(182,33),(183,33),(184,33),(185,33),(186,33),(187,34),(188,34),(189,34),(190,34),(191,34),(192,34),(193,34),(194,35),(195,35),(196,35),(197,35),(198,35),(199,36),(200,36),(201,36),(202,36),(203,36),(204,36);
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `st_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `name_UA` varchar(45) NOT NULL,
  PRIMARY KEY (`st_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station`
--

LOCK TABLES `station` WRITE;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` VALUES (1,'Kyiv','Київ'),(2,'Lviv','Львів'),(3,'Kharkiv','Харків'),(4,'Odesa','Одеса'),(5,'Dnipro','Дніпро'),(6,'Vinnytsia','Вінниця'),(7,'Uzhhorod','Ужгород'),(8,'Lutsk','Луцьк'),(9,'Khmelnytsky','Хмельницький'),(10,'Rivne','Рівне'),(11,'Chernivtsi','Чернівці'),(12,'Zhytomyr','Житомир'),(13,'Cherkasy','Черкаси'),(14,'Zaporizhzhia','Запоріжжя'),(15,'Kropyvnytskyi','Кропивницький'),(16,'Kryvy Rih','Кривий Ріг'),(17,'Mykolayv','Миколаїв'),(18,'Kherson','Херсон'),(19,'Sumy','Суми'),(20,'Poltava','Полтава'),(21,'Chernihiv','Чернігів'),(22,'Ivano-Frankivsk','Івано-Франківськ'),(23,'Ternopil','Тернопіль'),(24,'Kremenchuk','Кременчуг');
/*!40000 ALTER TABLE `station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `tick_id` int(11) NOT NULL AUTO_INCREMENT,
  `User_login` varchar(18) DEFAULT NULL,
  `Seat_s_id` int(11) NOT NULL,
  `cost` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `departure_st_id` int(11) NOT NULL,
  `arrival_st_id` int(11) NOT NULL,
  `Train_t_id` int(11) NOT NULL,
  PRIMARY KEY (`tick_id`),
  KEY `fk_Ticket_Seat1_idx` (`Seat_s_id`),
  KEY `fk_Ticket_Station1_idx` (`departure_st_id`),
  KEY `fk_Ticket_Station2_idx` (`arrival_st_id`),
  KEY `fk_Ticket_User1_idx` (`User_login`),
  KEY `fk_Ticket_Train1_idx` (`Train_t_id`),
  CONSTRAINT `fk_Ticket_Seat1` FOREIGN KEY (`Seat_s_id`) REFERENCES `seat` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Ticket_Station1` FOREIGN KEY (`departure_st_id`) REFERENCES `station` (`st_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Ticket_Station2` FOREIGN KEY (`arrival_st_id`) REFERENCES `station` (`st_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Ticket_Train1` FOREIGN KEY (`Train_t_id`) REFERENCES `train` (`t_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Ticket_User1` FOREIGN KEY (`User_login`) REFERENCES `user` (`login`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (1,'relative_Login03',26,230,'2019-01-11 00:00:00',7,22,2),(2,'dean4iq',23,330,'2019-01-10 00:00:00',2,10,1),(3,'dean4iq',160,300,'2018-12-25 00:00:00',1,17,12),(4,'dean4iq',114,450,'2018-12-28 00:00:00',6,23,5),(5,'WeWillRockU',95,380,'2018-12-31 00:00:00',1,20,4),(6,'WeWillRockU',35,230,'2019-01-05 00:00:00',7,22,2),(7,'relative_Login03',113,450,'2019-01-12 00:00:00',2,23,5),(8,NULL,18,330,'2018-12-30 00:00:00',1,12,1),(9,NULL,51,535,'2019-01-08 00:00:00',1,21,3),(10,'thunder11',22,330,'2019-01-13 00:00:00',1,2,1),(11,'dean4iq',72,535,'2019-01-04 00:00:00',1,15,3),(12,'thunder11',153,300,'2019-01-14 00:00:00',1,15,12),(13,NULL,42,230,'2018-12-27 00:00:00',6,22,2),(14,NULL,64,535,'2019-01-01 00:00:00',4,17,3),(43,'dean4iq',52,535,'2019-01-10 00:00:00',1,4,3);
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `train`
--

DROP TABLE IF EXISTS `train`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `train` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `cost` int(11) NOT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `train`
--

LOCK TABLES `train` WRITE;
/*!40000 ALTER TABLE `train` DISABLE KEYS */;
INSERT INTO `train` VALUES (1,330),(2,230),(3,535),(4,380),(5,450),(6,400),(12,300),(13,142),(65,254);
/*!40000 ALTER TABLE `train` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `login` varchar(18) NOT NULL,
  `password` varchar(28) NOT NULL,
  `name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `name_ua` varchar(45) NOT NULL,
  `last_name_ua` varchar(45) NOT NULL,
  `admin` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`login`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('admin111','admin111','Admin','Admin','Адмін','Адмін',1),('dean4iq','dean4iq','Den','Onyshchenko','Денис','Онищенко',0),('relative_Login03','relative_Login03','Albert','Einstein','Альберт','Ейнштейн',0),('thunder11','thunder11','Thor','Odinson','Анатолій','Охріменко',0),('WeWillRockU','WeWillRockU','Freddy','Mercury','Фредді','Меркурі',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wagon`
--

DROP TABLE IF EXISTS `wagon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wagon` (
  `w_id` int(11) NOT NULL AUTO_INCREMENT,
  `Train_t_id` int(11) DEFAULT NULL,
  `typeUA` varchar(15) NOT NULL,
  `typeEN` varchar(25) NOT NULL,
  PRIMARY KEY (`w_id`),
  KEY `fk_Wagon_Train2_idx` (`Train_t_id`),
  CONSTRAINT `fk_Wagon_Train2` FOREIGN KEY (`Train_t_id`) REFERENCES `train` (`t_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wagon`
--

LOCK TABLES `wagon` WRITE;
/*!40000 ALTER TABLE `wagon` DISABLE KEYS */;
INSERT INTO `wagon` VALUES (1,1,'Плацкарт','3rd class'),(2,1,'Купе','2nd class'),(3,1,'Купе','2nd class'),(4,1,'Люкс','1st class'),(5,2,'Плацкарт','3rd class'),(6,2,'Плацкарт','3rd class'),(7,2,'Купе','2nd class'),(8,2,'Плацкарт','3rd class'),(9,3,'Люкс','1st class'),(10,3,'Люкс','1st class'),(11,3,'Купе','2nd class'),(12,3,'Купе','2nd class'),(13,4,'Плацкарт','3rd class'),(14,4,'Плацкарт','3rd class'),(15,4,'Плацкарт','3rd class'),(16,4,'Купе','2nd class'),(17,4,'Купе','2nd class'),(18,5,'Люкс','1st class'),(19,5,'Купе','2nd class'),(20,5,'Купе','2nd class'),(21,5,'Плацкарт','3rd class'),(22,6,'Купе','2nd class'),(23,6,'Купе','2nd class'),(24,6,'Купе','2nd class'),(25,6,'Плацкарт','3rd class'),(26,6,'Плацкарт','3rd class'),(27,12,'Плацкарт','3rd class'),(28,12,'Плацкарт','3rd class'),(29,12,'Купе','2nd class'),(30,13,'Купе','2nd class'),(31,13,'Купе','2nd class'),(32,13,'Купе','2nd class'),(33,65,'Сидячий','Seating'),(34,65,'Сидячий','Seating'),(35,65,'Сидячий','Seating'),(36,65,'Сидячий','Seating');
/*!40000 ALTER TABLE `wagon` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
