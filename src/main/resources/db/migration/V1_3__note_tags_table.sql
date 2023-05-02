--
-- Table structure for table `note_tags`
--

DROP TABLE IF EXISTS `note_tags`;
CREATE TABLE `note_tags` (
  `note_id` int(11) NOT NULL,
  `tags_tag` varchar(255) NOT NULL,
  PRIMARY KEY (`note_id`,`tags_tag`),
  KEY `FK5h92lbkb4ijn7u159b3offfsi` (`tags_tag`),
  CONSTRAINT `FK5h92lbkb4ijn7u159b3offfsi` FOREIGN KEY (`tags_tag`) REFERENCES `tag` (`tag`),
  CONSTRAINT `FK6a4cyw47043aaai8rbthb33w7` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
