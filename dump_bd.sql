-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: diplom
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `type` tinyint DEFAULT NULL,
  `image1` longtext,
  `image2` longtext,
  `id_creator` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `chats_ibfk1_idx` (`id_creator`),
  CONSTRAINT `chats_ibfk1` FOREIGN KEY (`id_creator`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_chat` int DEFAULT NULL,
  `id_user` int DEFAULT NULL,
  `message` varchar(512) DEFAULT 'default_expression/ZXCGOD',
  `image` longtext,
  PRIMARY KEY (`id`),
  KEY `messages_ibfk_1` (`id_chat`),
  KEY `messages_ibfk_2` (`id_user`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`id_chat`) REFERENCES `chats` (`id`) ON DELETE CASCADE,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_in_chat`
--

DROP TABLE IF EXISTS `user_in_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_in_chat` (
  `id_chat` int DEFAULT NULL,
  `id_user` int DEFAULT NULL,
  KEY `user_in_chat_ibfk_1` (`id_chat`),
  KEY `user_in_chat_ibfk_2` (`id_user`),
  CONSTRAINT `user_in_chat_ibfk_1` FOREIGN KEY (`id_chat`) REFERENCES `chats` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_in_chat_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_userinchat_insert` BEFORE INSERT ON `user_in_chat` FOR EACH ROW BEGIN
    DECLARE rowcount INT;
    
    SELECT COUNT(*) 
    INTO rowcount
    FROM user_in_chat where id_user = new.id_user and id_chat = new.id_chat;
    
    IF rowcount > 0 THEN
        signal sqlstate '45000';
    END IF; 

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `photo` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `addPhoto` BEFORE INSERT ON `users` FOR EACH ROW set new.photo = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDACgcHiMeGSgjISMtKygwPGRBPDc3PHtYXUlkkYCZlo+AjIqgtObDoKrarYqMyP/L2u71////m8H////6/+b9//j/2wBDASstLTw1PHZBQXb4pYyl+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj4+Pj/wAARCAC6ALgDASIAAhEBAxEB/8QAGQAAAgMBAAAAAAAAAAAAAAAAAgMAAQQF/8QANBAAAQMCBQIFAgQHAQEAAAAAAQACEQMhBBIxQVETYSIycZGhFIEFM0JSI2JyscHR8UPw/8QAFAEBAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/ANfVloMKjUI2RObog09EA06xNMGEXVPCFmUtkCIJCuEBdU8KuqeFIUhALKxLQYRdU8JdMEMRAID6p4U6p4Q5VIQQVjncI0hX1TwltH8V/eEUIC6x4U6p4QwrhBT6xDTbZW2sS0WQVB4HeitosPRAzqnhTqlZ8TVFGkT+o2AWf8PrlxNN5ncEoN1TEZGEusiFU8Ll4usatbI3ytMepXUa2wCCdU8KdU8KZVA1AnEV3CnYaqIcWIpqINjkBRuuEMIFUfKf6j/dE5gNxIPZVR0cP5imIFy5tnCRyFcg6FEhLRM6FALPL9yiCFlh9z/dGEFwqKsKIEj89/oP8o0I/OcP5R/lGgpCSeYhHZSAf+oFvMs9bKw60QrqeQ9goAC0GYsgwYo56sHLIMAOntws9OWVQ5hixIn7/wCldd5fULgYh0iEAqOLokCbaID6ZY5rrmTwuu18tB5Nvdcwlzd5ATaGJizzInQoN2ckWCYEthY9stMowgz4z8tRTGflqINZsY5QrO6vUztzARN0D8RVY4tystugfSHiqD+b/CYsTcRUBccg8RnXsiOKf+we6DUVRWX6tw/8vlCcdF+mfdBoboT3P91bjl0FysjMZDT/AAzqTqq+rL3hwYQG63QbRUsC6yLadRyFiOKaWwabkluJex3hm6DbMV3f0hFmWeliqT3zUaWk2zBPLW2LXhwOkIIXK5SzUawnPmbtcFCK7C7KJn0QNfLmkDdDV8FK5ujaYZLjosWKxAeIE6oMb9TxKlPwulQiTyiAAQMFQEXSnOl0jQIqgGUEJYPZBr/Dy7rG9okhdKflYKL3HChlJzQ/c6FaMMXhuWrOYGZJ1CCYs/w1FWLjILqILqC4shcMzAY8Q/stz8JJBD4+yD6NwJOcHtCDDlIuoGEFa24RzhLSIU+jq8tv3QYyDMJbmAiZK2nCVh+kH7qvpK2Xyj0lBjAAaQhYBFgt30VUiIHuktwdePLvygUQPdBlHG61nCVv2H4QuwtQkgNI7lBieImPdXSqObLZ8JTHsyEtcBZQU2hslA1lQtGXKHA8o3NY12dgtuOEqlUAqjgLXUyvHh2FiEGPFVCWgDTdYySStVZviIIuTuszmxqCPVAI1VkptMDKTGtlbKBquhjZhAkmykWW0fhtQgEkD5WatQdReWuEjYoAY4tMjVbadQ1CwEAifj/SyU6L33AtyujhqBpMJMyRH2QKxVKnk8LQCorxDCxuqiDqF0Ks2t1Tr6KZNyUFtdBMHVWapHdLywZzKyBygI1n7BLNeoPMQB6KyeEqsCaboNwgY3E5jAc0kdlT8YGWkE9lz3OgzvuUsv7oNr/xB5nK0N7lZKuIfU8zieyS58nWyoHkoCkk+iKq4wAEA1R1RGXugjIJ3TQXMb4TZAGgEEbonOAAET6IF1XZtkDWvfa8d9kxwB9DuteUU2gNGo0hBjFNzTlt6hdjDYWk2i0i5IueViyeIGLoqZewWJ8JhB0eizv7oKmFp1RDhZZDWfFy5V1Hg2c5Br+jpgQJCsYciYf6AhZfqH6ZyrGKeBAcSTzsgHG0nNb5mn7KJWIrOFLKQD35UQbnFVmVPNkMwYQESEDpzCNN1CUDnAboGZm8pOJccsDRRpBuTYFBWdI1QY6rvhZy43TKp+UoAk6SgJp3Vk8KhYEQVDsgsG8rQYcwHhZU8OIpNQECIuVU5jCF58NlbGnLmQMaJgb7LpspEBocJACz4WiGt6jxPC3B0jgoFVWCxA0V0WBocO6jKueoWcKjVpscQXboDLBslmRaIRCrTP6woatL94QLcNJhUWtNg3VGalM6OCrO0Tce6DLi6YDFEeMcDSF/lRA97wN0l1YfpQVXfCVIlAbnknVLJLjF9eVCQmsHTpmq77IBqODGho1WapVJECUFV5c5Lgz3QU476oQ47GycKMi5uVKlAgS06C8oFSeSiBm2qGRGl1bDDweEEKY0yyCdFVTxuJgAlCARZAxskwdEbXBomUjMQbKZidUHVoVJpi6e+rlAuuTh3lpOt0975b5ogXQa6dQNlyzl+dxMi6R1C9utlQubHTlBotGqvLO6BpaWi4RiCQAZ+6C8otBVlsCVIM2GihB5QIxA8CivEz0wogurqLpYB4TqrSSP7qmsNoF0C2U3OqAAJ2Iu2JgDQLQym2i0uebgLDVqOxD4YLIEZSHANEkpzMORc68p9GiKYzG7iNUTnlrfDH3QJyQJdEAapNaqXtIaMrNuSnVHumGNbBF1lfJqQ7ZAtrC42BsnMoxqZKZTOZsgRB0G6M3BKBWRqstGhm/COJUje0oM9SkWuMj0QZTOq01h4iFVJozEwgRmLFYcXazHCPENuBA+yFouAge2mMovtoj6UnVEAY1Ukjf4QD0u49UQpgG5UzdlYI/4gJoIGp90TSQZnVDcKpOqAcUZYogxHk7qIH1TeyKlFFvVe0m/hWinhQ8hzhYfKViwH1QxpgAIEVXuxT4Ehg1PKNrAxoAAVeFggDTZAXTc+0oGOcIhJ8xVudsllxQEASb/AAU2hh21cRc/pkrM4ku39VKZeypma+Cg1VsM6lcXZOoSSI0U6tSCC4ZTqAhiLQdUBRvKrfSYQlx
P+kQcBsgXXM1PVFTkCUNceMG14TWRkHdAmuCYOylFvj9EysCWgR7KqIOY+miBwBv3UjsqIuZ2VgjRBeTtqoGke6mbaFM2vPCCiSDp8q5V2OqnhQJxPk9VFeIHgUQdgVIEuho2C52Jr031SaegFyn1mB2ouUnI0aC6DOXkme6rNGye5g0hCWj7IEEn/iqb6JrmQJlBlHZAOqoSLyiIMwRqqiNz6ILBMKAw6VIMTsp9igkA2UIIUGquQRvKCq2jTGyjASwLQ6gamHY5jSbbeqWaL6bRmaRPKCyB0WuPJmEpl3n0snU6edhsbKPpOpkTFwgqDlvKg1UvorG0a7oJvpFlN5jZFNlQ+N0FHhFBM2OitpmRGqKRpEcIM2IMtAUR4pvgnZRB0KgnRJI91rey6U6mgQWyZQup/wDxTiyCqDdQgzuagLYK1ZfCgNORCDORbXQ6yhDSRK0ZAFMreEGbJJ3sqDTotHTtrJ4Q5O90CsvBUyx6Iyw2EfCotg8oOjgGj6edySpj2A0g6DIKLBQMMPUp1an1KTmchBz8ET1ModE/K2Ymh1mAAwQudh/4WIa58wNYW52MpAgjMe4FkGBzHNcWuEEKwN4R1ahqVC+Ms7FUAYsgqI7FUTbVEAfRXHZAOhkD2Vj091ZFuFcbboEYn8tRXihNOfhRB1KtWmx3icAUs4iidHhcnFXrOnlJQdo16JnxITWogWeuOog65rUos4eyoVqU3fAXJUQdU1aOhPsqNSkR5hC5aiDpmpTsc4VF9IaO+65qiDpdWnoHaIc9I6uBlc/dUg6lPEtp+HOCwGYPqjP4hc2ZBFrrkKIN4ezXMEXUYdwD2XOCiDpGpTI8wUFWkB5lzSog6fUpfuF7KdSj+5c1Ug6YqUv3KdWnrnC5qpBtxVWm6mA10qLGog//2Q==" */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping routines for database 'diplom'
--
/*!50003 DROP PROCEDURE IF EXISTS `addUserInChat` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addUserInChat`( IN emailUser varchar(60) , IN idchat INT)
BEGIN
 
   DECLARE iduser INT;
 
  SELECT id into iduser from users  where email = emailUSer;
  INSERT INTO user_in_chat VALUES(idchat,iduser);
 
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `createChat` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `createChat`(IN emailUser varchar(60), IN iduserthatadd INT)
BEGIN
	DECLARE iduser INT;
    DECLARE name_user INT;
    declare idchat int;
    declare image1 longtext;
    declare image2 longtext;
    SELECT id into iduser from users  where email = emailUSer;
    select photo into image2 from users where id = iduser;
    select photo into image1 from users where id = iduserthatadd;
    SELECT name into name_user from users  where email = emailUSer;
     INSERT INTO chats values(null,name_user,0,image1,image2,iduserthatadd);
     SELECT last_insert_id() into idchat;
    INSERT INTO user_in_chat values(idchat,iduser);
    insert into user_in_chat values(idchat,iduserthatadd);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `createGroupChat` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `createGroupChat`(IN name_chat varchar(60), IN iduserthatadd INT)
BEGIN
	
    declare idchat int;
    declare image longtext;
    select photo into image from users where id = iduserthatadd;
     INSERT INTO chats values(null,name_chat,1,image,image,1);
     SELECT last_insert_id() into idchat;
    INSERT INTO user_in_chat values(idchat,iduserthatadd);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-15 19:14:51
