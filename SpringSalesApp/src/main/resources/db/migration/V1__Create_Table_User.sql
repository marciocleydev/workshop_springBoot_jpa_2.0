-- Dumping structure for table salesapp.tb_user
CREATE TABLE IF NOT EXISTS `tb_user` (
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `email` varchar(255) DEFAULT NULL,
                                         `name` varchar(255) DEFAULT NULL,
                                         `pass_word` varchar(255) DEFAULT NULL,
                                         `phone` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
);