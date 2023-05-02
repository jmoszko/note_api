--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` int(11) NOT NULL,
  `content` longtext DEFAULT NULL,
  `creation_time` time DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKga1qdqmqxhsrx4ee2gui0lnsn` (`author_id`),
  CONSTRAINT `FKga1qdqmqxhsrx4ee2gui0lnsn` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
