-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 26, 2022 at 06:04 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `search-engine`
--

-- --------------------------------------------------------

--
-- Table structure for table `links`
--

CREATE TABLE `links` (
  `Link` text NOT NULL,
  `Id` int(11) NOT NULL,
  `Layer` int(11) DEFAULT NULL,
  `ThreadName` varchar(45) DEFAULT NULL,
  `LinkParent` int(11) DEFAULT NULL,
  `LastTime` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `links`
--

INSERT INTO `links` (`Link`, `Id`, `Layer`, `ThreadName`, `LinkParent`, `LastTime`) VALUES
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Animation/', 1, 0, 'Thread1', NULL, '2022-03-26 18:26:37'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Recreation/Antiques/', 2, 0, 'Thread2', NULL, '2022-03-26 18:26:37'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Architecture/', 3, 0, 'Thread3', NULL, '2022-03-26 18:26:37'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Art_History/', 4, 0, 'Thread4', NULL, '2022-03-26 18:26:37'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Bodyart/', 5, 0, 'Thread5', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Classical_Studies/', 6, 0, 'Thread6', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Comics/', 7, 0, 'Thread7', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Costumes/', 8, 0, 'Thread8', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Crafts/', 9, 0, 'Thread9', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Performing_Arts/Dance/', 10, 0, 'Thread10', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Movies/', 11, 0, 'Thread11', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Music/', 12, 0, 'Thread12', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Entertainment/', 13, 0, 'Thread13', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Humanities/', 14, 0, 'Thread14', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Reference/Museums/Arts_and_Entertainment/', 15, 0, 'Thread15', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20080914093118/http://www.dmoz.org/Arts/Television/', 16, 0, 'Thread16', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20081204075623/http://www.dmoz.org/Sports/Soccer/', 17, 0, 'Thread17', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20081204075623/http://www.dmoz.org/Sports/Football/', 18, 0, 'Thread18', NULL, '2022-03-26 18:28:36'),
('https://web.archive.org/web/20081204075623/http://www.dmoz.org/Sports/Volleyball/', 19, 0, 'Thread19', NULL, '2022-03-26 18:28:36'),
('https://www.iflscience.com/environment/', 20, 0, 'Thread20', NULL, '2022-03-26 18:28:36'),
('https://www.iflscience.com/technology/', 21, 0, 'Thread21', NULL, '2022-03-26 18:28:36'),
('https://www.iflscience.com/space/', 22, 0, 'Thread22', NULL, '2022-03-26 18:28:36'),
('https://www.iflscience.com/health-and-medicine/', 23, 0, 'Thread23', NULL, '2022-03-26 18:28:36'),
('https://www.iflscience.com/chemistry/', 24, 0, 'Thread24', NULL, '2022-03-26 18:28:36'),
('https://www.iflscience.com/plants-and-animals/', 25, 0, 'Thread25', NULL, '2022-03-26 18:28:36'),
('https://www.bbc.com/', 26, 0, 'Thread26', NULL, '2022-03-26 18:28:36'),
('https://www.bbcgoodfoodme.com/', 27, 0, 'Thread27', NULL, '2022-03-26 18:28:36'),
('https://edition.cnn.com/', 28, 0, 'Thread28', NULL, '2022-03-26 18:28:36'),
('https://www.w3schools.com/', 29, 0, 'Thread29', NULL, '2022-03-26 18:28:36'),
('https://developer.mozilla.org/en-US/', 30, 0, 'Thread30', NULL, '2022-03-26 18:28:36'),
('https://www.geeksforgeeks.org/', 31, 0, 'Thread31', NULL, '2022-03-26 18:28:36'),
('https://www.tutorialspoint.com/', 32, 0, 'Thread32', NULL, '2022-03-26 18:28:36'),
('https://www.nature.com/', 33, 0, 'Thread33', NULL, '2022-03-26 18:28:36'),
('https://www.yourbrainonporn.com/', 34, 0, 'Thread34', NULL, '2022-03-26 18:28:36'),
('https://www.science.org/', 35, 0, 'Thread35', NULL, '2022-03-26 18:28:36'),
('https://www.nationalgeographic.com/', 36, 0, 'Thread36', NULL, '2022-03-26 18:28:36');

-- --------------------------------------------------------

--
-- Table structure for table `threads`
--

CREATE TABLE `threads` (
  `ThreadName` varchar(50) NOT NULL,
  `Layer` int(11) NOT NULL,
  `UrlIndex` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `threads`
--

INSERT INTO `threads` (`ThreadName`, `Layer`, `UrlIndex`) VALUES
('Thread1', 0, 0),
('Thread2', 0, 0),
('Thread3', 0, 0),
('Thread4', 0, 0),
('Thread5', 0, 0),
('Thread6', 0, 0),
('Thread7', 0, 0),
('Thread8', 0, 0),
('Thread9', 0, 0),
('Thread10', 0, 0),
('Thread11', 0, 0),
('Thread12', 0, 0),
('Thread13', 0, 0),
('Thread14', 0, 0),
('Thread15', 0, 0),
('Thread16', 0, 0),
('Thread17', 0, 0),
('Thread18', 0, 0),
('Thread19', 0, 0),
('Thread20', 0, 0),
('Thread21', 0, 0),
('Thread22', 0, 0),
('Thread23', 0, 0),
('Thread24', 0, 0),
('Thread25', 0, 0),
('Thread26', 0, 0),
('Thread27', 0, 0),
('Thread28', 0, 0),
('Thread29', 0, 0),
('Thread30', 0, 0),
('Thread31', 0, 0),
('Thread32', 0, 0),
('Thread33', 0, 0),
('Thread34', 0, 0),
('Thread35', 0, 0),
('Thread36', 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `links`
--
ALTER TABLE `links`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `LinkParent` (`LinkParent`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `links`
--
ALTER TABLE `links`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `links`
--
ALTER TABLE `links`
  ADD CONSTRAINT `links_ibfk_1` FOREIGN KEY (`LinkParent`) REFERENCES `links` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
