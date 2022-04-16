-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 16, 2022 at 01:38 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.2

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
  `LastTime` datetime DEFAULT current_timestamp(),
  `Completed` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `links`
--

INSERT INTO `links` (`Link`, `Id`, `Layer`, `ThreadName`, `LinkParent`, `LastTime`, `Completed`) VALUES
('https://www.bbc.co.uk/', 1, 1, 'Thread1', -1, '2022-03-26 18:26:37', 0),
('https://www.javatpoint.com/', 2, 1, 'Thread2', -1, '2022-03-26 18:26:37', 0),
('https://www.oracle.com/in/java/', 3, 1, 'Thread3', -1, '2022-03-26 18:26:37', 0),
('https://www.cbc.ca/news', 4, 1, 'Thread4', -1, '2022-03-26 18:26:37', 0),
('https://www.yallakora.com/', 5, 1, 'Thread5', -1, '2022-03-26 18:28:36', 0),
('https://www.yallakora-live.com/', 6, 1, 'Thread6', -1, '2022-03-26 18:28:36', 0),
('https://access.clarivate.com/', 7, 1, 'Thread7', -1, '2022-03-26 18:28:36', 0),
('https://w3school.com/', 8, 1, 'Thread8', -1, '2022-03-26 18:28:36', 0),
('https://www.bbc.com', 9, 1, 'Thread9', -1, '2022-03-26 18:28:36', 0),
('https://stackoverflow.com/', 10, 1, 'Thread10', -1, '2022-03-26 18:28:36', 0),
('https://www.nbcnews.com/', 11, 1, 'Thread11', -1, '2022-03-26 18:28:36', 0),
('https://iopscience.iop.org/', 12, 1, 'Thread12', -1, '2022-03-26 18:28:36', 0),
('https://www.reuters.com/', 13, 1, 'Thread13', -1, '2022-03-26 18:28:36', 0),
('https://www.nytimes.com/', 14, 1, 'Thread14', -1, '2022-03-26 18:28:36', 0),
('https://news.sky.com/', 15, 1, 'Thread15', -1, '2022-03-26 18:28:36', 0),
('https://www.academia.edu/', 16, 1, 'Thread16', -1, '2022-03-26 18:28:36', 0),
('https://libserver.cedefop.europa.eu/', 17, 1, 'Thread17', -1, '2022-03-26 18:28:36', 0),
('https://eric.ed.gov/', 18, 1, 'Thread18', -1, '2022-03-26 18:28:36', 0),
('https://www.microsoft.com/en-eg/', 19, 1, 'Thread19', -1, '2022-03-26 18:28:36', 0),
('https://academic.oup.com/crawlprevention/governor?content=%2fjournals%2fadvanced-search', 20, 1, 'Thread20', -1, '2022-03-26 18:28:36', 0),
('https://www.iflscience.com/technology/', 21, 1, 'Thread21', -1, '2022-03-26 18:28:36', 0),
('https://www.iflscience.com/space/', 22, 1, 'Thread22', -1, '2022-03-26 18:28:36', 0),
('https://www.iflscience.com/health-and-medicine/', 23, 1, 'Thread23', -1, '2022-03-26 18:28:36', 0),
('https://www.iflscience.com/chemistry/', 24, 1, 'Thread24', -1, '2022-03-26 18:28:36', 0),
('https://www.iflscience.com/plants-and-animals/', 25, 1, 'Thread25', -1, '2022-03-26 18:28:36', 0),
('https://www.foxnews.com/', 26, 1, 'Thread26', -1, '2022-03-26 18:28:36', 0),
('https://muse.jhu.edu/', 27, 1, 'Thread27', -1, '2022-03-26 18:28:36', 0),
('https://edition.cnn.com/', 28, 1, 'Thread28', -1, '2022-03-26 18:28:36', 0),
('https://cplusplus.com/', 29, 1, 'Thread29', -1, '2022-03-26 18:28:36', 0),
('https://developer.mozilla.org/en-US/', 30, 1, 'Thread30', -1, '2022-03-26 18:28:36', 0),
('https://www.geeksforgeeks.org/', 31, 1, 'Thread31', -1, '2022-03-26 18:28:36', 0),
('https://www.tutorialspoint.com/', 32, 1, 'Thread32', -1, '2022-03-26 18:28:36', 0),
('https://www.nature.com/', 33, 1, 'Thread33', -1, '2022-03-26 18:28:36', 0),
('https://www.yourbrainonporn.com/', 34, 1, 'Thread34', -1, '2022-03-26 18:28:36', 0),
('https://www.science.org/', 35, 1, 'Thread35', -1, '2022-03-26 18:28:36', 0),
('https://www.nationalgeographic.com/', 36, 1, 'Thread36', -1, '2022-03-26 18:28:36', 0),
('https://vpnoverview.com/', 37, 1, 'Thread37', -1, '2022-03-31 14:37:10', 0),
('https://match.koragol.com/', 38, 1, 'Thread38', -1, '2022-03-31 14:55:56', 0),
('https://www.merriam-webster.com/', 39, 1, 'Thread39', -1, '2022-03-31 15:08:17', 0),
('https://en.wikipedia.org/', 40, 1, 'Thread40', -1, '2022-03-31 15:10:11', 0),
('https://developers.google.com/', 41, 1, 'Thread41', -1, '2022-03-31 15:15:57', 0),
('https://en.ryte.com/', 42, 1, 'Thread42', -1, '2022-03-31 15:15:57', 0),
('https://johnnn.tech/', 43, 1, 'Thread43', -1, '2022-03-31 15:18:16', 0);

