-- Dumping structure for table salesapp.tb_order
CREATE TABLE IF NOT EXISTS `tb_order` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `status` int DEFAULT NULL,
                                          `moment` datetime(6) DEFAULT NULL,
                                          `id_client` bigint DEFAULT NULL,
                                          PRIMARY KEY (`id`),
                                          KEY `FKr21spbxmmj12o6tt6fye1xe5s` (`id_client`),
                                          CONSTRAINT `FKr21spbxmmj12o6tt6fye1xe5s` FOREIGN KEY (`id_client`) REFERENCES `tb_user` (`id`)
);