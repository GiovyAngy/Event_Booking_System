-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Erstellungszeit: 17. Dez 2025 um 12:20
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `event_booking_db`
--
CREATE DATABASE IF NOT EXISTS `event_booking_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `event_booking_db`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bookings`
--

CREATE TABLE `bookings` (
  `id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `seat_id` bigint(20) NOT NULL,
  `status` enum('RESERVED','CONFIRMED','CANCELLED') NOT NULL DEFAULT 'RESERVED',
  `booking_date` datetime NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `bookings`
--

INSERT INTO `bookings` (`id`, `customer_id`, `event_id`, `seat_id`, `status`, `booking_date`, `price`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 'CONFIRMED', '2025-12-16 08:39:42', 49.90, '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(2, 2, 1, 2, 'RESERVED', '2025-12-16 08:39:42', 49.90, '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(3, 3, 2, 51, 'CONFIRMED', '2025-12-16 08:39:42', 12.50, '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(4, 4, 3, 100, 'RESERVED', '2025-12-16 08:39:42', 35.00, '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(5, 5, 4, 75, 'CONFIRMED', '2025-12-16 08:39:42', 25.00, '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(6, 6, 1, 3, 'CONFIRMED', '2025-12-16 10:30:14', 49.90, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(7, 7, 2, 52, 'CONFIRMED', '2025-12-16 10:30:14', 12.50, '2025-12-16 09:30:14', '2025-12-16 11:24:11'),
(8, 8, 3, 105, 'CONFIRMED', '2025-12-16 10:30:14', 35.00, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(9, 9, 4, 80, 'RESERVED', '2025-12-16 10:30:14', 25.00, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(10, 10, 5, 10, 'CONFIRMED', '2025-12-16 10:30:14', 49.90, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(11, 1, 6, 11, 'RESERVED', '2025-12-16 10:30:14', 45.00, '2025-12-16 09:30:14', '2025-12-17 11:19:26'),
(12, 2, 7, 53, 'RESERVED', '2025-12-16 10:30:14', 30.00, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(13, 3, 8, 106, 'CONFIRMED', '2025-12-16 10:30:14', 15.00, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(14, 4, 9, 81, 'CONFIRMED', '2025-12-16 10:30:14', 40.00, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(15, 5, 1, 12, 'RESERVED', '2025-12-16 10:30:14', 49.90, '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(16, 5, 9, 770, 'RESERVED', '2025-12-17 07:08:15', 48.00, '2025-12-17 07:08:15', '2025-12-17 07:08:15'),
(17, 5, 7, 654, 'RESERVED', '2025-12-17 10:51:57', 30.00, '2025-12-17 10:51:57', '2025-12-17 10:51:57'),
(18, 10, 8, 605, 'RESERVED', '2025-12-17 11:05:04', 18.00, '2025-12-17 11:05:04', '2025-12-17 11:05:04'),
(19, 11, 7, 601, 'RESERVED', '2025-12-17 11:05:54', 36.00, '2025-12-17 11:05:54', '2025-12-17 11:05:54');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `customers`
--

CREATE TABLE `customers` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `customers`
--

INSERT INTO `customers` (`id`, `first_name`, `last_name`, `email`, `phone`, `created_at`, `updated_at`) VALUES
(1, 'Max', 'Mustermann', 'max.mustermann@email.de', '0171-1234567', '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(2, 'Anna', 'Schmidt', 'anna.schmidt@email.de', '0172-2345678', '2025-12-16 07:39:42', '2025-12-16 07:39:42'),
(3, 'Peter', 'Müller', 'peter.mueller@email.de', '0173-3123456', '2025-12-16 07:39:42', '2025-12-17 10:02:00'),
(4, 'Lisa', 'Weber', 'lisa.weber@email.de', '0174-4123456', '2025-12-16 07:39:42', '2025-12-17 10:02:15'),
(5, 'Tom', 'Fischer', 'tom.fischer@email.de', '0175-5123456', '2025-12-16 07:39:42', '2025-12-17 10:01:42'),
(6, 'Julia', 'Meier', 'julia.meier@email.de', '0176-6123456', '2025-12-16 09:30:14', '2025-12-17 10:01:54'),
(7, 'Lukas', 'Klein', 'lukas.klein@email.de', '0177-7123456', '2025-12-16 09:30:14', '2025-12-17 10:01:48'),
(8, 'Sophie', 'Schneider', 'sophie.schneider@email.de', '0178-89123456', '2025-12-16 09:30:14', '2025-12-17 10:02:10'),
(9, 'David', 'Wolf', 'david.wolf@email.de', '0179-9012345', '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(10, 'Laura', 'Becker', 'laura.becker@email.de', '0170-0123456', '2025-12-16 09:30:14', '2025-12-16 09:30:14'),
(11, 'Bernd', 'Schmit', 'bernd.schmit@email.de', '0157-1234567', '2025-12-17 11:04:52', '2025-12-17 11:04:52');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `events`
--

CREATE TABLE `events` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `date_time` datetime NOT NULL,
  `category` varchar(50) NOT NULL,
  `base_price` decimal(10,2) NOT NULL,
  `hall_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `events`
--

INSERT INTO `events` (`id`, `name`, `description`, `date_time`, `category`, `base_price`, `hall_id`, `created_at`, `updated_at`) VALUES
(1, 'Rock Konzert 2025', 'Die besten Rock-Bands live', '2026-03-15 20:00:00', 'Musik', 49.90, 1, '2025-12-16 07:39:42', '2025-12-17 06:55:18'),
(2, 'Actionfilm Premiere', 'Der neueste Blockbuster', '2026-02-20 18:30:00', 'Kino', 12.50, 2, '2025-12-16 07:39:42', '2025-12-16 11:23:30'),
(3, 'Shakespeare am Abend', 'Romeo und Julia', '2026-04-10 19:00:00', 'Theater', 35.00, 1, '2025-12-16 07:39:42', '2025-12-17 06:55:28'),
(4, 'Comedy Night', 'Stand-up Comedy vom Feinsten', '2026-03-05 21:00:00', 'Comedy', 25.00, 2, '2025-12-16 07:39:42', '2025-12-16 11:22:50'),
(5, 'Jazz im Kerzenlicht', 'Entspannter Jazz-Abend', '2026-05-01 20:30:00', 'Musik', 39.90, 3, '2025-12-16 07:39:42', '2025-12-17 06:55:38'),
(6, 'Pop Konzert 2025', 'Top Pop-Künstler live', '2026-06-10 20:00:00', 'Musik', 45.00, 1, '2025-12-16 09:30:14', '2025-12-17 06:55:46'),
(7, 'Drama Abend', 'Intensives Theaterstück', '2026-06-15 19:30:00', 'Theater', 30.00, 2, '2025-12-16 09:30:14', '2025-12-17 06:55:53'),
(8, 'Filmnacht Spezial', 'Kultfilme am Abend', '2026-06-20 21:00:00', 'Kino', 15.00, 2, '2025-12-16 09:30:14', '2025-12-17 06:55:58'),
(9, 'Jazz Festival', 'Jazz unter Sternen', '2026-07-05 20:30:00', 'Musik', 40.00, 3, '2025-12-16 09:30:14', '2025-12-17 06:56:06');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `halls`
--

CREATE TABLE `halls` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `capacity` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `halls`
--

INSERT INTO `halls` (`id`, `name`, `capacity`, `created_at`, `updated_at`) VALUES
(1, 'Hauptsaal', 200, '2025-12-16 07:35:08', '2025-12-16 07:35:08'),
(2, 'Kleiner Saal', 56, '2025-12-16 07:35:08', '2025-12-17 07:07:35'),
(3, 'VIP-Lounge', 30, '2025-12-16 07:35:08', '2025-12-16 07:35:08');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `seats`
--

CREATE TABLE `seats` (
  `id` bigint(20) NOT NULL,
  `row_label` varchar(50) NOT NULL,
  `seat_number` int(11) NOT NULL,
  `hall_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `seats`
--

INSERT INTO `seats` (`id`, `row_label`, `seat_number`, `hall_id`, `created_at`) VALUES
(1, 'Reihe 1', 1, 1, '2025-12-16 07:39:42'),
(2, 'Reihe 2', 1, 1, '2025-12-16 07:39:42'),
(3, 'Reihe 3', 1, 1, '2025-12-16 07:39:42'),
(4, 'Reihe 4', 1, 1, '2025-12-16 07:39:42'),
(5, 'Reihe 5', 1, 1, '2025-12-16 07:39:42'),
(6, 'Reihe 6', 1, 1, '2025-12-16 07:39:42'),
(7, 'Reihe 7', 1, 1, '2025-12-16 07:39:42'),
(8, 'Reihe 8', 1, 1, '2025-12-16 07:39:42'),
(9, 'Reihe 9', 1, 1, '2025-12-16 07:39:42'),
(10, 'Reihe 10', 1, 1, '2025-12-16 07:39:42'),
(11, 'Reihe 1', 2, 1, '2025-12-16 07:39:42'),
(12, 'Reihe 2', 2, 1, '2025-12-16 07:39:42'),
(13, 'Reihe 3', 2, 1, '2025-12-16 07:39:42'),
(14, 'Reihe 4', 2, 1, '2025-12-16 07:39:42'),
(15, 'Reihe 5', 2, 1, '2025-12-16 07:39:42'),
(16, 'Reihe 6', 2, 1, '2025-12-16 07:39:42'),
(17, 'Reihe 7', 2, 1, '2025-12-16 07:39:42'),
(18, 'Reihe 8', 2, 1, '2025-12-16 07:39:42'),
(19, 'Reihe 9', 2, 1, '2025-12-16 07:39:42'),
(20, 'Reihe 10', 2, 1, '2025-12-16 07:39:42'),
(21, 'Reihe 1', 3, 1, '2025-12-16 07:39:42'),
(22, 'Reihe 2', 3, 1, '2025-12-16 07:39:42'),
(23, 'Reihe 3', 3, 1, '2025-12-16 07:39:42'),
(24, 'Reihe 4', 3, 1, '2025-12-16 07:39:42'),
(25, 'Reihe 5', 3, 1, '2025-12-16 07:39:42'),
(26, 'Reihe 6', 3, 1, '2025-12-16 07:39:42'),
(27, 'Reihe 7', 3, 1, '2025-12-16 07:39:42'),
(28, 'Reihe 8', 3, 1, '2025-12-16 07:39:42'),
(29, 'Reihe 9', 3, 1, '2025-12-16 07:39:42'),
(30, 'Reihe 10', 3, 1, '2025-12-16 07:39:42'),
(31, 'Reihe 1', 4, 1, '2025-12-16 07:39:42'),
(32, 'Reihe 2', 4, 1, '2025-12-16 07:39:42'),
(33, 'Reihe 3', 4, 1, '2025-12-16 07:39:42'),
(34, 'Reihe 4', 4, 1, '2025-12-16 07:39:42'),
(35, 'Reihe 5', 4, 1, '2025-12-16 07:39:42'),
(36, 'Reihe 6', 4, 1, '2025-12-16 07:39:42'),
(37, 'Reihe 7', 4, 1, '2025-12-16 07:39:42'),
(38, 'Reihe 8', 4, 1, '2025-12-16 07:39:42'),
(39, 'Reihe 9', 4, 1, '2025-12-16 07:39:42'),
(40, 'Reihe 10', 4, 1, '2025-12-16 07:39:42'),
(41, 'Reihe 1', 5, 1, '2025-12-16 07:39:42'),
(42, 'Reihe 2', 5, 1, '2025-12-16 07:39:42'),
(43, 'Reihe 3', 5, 1, '2025-12-16 07:39:42'),
(44, 'Reihe 4', 5, 1, '2025-12-16 07:39:42'),
(45, 'Reihe 5', 5, 1, '2025-12-16 07:39:42'),
(46, 'Reihe 6', 5, 1, '2025-12-16 07:39:42'),
(47, 'Reihe 7', 5, 1, '2025-12-16 07:39:42'),
(48, 'Reihe 8', 5, 1, '2025-12-16 07:39:42'),
(49, 'Reihe 9', 5, 1, '2025-12-16 07:39:42'),
(50, 'Reihe 10', 5, 1, '2025-12-16 07:39:42'),
(51, 'Reihe 1', 6, 1, '2025-12-16 07:39:42'),
(52, 'Reihe 2', 6, 1, '2025-12-16 07:39:42'),
(53, 'Reihe 3', 6, 1, '2025-12-16 07:39:42'),
(54, 'Reihe 4', 6, 1, '2025-12-16 07:39:42'),
(55, 'Reihe 5', 6, 1, '2025-12-16 07:39:42'),
(56, 'Reihe 6', 6, 1, '2025-12-16 07:39:42'),
(57, 'Reihe 7', 6, 1, '2025-12-16 07:39:42'),
(58, 'Reihe 8', 6, 1, '2025-12-16 07:39:42'),
(59, 'Reihe 9', 6, 1, '2025-12-16 07:39:42'),
(60, 'Reihe 10', 6, 1, '2025-12-16 07:39:42'),
(61, 'Reihe 1', 7, 1, '2025-12-16 07:39:42'),
(62, 'Reihe 2', 7, 1, '2025-12-16 07:39:42'),
(63, 'Reihe 3', 7, 1, '2025-12-16 07:39:42'),
(64, 'Reihe 4', 7, 1, '2025-12-16 07:39:42'),
(65, 'Reihe 5', 7, 1, '2025-12-16 07:39:42'),
(66, 'Reihe 6', 7, 1, '2025-12-16 07:39:42'),
(67, 'Reihe 7', 7, 1, '2025-12-16 07:39:42'),
(68, 'Reihe 8', 7, 1, '2025-12-16 07:39:42'),
(69, 'Reihe 9', 7, 1, '2025-12-16 07:39:42'),
(70, 'Reihe 10', 7, 1, '2025-12-16 07:39:42'),
(71, 'Reihe 1', 8, 1, '2025-12-16 07:39:42'),
(72, 'Reihe 2', 8, 1, '2025-12-16 07:39:42'),
(73, 'Reihe 3', 8, 1, '2025-12-16 07:39:42'),
(74, 'Reihe 4', 8, 1, '2025-12-16 07:39:42'),
(75, 'Reihe 5', 8, 1, '2025-12-16 07:39:42'),
(76, 'Reihe 6', 8, 1, '2025-12-16 07:39:42'),
(77, 'Reihe 7', 8, 1, '2025-12-16 07:39:42'),
(78, 'Reihe 8', 8, 1, '2025-12-16 07:39:42'),
(79, 'Reihe 9', 8, 1, '2025-12-16 07:39:42'),
(80, 'Reihe 10', 8, 1, '2025-12-16 07:39:42'),
(81, 'Reihe 1', 9, 1, '2025-12-16 07:39:42'),
(82, 'Reihe 2', 9, 1, '2025-12-16 07:39:42'),
(83, 'Reihe 3', 9, 1, '2025-12-16 07:39:42'),
(84, 'Reihe 4', 9, 1, '2025-12-16 07:39:42'),
(85, 'Reihe 5', 9, 1, '2025-12-16 07:39:42'),
(86, 'Reihe 6', 9, 1, '2025-12-16 07:39:42'),
(87, 'Reihe 7', 9, 1, '2025-12-16 07:39:42'),
(88, 'Reihe 8', 9, 1, '2025-12-16 07:39:42'),
(89, 'Reihe 9', 9, 1, '2025-12-16 07:39:42'),
(90, 'Reihe 10', 9, 1, '2025-12-16 07:39:42'),
(91, 'Reihe 1', 10, 1, '2025-12-16 07:39:42'),
(92, 'Reihe 2', 10, 1, '2025-12-16 07:39:42'),
(93, 'Reihe 3', 10, 1, '2025-12-16 07:39:42'),
(94, 'Reihe 4', 10, 1, '2025-12-16 07:39:42'),
(95, 'Reihe 5', 10, 1, '2025-12-16 07:39:42'),
(96, 'Reihe 6', 10, 1, '2025-12-16 07:39:42'),
(97, 'Reihe 7', 10, 1, '2025-12-16 07:39:42'),
(98, 'Reihe 8', 10, 1, '2025-12-16 07:39:42'),
(99, 'Reihe 9', 10, 1, '2025-12-16 07:39:42'),
(100, 'Reihe 10', 10, 1, '2025-12-16 07:39:42'),
(101, 'Reihe 1', 11, 1, '2025-12-16 07:39:42'),
(102, 'Reihe 2', 11, 1, '2025-12-16 07:39:42'),
(103, 'Reihe 3', 11, 1, '2025-12-16 07:39:42'),
(104, 'Reihe 4', 11, 1, '2025-12-16 07:39:42'),
(105, 'Reihe 5', 11, 1, '2025-12-16 07:39:42'),
(106, 'Reihe 6', 11, 1, '2025-12-16 07:39:42'),
(107, 'Reihe 7', 11, 1, '2025-12-16 07:39:42'),
(108, 'Reihe 8', 11, 1, '2025-12-16 07:39:42'),
(109, 'Reihe 9', 11, 1, '2025-12-16 07:39:42'),
(110, 'Reihe 10', 11, 1, '2025-12-16 07:39:42'),
(111, 'Reihe 1', 12, 1, '2025-12-16 07:39:42'),
(112, 'Reihe 2', 12, 1, '2025-12-16 07:39:42'),
(113, 'Reihe 3', 12, 1, '2025-12-16 07:39:42'),
(114, 'Reihe 4', 12, 1, '2025-12-16 07:39:42'),
(115, 'Reihe 5', 12, 1, '2025-12-16 07:39:42'),
(116, 'Reihe 6', 12, 1, '2025-12-16 07:39:42'),
(117, 'Reihe 7', 12, 1, '2025-12-16 07:39:42'),
(118, 'Reihe 8', 12, 1, '2025-12-16 07:39:42'),
(119, 'Reihe 9', 12, 1, '2025-12-16 07:39:42'),
(120, 'Reihe 10', 12, 1, '2025-12-16 07:39:42'),
(121, 'Reihe 1', 13, 1, '2025-12-16 07:39:42'),
(122, 'Reihe 2', 13, 1, '2025-12-16 07:39:42'),
(123, 'Reihe 3', 13, 1, '2025-12-16 07:39:42'),
(124, 'Reihe 4', 13, 1, '2025-12-16 07:39:42'),
(125, 'Reihe 5', 13, 1, '2025-12-16 07:39:42'),
(126, 'Reihe 6', 13, 1, '2025-12-16 07:39:42'),
(127, 'Reihe 7', 13, 1, '2025-12-16 07:39:42'),
(128, 'Reihe 8', 13, 1, '2025-12-16 07:39:42'),
(129, 'Reihe 9', 13, 1, '2025-12-16 07:39:42'),
(130, 'Reihe 10', 13, 1, '2025-12-16 07:39:42'),
(131, 'Reihe 1', 14, 1, '2025-12-16 07:39:42'),
(132, 'Reihe 2', 14, 1, '2025-12-16 07:39:42'),
(133, 'Reihe 3', 14, 1, '2025-12-16 07:39:42'),
(134, 'Reihe 4', 14, 1, '2025-12-16 07:39:42'),
(135, 'Reihe 5', 14, 1, '2025-12-16 07:39:42'),
(136, 'Reihe 6', 14, 1, '2025-12-16 07:39:42'),
(137, 'Reihe 7', 14, 1, '2025-12-16 07:39:42'),
(138, 'Reihe 8', 14, 1, '2025-12-16 07:39:42'),
(139, 'Reihe 9', 14, 1, '2025-12-16 07:39:42'),
(140, 'Reihe 10', 14, 1, '2025-12-16 07:39:42'),
(141, 'Reihe 1', 15, 1, '2025-12-16 07:39:42'),
(142, 'Reihe 2', 15, 1, '2025-12-16 07:39:42'),
(143, 'Reihe 3', 15, 1, '2025-12-16 07:39:42'),
(144, 'Reihe 4', 15, 1, '2025-12-16 07:39:42'),
(145, 'Reihe 5', 15, 1, '2025-12-16 07:39:42'),
(146, 'Reihe 6', 15, 1, '2025-12-16 07:39:42'),
(147, 'Reihe 7', 15, 1, '2025-12-16 07:39:42'),
(148, 'Reihe 8', 15, 1, '2025-12-16 07:39:42'),
(149, 'Reihe 9', 15, 1, '2025-12-16 07:39:42'),
(150, 'Reihe 10', 15, 1, '2025-12-16 07:39:42'),
(151, 'Reihe 1', 16, 1, '2025-12-16 07:39:42'),
(152, 'Reihe 2', 16, 1, '2025-12-16 07:39:42'),
(153, 'Reihe 3', 16, 1, '2025-12-16 07:39:42'),
(154, 'Reihe 4', 16, 1, '2025-12-16 07:39:42'),
(155, 'Reihe 5', 16, 1, '2025-12-16 07:39:42'),
(156, 'Reihe 6', 16, 1, '2025-12-16 07:39:42'),
(157, 'Reihe 7', 16, 1, '2025-12-16 07:39:42'),
(158, 'Reihe 8', 16, 1, '2025-12-16 07:39:42'),
(159, 'Reihe 9', 16, 1, '2025-12-16 07:39:42'),
(160, 'Reihe 10', 16, 1, '2025-12-16 07:39:42'),
(161, 'Reihe 1', 17, 1, '2025-12-16 07:39:42'),
(162, 'Reihe 2', 17, 1, '2025-12-16 07:39:42'),
(163, 'Reihe 3', 17, 1, '2025-12-16 07:39:42'),
(164, 'Reihe 4', 17, 1, '2025-12-16 07:39:42'),
(165, 'Reihe 5', 17, 1, '2025-12-16 07:39:42'),
(166, 'Reihe 6', 17, 1, '2025-12-16 07:39:42'),
(167, 'Reihe 7', 17, 1, '2025-12-16 07:39:42'),
(168, 'Reihe 8', 17, 1, '2025-12-16 07:39:42'),
(169, 'Reihe 9', 17, 1, '2025-12-16 07:39:42'),
(170, 'Reihe 10', 17, 1, '2025-12-16 07:39:42'),
(171, 'Reihe 1', 18, 1, '2025-12-16 07:39:42'),
(172, 'Reihe 2', 18, 1, '2025-12-16 07:39:42'),
(173, 'Reihe 3', 18, 1, '2025-12-16 07:39:42'),
(174, 'Reihe 4', 18, 1, '2025-12-16 07:39:42'),
(175, 'Reihe 5', 18, 1, '2025-12-16 07:39:42'),
(176, 'Reihe 6', 18, 1, '2025-12-16 07:39:42'),
(177, 'Reihe 7', 18, 1, '2025-12-16 07:39:42'),
(178, 'Reihe 8', 18, 1, '2025-12-16 07:39:42'),
(179, 'Reihe 9', 18, 1, '2025-12-16 07:39:42'),
(180, 'Reihe 10', 18, 1, '2025-12-16 07:39:42'),
(181, 'Reihe 1', 19, 1, '2025-12-16 07:39:42'),
(182, 'Reihe 2', 19, 1, '2025-12-16 07:39:42'),
(183, 'Reihe 3', 19, 1, '2025-12-16 07:39:42'),
(184, 'Reihe 4', 19, 1, '2025-12-16 07:39:42'),
(185, 'Reihe 5', 19, 1, '2025-12-16 07:39:42'),
(186, 'Reihe 6', 19, 1, '2025-12-16 07:39:42'),
(187, 'Reihe 7', 19, 1, '2025-12-16 07:39:42'),
(188, 'Reihe 8', 19, 1, '2025-12-16 07:39:42'),
(189, 'Reihe 9', 19, 1, '2025-12-16 07:39:42'),
(190, 'Reihe 10', 19, 1, '2025-12-16 07:39:42'),
(191, 'Reihe 1', 20, 1, '2025-12-16 07:39:42'),
(192, 'Reihe 2', 20, 1, '2025-12-16 07:39:42'),
(193, 'Reihe 3', 20, 1, '2025-12-16 07:39:42'),
(194, 'Reihe 4', 20, 1, '2025-12-16 07:39:42'),
(195, 'Reihe 5', 20, 1, '2025-12-16 07:39:42'),
(196, 'Reihe 6', 20, 1, '2025-12-16 07:39:42'),
(197, 'Reihe 7', 20, 1, '2025-12-16 07:39:42'),
(198, 'Reihe 8', 20, 1, '2025-12-16 07:39:42'),
(199, 'Reihe 9', 20, 1, '2025-12-16 07:39:42'),
(200, 'Reihe 10', 20, 1, '2025-12-16 07:39:42'),
(601, 'Reihe 1', 1, 2, '2025-12-17 07:07:35'),
(602, 'Reihe 1', 2, 2, '2025-12-17 07:07:35'),
(603, 'Reihe 1', 3, 2, '2025-12-17 07:07:35'),
(604, 'Reihe 1', 4, 2, '2025-12-17 07:07:35'),
(605, 'Reihe 1', 5, 2, '2025-12-17 07:07:35'),
(606, 'Reihe 1', 6, 2, '2025-12-17 07:07:35'),
(607, 'Reihe 1', 7, 2, '2025-12-17 07:07:35'),
(608, 'Reihe 1', 8, 2, '2025-12-17 07:07:35'),
(609, 'Reihe 2', 1, 2, '2025-12-17 07:07:35'),
(610, 'Reihe 2', 2, 2, '2025-12-17 07:07:35'),
(611, 'Reihe 2', 3, 2, '2025-12-17 07:07:35'),
(612, 'Reihe 2', 4, 2, '2025-12-17 07:07:35'),
(613, 'Reihe 2', 5, 2, '2025-12-17 07:07:35'),
(614, 'Reihe 2', 6, 2, '2025-12-17 07:07:35'),
(615, 'Reihe 2', 7, 2, '2025-12-17 07:07:35'),
(616, 'Reihe 2', 8, 2, '2025-12-17 07:07:35'),
(617, 'Reihe 3', 1, 2, '2025-12-17 07:07:35'),
(618, 'Reihe 3', 2, 2, '2025-12-17 07:07:35'),
(619, 'Reihe 3', 3, 2, '2025-12-17 07:07:35'),
(620, 'Reihe 3', 4, 2, '2025-12-17 07:07:35'),
(621, 'Reihe 3', 5, 2, '2025-12-17 07:07:35'),
(622, 'Reihe 3', 6, 2, '2025-12-17 07:07:35'),
(623, 'Reihe 3', 7, 2, '2025-12-17 07:07:35'),
(624, 'Reihe 3', 8, 2, '2025-12-17 07:07:35'),
(625, 'Reihe 4', 1, 2, '2025-12-17 07:07:35'),
(626, 'Reihe 4', 2, 2, '2025-12-17 07:07:35'),
(627, 'Reihe 4', 3, 2, '2025-12-17 07:07:35'),
(628, 'Reihe 4', 4, 2, '2025-12-17 07:07:35'),
(629, 'Reihe 4', 5, 2, '2025-12-17 07:07:35'),
(630, 'Reihe 4', 6, 2, '2025-12-17 07:07:35'),
(631, 'Reihe 4', 7, 2, '2025-12-17 07:07:35'),
(632, 'Reihe 4', 8, 2, '2025-12-17 07:07:35'),
(633, 'Reihe 5', 1, 2, '2025-12-17 07:07:35'),
(634, 'Reihe 5', 2, 2, '2025-12-17 07:07:35'),
(635, 'Reihe 5', 3, 2, '2025-12-17 07:07:35'),
(636, 'Reihe 5', 4, 2, '2025-12-17 07:07:35'),
(637, 'Reihe 5', 5, 2, '2025-12-17 07:07:35'),
(638, 'Reihe 5', 6, 2, '2025-12-17 07:07:35'),
(639, 'Reihe 5', 7, 2, '2025-12-17 07:07:35'),
(640, 'Reihe 5', 8, 2, '2025-12-17 07:07:35'),
(641, 'Reihe 6', 1, 2, '2025-12-17 07:07:35'),
(642, 'Reihe 6', 2, 2, '2025-12-17 07:07:35'),
(643, 'Reihe 6', 3, 2, '2025-12-17 07:07:35'),
(644, 'Reihe 6', 4, 2, '2025-12-17 07:07:35'),
(645, 'Reihe 6', 5, 2, '2025-12-17 07:07:35'),
(646, 'Reihe 6', 6, 2, '2025-12-17 07:07:35'),
(647, 'Reihe 6', 7, 2, '2025-12-17 07:07:35'),
(648, 'Reihe 6', 8, 2, '2025-12-17 07:07:35'),
(649, 'Reihe 7', 1, 2, '2025-12-17 07:07:35'),
(650, 'Reihe 7', 2, 2, '2025-12-17 07:07:35'),
(651, 'Reihe 7', 3, 2, '2025-12-17 07:07:35'),
(652, 'Reihe 7', 4, 2, '2025-12-17 07:07:35'),
(653, 'Reihe 7', 5, 2, '2025-12-17 07:07:35'),
(654, 'Reihe 7', 6, 2, '2025-12-17 07:07:35'),
(655, 'Reihe 7', 7, 2, '2025-12-17 07:07:35'),
(656, 'Reihe 7', 8, 2, '2025-12-17 07:07:35'),
(769, 'Reihe 1', 1, 3, '2025-12-17 07:07:35'),
(770, 'Reihe 1', 2, 3, '2025-12-17 07:07:35'),
(771, 'Reihe 1', 3, 3, '2025-12-17 07:07:35'),
(772, 'Reihe 1', 4, 3, '2025-12-17 07:07:35'),
(773, 'Reihe 1', 5, 3, '2025-12-17 07:07:35'),
(774, 'Reihe 2', 1, 3, '2025-12-17 07:07:35'),
(775, 'Reihe 2', 2, 3, '2025-12-17 07:07:35'),
(776, 'Reihe 2', 3, 3, '2025-12-17 07:07:35'),
(777, 'Reihe 2', 4, 3, '2025-12-17 07:07:35'),
(778, 'Reihe 2', 5, 3, '2025-12-17 07:07:35'),
(779, 'Reihe 3', 1, 3, '2025-12-17 07:07:35'),
(780, 'Reihe 3', 2, 3, '2025-12-17 07:07:35'),
(781, 'Reihe 3', 3, 3, '2025-12-17 07:07:35'),
(782, 'Reihe 3', 4, 3, '2025-12-17 07:07:35'),
(783, 'Reihe 3', 5, 3, '2025-12-17 07:07:35'),
(784, 'Reihe 4', 1, 3, '2025-12-17 07:07:35'),
(785, 'Reihe 4', 2, 3, '2025-12-17 07:07:35'),
(786, 'Reihe 4', 3, 3, '2025-12-17 07:07:35'),
(787, 'Reihe 4', 4, 3, '2025-12-17 07:07:35'),
(788, 'Reihe 4', 5, 3, '2025-12-17 07:07:35'),
(789, 'Reihe 5', 1, 3, '2025-12-17 07:07:35'),
(790, 'Reihe 5', 2, 3, '2025-12-17 07:07:35'),
(791, 'Reihe 5', 3, 3, '2025-12-17 07:07:35'),
(792, 'Reihe 5', 4, 3, '2025-12-17 07:07:35'),
(793, 'Reihe 5', 5, 3, '2025-12-17 07:07:35'),
(794, 'Reihe 6', 1, 3, '2025-12-17 07:07:35'),
(795, 'Reihe 6', 2, 3, '2025-12-17 07:07:35'),
(796, 'Reihe 6', 3, 3, '2025-12-17 07:07:35'),
(797, 'Reihe 6', 4, 3, '2025-12-17 07:07:35'),
(798, 'Reihe 6', 5, 3, '2025-12-17 07:07:35');

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_available_seats`
--
CREATE TABLE `v_available_seats` (
`event_id` bigint(20),
`event_name` varchar(200),
`total_seats` bigint(21),
`available_seats` bigint(22)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_booking_details`
--
CREATE TABLE `v_booking_details` (
`booking_id` bigint(20),
`first_name` varchar(100),
`last_name` varchar(100),
`email` varchar(255),
`event_name` varchar(200),
`event_date` datetime,
`category` varchar(50),
`hall_name` varchar(100),
`row_label` varchar(50),
`seat_number` int(11),
`status` enum('RESERVED','CONFIRMED','CANCELLED'),
`booking_date` datetime,
`price` decimal(10,2)
);

-- --------------------------------------------------------

--
-- Struktur des Views `v_available_seats`
--
DROP TABLE IF EXISTS `v_available_seats`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_available_seats` AS
SELECT
  `e`.`id` AS `event_id`,
  `e`.`name` AS `event_name`,
  COUNT(`s`.`id`) AS `total_seats`,
  COUNT(`s`.`id`) - COUNT(`b`.`id`) AS `available_seats`
FROM
  `events` `e`
  JOIN `halls` `h` ON `e`.`hall_id` = `h`.`id`
  JOIN `seats` `s` ON `s`.`hall_id` = `h`.`id`
  LEFT JOIN `bookings` `b` ON `b`.`event_id` = `e`.`id`
    AND `b`.`seat_id` = `s`.`id`
    AND `b`.`status` <> 'CANCELLED'
GROUP BY `e`.`id`, `e`.`name`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_booking_details`
--
DROP TABLE IF EXISTS `v_booking_details`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_booking_details` AS
SELECT
  `b`.`id` AS `booking_id`,
  `c`.`first_name` AS `first_name`,
  `c`.`last_name` AS `last_name`,
  `c`.`email` AS `email`,
  `e`.`name` AS `event_name`,
  `e`.`date_time` AS `event_date`,
  `e`.`category` AS `category`,
  `h`.`name` AS `hall_name`,
  `s`.`row_label` AS `row_label`,
  `s`.`seat_number` AS `seat_number`,
  `b`.`status` AS `status`,
  `b`.`booking_date` AS `booking_date`,
  `b`.`price` AS `price`
FROM
  `bookings` `b`
  JOIN `customers` `c` ON `b`.`customer_id` = `c`.`id`
  JOIN `events` `e` ON `b`.`event_id` = `e`.`id`
  JOIN `seats` `s` ON `b`.`seat_id` = `s`.`id`
  JOIN `halls` `h` ON `e`.`hall_id` = `h`.`id`;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_booking` (`event_id`,`seat_id`),
  ADD KEY `seat_id` (`seat_id`),
  ADD KEY `idx_customer` (`customer_id`),
  ADD KEY `idx_event` (`event_id`),
  ADD KEY `idx_status` (`status`),
  ADD KEY `idx_booking_date` (`booking_date`);

--
-- Indizes für die Tabelle `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_name` (`last_name`,`first_name`);

--
-- Indizes für die Tabelle `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_date` (`date_time`),
  ADD KEY `idx_category` (`category`),
  ADD KEY `idx_hall` (`hall_id`);

--
-- Indizes für die Tabelle `halls`
--
ALTER TABLE `halls`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_name` (`name`);

--
-- Indizes für die Tabelle `seats`
--
ALTER TABLE `seats`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_seat` (`hall_id`,`row_label`,`seat_number`),
  ADD KEY `idx_hall` (`hall_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT für Tabelle `customers`
--
ALTER TABLE `customers`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT für Tabelle `events`
--
ALTER TABLE `events`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT für Tabelle `halls`
--
ALTER TABLE `halls`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT für Tabelle `seats`
--
ALTER TABLE `seats`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1037;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `bookings_ibfk_3` FOREIGN KEY (`seat_id`) REFERENCES `seats` (`id`);

--
-- Constraints der Tabelle `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `events_ibfk_1` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`);

--
-- Constraints der Tabelle `seats`
--
ALTER TABLE `seats`
  ADD CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`) ON DELETE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;