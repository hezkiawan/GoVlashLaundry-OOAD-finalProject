-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 04, 2025 at 10:39 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `govlash_laundry`
--

-- --------------------------------------------------------

--
-- Table structure for table `ms_service`
--

CREATE TABLE `ms_service` (
  `ServiceId` int(11) NOT NULL,
  `ServiceName` varchar(50) NOT NULL,
  `ServiceDescription` varchar(250) NOT NULL,
  `ServicePrice` int(11) NOT NULL,
  `ServiceDuration` int(11) NOT NULL,
  `IsDeleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ms_service`
--

INSERT INTO `ms_service` (`ServiceId`, `ServiceName`, `ServiceDescription`, `ServicePrice`, `ServiceDuration`, `IsDeleted`) VALUES
(1, 'Dry Clean', 'Untuk jas dan pakaian formal', 50000, 3, 0);

-- --------------------------------------------------------

--
-- Table structure for table `ms_user`
--

CREATE TABLE `ms_user` (
  `UserId` int(11) NOT NULL,
  `UserName` varchar(50) NOT NULL,
  `UserEmail` varchar(100) NOT NULL,
  `UserPassword` varchar(50) NOT NULL,
  `UserGender` varchar(10) NOT NULL,
  `UserDOB` date NOT NULL,
  `UserRole` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ms_user`
--

INSERT INTO `ms_user` (`UserId`, `UserName`, `UserEmail`, `UserPassword`, `UserGender`, `UserDOB`, `UserRole`) VALUES
(1, 'FirstAdmin', 'admin@govlash.com', 'admin123', 'Male', '2003-08-30', 'Admin'),
(2, 'FirstCustomer', 'firstCustomer@email.com', 'customer123', 'Male', '2003-08-30', 'Customer'),
(3, 'FirstReceptionist', 'firstReceptionist@govlash.com', 'recep123', 'Female', '2002-12-13', 'Receptionist'),
(4, 'FirstStaff', 'firstStaff@govlash.com', 'staff123', 'Male', '2005-12-01', 'Laundry Staff');

-- --------------------------------------------------------

--
-- Table structure for table `tr_notification`
--

CREATE TABLE `tr_notification` (
  `NotificationId` int(11) NOT NULL,
  `RecipientId` int(11) NOT NULL,
  `NotificationMessage` varchar(255) NOT NULL,
  `CreatedAt` datetime DEFAULT current_timestamp(),
  `IsRead` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tr_notification`
--

INSERT INTO `tr_notification` (`NotificationId`, `RecipientId`, `NotificationMessage`, `CreatedAt`, `IsRead`) VALUES
(2, 2, 'Your order is finished and ready for pickup. Thank you for choosing our service!', '2025-12-04 15:25:42', 0),
(3, 2, 'Your order is finished and ready for pickup. Thank you for choosing our service!', '2025-12-04 15:25:44', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tr_transaction`
--

CREATE TABLE `tr_transaction` (
  `TransactionId` int(11) NOT NULL,
  `ServiceId` int(11) NOT NULL,
  `CustomerId` int(11) NOT NULL,
  `ReceptionistId` int(11) DEFAULT NULL,
  `LaundryStaffId` int(11) DEFAULT NULL,
  `TransactionDate` date DEFAULT curdate(),
  `TransactionStatus` varchar(20) DEFAULT 'Pending',
  `TotalWeight` int(11) NOT NULL,
  `TransactionNotes` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tr_transaction`
--

INSERT INTO `tr_transaction` (`TransactionId`, `ServiceId`, `CustomerId`, `ReceptionistId`, `LaundryStaffId`, `TransactionDate`, `TransactionStatus`, `TotalWeight`, `TransactionNotes`) VALUES
(1, 1, 2, 3, 4, '2025-12-03', 'Finished', 2, 'Please separate whites'),
(2, 1, 2, 3, 4, '2025-12-04', 'Finished', 20, 'Tolong yang bener'),
(3, 1, 2, NULL, NULL, '2025-12-04', 'Pending', 15, 'Hati hati sama putih');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ms_service`
--
ALTER TABLE `ms_service`
  ADD PRIMARY KEY (`ServiceId`);

--
-- Indexes for table `ms_user`
--
ALTER TABLE `ms_user`
  ADD PRIMARY KEY (`UserId`),
  ADD UNIQUE KEY `UserName` (`UserName`),
  ADD UNIQUE KEY `UserEmail` (`UserEmail`);

--
-- Indexes for table `tr_notification`
--
ALTER TABLE `tr_notification`
  ADD PRIMARY KEY (`NotificationId`),
  ADD KEY `RecipientId` (`RecipientId`);

--
-- Indexes for table `tr_transaction`
--
ALTER TABLE `tr_transaction`
  ADD PRIMARY KEY (`TransactionId`),
  ADD KEY `ServiceId` (`ServiceId`),
  ADD KEY `CustomerId` (`CustomerId`),
  ADD KEY `ReceptionistId` (`ReceptionistId`),
  ADD KEY `LaundryStaffId` (`LaundryStaffId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ms_service`
--
ALTER TABLE `ms_service`
  MODIFY `ServiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `ms_user`
--
ALTER TABLE `ms_user`
  MODIFY `UserId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tr_notification`
--
ALTER TABLE `tr_notification`
  MODIFY `NotificationId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tr_transaction`
--
ALTER TABLE `tr_transaction`
  MODIFY `TransactionId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tr_notification`
--
ALTER TABLE `tr_notification`
  ADD CONSTRAINT `tr_notification_ibfk_1` FOREIGN KEY (`RecipientId`) REFERENCES `ms_user` (`UserId`);

--
-- Constraints for table `tr_transaction`
--
ALTER TABLE `tr_transaction`
  ADD CONSTRAINT `tr_transaction_ibfk_1` FOREIGN KEY (`ServiceId`) REFERENCES `ms_service` (`ServiceId`),
  ADD CONSTRAINT `tr_transaction_ibfk_2` FOREIGN KEY (`CustomerId`) REFERENCES `ms_user` (`UserId`),
  ADD CONSTRAINT `tr_transaction_ibfk_3` FOREIGN KEY (`ReceptionistId`) REFERENCES `ms_user` (`UserId`),
  ADD CONSTRAINT `tr_transaction_ibfk_4` FOREIGN KEY (`LaundryStaffId`) REFERENCES `ms_user` (`UserId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
