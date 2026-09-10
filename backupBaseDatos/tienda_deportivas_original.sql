-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: tienda_deportivas
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `tienda_deportivas`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tienda_deportivas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `tienda_deportivas`;

--
-- Table structure for table `carrito_items`
--

DROP TABLE IF EXISTS `carrito_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrito_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `talla` int(11) NOT NULL,
  `carrito_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito_items`
--

LOCK TABLES `carrito_items` WRITE;
/*!40000 ALTER TABLE `carrito_items` DISABLE KEYS */;
INSERT INTO `carrito_items` VALUES (44,1,'Rojo',40,3,5),(45,1,'Verde',39,3,27);
/*!40000 ALTER TABLE `carrito_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carritos`
--

DROP TABLE IF EXISTS `carritos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carritos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carritos`
--

LOCK TABLES `carritos` WRITE;
/*!40000 ALTER TABLE `carritos` DISABLE KEYS */;
INSERT INTO `carritos` VALUES (1,4),(2,10),(3,11),(4,9),(5,5);
/*!40000 ALTER TABLE `carritos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facturas`
--

DROP TABLE IF EXISTS `facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `facturas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_factura` varchar(30) NOT NULL,
  `fecha_emision` datetime NOT NULL,
  `metodo_pago` varchar(30) NOT NULL,
  `pedido_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factura_numero` (`numero_factura`),
  UNIQUE KEY `uk_factura_pedido` (`pedido_id`),
  CONSTRAINT `fk_factura_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facturas`
--

