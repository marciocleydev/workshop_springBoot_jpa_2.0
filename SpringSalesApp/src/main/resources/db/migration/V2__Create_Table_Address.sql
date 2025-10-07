-- Dumping structure for table salesapp.tb_address
CREATE TABLE IF NOT EXISTS `tb_address` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `id_user` bigint DEFAULT NULL,
                                            `zip_code` varchar(9) NOT NULL,
                                            `number` varchar(10) NOT NULL,
                                            `state` varchar(10) NOT NULL,
                                            `country` varchar(20) NOT NULL,
                                            `city` varchar(50) NOT NULL,
                                            `street` varchar(50) NOT NULL,
                                            `complement` varchar(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `UKeg37lu0wi6hw4oc3udvir251o` (`id_user`),
                                            CONSTRAINT `FKesu5joqddincpa7t76t0876wy` FOREIGN KEY (`id_user`) REFERENCES `tb_user` (`id`)
);