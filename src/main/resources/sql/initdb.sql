-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: user_permissions
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
SET NAMES utf8;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `authority`
(
    `id`   int(11)      NOT NULL AUTO_INCREMENT,
    `name` varchar(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority`
    DISABLE KEYS */;
INSERT INTO `authority`
VALUES (1, 'DO_WHATEVER');
/*!40000 ALTER TABLE `authority`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grp_auth`
--

DROP TABLE IF EXISTS `grp_auth`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `grp_auth`
(
    `group_id`     int(11) DEFAULT NULL,
    `authority_id` int(11) DEFAULT NULL,
    KEY `fk_group_idx` (`group_id`),
    KEY `fk_authority_idx` (`authority_id`),
    KEY `fk_group_grpauth_idx` (`group_id`),
    KEY `fk_authority_grpauth_idx` (`authority_id`),
    CONSTRAINT `fk_authority_grpauth` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`),
    CONSTRAINT `fk_group_grpauth` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grp_auth`
--

LOCK TABLES `grp_auth` WRITE;
/*!40000 ALTER TABLE `grp_auth`
    DISABLE KEYS */;
INSERT INTO `grp_auth`
VALUES (1, 1);
/*!40000 ALTER TABLE `grp_auth`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `user`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `login`    varchar(256) NOT NULL,
    `password` varchar(256) NOT NULL,
    `group_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `login_UNIQUE` (`login`),
    KEY `fk_group_user_idx` (`group_id`),
    CONSTRAINT `fk_group_user` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (3, 'abc', '039410e18a02ea5692db20b5d5fb17be', 1),
       (5, 'Oleg', '235ca1c5552c2c4f14a48b26052d6932', 1);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_auth`
--

DROP TABLE IF EXISTS `user_auth`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `user_auth`
(
    `user_id` int(11) NOT NULL,
    `auth_id` int(11) NOT NULL,
    `enabled` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`user_id`, `auth_id`),
    KEY `fk_auth_usrauth_idx` (`auth_id`),
    CONSTRAINT `fk_auth_usrauth` FOREIGN KEY (`auth_id`) REFERENCES `authority` (`id`),
    CONSTRAINT `fk_user_usrauth` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_auth`
--

LOCK TABLES `user_auth` WRITE;
/*!40000 ALTER TABLE `user_auth`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_auth`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `user_group`
(
    `id`   int(11)      NOT NULL AUTO_INCREMENT,
    `name` varchar(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group`
--

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group`
    DISABLE KEYS */;
INSERT INTO `user_group`
VALUES (1, 'ADMIN');
/*!40000 ALTER TABLE `user_group`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2019-06-28 17:22:30