LOCK TABLES `facturas` WRITE;
/*!40000 ALTER TABLE `facturas` DISABLE KEYS */;
INSERT INTO `facturas` VALUES (1,'FAC-2026-000016','2026-08-16 00:35:23','TARJETA',16),(2,'FAC-2026-000017','2026-08-18 21:21:18','TARJETA',17),(3,'FAC-2026-000018','2026-08-19 02:49:24','TARJETA',18),(4,'FAC-2026-000020','2026-08-19 15:02:12','TARJETA',20),(5,'FAC-2026-000021','2026-08-19 15:09:20','TARJETA',21);
/*!40000 ALTER TABLE `facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_pedidos`
--

DROP TABLE IF EXISTS `historial_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial_pedidos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `estado_anterior` enum('CANCELADO','ENTREGADO','ENVIADO','PAGADO','PENDIENTE','PREPARANDO') DEFAULT NULL,
  `estado_nuevo` enum('CANCELADO','ENTREGADO','ENVIADO','PAGADO','PENDIENTE','PREPARANDO') NOT NULL,
  `fecha_cambio` datetime(6) NOT NULL,
  `pedido_id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_pedidos`
--

LOCK TABLES `historial_pedidos` WRITE;
/*!40000 ALTER TABLE `historial_pedidos` DISABLE KEYS */;
INSERT INTO `historial_pedidos` VALUES (10,'PENDIENTE','ENTREGADO','2026-08-11 17:31:52.368878',2,1),(11,'PENDIENTE','ENTREGADO','2026-08-13 19:21:49.542924',6,1),(12,'PREPARANDO','ENVIADO','2026-08-15 20:15:39.001834',15,1),(13,'PREPARANDO','ENVIADO','2026-08-19 15:11:38.576736',21,1);
/*!40000 ALTER TABLE `historial_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_items`
--

DROP TABLE IF EXISTS `pedido_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `nombre_producto` varchar(255) DEFAULT NULL,
  `precio_unitario` decimal(10,2) DEFAULT NULL,
  `producto_id` int(11) NOT NULL,
  `subtotal_linea` decimal(10,2) DEFAULT NULL,
  `talla` varchar(255) DEFAULT NULL,
  `pedido_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_items`
--

LOCK TABLES `pedido_items` WRITE;
/*!40000 ALTER TABLE `pedido_items` DISABLE KEYS */;
INSERT INTO `pedido_items` VALUES (2,1,'Blanco','Nike Air Max 90',129.99,1,129.99,'38',2),(3,1,'Verde','Adidas Gazelle',94.99,16,94.99,'38',2),(4,1,'Rojo','Nike Air Max 90',129.99,1,129.99,'38',3),(5,1,'Amarillo','Nike Waffle Trainer',99.99,7,99.99,'37',3),(6,1,'Negro','Puma Smash 3.0',79.99,29,79.99,'36',3),(7,1,'Verde','Puma X-Ray 2',114.99,27,114.99,'39',4),(8,1,'Blanco','Puma Smash 3.0',79.99,29,79.99,'36',4),(9,1,'Azul','Puma Future Rider',109.99,24,109.99,'38',4),(10,1,'Beige','New Balance 327',119.99,42,119.99,'38',4),(11,1,'Blanco','New Balance 550',124.99,43,124.99,'39',4),(12,1,'Blanco','Nike Air Force 1',109.99,2,109.99,'37',5),(13,1,'Negro','Nike Zoom Fly',129.99,3,129.99,'39',5),(14,1,'Negro','Puma Suede Classic',89.99,21,89.99,'37',5),(15,1,'Blanco','Nike Air Max 90',129.99,1,129.99,'38',6),(16,1,'Azul','Reebok Classic Nylon',84.99,39,84.99,'36',6),(17,1,'Negro','Nike Revolution 6',89.99,4,89.99,'36',7),(18,1,'Negro','Nike Zoom Fly',129.99,3,129.99,'39',7),(19,1,'Negro','Nike Revolution 6',89.99,4,89.99,'36',8),(20,1,'Rojo','Nike Air Jordan 1',149.99,5,149.99,'40',8),(21,2,'Gris','New Balance 574',99.99,41,199.98,'37',9),(22,1,'Blanco','Reebok Club C 85',99.99,32,99.99,'36',10),(23,1,'Negro','Reebok Zig Dynamica',114.99,33,114.99,'38',10),(24,1,'Azul','Reebok Floatride',124.99,35,124.99,'38',10),(25,1,'Blanco','Nike Air Force 1',109.99,2,109.99,'37',11),(26,1,'Negro','Nike Zoom Fly',129.99,3,129.99,'39',11),(27,1,'Negro','Nike Revolution 6',89.99,4,89.99,'36',12),(28,1,'Negro','Nike Zoom Fly',129.99,3,129.99,'39',13),(29,1,'Rojo','Nike Air Jordan 1',149.99,5,149.99,'40',13),(30,1,'Negro','Nike Zoom Fly',129.99,3,129.99,'39',14),(31,1,'Rojo','Nike Air Jordan 1',149.99,5,149.99,'40',14),(32,1,'Blanco','Nike Air Force 1',109.99,2,109.99,'37',15),(33,1,'Blanco','Nike Air Max 90',129.99,1,129.99,'38',15),(34,1,'Negro','Puma Suede Classic',89.99,21,89.99,'37',16),(35,1,'Rosa','Nike Revolution 6',89.99,4,89.99,'36',17),(36,2,'Blanco','Nike Air Max 90',129.99,1,259.98,'38',18),(37,1,'Blanco','Nike Air Force 1',109.99,2,109.99,'37',18),(38,1,'Rojo','Nike Air Jordan 1',149.99,5,149.99,'40',19),(39,1,'Verde','Puma X-Ray 2',114.99,27,114.99,'39',19),(40,1,'Blanco','Nike Air Force 1',109.99,2,109.99,'37',20),(41,1,'Negro','Nike Revolution 6',89.99,4,89.99,'36',20),(42,1,'Verde','Adidas OZWEEGO',114.99,18,114.99,'37',21),(43,1,'Rosa','Adidas Continental 80',84.99,19,84.99,'36',21);
/*!40000 ALTER TABLE `pedido_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedidos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) DEFAULT NULL,
  `ciudad` varchar(255) DEFAULT NULL,
  `cp` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `envio` decimal(10,2) DEFAULT NULL,
  `estado` enum('PENDIENTE','PREPARANDO','ENVIADO','ENTREGADO','DEVUELTO','CANCELADO') DEFAULT NULL,
  `fecha_pedido` datetime(6) DEFAULT NULL,
  `id_pedido` varchar(255) DEFAULT NULL,
  `iva` decimal(10,2) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `pais` varchar(255) DEFAULT NULL,
  `provincia` varchar(255) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `estado_pago` varchar(50) DEFAULT NULL,
  `stripe_session_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (2,'Escudero Herrera','Toledo','45600','Calle Ave María','luisito@gmail.com',0.00,'ENTREGADO','2026-08-11 17:24:28.712545','PED-BEB5F322',47.25,'Luis','España','Toledo',224.98,'111222333',272.23,4,NULL,NULL),(3,'López','Toledo','42111','Calle el faro, numero 13','manu@gmail.com',0.00,'PENDIENTE','2026-08-12 19:24:26.400382','PED-8EF4633C',65.09,'Manuel','España','Toledo',309.97,'666111222',375.06,10,NULL,NULL),(4,'López','Toledo','44111','Calle el faro, número 13','manu@gmail.com',0.00,'PENDIENTE','2026-08-12 19:26:45.298139','PED-01788906',115.49,'Manu','España','Toledo',549.95,'666111222',665.44,10,NULL,NULL),(5,'López','Ciuadad Real','55555','Calle Barco, número 2','manu@gmail.com',0.00,'PENDIENTE','2026-08-13 18:57:38.297567','PED-C316306C',69.29,'Manuel','España','Alcazar',329.97,'111222111',399.26,10,NULL,NULL),(6,'Panol','El toboso','11111','Calle la oliva, número 13','berta@gmail.com',0.00,'ENTREGADO','2026-08-13 19:20:57.338623','PED-0C9D8CA9',45.15,'Berta','España','Toledo',214.98,'222333111',260.13,11,NULL,NULL),(7,'Escudero','Toboso','22132','calle princesa n 12','arturo@gmail.com',0.00,'PENDIENTE','2026-08-14 20:49:04.710439','PED-081D29AE',46.20,'Arturo','España','Toledo',219.98,'111333222',266.18,9,'PENDIENTE','cs_test_b1r989vZC8Ww75teQPwW5CdPAUc61vO9KiuKopQVLZRiWkc8VQjJ5272oa'),(8,'Escudero','Madrid','22222','calle real número 1','luis@gmail.com',0.00,'PREPARANDO','2026-08-14 23:21:09.332892','PED-E9DF83ED',50.40,'Luis','España','Madrid',239.98,'111222333',290.38,4,'PAGADO','cs_test_b1ByvnHC1OpRPojEkOSAz91XeYVPUjbH5CbpnPJorEgjL8mAWdpMJb8wlD'),(9,'Ortíz','Madrid','24322','calle pino número 12','pepe@gmail.com',0.00,'PREPARANDO','2026-08-15 00:26:54.755296','PED-C40F4F46',42.00,'Pepe','España','Madrid',199.98,'111222333',241.98,5,'PAGADO','cs_test_b1F3mpiDCr5AWhJ3ZmnUHRCHwmaFX1JI8YownDrYdWs4PUry7Md5zVYXVs'),(10,'García','Madrid','23111','calle mayor número 14','pepe@gmail.com',0.00,'PREPARANDO','2026-08-15 00:36:43.650854','PED-D7F4343F',71.39,'Pepe','España','Madrid',339.97,'111222333',411.36,5,'PAGADO','cs_test_b1vbCdl7hK8o9Go5WoME2bcJw8EAz6KXeX1DOA0VIKVkA3YaafDXnF4zsc'),(11,'Escudero','Madrid','22222','calle real número 1','luis@gmail.com',0.00,'PREPARANDO','2026-08-15 00:45:42.740915','PED-FD1D991F',50.40,'Pepe','España','Madrid',239.98,'111222333',290.38,5,'PAGADO','cs_test_b1IFj1LB0dFwTcBFf8DPOrOeynKtwTOwnv2nAqpeq1MeBOLtK7tvtE8YUZ'),(12,'Perez','Madrid','44221','calle mayor número 14','berta@gmail.com',4.99,'PENDIENTE','2026-08-15 18:43:32.856127','PED-5787F337',18.90,'berta','España','Madrid',89.99,'111222111',113.88,11,'PENDIENTE','cs_test_b1SFH4ruaPTOmI2lV0wo0VUYLvAfbGiKwr7veBjhnbKwo0Ao24AQGoEfhH'),(13,'García','Madrid','33222','calle real numero 1','berta@gmail.com',0.00,'PENDIENTE','2026-08-15 18:55:49.332069','PED-74C06F4F',58.80,'Berta','España','Madrid',279.98,'111222111',338.78,11,'PENDIENTE','cs_test_b1llJRRU2uQ4R8Px2xQD0ylXunNHYWGoZyDLRl1PBfNfWSA438PsYOLNmV'),(14,'García','Madrid','11111','calle real numero 1','García@gmail.com',0.00,'PREPARANDO','2026-08-15 18:56:35.081012','PED-E725949C',58.80,'Berta','España','Madrid',279.98,'111222111',338.78,11,'PAGADO','cs_test_b1vZkAlNaF7uz1lSKlELv5H5qbQ7o0Yzai8uod1DWsBcKfOxLH6HznQesf'),(15,'García García','Madrid','42100','calle real numero 1','manu@gmail.com',0.00,'ENVIADO','2026-08-15 19:48:56.709113','PED-B22EF7F7',50.40,'Manu','España','Madrid',239.98,'111222111',290.38,10,'PAGADO','cs_test_b12h7fecL7rLVI2mDHJhAgUprF7GyQZFjkhUtYouHMfCrmCUMONKgX5FGu'),(16,'García García','Madrid','43222','calle real numero 1','arturo@gmail.com',4.99,'PREPARANDO','2026-08-16 00:34:28.195989','PED-761B274D',18.90,'Arturo','España','Madrid',89.99,'111222111',113.88,9,'PAGADO','cs_test_b1FmIIafZt3TrCKINnn82zCtUlnOFyN8ZWlcHDZMcg00IVNzHtgtxhLHc1'),(17,'Escudero Polo','Ciuadad Real','45800','calle real numero 1','manu@gmail.com',4.99,'PREPARANDO','2026-08-18 21:20:20.843945','PED-D84602B6',18.90,'dfasfdas','España','Alcazar',89.99,'111222111',113.88,11,'PAGADO','cs_test_b1pulQ5tmfvTEWM4zO8ZZwU8xv9fIYKVpouPUrVacxnzj8BzXAzIUuAIKu'),(18,'Perez','Ciuadad Real','45800','calle real numero 1','berta@gmail.com',0.00,'PREPARANDO','2026-08-19 02:48:40.329826','PED-F43A3AE5',77.69,'Berta','España','Alcazar',369.97,'111222111',447.66,11,'PAGADO','cs_test_b1DcrHg2bycOpL9k2zr7Cm9dXWNl9yGfi7kQSpgX2NqAWEVXP54aHroPB9'),(19,'Perez','Ciuadad Real','45800','calle real numero 1','berta@gmail.com',0.00,'PENDIENTE','2026-08-19 04:30:56.721584','PED-E591E1B9',55.65,'afasd','España','Alcazar',264.98,'111222111',320.63,11,'PENDIENTE','cs_test_b1UOSBSephAKGhFLKd8gy2PFJw8PVina8Zn5or9cQpYpd6mAkAcDwcmRBb'),(20,'Perez','Ciuadad Real','45800','calle real numero 1','berta@gmail.com',0.00,'PREPARANDO','2026-08-19 15:01:26.507219','PED-70D0E13A',42.00,'asfasdf','España','Alcazar',199.98,'111222111',241.98,4,'PAGADO','cs_test_b14j4SrJvA44sExXx2YEyM4jzlczShOpeannmPrGODaAwpzc3h3ocdRqTc'),(21,'Perez','Ciuadad Real','45800','calle real numero 1','berta@gmail.com',0.00,'ENVIADO','2026-08-19 15:08:31.282210','PED-A878F040',42.00,'Luis','España','Alcazar',199.98,'111222111',241.98,4,'PAGADO','cs_test_b1KMj11H46MesDtf6vvAxP22xrNeUK4T7yc5LycT9oDKsiGWG9Z7XKNZO5');
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto_colores`
--

DROP TABLE IF EXISTS `producto_colores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_colores` (
  `producto_id` bigint(20) NOT NULL,
  `color` varchar(50) NOT NULL,
  `orden_color` int(11) NOT NULL CHECK (`orden_color` >= 0),
  PRIMARY KEY (`producto_id`,`orden_color`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_colores`
--

LOCK TABLES `producto_colores` WRITE;
/*!40000 ALTER TABLE `producto_colores` DISABLE KEYS */;
INSERT INTO `producto_colores` VALUES (1,'Blanco',0),(1,'Negro',1),(1,'Rojo',2),(2,'Blanco',0),(2,'Azul',1),(2,'Gris',2),(3,'Negro',0),(3,'Rojo',1),(3,'Verde',2),(4,'Negro',0),(4,'Blanco',1),(4,'Rosa',2),(5,'Rojo',0),(5,'Blanco',1),(5,'Negro',2),(6,'Verde',0),(6,'Negro',1),(6,'Gris',2),(7,'Amarillo',0),(7,'Blanco',1),(7,'Negro',2),(8,'Blanco',0),(8,'Negro',1),(8,'Verde',2),(9,'Azul',0),(9,'Negro',1),(9,'Gris',2),(10,'Negro',0),(10,'Rosa',1),(10,'Blanco',2),(11,'Blanco',0),(11,'Negro',1),(11,'Azul',2),(12,'Blanco',0),(12,'Negro',1),(12,'Dorado',2),(13,'Blanco',0),(13,'Verde',1),(13,'Azul',2),(14,'Negro',0),(14,'Gris',1),(14,'Rojo',2),(15,'Blanco',0),(15,'Azul',1),(15,'Rojo',2),(16,'Verde',0),(16,'Azul',1),(16,'Negro',2),(17,'Negro',0),(17,'Gris',1),(17,'Naranja',2),(18,'Verde',0),(18,'Gris',1),(18,'Negro',2),(19,'Blanco',0),(19,'Rosa',1),(19,'Negro',2),(20,'Negro',0),(20,'Blanco',1),(20,'Gris',2),(21,'Negro',0),(21,'Rojo',1),(21,'Azul',2),(22,'Blanco',0),(22,'Negro',1),(22,'Verde',2),(23,'Blanco',0),(23,'Rosa',1),(23,'Negro',2),(24,'Azul',0),(24,'Gris',1),(24,'Rojo',2),(25,'Negro',0),(25,'Naranja',1),(25,'Gris',2),(26,'Blanco',0),(26,'Rosa',1),(26,'Negro',2),(27,'Verde',0),(27,'Azul',1),(27,'Negro',2),(28,'Gris',0),(28,'Rojo',1),(28,'Azul',2),(29,'Blanco',0),(29,'Negro',1),(29,'Azul',2),(30,'Negro',0),(30,'Rosa',1),(30,'Gris',2),(31,'Blanco',0),(31,'Beige',1),(31,'Negro',2),(32,'Blanco',0),(32,'Verde',1),(32,'Gris',2),(33,'Negro',0),(33,'Naranja',1),(33,'Gris',2),(34,'Gris',0),(34,'Verde',1),(34,'Negro',2),(35,'Azul',0),(35,'Negro',1),(35,'Blanco',2),(36,'Blanco',0),(36,'Rosa',1),(36,'Negro',2),(37,'Negro',0),(37,'Verde',1),(37,'Gris',2),(38,'Blanco',0),(38,'Azul',1),(38,'Negro',2),(39,'Azul',0),(39,'Gris',1),(39,'Negro',2),(40,'Negro',0),(40,'Rojo',1),(40,'Gris',2),(41,'Gris',0),(41,'Negro',1),(41,'Azul',2),(42,'Beige',0),(42,'Verde',1),(42,'Blanco',2),(43,'Blanco',0),(43,'Rojo',1),(43,'Azul',2),(44,'Gris',0),(44,'Azul',1),(44,'Negro',2),(45,'Verde',0),(45,'Gris',1),(45,'Blanco',2),(46,'Negro',0),(46,'Azul',1),(46,'Blanco',2),(47,'Gris',0),(47,'Negro',1),(47,'Rojo',2),(48,'Azul',0),(48,'Beige',1),(48,'Negro',2),(49,'Gris',0),(49,'Negro',1),(49,'Verde',2),(50,'Blanco',0),(50,'Gris',1),(50,'Negro',2),(51,'blanco',0),(51,'negro',1),(51,'azul',2),(52,'Azul',0),(52,'Rojo',1),(52,'Verde',2),(53,'Rojo',0),(54,'Rojo',0),(54,'Verde',1),(54,'Azul',2);
/*!40000 ALTER TABLE `producto_colores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto_tallas`
--

DROP TABLE IF EXISTS `producto_tallas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_tallas` (
  `producto_id` bigint(20) NOT NULL,
  `talla` int(11) NOT NULL,
  `orden_talla` int(11) NOT NULL CHECK (`orden_talla` >= 0),
  PRIMARY KEY (`producto_id`,`orden_talla`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_tallas`
--

LOCK TABLES `producto_tallas` WRITE;
/*!40000 ALTER TABLE `producto_tallas` DISABLE KEYS */;
INSERT INTO `producto_tallas` VALUES (1,38,0),(1,39,1),(1,40,2),(1,41,3),(1,42,4),(1,43,5),(2,37,0),(2,38,1),(2,39,2),(2,40,3),(2,41,4),(2,42,5),(3,39,0),(3,40,1),(3,41,2),(3,42,3),(3,43,4),(3,44,5),(4,36,0),(4,37,1),(4,38,2),(4,39,3),(4,40,4),(4,41,5),(5,40,0),(5,41,1),(5,42,2),(5,43,3),(5,44,4),(6,38,0),(6,39,1),(6,40,2),(6,41,3),(6,42,4),(6,43,5),(7,37,0),(7,38,1),(7,39,2),(7,40,3),(7,41,4),(7,42,5),(8,39,0),(8,40,1),(8,41,2),(8,42,3),(8,43,4),(9,38,0),(9,39,1),(9,40,2),(9,41,3),(9,42,4),(10,36,0),(10,37,1),(10,38,2),(10,39,3),(10,40,4),(11,38,0),(11,39,1),(11,40,2),(11,41,3),(11,42,4),(12,37,0),(12,38,1),(12,39,2),(12,40,3),(12,41,4),(13,36,0),(13,37,1),(13,38,2),(13,39,3),(13,40,4),(14,38,0),(14,39,1),(14,40,2),(14,41,3),(14,42,4),(15,37,0),(15,38,1),(15,39,2),(15,40,3),(15,41,4),(16,38,0),(16,39,1),(16,40,2),(16,41,3),(16,42,4),(17,39,0),(17,40,1),(17,41,2),(17,42,3),(17,43,4),(18,37,0),(18,38,1),(18,39,2),(18,40,3),(18,41,4),(19,36,0),(19,37,1),(19,38,2),(19,39,3),(19,40,4),(20,39,0),(20,40,1),(20,41,2),(20,42,3),(20,43,4),(21,37,0),(21,38,1),(21,39,2),(21,40,3),(21,41,4),(22,38,0),(22,39,1),(22,40,2),(22,41,3),(22,42,4),(23,36,0),(23,37,1),(23,38,2),(23,39,3),(23,40,4),(24,38,0),(24,39,1),(24,40,2),(24,41,3),(24,42,4),(25,37,0),(25,38,1),(25,39,2),(25,40,3),(25,41,4),(26,36,0),(26,37,1),(26,38,2),(26,39,3),(26,40,4),(27,39,0),(27,40,1),(27,41,2),(27,42,3),(27,43,4),(28,37,0),(28,38,1),(28,39,2),(28,40,3),(28,41,4),(29,36,0),(29,37,1),(29,38,2),(29,39,3),(29,40,4),(30,37,0),(30,38,1),(30,39,2),(30,40,3),(30,41,4),(31,37,0),(31,38,1),(31,39,2),(31,40,3),(31,41,4),(32,36,0),(32,37,1),(32,38,2),(32,39,3),(32,40,4),(33,38,0),(33,39,1),(33,40,2),(33,41,3),(33,42,4),(34,39,0),(34,40,1),(34,41,2),(34,42,3),(34,43,4),(35,38,0),(35,39,1),(35,40,2),(35,41,3),(35,42,4),(36,36,0),(36,37,1),(36,38,2),(36,39,3),(36,40,4),(37,39,0),(37,40,1),(37,41,2),(37,42,3),(37,43,4),(38,37,0),(38,38,1),(38,39,2),(38,40,3),(38,41,4),(39,36,0),(39,37,1),(39,38,2),(39,39,3),(39,40,4),(40,39,0),(40,40,1),(40,41,2),(40,42,3),(40,43,4),(41,37,0),(41,38,1),(41,39,2),(41,40,3),(41,41,4),(42,38,0),(42,39,1),(42,40,2),(42,41,3),(42,42,4),(43,39,0),(43,40,1),(43,41,2),(43,42,3),(43,43,4),(44,38,0),(44,39,1),(44,40,2),(44,41,3),(44,42,4),(45,37,0),(45,38,1),(45,39,2),(45,40,3),(45,41,4),(46,39,0),(46,40,1),(46,41,2),(46,42,3),(46,43,4),(47,37,0),(47,38,1),(47,39,2),(47,40,3),(47,41,4),(48,38,0),(48,39,1),(48,40,2),(48,41,3),(48,42,4),(49,39,0),(49,40,1),(49,41,2),(49,42,3),(49,43,4),(50,38,0),(50,39,1),(50,40,2),(50,41,3),(50,42,4),(51,38,0),(51,39,1),(51,40,2),(51,41,3),(51,42,4),(52,22,0),(52,23,1),(52,24,2),(53,12,0),(54,40,0),(54,42,1),(54,44,2);
/*!40000 ALTER TABLE `producto_tallas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `marca` varchar(100) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,0x01,'Nike','Nike Air Max 90',129.99),(2,0x01,'Nike','Nike Air Force 1',109.99),(3,0x01,'Nike','Nike Zoom Fly',129.99),(4,0x01,'Nike','Nike Revolution 6',89.99),(5,0x01,'Nike','Nike Air Jordan 1',149.99),(6,0x01,'Nike','Nike Pegasus Trail',134.99),(7,0x01,'Nike','Nike Waffle Trainer',99.99),(8,0x01,'Nike','Nike Blazer Mid',114.99),(9,0x01,'Nike','Nike Air Huarache',124.99),(10,0x01,'Nike','Nike Free Run 5.0',99.99),(11,0x01,'Adidas','Adidas Ultraboost',139.99),(12,0x01,'Adidas','Adidas Superstar',99.99),(13,0x01,'Adidas','Adidas Stan Smith',89.99),(14,0x01,'Adidas','Adidas NMD R1',129.99),(15,0x01,'Adidas','Adidas Forum Low',109.99),(16,0x01,'Adidas','Adidas Gazelle',94.99),(17,0x01,'Adidas','Adidas ZX 2K Boost',124.99),(18,0x01,'Adidas','Adidas OZWEEGO',114.99),(19,0x01,'Adidas','Adidas Continental 80',84.99),(20,0x01,'Adidas','Adidas 4DFWD',159.99),(21,0x01,'Puma','Puma Suede Classic',89.99),(22,0x01,'Puma','Puma RS-X',119.99),(23,0x01,'Puma','Puma Cali',99.99),(24,0x01,'Puma','Puma Future Rider',109.99),(25,0x01,'Puma','Puma Mirage Sport',124.99),(26,0x01,'Puma','Puma Carina Street',94.99),(27,0x01,'Puma','Puma X-Ray 2',114.99),(28,0x01,'Puma','Puma R78',89.99),(29,0x01,'Puma','Puma Smash 3.0',79.99),(30,0x01,'Puma','Puma Mayze Stack',129.99),(31,0x01,'Reebok','Reebok Classic Leather',94.99),(32,0x01,'Reebok','Reebok Club C 85',99.99),(33,0x01,'Reebok','Reebok Zig Dynamica',114.99),(34,0x01,'Reebok','Reebok Nano X3',129.99),(35,0x01,'Reebok','Reebok Floatride',124.99),(36,0x01,'Reebok','Reebok Royal Glide',89.99),(37,0x01,'Reebok','Reebok DMX Trail',139.99),(38,0x01,'Reebok','Reebok Energen Lite',104.99),(39,0x01,'Reebok','Reebok Classic Nylon',84.99),(40,0x01,'Reebok','Reebok Zig Kinetica 2',149.99),(41,0x01,'New Balance','New Balance 574',99.99),(42,0x01,'New Balance','New Balance 327',119.99),(43,0x01,'New Balance','New Balance 550',124.99),(44,0x01,'New Balance','New Balance Fresh Foam',129.99),(45,0x01,'New Balance','New Balance 997H',114.99),(46,0x01,'New Balance','New Balance 1080v12',139.99),(47,0x01,'New Balance','New Balance 237',109.99),(48,0x01,'New Balance','New Balance XC-72',129.99),(49,0x01,'New Balance','New Balance 2002R',134.99),(50,0x01,'New Balance','New Balance 990v6',159.99),(51,0x01,'Adidas','Adida falsa de prueba',119.99),(52,0x00,'Nike','Deportiva Prueba',111.12),(53,0x01,'Nike','Nike',12.00),(54,0x00,'Nike','Deportiva nueva prueba',100.00);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `email` varchar(150) NOT NULL,
  `fecha_alta` datetime(6) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,0x01,'jefe@urbansneakers.com','2026-07-30 13:39:38.308366','Jefe UrbanSneakers','$2a$10$lEqT60i8D.7li8jl6ystP.MIcHSd2jipbXHunch/IV3yauZAqZYqm','ADMIN'),(2,0x01,'pepe@gmail.com','2026-07-30 13:44:00.076145','Pepe','$2a$10$L5xOMp5ziTIW6gg146dtq.7ojHaZ4hkyUPZX16bDPri3ImXiWmgJa','JEFE'),(3,0x01,'pacopepe@gmail.com','2026-07-30 13:45:10.970291','Paco Pepe','$2a$10$O4THt1n2NCXZ2FKT47qGYOaUTNFgfmcoO77qP9BjQ3mJ12rDM09/u','TRABAJADOR'),(4,0x01,'luis@gmail.com','2026-07-30 13:48:01.170483','Luis','$2a$10$chhxRXdKJOgo.nnSmD4c7e/K17njsIcFW.VaOpemkOGIP/KZhBky2','CLIENTE'),(5,0x01,'pepito@gmail.com','2026-08-11 02:58:18.021175','Pepe','$2a$10$PzTfMFMysjLt.pxVd9Q2ZOG/sZlx6IXD7eIxfcJQPVQbsHkrKJbtK','CLIENTE'),(6,0x01,'laura@gmail.com','2026-08-11 18:28:55.595374','Laura Parla','$2a$10$7hgy/MBnhR8tMizggMHxq.3X.Ac.fYgDKEAeajzYEG9XC8/tJ5Nju','JEFE'),(7,0x01,'raul@gmail.com','2026-08-11 18:31:49.371507','Raúl','$2a$10$EwlnkAeewTvQOwSFS9VTLukIr3jmUUQsnsXGWxI5sV4OqyF8xO95i','TRABAJADOR'),(8,0x01,'alex@gmail.com','2026-08-11 18:36:15.149698','Alex','$2a$10$1mMLGnPjvcuDNaEex8cNh.170P2VdCYIaJvkRDkrwZ87Jr8NJb5G.','TRABAJADOR'),(9,0x01,'arturo@gmail.com','2026-08-12 17:18:28.138980','Arturo','$2a$10$6FG1n62AMxBawIs2ojdQweWLMVvStmgx7ESyc5C/lDnsfB7Am0Bf.','CLIENTE'),(10,0x01,'manu@gmail.com','2026-08-12 19:21:07.405485','Manuel','$2a$10$/WUpHVUj07fRrf0bFStjyOXW16yqUbSEzDEy7hjRvTgA2EQUlbWDu','CLIENTE'),(11,0x01,'berta@gmail.com','2026-08-13 19:19:23.668075','Berta','$2a$10$6MDZEv.hq8jsWxXsvasBau9XV2gBkUGBy4OOYukQ9ZtaH6y73khSO','CLIENTE'),(12,0x01,'lolo@gmail.com','2026-08-13 19:23:51.551140','Lolito','$2a$10$FAf62bHjyiVFUR47sMUyiuopsjmK4.Bw4o7zwSfdDRKaLA3gRKhw2','JEFE');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'tienda_deportivas'
--

--
-- Dumping routines for database 'tienda_deportivas'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-19 15:25:52
