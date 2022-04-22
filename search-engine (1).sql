-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 22, 2022 at 07:38 PM
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
  `Completed` int(11) NOT NULL DEFAULT 0,
  `Descripation` text DEFAULT NULL,
  `Paragraph` longtext DEFAULT NULL,
  `Title` longtext DEFAULT NULL,
  `Headers` longtext DEFAULT NULL,
  `ListItems` longtext DEFAULT NULL,
  `Strong` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `links`
--

INSERT INTO `links` (`Link`, `Id`, `Layer`, `ThreadName`, `LinkParent`, `LastTime`, `Completed`, `Descripation`, `Paragraph`, `Title`, `Headers`, `ListItems`, `Strong`) VALUES
('https://www.bbc.co.uk', 1, 1, 'Thread1', -1, '2022-03-26 18:26:37', 0, 'The best of the BBC, with the latest news and sport headlines, weather, TV & radio highlights and much more from across the whole of BBC Online.', NULL, NULL, NULL, NULL, ''),
('https://www.javatpoint.com', 2, 1, 'Thread2', -1, '2022-03-26 18:26:37', 0, 'Tutorials, Free Online Tutorials, Javatpoint provides tutorials and interview questions of all technology like java tutorial, android, java frameworks, javascript, ajax, core java, sql, python, php, c language etc. for beginners and professionals.', NULL, NULL, NULL, NULL, ''),
('https://www.oracle.com/in/java/', 3, 1, 'Thread3', -1, '2022-03-26 18:26:37', 0, 'Java software reduces costs, drives innovation, and improves application services. Learn more about Java, the #1 development platform. ', NULL, NULL, NULL, NULL, ''),
('https://www.cbc.ca/news', 4, 1, 'Thread4', -1, '2022-03-26 18:26:37', 0, 'The latest news from across Canada and around the world.', NULL, NULL, NULL, NULL, ''),
('https://www.yallakora.com', 5, 1, 'Thread5', -1, '2022-03-26 18:28:36', 0, 'اخبار الكورة والرياضة المصرية والعالمية ومواعيد المباريات علي يلاكورة', NULL, NULL, NULL, NULL, ''),
('https://www.yallakora-live.com', 6, 1, 'Thread6', -1, '2022-03-26 18:28:36', 0, 'كورة لايف kora live يقدم خدمة البث المباشر لمباريات اليوم كوره لايف koora live بجودة عالية عبرموقع يلا كورة لايف yallakora-live روابط مباريات اليوم كورة اون لاين kooralive اتش دي', NULL, NULL, NULL, NULL, ''),
('https://access.clarivate.com', 7, 1, 'Thread7', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://w3school.com', 8, 1, 'Thread8', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://www.bbc.com', 9, 1, 'Thread9', -1, '2022-03-26 18:28:36', 0, 'Breaking news, sport, TV, radio and a whole lot more.\n        The BBC informs, educates and entertains - wherever you are, whatever your age.', NULL, NULL, NULL, NULL, ''),
('https://stackoverflow.com', 10, 1, 'Thread10', -1, '2022-03-26 18:28:36', 0, 'Stack Overflow is the largest, most trusted online community for developers to learn, share​ ​their programming ​knowledge, and build their careers.', NULL, NULL, NULL, NULL, ''),
('https://www.nbcnews.com', 11, 1, 'Thread11', -1, '2022-03-26 18:28:36', 0, 'Go to NBCNews.com for breaking news, videos, and the latest top stories in world news, business, politics, health and pop culture.', NULL, NULL, NULL, NULL, ''),
('https://iopscience.iop.org', 12, 1, 'Thread12', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://www.reuters.com', 13, 1, 'Thread13', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://www.nytimes.com', 14, 1, 'Thread14', -1, '2022-03-26 18:28:36', 0, 'Live news, investigations, opinion, photos and video by the journalists of The New York Times from more than 150 countries around the world. Subscribe for coverage of U.S. and international news, politics, business, technology, science, health, arts, sports and more.', NULL, NULL, NULL, NULL, ''),
('https://news.sky.com', 15, 1, 'Thread15', -1, '2022-03-26 18:28:36', 0, 'Sky News delivers breaking news, headlines and top stories from business, politics, entertainment and more in the UK and worldwide.', NULL, NULL, NULL, NULL, ''),
('https://www.academia.edu', 16, 1, 'Thread16', -1, '2022-03-26 18:28:36', 0, 'Academia.edu is a place to share and follow research.', NULL, NULL, NULL, NULL, ''),
('https://libserver.cedefop.europa.eu', 17, 1, 'Thread17', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://eric.ed.gov', 18, 1, 'Thread18', -1, '2022-03-26 18:28:36', 0, 'ERIC is an online library of education research and information, sponsored by the Institute of Education Sciences (IES) of the U.S. Department of Education.', NULL, NULL, NULL, NULL, ''),
('https://www.microsoft.com/en-eg/', 19, 1, 'Thread19', -1, '2022-03-26 18:28:36', 0, 'Get product information, support, and news from Microsoft.', NULL, NULL, NULL, NULL, ''),
('https://academic.oup.com/crawlprevention/governor?content=%2fjournals%2fadvanced-search', 20, 1, 'Thread20', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://www.iflscience.com/technology/', 21, 1, 'Thread21', -1, '2022-03-26 18:28:36', 0, 'Technology', NULL, NULL, NULL, NULL, ''),
('https://www.iflscience.com/space/', 22, 1, 'Thread22', -1, '2022-03-26 18:28:36', 0, 'Space', NULL, NULL, NULL, NULL, ''),
('https://www.iflscience.com/health-and-medicine/', 23, 1, 'Thread23', -1, '2022-03-26 18:28:36', 0, 'Health and Medicine', NULL, NULL, NULL, NULL, ''),
('https://www.iflscience.com/chemistry/', 24, 1, 'Thread24', -1, '2022-03-26 18:28:36', 0, 'Chemistry', NULL, NULL, NULL, NULL, ''),
('https://www.iflscience.com/plants-and-animals/', 25, 1, 'Thread25', -1, '2022-03-26 18:28:36', 0, 'Plants and Animals', NULL, NULL, NULL, NULL, ''),
('https://www.foxnews.com', 26, 1, 'Thread26', -1, '2022-03-26 18:28:36', 0, 'Breaking News, Latest News and Current News from FOXNews.com. Breaking news and video. Latest Current News: U.S., World, Entertainment, Health, Business, Technology, Politics, Sports.', NULL, NULL, NULL, NULL, ''),
('https://muse.jhu.edu', 27, 1, 'Thread27', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://edition.cnn.com', 28, 1, 'Thread28', -1, '2022-03-26 18:28:36', 0, 'Find the latest breaking news and information on the top stories, weather, business, entertainment, politics, and more. For in-depth coverage, CNN provides special reports, video, audio, photo galleries, and interactive guides.', NULL, NULL, NULL, NULL, ''),
('https://cplusplus.com', 29, 1, 'Thread29', -1, '2022-03-26 18:28:36', 0, '', NULL, NULL, NULL, NULL, ''),
('https://developer.mozilla.org/en-US/', 30, 1, 'Thread30', -1, '2022-03-26 18:28:36', 0, 'The MDN Web Docs site provides information about Open Web technologies including HTML, CSS, and APIs for both Web sites and progressive web apps.', NULL, NULL, NULL, NULL, ''),
('https://www.geeksforgeeks.org', 31, 1, 'Thread31', -1, '2022-03-26 18:28:36', 0, 'A Computer Science portal for geeks. It contains well written, well thought and well explained computer science and programming articles, quizzes and practice/competitive programming/company interview Questions.', NULL, NULL, NULL, NULL, ''),
('https://www.tutorialspoint.com', 32, 1, 'Thread32', -1, '2022-03-26 18:28:36', 0, 'Online Tutorials Library - The Best Content on latest technologies including C, C++, Java, Python, PHP, Machine Learning, Data Science, AppML, AI with Python, Behave, Java16, Spacy.', NULL, NULL, NULL, NULL, ''),
('https://www.nature.com', 33, 1, 'Thread33', -1, '2022-03-26 18:28:36', 0, 'First published in 1869, Nature is the world’s leading multidisciplinary science journal. Nature publishes the finest peer-reviewed research that drives ground-breaking discovery, and is read by thought-leaders and decision-makers around the world.', NULL, NULL, NULL, NULL, ''),
('https://www.yourbrainonporn.com', 34, 1, 'Thread34', -1, '2022-03-26 18:28:36', 0, 'Evolution has not prepared your brain for today’s porn. Are you curious about the latest research on internet porn s effects? Wondering about erectile dysfunction, inability to orgasm or low libido? Escalation to extreme material? A lack of desire for partnered sex? Social anxiety, cognitive problems, lack of motivation? You re in the right place.', NULL, NULL, NULL, NULL, ''),
('https://www.science.org', 35, 1, 'Thread35', -1, '2022-03-26 18:28:36', 0, 'AAAS, an international nonprofit scientific association established in 1849, publishes: Science, Science Advances, Science Immunology, Science Robotics, Science Signaling and Science Translational Medicine.  Our journals are essential to fulfilling the AAAS mission to  advance science, engineering, and innovation throughout the world for the benefit of all people.   By publishing the very best in scientific research, commentary and news, the Science family of journals furthers the AAAS goal to  enhance communication among scientists, engineers, and the public. ', NULL, NULL, NULL, NULL, ''),
('https://www.nationalgeographic.com', 36, 1, 'Thread36', -1, '2022-03-26 18:28:36', 0, 'Explore National Geographic. A world leader in geography, cartography and exploration.', NULL, NULL, NULL, NULL, ''),
('https://vpnoverview.com', 37, 1, 'Thread37', -1, '2022-03-31 14:37:10', 0, 'All you need to know about VPNs, privacy, unblocking, and protection. Learn how to browse the web anonymously, safely, and freely.', NULL, NULL, NULL, NULL, ''),
('https://match.koragol.com', 38, 1, 'Thread38', -1, '2022-03-31 14:55:56', 0, 'كورة جول koooragoal أفضل المواقع لمشاهدة البث المباشر لمباريات اليوم جوال مجانا kooragoal لايف حصري على عدة سيرفرات يوتيوب بدون تقطيع كووورة جول kora goal.', NULL, NULL, NULL, NULL, ''),
('https://www.merriam-webster.com', 39, 1, 'Thread39', -1, '2022-03-31 15:08:17', 0, 'The dictionary by Merriam-Webster is America s most trusted online dictionary for English word definitions, meanings, and pronunciation. #wordsmatter', NULL, NULL, NULL, NULL, ''),
('https://en.wikipedia.org', 40, 1, 'Thread40', -1, '2022-03-31 15:10:11', 0, '', NULL, NULL, NULL, NULL, ''),
('https://developers.google.com', 41, 1, 'Thread41', -1, '2022-03-31 15:15:57', 0, 'Everything you need to build better apps.', NULL, NULL, NULL, NULL, ''),
('https://en.ryte.com', 42, 1, 'Thread42', -1, '2022-03-31 15:15:57', 0, 'Get more traffic, improve your website’s usability, reduce legal risks and increase conversion rates with Ryte: the leading platform for SEO, Quality Assurance and Performance.', NULL, NULL, NULL, NULL, ''),
('https://johnnn.tech', 43, 1, 'Thread43', -1, '2022-03-31 15:18:16', 0, 'range of library barcode labels, barcode scanners & borrower library cards. C...', NULL, NULL, NULL, NULL, '');

-- --------------------------------------------------------

--
-- Table structure for table `threads`
--

CREATE TABLE `threads` (
  `ThreadName` varchar(50) NOT NULL,
  `Layer` int(11) NOT NULL,
  `UrlIndex` int(11) NOT NULL,
  `UrlIndex1` int(11) NOT NULL DEFAULT 0,
  `UrlIndex2` int(11) NOT NULL DEFAULT 0,
  `UrlIndex3` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `threads`
--

INSERT INTO `threads` (`ThreadName`, `Layer`, `UrlIndex`, `UrlIndex1`, `UrlIndex2`, `UrlIndex3`) VALUES
('Thread1', 1, 0, 1, 1, 1),
('Thread10', 1, 1, 1, 1, 1),
('Thread11', 1, 1, 1, 1, 1),
('Thread12', 1, 1, 1, 1, 1),
('Thread13', 1, 1, 1, 1, 1),
('Thread14', 1, 1, 1, 1, 1),
('Thread15', 1, 1, 1, 1, 1),
('Thread16', 1, 1, 1, 1, 1),
('Thread17', 1, 1, 1, 1, 1),
('Thread18', 1, 1, 1, 1, 1),
('Thread19', 1, 1, 1, 1, 1),
('Thread2', 1, 1, 1, 1, 1),
('Thread20', 1, 1, 1, 1, 1),
('Thread21', 1, 1, 1, 1, 1),
('Thread22', 1, 1, 1, 1, 1),
('Thread23', 1, 1, 1, 1, 1),
('Thread24', 1, 1, 1, 1, 1),
('Thread25', 1, 1, 1, 1, 1),
('Thread26', 1, 1, 1, 1, 1),
('Thread27', 1, 1, 1, 1, 1),
('Thread28', 1, 1, 1, 1, 1),
('Thread29', 1, 1, 1, 1, 1),
('Thread3', 1, 1, 1, 1, 1),
('Thread30', 1, 1, 1, 1, 1),
('Thread31', 1, 1, 1, 1, 1),
('Thread32', 1, 1, 1, 1, 1),
('Thread33', 1, 1, 1, 1, 1),
('Thread34', 1, 1, 1, 1, 1),
('Thread35', 1, 1, 1, 1, 1),
('Thread36', 1, 1, 1, 1, 1),
('Thread37', 1, 1, 1, 1, 1),
('Thread38', 1, 1, 1, 1, 1),
('Thread39', 1, 1, 1, 1, 1),
('Thread4', 1, 1, 1, 1, 1),
('Thread40', 1, 1, 1, 1, 1),
('Thread41', 1, 1, 1, 1, 1),
('Thread42', 1, 1, 1, 1, 1),
('Thread43', 1, 1, 1, 1, 1),
('Thread5', 1, 1, 1, 1, 1),
('Thread6', 1, 1, 1, 1, 1),
('Thread7', 1, 1, 1, 1, 1),
('Thread8', 1, 1, 1, 1, 1),
('Thread9', 1, 1, 1, 1, 1);

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
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=175379;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
