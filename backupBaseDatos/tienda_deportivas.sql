-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-09-2026 a las 17:42:57
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `tienda_deportivas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carritos`
--

CREATE TABLE `carritos` (
  `id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `carritos`
--

INSERT INTO `carritos` (`id`, `usuario_id`) VALUES
(6, 4),
(7, 5),
(8, 9),
(10, 10),
(9, 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carrito_items`
--

CREATE TABLE `carrito_items` (
  `id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `talla` int(11) NOT NULL,
  `carrito_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL
) ;

--
-- Volcado de datos para la tabla `carrito_items`
--

INSERT INTO `carrito_items` (`id`, `cantidad`, `color`, `talla`, `carrito_id`, `producto_id`) VALUES
(100, 1, 'Blanco', 36, 6, 13),
(101, 1, 'Blanco', 37, 6, 12),
(110, 1, 'Blanco', 37, 9, 12),
(111, 1, 'Blanco', 36, 9, 13),
(112, 1, 'Blanco', 38, 9, 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `facturas`
--

CREATE TABLE `facturas` (
  `id` bigint(20) NOT NULL,
  `numero_factura` varchar(30) NOT NULL,
  `fecha_emision` datetime NOT NULL,
  `metodo_pago` varchar(30) NOT NULL,
  `pedido_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `facturas`
--

INSERT INTO `facturas` (`id`, `numero_factura`, `fecha_emision`, `metodo_pago`, `pedido_id`) VALUES
(16, 'FAC-2026-000040', '2026-09-10 05:41:13', 'TARJETA', 40),
(17, 'FAC-2026-000041', '2026-09-10 18:24:33', 'TARJETA', 41),
(18, 'FAC-2026-000042', '2026-09-10 18:37:00', 'TARJETA', 42),
(19, 'FAC-2026-000043', '2026-09-10 19:28:46', 'TARJETA', 43),
(20, 'FAC-2026-000044', '2026-09-10 20:23:54', 'TARJETA', 44),
(21, 'FAC-2026-000046', '2026-09-11 02:57:37', 'TARJETA', 46),
(22, 'FAC-2026-000050', '2026-09-11 03:23:42', 'TARJETA', 50),
(23, 'FAC-2026-000051', '2026-09-11 03:31:21', 'TARJETA', 51),
(24, 'FAC-2026-000052', '2026-09-11 17:19:48', 'TARJETA', 52),
(25, 'FAC-2026-000053', '2026-09-11 17:23:38', 'TARJETA', 53),
(26, 'FAC-2026-000054', '2026-09-11 17:32:33', 'TARJETA', 54);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_pedidos`
--

CREATE TABLE `historial_pedidos` (
  `id` bigint(20) NOT NULL,
  `estado_anterior` enum('PENDIENTE','PREPARANDO','ENVIADO','ENTREGADO','DEVUELTO_A_TIENDA') DEFAULT NULL,
  `estado_nuevo` enum('PENDIENTE','PREPARANDO','ENVIADO','ENTREGADO','DEVUELTO_A_TIENDA') NOT NULL,
  `fecha_cambio` datetime(6) NOT NULL,
  `pedido_id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `tipo_actor` enum('USUARIO','SISTEMA') NOT NULL,
  `origen` enum('PANEL_ADMIN','STRIPE') NOT NULL
) ;

--
-- Volcado de datos para la tabla `historial_pedidos`
--

INSERT INTO `historial_pedidos` (`id`, `estado_anterior`, `estado_nuevo`, `fecha_cambio`, `pedido_id`, `usuario_id`, `tipo_actor`, `origen`) VALUES
(41, 'PREPARANDO', 'ENVIADO', '2026-09-10 05:42:27.590237', 40, 1, 'USUARIO', 'PANEL_ADMIN'),
(42, 'ENVIADO', 'ENTREGADO', '2026-09-10 05:42:32.772482', 40, 1, 'USUARIO', 'PANEL_ADMIN'),
(43, 'PREPARANDO', 'ENVIADO', '2026-09-10 18:25:38.313585', 41, 1, 'USUARIO', 'PANEL_ADMIN'),
(44, 'PENDIENTE', 'PREPARANDO', '2026-09-10 18:37:00.048243', 42, NULL, 'SISTEMA', 'STRIPE'),
(45, 'PREPARANDO', 'ENVIADO', '2026-09-10 18:39:50.229423', 42, 1, 'USUARIO', 'PANEL_ADMIN'),
(46, 'PENDIENTE', 'PREPARANDO', '2026-09-10 19:28:46.310148', 43, NULL, 'SISTEMA', 'STRIPE'),
(47, 'PREPARANDO', 'ENVIADO', '2026-09-10 19:29:52.655935', 43, 1, 'USUARIO', 'PANEL_ADMIN'),
(48, 'ENVIADO', 'DEVUELTO_A_TIENDA', '2026-09-10 19:30:27.430317', 43, 1, 'USUARIO', 'PANEL_ADMIN'),
(49, 'DEVUELTO_A_TIENDA', 'PREPARANDO', '2026-09-10 19:54:30.898987', 43, 1, 'USUARIO', 'PANEL_ADMIN'),
(50, 'PREPARANDO', 'ENVIADO', '2026-09-10 19:54:34.078124', 43, 1, 'USUARIO', 'PANEL_ADMIN'),
(51, 'ENVIADO', 'ENTREGADO', '2026-09-10 19:54:38.398959', 43, 1, 'USUARIO', 'PANEL_ADMIN'),
(52, 'ENVIADO', 'ENTREGADO', '2026-09-10 20:19:52.729162', 42, 1, 'USUARIO', 'PANEL_ADMIN'),
(53, 'PENDIENTE', 'PREPARANDO', '2026-09-10 20:23:54.259008', 44, NULL, 'SISTEMA', 'STRIPE'),
(57, 'PENDIENTE', 'PREPARANDO', '2026-09-11 02:57:37.080094', 46, NULL, 'SISTEMA', 'STRIPE'),
(58, 'PENDIENTE', 'PREPARANDO', '2026-09-11 03:23:42.923222', 50, NULL, 'SISTEMA', 'STRIPE'),
(59, 'PREPARANDO', 'ENVIADO', '2026-09-11 03:29:21.098397', 50, 1, 'USUARIO', 'PANEL_ADMIN'),
(60, 'PENDIENTE', 'PREPARANDO', '2026-09-11 03:31:21.836958', 51, NULL, 'SISTEMA', 'STRIPE'),
(61, 'PENDIENTE', 'PREPARANDO', '2026-09-11 17:19:48.585716', 52, NULL, 'SISTEMA', 'STRIPE'),
(62, 'PENDIENTE', 'PREPARANDO', '2026-09-11 17:23:38.033282', 53, NULL, 'SISTEMA', 'STRIPE'),
(63, 'PENDIENTE', 'PREPARANDO', '2026-09-11 17:32:33.065796', 54, NULL, 'SISTEMA', 'STRIPE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id` bigint(20) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `ciudad` varchar(255) NOT NULL,
  `cp` varchar(255) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `envio` decimal(10,2) NOT NULL,
  `estado` enum('PENDIENTE','PREPARANDO','ENVIADO','ENTREGADO','DEVUELTO_A_TIENDA') NOT NULL,
  `fecha_pedido` datetime(6) NOT NULL,
  `id_pedido` varchar(40) NOT NULL,
  `iva` decimal(10,2) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `pais` varchar(255) NOT NULL,
  `provincia` varchar(255) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `telefono` varchar(255) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `estado_pago` enum('PENDIENTE','PAGADO','FALLIDO','CANCELADO') NOT NULL,
  `stripe_session_id` varchar(255) DEFAULT NULL,
  `version` bigint(20) NOT NULL DEFAULT 0,
  `idempotency_key` varchar(36) NOT NULL
) ;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id`, `apellidos`, `ciudad`, `cp`, `direccion`, `email`, `envio`, `estado`, `fecha_pedido`, `id_pedido`, `iva`, `nombre`, `pais`, `provincia`, `subtotal`, `telefono`, `total`, `usuario_id`, `estado_pago`, `stripe_session_id`, `version`, `idempotency_key`) VALUES
(40, 'García García', 'Madrid', '22222', 'calle pino número 12', 'luisito@gmail.com', 0.00, 'ENTREGADO', '2026-09-10 05:40:27.902136', 'PED-3C6C2212-1730-4F7A-A475-78569C98446B', 42.00, 'Luis', 'España', 'Madrid', 199.99, '111222111', 241.99, 4, 'PAGADO', 'cs_test_b1lsBPkrN9u3iiKY9BLdYMESeVkOgrXRQjNtNxtajRDHLMvcTrTiN55dii', 4, 'f08e99cb-83c8-44b0-960a-101c9f3439a4'),
(41, 'Herrera Herrara', 'Madrid', '11111', 'Calle el faro, numero 13', 'pepe@gmail.com', 0.00, 'ENVIADO', '2026-09-10 18:23:53.229474', 'PED-4885F665-AD85-4DC9-A3E0-CAEF524CAA71', 49.35, 'Pepe', 'España', 'Madrid', 234.98, '111222111', 284.33, 5, 'PAGADO', 'cs_test_b1PzSdqnsIq0xKXBcR0TtzzEgjYNZyGZ2wu3JBGgE2DkZggFnuE2yPDrcd', 3, '6e436c90-97c7-49eb-a65d-d92ae8eefcfb'),
(42, 'García García', 'Madrid', '11111', 'calle princesa n 12', 'arturo@gmail.com', 4.99, 'ENTREGADO', '2026-09-10 18:36:35.781347', 'PED-E8FC2683-0889-4AE6-9F8A-EFEFEF8C4672', 18.90, 'Arturo', 'España', 'Madrid', 89.99, '111222111', 113.88, 9, 'PAGADO', 'cs_test_b1zXVPoVnyuvHmGozl5TGKVX9YxIwGIoZk1ZUfcSVwXFYtQcZiblVcdS7U', 4, '885151ae-5fc4-435a-bc0b-33b9e398015e'),
(43, 'García García', 'Madrid', '33222', 'Calle viento', 'berta@gmail.com', 0.00, 'ENTREGADO', '2026-09-10 19:28:26.653058', 'PED-19736BDD-6BAB-4A67-BFE7-73564CF65A43', 55.65, 'Berta', 'España', 'Madrid', 264.98, '111222111', 320.63, 11, 'PAGADO', 'cs_test_b1uwTdBP5yEp6adfQMhWpn0bjIpvSyRJFVxvV3IZxHthhCHXVYwX0KyZ5L', 7, '60fb626c-6ab9-413f-9886-067b9246bc4f'),
(44, 'Monserrrat Caballero', 'Madrid', '44111', 'Calle Ave María', 'manu@gmail.com', 0.00, 'PREPARANDO', '2026-09-10 20:23:12.090172', 'PED-225944FE-B02D-4CF9-8B5D-6E34679FB802', 40.95, 'Manu', 'España', 'Madrid', 194.98, '111222111', 235.93, 10, 'PAGADO', 'cs_test_b1WPUMJpd8YCv4LfMQ6zg3XG7BAkSlAPfLT0I2xICxHj1IXeoRAMVQOiMQ', 2, '0274f778-7572-4f16-980c-314c679d9883'),
(45, 'Escudero Polo', 'Madrid', '11111', 'calle pino número 12', 'luis@gmail.com', 0.00, 'PENDIENTE', '2026-09-10 20:59:22.716156', 'PED-F9B2284F-B405-40BF-9DF6-412796FC0307', 39.90, 'Luis', 'España', 'Madrid', 189.98, '111222111', 229.88, 4, 'PENDIENTE', 'cs_test_b1Ck9K6g5ao7WI0jr8d6kTGnG4ogPHBjZkZKIy2cIxItcMCa9TdZYOzUde', 1, 'b429bf81-68aa-4aaf-96fd-61f349ab50b2'),
(46, 'Herrera Herrara', 'Madrid', '11223', 'calle princesa n 12', 'berta@gmail.com', 0.00, 'PREPARANDO', '2026-09-11 02:57:11.960072', 'PED-2D0E5BAB-729C-43A5-A0ED-CD9C3118C6D5', 65.10, 'Berta', 'España', 'Madrid', 309.98, '111222111', 375.08, 11, 'PAGADO', 'cs_test_b11MuoH3hdwaNjlXh2OpPaTxMqRGLhQd0ITipc45wZz4E0MJvUQcFPitEg', 2, 'd06f5594-b4cc-49a1-b90d-a00fde5e971f'),
(47, 'García García', 'Madrid', '11111', 'Calle Ave María', 'berta@gmail.com', 0.00, 'PENDIENTE', '2026-09-11 03:01:47.038554', 'PED-06ADC235-8A2F-4854-9603-123913034681', 80.84, 'berta', 'España', 'Madrid', 384.97, '111222111', 465.81, 11, 'PENDIENTE', 'cs_test_b1cqfgl6ZElPSE53qTE7DIQNeRuZ2oG4AkWkrrRePTCAUbCqusZHM1VsBT', 1, '8294b749-be46-4383-ad0f-68d0a248c7eb'),
(48, 'García García', 'Madrid', '11111', 'Calle el faro, número 13', 'berta@gmail.com', 0.00, 'PENDIENTE', '2026-09-11 03:11:01.420159', 'PED-C8B4C6F3-61ED-4D19-9212-C2E3402D10E7', 61.94, 'Berta', 'España', 'Madrid', 294.97, '111222111', 356.91, 11, 'PENDIENTE', 'cs_test_b1JJ5iffJiBqhH0E5ilYvQtgvQfTK1Mx9TA8MvPRSNeVEx4NaoLvL0E7NL', 1, 'eaa23910-ab23-4056-969c-d158dc9ba8fa'),
(49, 'García García', 'Madrid', '11111', 'Calle el faro, número 13', 'berta@gmail.com', 0.00, 'PENDIENTE', '2026-09-11 03:17:08.298257', 'PED-6BA6D6BA-64B3-4877-8263-9FC52EA4EAF8', 69.29, 'Berta', 'España', 'Madrid', 329.97, '111222111', 399.26, 11, 'PENDIENTE', 'cs_test_b1dXtBQ85I9cq6zlRUOuPCaOArZwXmbMqqyFC9ZkMKBkiUGL85jh29CFlb', 1, 'a5a99a27-14b8-41a9-b280-8c228c944f6e'),
(50, 'Herrera Herrara', 'Madrid', '11111', 'calle real número 1', 'manu@gmail.com', 0.00, 'ENVIADO', '2026-09-11 03:23:16.378336', 'PED-6A738811-24D8-497E-86A1-4A5DD85436BB', 65.10, 'Manu', 'España', 'Madrid', 309.98, '111222111', 375.08, 10, 'PAGADO', 'cs_test_b1dr0Z7LEn4kzf9NPBM38fyFLOdjs5LgQ5XCVmxV1GYohjlCAWRc90x1qb', 3, '39cfca82-f5c8-4036-98bd-31e66156b838'),
(51, 'García García', 'Madrid', '11111', 'Calle la oliva, número 13', 'arturo@gmail.com', 0.00, 'PREPARANDO', '2026-09-11 03:31:02.478490', 'PED-B407298B-2E90-4FDA-8A3F-AD6C79AE0204', 47.25, 'Arturo', 'España', 'Madrid', 224.98, '111222111', 272.23, 9, 'PAGADO', 'cs_test_b1HbCR0a1zROIywbVDGrKlZqUMZ4Cgvi6KgVAYuKuqPR1bCYvo79EftkGx', 2, '8454bc74-aa8b-48d4-9738-2fc0ca37a6da'),
(52, 'García García', 'Madrid', '22222', 'calle princesa n 12', 'pepito@gmail.com', 0.00, 'PREPARANDO', '2026-09-11 17:19:19.909243', 'PED-73C95867-FC98-46D5-8E8C-AB3FF94A1B1B', 31.50, 'Pepe', 'España', 'Madrid', 149.99, '111222111', 181.49, 5, 'PAGADO', 'cs_test_b18O0wUmKvnZMlRzNS01mPy0UD6BcwokYF17vVBXLkOlK4NRCP8IVwDNdQ', 2, '90a7f225-8fe8-471a-afd0-6e2f344e7e87'),
(53, 'García García', 'Toledo', '24322', 'Calle Barco, número 2', 'García@gmail.com', 0.00, 'PREPARANDO', '2026-09-11 17:23:00.669993', 'PED-50947133-A73D-478A-B7D8-E09D186C5191', 31.50, 'Luis', 'España', 'Toledo', 149.99, '111222111', 181.49, 5, 'PAGADO', 'cs_test_b1giK1pndRQyruo41pF4XDnstEo0Vdo9lZwVclE6CJcAvZx0muluqEq1SF', 2, '39d3920c-6cbb-44ec-9ba7-5a41a8aa0e59'),
(54, 'García García', 'Madrid', '11111', 'Calle Barco, número 2', 'arturo@gmail.com', 0.00, 'PREPARANDO', '2026-09-11 17:32:09.326206', 'PED-27A08E73-EF16-4AC8-974A-7AC0386D45B5', 109.19, 'Arturo', 'España', 'Madrid', 519.96, '111222111', 629.15, 9, 'PAGADO', 'cs_test_b1R9GYRfdApSEoTxbNWEpNnnDF2S9cf74gif1oK0Dx0fiSnGhMrzY0CZMI', 2, '595db91d-9a4f-48bd-a98d-7347d50073e2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido_items`
--

CREATE TABLE `pedido_items` (
  `id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `nombre_producto` varchar(255) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `subtotal_linea` decimal(10,2) NOT NULL,
  `talla` varchar(255) DEFAULT NULL,
  `pedido_id` bigint(20) NOT NULL
) ;

--
-- Volcado de datos para la tabla `pedido_items`
--

INSERT INTO `pedido_items` (`id`, `cantidad`, `color`, `nombre_producto`, `precio_unitario`, `producto_id`, `subtotal_linea`, `talla`, `pedido_id`) VALUES
(71, 1, 'Blanco', 'Nike Air 90 Mix', 199.99, 1, 199.99, '38', 40),
(72, 1, 'Verde', 'Nike Pegasus Trail', 134.99, 6, 134.99, '38', 41),
(73, 1, 'Amarillo', 'Nike Waffle Trainer', 99.99, 7, 99.99, '37', 41),
(74, 1, 'Rosa', 'Reebok Royal Glide', 89.99, 36, 89.99, '36', 42),
(75, 1, 'Negro', 'Nike Zoom Fly', 129.99, 3, 129.99, '39', 43),
(76, 1, 'Verde', 'Nike Pegasus Trail', 134.99, 6, 134.99, '38', 43),
(77, 1, 'Blanco', 'Reebok Classic Leather', 94.99, 31, 94.99, '37', 44),
(78, 1, 'Blanco', 'Reebok Club C 85', 99.99, 32, 99.99, '36', 44),
(79, 1, 'Blanco', 'Adidas Stan Smith', 89.99, 13, 89.99, '36', 45),
(80, 1, 'Blanco', 'Adidas Superstar', 99.99, 12, 99.99, '37', 45),
(81, 1, 'Blanco', 'Nike Air 90 Mix', 199.99, 1, 199.99, '38', 46),
(82, 1, 'Blanco', 'Nike Air Force 1', 109.99, 2, 109.99, '37', 46),
(83, 1, 'Rojo', 'Nike Air Jordan 1', 149.99, 5, 149.99, '40', 47),
(84, 1, 'Verde', 'Nike Pegasus Trail', 134.99, 6, 134.99, '38', 47),
(85, 1, 'Amarillo', 'Nike Waffle Trainer', 99.99, 7, 99.99, '37', 47),
(86, 1, 'Negro', 'Puma Suede Classic', 89.99, 21, 89.99, '37', 48),
(87, 1, 'Verde', 'Puma X-Ray 2', 114.99, 27, 114.99, '39', 48),
(88, 1, 'Gris', 'Puma R78', 89.99, 28, 89.99, '37', 48),
(89, 1, 'Blanco', 'Adidas Superstar', 99.99, 12, 99.99, '37', 49),
(90, 1, 'Blanco', 'Adidas Stan Smith', 89.99, 13, 89.99, '36', 49),
(91, 1, 'Blanco', 'Adidas Ultraboost', 139.99, 11, 139.99, '38', 49),
(92, 1, 'Blanco', 'Nike Air 90 Mix', 199.99, 1, 199.99, '38', 50),
(93, 1, 'Blanco', 'Nike Air Force 1', 109.99, 2, 109.99, '37', 50),
(94, 1, 'Azul', 'Puma Future Rider', 109.99, 24, 109.99, '38', 51),
(95, 1, 'Verde', 'Puma X-Ray 2', 114.99, 27, 114.99, '39', 51),
(96, 1, 'Rojo', 'Nike Air Jordan 1', 149.99, 5, 149.99, '40', 52),
(97, 1, 'Rojo', 'Nike Air Jordan 1', 149.99, 5, 149.99, '40', 53),
(98, 1, 'Blanco', 'Nike Air 90 Mix', 199.99, 1, 199.99, '38', 54),
(99, 1, 'Blanco', 'Nike Air Force 1', 109.99, 2, 109.99, '37', 54),
(100, 1, 'Negro', 'Puma Suede Classic', 89.99, 21, 89.99, '37', 54),
(101, 1, 'Blanco', 'Puma RS-X', 119.99, 22, 119.99, '38', 54);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) NOT NULL,
  `marca` varchar(100) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `precio` decimal(10,2) NOT NULL
) ;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `activo`, `marca`, `nombre`, `precio`) VALUES
(1, b'1', 'Nike', 'Nike Air 90 Mix', 199.99),
(2, b'1', 'Nike', 'Nike Air Force 1', 109.99),
(3, b'1', 'Nike', 'Nike Zoom Fly', 129.99),
(4, b'1', 'Nike', 'Nike Revolution 6', 89.99),
(5, b'1', 'Nike', 'Nike Air Jordan 1', 149.99),
(6, b'1', 'Nike', 'Nike Pegasus Trail', 134.99),
(7, b'1', 'Nike', 'Nike Waffle Trainer', 99.99),
(8, b'1', 'Nike', 'Nike Blazer Mid', 114.99),
(9, b'1', 'Nike', 'Nike Air Huarache', 124.99),
(10, b'1', 'Nike', 'Nike Free Run 5.0', 99.99),
(11, b'1', 'Adidas', 'Adidas Ultraboost', 139.99),
(12, b'1', 'Adidas', 'Adidas Superstar', 99.99),
(13, b'1', 'Adidas', 'Adidas Stan Smith', 89.99),
(14, b'1', 'Adidas', 'Adidas NMD R1', 129.99),
(15, b'1', 'Adidas', 'Adidas Forum Low', 109.99),
(16, b'1', 'Adidas', 'Adidas Gazelle', 94.99),
(17, b'1', 'Adidas', 'Adidas ZX 2K Boost', 124.99),
(18, b'1', 'Adidas', 'Adidas OZWEEGO', 114.99),
(19, b'1', 'Adidas', 'Adidas Continental 80', 84.99),
(20, b'1', 'Adidas', 'Adidas 4DFWD', 159.99),
(21, b'1', 'Puma', 'Puma Suede Classic', 89.99),
(22, b'1', 'Puma', 'Puma RS-X', 119.99),
(23, b'1', 'Puma', 'Puma Cali', 99.99),
(24, b'1', 'Puma', 'Puma Future Rider', 109.99),
(25, b'1', 'Puma', 'Puma Mirage Sport', 124.99),
(26, b'1', 'Puma', 'Puma Carina Street', 94.99),
(27, b'1', 'Puma', 'Puma X-Ray 2', 114.99),
(28, b'1', 'Puma', 'Puma R78', 89.99),
(29, b'1', 'Puma', 'Puma Smash 3.0', 79.99),
(30, b'1', 'Puma', 'Puma Mayze Stack', 129.99),
(31, b'1', 'Reebok', 'Reebok Classic Leather', 94.99),
(32, b'1', 'Reebok', 'Reebok Club C 85', 99.99),
(33, b'1', 'Reebok', 'Reebok Zig Dynamica', 114.99),
(34, b'1', 'Reebok', 'Reebok Nano X3', 129.99),
(35, b'1', 'Reebok', 'Reebok Floatride', 124.99),
(36, b'1', 'Reebok', 'Reebok Royal Glide', 89.99),
(37, b'1', 'Reebok', 'Reebok DMX Trail', 139.99),
(38, b'1', 'Reebok', 'Reebok Energen Lite', 104.99),
(39, b'1', 'Reebok', 'Reebok Classic Nylon', 84.99),
(40, b'1', 'Reebok', 'Reebok Zig Kinetica 2', 149.99),
(41, b'1', 'New Balance', 'New Balance 574', 99.99),
(42, b'1', 'New Balance', 'New Balance 327', 119.99),
(43, b'1', 'New Balance', 'New Balance 550', 124.99),
(44, b'1', 'New Balance', 'New Balance Fresh Foam', 129.99),
(45, b'1', 'New Balance', 'New Balance 997H', 114.99),
(46, b'1', 'New Balance', 'New Balance 1080v12', 139.99),
(47, b'1', 'New Balance', 'New Balance 237', 109.99),
(48, b'1', 'New Balance', 'New Balance XC-72', 129.99),
(49, b'1', 'New Balance', 'New Balance 2002R', 134.99),
(50, b'1', 'New Balance', 'New Balance 990v6', 159.99),
(51, b'1', 'Adidas', 'Adida falsa de prueba', 119.99),
(52, b'0', 'Nike', 'Deportiva Prueba', 111.12),
(53, b'1', 'Nike', 'Nike', 12.00),
(54, b'0', 'Nike', 'Deportiva nueva prueba', 100.00),
(55, b'0', 'Nike', 'Nike prueba seguridad', 199.99);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_colores`
--

CREATE TABLE `producto_colores` (
  `producto_id` bigint(20) NOT NULL,
  `color` varchar(50) NOT NULL,
  `orden_color` int(11) NOT NULL CHECK (`orden_color` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `producto_colores`
--

INSERT INTO `producto_colores` (`producto_id`, `color`, `orden_color`) VALUES
(1, 'Blanco', 0),
(1, 'Negro', 1),
(1, 'Rojo', 2),
(2, 'Azul', 1),
(2, 'Blanco', 0),
(2, 'Gris', 2),
(3, 'Negro', 0),
(3, 'Rojo', 1),
(3, 'Verde', 2),
(4, 'Blanco', 1),
(4, 'Negro', 0),
(4, 'Rosa', 2),
(5, 'Blanco', 1),
(5, 'Negro', 2),
(5, 'Rojo', 0),
(6, 'Gris', 2),
(6, 'Negro', 1),
(6, 'Verde', 0),
(7, 'Amarillo', 0),
(7, 'Blanco', 1),
(7, 'Negro', 2),
(8, 'Blanco', 0),
(8, 'Negro', 1),
(8, 'Verde', 2),
(9, 'Azul', 0),
(9, 'Gris', 2),
(9, 'Negro', 1),
(10, 'Blanco', 2),
(10, 'Negro', 0),
(10, 'Rosa', 1),
(11, 'Azul', 2),
(11, 'Blanco', 0),
(11, 'Negro', 1),
(12, 'Blanco', 0),
(12, 'Dorado', 2),
(12, 'Negro', 1),
(13, 'Azul', 2),
(13, 'Blanco', 0),
(13, 'Verde', 1),
(14, 'Gris', 1),
(14, 'Negro', 0),
(14, 'Rojo', 2),
(15, 'Azul', 1),
(15, 'Blanco', 0),
(15, 'Rojo', 2),
(16, 'Azul', 1),
(16, 'Negro', 2),
(16, 'Verde', 0),
(17, 'Gris', 1),
(17, 'Naranja', 2),
(17, 'Negro', 0),
(18, 'Gris', 1),
(18, 'Negro', 2),
(18, 'Verde', 0),
(19, 'Blanco', 0),
(19, 'Negro', 2),
(19, 'Rosa', 1),
(20, 'Blanco', 1),
(20, 'Gris', 2),
(20, 'Negro', 0),
(21, 'Azul', 2),
(21, 'Negro', 0),
(21, 'Rojo', 1),
(22, 'Blanco', 0),
(22, 'Negro', 1),
(22, 'Verde', 2),
(23, 'Blanco', 0),
(23, 'Negro', 2),
(23, 'Rosa', 1),
(24, 'Azul', 0),
(24, 'Gris', 1),
(24, 'Rojo', 2),
(25, 'Gris', 2),
(25, 'Naranja', 1),
(25, 'Negro', 0),
(26, 'Blanco', 0),
(26, 'Negro', 2),
(26, 'Rosa', 1),
(27, 'Azul', 1),
(27, 'Negro', 2),
(27, 'Verde', 0),
(28, 'Azul', 2),
(28, 'Gris', 0),
(28, 'Rojo', 1),
(29, 'Azul', 2),
(29, 'Blanco', 0),
(29, 'Negro', 1),
(30, 'Gris', 2),
(30, 'Negro', 0),
(30, 'Rosa', 1),
(31, 'Beige', 1),
(31, 'Blanco', 0),
(31, 'Negro', 2),
(32, 'Blanco', 0),
(32, 'Gris', 2),
(32, 'Verde', 1),
(33, 'Gris', 2),
(33, 'Naranja', 1),
(33, 'Negro', 0),
(34, 'Gris', 0),
(34, 'Negro', 2),
(34, 'Verde', 1),
(35, 'Azul', 0),
(35, 'Blanco', 2),
(35, 'Negro', 1),
(36, 'Blanco', 0),
(36, 'Negro', 2),
(36, 'Rosa', 1),
(37, 'Gris', 2),
(37, 'Negro', 0),
(37, 'Verde', 1),
(38, 'Azul', 1),
(38, 'Blanco', 0),
(38, 'Negro', 2),
(39, 'Azul', 0),
(39, 'Gris', 1),
(39, 'Negro', 2),
(40, 'Gris', 2),
(40, 'Negro', 0),
(40, 'Rojo', 1),
(41, 'Azul', 2),
(41, 'Gris', 0),
(41, 'Negro', 1),
(42, 'Beige', 0),
(42, 'Blanco', 2),
(42, 'Verde', 1),
(43, 'Azul', 2),
(43, 'Blanco', 0),
(43, 'Rojo', 1),
(44, 'Azul', 1),
(44, 'Gris', 0),
(44, 'Negro', 2),
(45, 'Blanco', 2),
(45, 'Gris', 1),
(45, 'Verde', 0),
(46, 'Azul', 1),
(46, 'Blanco', 2),
(46, 'Negro', 0),
(47, 'Gris', 0),
(47, 'Negro', 1),
(47, 'Rojo', 2),
(48, 'Azul', 0),
(48, 'Beige', 1),
(48, 'Negro', 2),
(49, 'Gris', 0),
(49, 'Negro', 1),
(49, 'Verde', 2),
(50, 'Blanco', 0),
(50, 'Gris', 1),
(50, 'Negro', 2),
(51, 'azul', 2),
(51, 'blanco', 0),
(51, 'negro', 1),
(52, 'Azul', 0),
(52, 'Rojo', 1),
(52, 'Verde', 2),
(53, 'Rojo', 0),
(54, 'Azul', 2),
(54, 'Rojo', 0),
(54, 'Verde', 1),
(55, 'amarillo', 1),
(55, 'Blanco', 0),
(55, 'naranja', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_tallas`
--

CREATE TABLE `producto_tallas` (
  `producto_id` bigint(20) NOT NULL,
  `talla` int(11) NOT NULL,
  `orden_talla` int(11) NOT NULL CHECK (`orden_talla` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `producto_tallas`
--

INSERT INTO `producto_tallas` (`producto_id`, `talla`, `orden_talla`) VALUES
(1, 38, 0),
(1, 39, 1),
(1, 40, 2),
(1, 41, 3),
(1, 42, 4),
(1, 43, 5),
(2, 37, 0),
(2, 38, 1),
(2, 39, 2),
(2, 40, 3),
(2, 41, 4),
(2, 42, 5),
(3, 39, 0),
(3, 40, 1),
(3, 41, 2),
(3, 42, 3),
(3, 43, 4),
(3, 44, 5),
(4, 36, 0),
(4, 37, 1),
(4, 38, 2),
(4, 39, 3),
(4, 40, 4),
(4, 41, 5),
(5, 40, 0),
(5, 41, 1),
(5, 42, 2),
(5, 43, 3),
(5, 44, 4),
(6, 38, 0),
(6, 39, 1),
(6, 40, 2),
(6, 41, 3),
(6, 42, 4),
(6, 43, 5),
(7, 37, 0),
(7, 38, 1),
(7, 39, 2),
(7, 40, 3),
(7, 41, 4),
(7, 42, 5),
(8, 39, 0),
(8, 40, 1),
(8, 41, 2),
(8, 42, 3),
(8, 43, 4),
(9, 38, 0),
(9, 39, 1),
(9, 40, 2),
(9, 41, 3),
(9, 42, 4),
(10, 36, 0),
(10, 37, 1),
(10, 38, 2),
(10, 39, 3),
(10, 40, 4),
(11, 38, 0),
(11, 39, 1),
(11, 40, 2),
(11, 41, 3),
(11, 42, 4),
(12, 37, 0),
(12, 38, 1),
(12, 39, 2),
(12, 40, 3),
(12, 41, 4),
(13, 36, 0),
(13, 37, 1),
(13, 38, 2),
(13, 39, 3),
(13, 40, 4),
(14, 38, 0),
(14, 39, 1),
(14, 40, 2),
(14, 41, 3),
(14, 42, 4),
(15, 37, 0),
(15, 38, 1),
(15, 39, 2),
(15, 40, 3),
(15, 41, 4),
(16, 38, 0),
(16, 39, 1),
(16, 40, 2),
(16, 41, 3),
(16, 42, 4),
(17, 39, 0),
(17, 40, 1),
(17, 41, 2),
(17, 42, 3),
(17, 43, 4),
(18, 37, 0),
(18, 38, 1),
(18, 39, 2),
(18, 40, 3),
(18, 41, 4),
(19, 36, 0),
(19, 37, 1),
(19, 38, 2),
(19, 39, 3),
(19, 40, 4),
(20, 39, 0),
(20, 40, 1),
(20, 41, 2),
(20, 42, 3),
(20, 43, 4),
(21, 37, 0),
(21, 38, 1),
(21, 39, 2),
(21, 40, 3),
(21, 41, 4),
(22, 38, 0),
(22, 39, 1),
(22, 40, 2),
(22, 41, 3),
(22, 42, 4),
(23, 36, 0),
(23, 37, 1),
(23, 38, 2),
(23, 39, 3),
(23, 40, 4),
(24, 38, 0),
(24, 39, 1),
(24, 40, 2),
(24, 41, 3),
(24, 42, 4),
(25, 37, 0),
(25, 38, 1),
(25, 39, 2),
(25, 40, 3),
(25, 41, 4),
(26, 36, 0),
(26, 37, 1),
(26, 38, 2),
(26, 39, 3),
(26, 40, 4),
(27, 39, 0),
(27, 40, 1),
(27, 41, 2),
(27, 42, 3),
(27, 43, 4),
(28, 37, 0),
(28, 38, 1),
(28, 39, 2),
(28, 40, 3),
(28, 41, 4),
(29, 36, 0),
(29, 37, 1),
(29, 38, 2),
(29, 39, 3),
(29, 40, 4),
(30, 37, 0),
(30, 38, 1),
(30, 39, 2),
(30, 40, 3),
(30, 41, 4),
(31, 37, 0),
(31, 38, 1),
(31, 39, 2),
(31, 40, 3),
(31, 41, 4),
(32, 36, 0),
(32, 37, 1),
(32, 38, 2),
(32, 39, 3),
(32, 40, 4),
(33, 38, 0),
(33, 39, 1),
(33, 40, 2),
(33, 41, 3),
(33, 42, 4),
(34, 39, 0),
(34, 40, 1),
(34, 41, 2),
(34, 42, 3),
(34, 43, 4),
(35, 38, 0),
(35, 39, 1),
(35, 40, 2),
(35, 41, 3),
(35, 42, 4),
(36, 36, 0),
(36, 37, 1),
(36, 38, 2),
(36, 39, 3),
(36, 40, 4),
(37, 39, 0),
(37, 40, 1),
(37, 41, 2),
(37, 42, 3),
(37, 43, 4),
(38, 37, 0),
(38, 38, 1),
(38, 39, 2),
(38, 40, 3),
(38, 41, 4),
(39, 36, 0),
(39, 37, 1),
(39, 38, 2),
(39, 39, 3),
(39, 40, 4),
(40, 39, 0),
(40, 40, 1),
(40, 41, 2),
(40, 42, 3),
(40, 43, 4),
(41, 37, 0),
(41, 38, 1),
(41, 39, 2),
(41, 40, 3),
(41, 41, 4),
(42, 38, 0),
(42, 39, 1),
(42, 40, 2),
(42, 41, 3),
(42, 42, 4),
(43, 39, 0),
(43, 40, 1),
(43, 41, 2),
(43, 42, 3),
(43, 43, 4),
(44, 38, 0),
(44, 39, 1),
(44, 40, 2),
(44, 41, 3),
(44, 42, 4),
(45, 37, 0),
(45, 38, 1),
(45, 39, 2),
(45, 40, 3),
(45, 41, 4),
(46, 39, 0),
(46, 40, 1),
(46, 41, 2),
(46, 42, 3),
(46, 43, 4),
(47, 37, 0),
(47, 38, 1),
(47, 39, 2),
(47, 40, 3),
(47, 41, 4),
(48, 38, 0),
(48, 39, 1),
(48, 40, 2),
(48, 41, 3),
(48, 42, 4),
(49, 39, 0),
(49, 40, 1),
(49, 41, 2),
(49, 42, 3),
(49, 43, 4),
(50, 38, 0),
(50, 39, 1),
(50, 40, 2),
(50, 41, 3),
(50, 42, 4),
(51, 38, 0),
(51, 39, 1),
(51, 40, 2),
(51, 41, 3),
(51, 42, 4),
(52, 22, 0),
(52, 23, 1),
(52, 24, 2),
(53, 12, 0),
(54, 40, 0),
(54, 42, 1),
(54, 44, 2),
(55, 38, 0),
(55, 39, 1),
(55, 40, 2),
(55, 41, 3),
(55, 42, 4),
(55, 43, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) NOT NULL,
  `email` varchar(150) NOT NULL,
  `fecha_alta` datetime(6) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('ADMIN','JEFE','TRABAJADOR','CLIENTE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `activo`, `email`, `fecha_alta`, `nombre`, `password`, `rol`) VALUES
(1, b'1', 'jefe@urbansneakers.com', '2026-07-30 13:39:38.308366', 'Jefe UrbanSneakers', '$2a$10$JAYRf54XzgTpXZgafTsTF.2EKKP7Y6l9z76jJGRYuBzM16H..IE0W', 'ADMIN'),
(2, b'1', 'pepe@gmail.com', '2026-07-30 13:44:00.076145', 'Pepe', '$2a$10$RZ9V5/nCB24Bj3E27o.oguUMlz5aFqydiUZN3Tg9MOWAhgDkdHXJm', 'JEFE'),
(3, b'1', 'pacopepe@gmail.com', '2026-07-30 13:45:10.970291', 'Paco Pepe', '$2a$10$8hOxcnnIJkT8Pk7lX0LHEO8rjIoCVo/YqhqMiMZsFRXNG0x0mptcy', 'TRABAJADOR'),
(4, b'1', 'luisito@gmail.com', '2026-07-30 13:48:01.170483', 'Luis', '$2a$10$5E/mjghONZalXYTpvIupo.5Eejp.PpAlwi5BZoDhaBHTp2PdPAQVK', 'CLIENTE'),
(5, b'1', 'pepito@gmail.com', '2026-08-11 02:58:18.021175', 'Pepe', '$2a$10$PzTfMFMysjLt.pxVd9Q2ZOG/sZlx6IXD7eIxfcJQPVQbsHkrKJbtK', 'CLIENTE'),
(6, b'1', 'laura@gmail.com', '2026-08-11 18:28:55.595374', 'Laura Parla', '$2a$10$7hgy/MBnhR8tMizggMHxq.3X.Ac.fYgDKEAeajzYEG9XC8/tJ5Nju', 'JEFE'),
(7, b'1', 'raul@gmail.com', '2026-08-11 18:31:49.371507', 'Raúl', '$2a$10$EwlnkAeewTvQOwSFS9VTLukIr3jmUUQsnsXGWxI5sV4OqyF8xO95i', 'TRABAJADOR'),
(8, b'1', 'alex@gmail.com', '2026-08-11 18:36:15.149698', 'Alex', '$2a$10$1mMLGnPjvcuDNaEex8cNh.170P2VdCYIaJvkRDkrwZ87Jr8NJb5G.', 'TRABAJADOR'),
(9, b'1', 'arturo@gmail.com', '2026-08-12 17:18:28.138980', 'Arturo', '$2a$10$6FG1n62AMxBawIs2ojdQweWLMVvStmgx7ESyc5C/lDnsfB7Am0Bf.', 'CLIENTE'),
(10, b'1', 'manu@gmail.com', '2026-08-12 19:21:07.405485', 'Manuel', '$2a$10$/WUpHVUj07fRrf0bFStjyOXW16yqUbSEzDEy7hjRvTgA2EQUlbWDu', 'CLIENTE'),
(11, b'1', 'berta@gmail.com', '2026-08-13 19:19:23.668075', 'Berta', '$2a$10$6MDZEv.hq8jsWxXsvasBau9XV2gBkUGBy4OOYukQ9ZtaH6y73khSO', 'CLIENTE'),
(12, b'1', 'lolo@gmail.com', '2026-08-13 19:23:51.551140', 'Lolito', '$2a$10$FAf62bHjyiVFUR47sMUyiuopsjmK4.Bw4o7zwSfdDRKaLA3gRKhw2', 'JEFE'),
(13, b'1', 'seguridad@gmail.com', '2026-08-28 03:47:09.654852', 'Seguridad', '$2a$10$Xlm9Eda5PETfOj7Hjwrsxuvj1xwoQNyfElNRsAorvB5q/3Fd2ZD9y', 'JEFE');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `carritos`
--
ALTER TABLE `carritos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_carritos_usuario` (`usuario_id`);

--
-- Indices de la tabla `carrito_items`
--
ALTER TABLE `carrito_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_carrito_items_carrito` (`carrito_id`),
  ADD KEY `fk_carrito_items_producto` (`producto_id`);

--
-- Indices de la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_factura_numero` (`numero_factura`),
  ADD UNIQUE KEY `uk_factura_pedido` (`pedido_id`);

--
-- Indices de la tabla `historial_pedidos`
--
ALTER TABLE `historial_pedidos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_historial_pedido` (`pedido_id`),
  ADD KEY `fk_historial_usuario` (`usuario_id`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_pedidos_id_pedido` (`id_pedido`),
  ADD UNIQUE KEY `uk_pedidos_usuario_idempotency` (`usuario_id`,`idempotency_key`),
  ADD UNIQUE KEY `uk_pedidos_stripe_session` (`stripe_session_id`),
  ADD KEY `idx_pedidos_usuario_fecha` (`usuario_id`,`fecha_pedido`);

--
-- Indices de la tabla `pedido_items`
--
ALTER TABLE `pedido_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_pedido_items_pedido` (`pedido_id`),
  ADD KEY `fk_pedido_items_producto` (`producto_id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `producto_colores`
--
ALTER TABLE `producto_colores`
  ADD PRIMARY KEY (`producto_id`,`orden_color`),
  ADD UNIQUE KEY `uk_producto_colores_producto_color` (`producto_id`,`color`);

--
-- Indices de la tabla `producto_tallas`
--
ALTER TABLE `producto_tallas`
  ADD PRIMARY KEY (`producto_id`,`orden_talla`),
  ADD UNIQUE KEY `uk_producto_tallas_producto_talla` (`producto_id`,`talla`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_usuarios_email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `carritos`
--
ALTER TABLE `carritos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `carrito_items`
--
ALTER TABLE `carrito_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `facturas`
--
ALTER TABLE `facturas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT de la tabla `historial_pedidos`
--
ALTER TABLE `historial_pedidos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pedido_items`
--
ALTER TABLE `pedido_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `carritos`
--
ALTER TABLE `carritos`
  ADD CONSTRAINT `fk_carritos_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `carrito_items`
--
ALTER TABLE `carrito_items`
  ADD CONSTRAINT `fk_carrito_items_carrito` FOREIGN KEY (`carrito_id`) REFERENCES `carritos` (`id`),
  ADD CONSTRAINT `fk_carrito_items_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD CONSTRAINT `fk_factura_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `historial_pedidos`
--
ALTER TABLE `historial_pedidos`
  ADD CONSTRAINT `fk_historial_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `fk_historial_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `fk_pedidos_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `pedido_items`
--
ALTER TABLE `pedido_items`
  ADD CONSTRAINT `fk_pedido_items_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `fk_pedido_items_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `producto_colores`
--
ALTER TABLE `producto_colores`
  ADD CONSTRAINT `fk_producto_colores_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `producto_tallas`
--
ALTER TABLE `producto_tallas`
  ADD CONSTRAINT `fk_producto_tallas_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