-- --------------------------------------------------------

--
-- Table structure for table `threads`
--

CREATE TABLE `threads` (
  `ThreadName` varchar(50) NOT NULL,
  `Layer` int(11) NOT NULL,
  `UrlIndex` int(11) NOT NULL,
  `UrlIndex1` int(11) NOT NULL DEFAULT 0,
  `UrlIndex2` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `threads`
--

INSERT INTO `threads` (`ThreadName`, `Layer`, `UrlIndex`, `UrlIndex1`, `UrlIndex2`) VALUES
('Thread1', 1, 0, 0, 0),
('Thread10', 1, 0, 0, 0),
('Thread11', 1, 0, 0, 0),
('Thread12', 1, 0, 0, 0),
('Thread13', 1, 0, 0, 0),
('Thread14', 1, 0, 0, 0),
('Thread15', 1, 0, 0, 0),
('Thread16', 1, 0, 0, 0),
('Thread17', 1, 0, 0, 0),
('Thread18', 1, 0, 0, 0),
('Thread19', 1, 0, 0, 0),
('Thread2', 1, 0, 0, 0),
('Thread20', 1, 0, 0, 0),
('Thread21', 1, 0, 0, 0),
('Thread22', 1, 0, 0, 0),
('Thread23', 1, 0, 0, 0),
('Thread24', 1, 0, 0, 0),
('Thread25', 1, 0, 0, 0),
('Thread26', 1, 0, 0, 0),
('Thread27', 1, 0, 0, 0),
('Thread28', 1, 0, 0, 0),
('Thread29', 1, 0, 0, 0),
('Thread3', 1, 0, 0, 0),
('Thread30', 1, 0, 0, 0),
('Thread31', 1, 0, 0, 0),
('Thread32', 1, 0, 0, 0),
('Thread33', 1, 0, 0, 0),
('Thread34', 1, 0, 0, 0),
('Thread35', 1, 0, 0, 0),
('Thread36', 1, 0, 0, 0),
('Thread37', 1, 0, 0, 0),
('Thread38', 1, 0, 0, 0),
('Thread39', 1, 0, 0, 0),
('Thread4', 1, 0, 0, 0),
('Thread40', 1, 0, 0, 0),
('Thread41', 1, 0, 0, 0),
('Thread42', 1, 0, 0, 0),
('Thread43', 1, 0, 0, 0),
('Thread5', 1, 0, 0, 0),
('Thread6', 1, 0, 0, 0),
('Thread7', 1, 0, 0, 0),
('Thread8', 1, 0, 0, 0),
('Thread9', 1, 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `links`
--
ALTER TABLE `links`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `uniqeLink` (`Link`) USING HASH;

--
-- Indexes for table `threads`
--
ALTER TABLE `threads`
  ADD PRIMARY KEY (`ThreadName`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `links`
--
ALTER TABLE `links`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=140244;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
