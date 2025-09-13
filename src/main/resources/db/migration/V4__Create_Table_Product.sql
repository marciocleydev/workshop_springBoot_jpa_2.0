-- Dumping structure for table salesapp.tb_product
CREATE TABLE IF NOT EXISTS `tb_product` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `price` double DEFAULT NULL,
                                            `description` varchar(255) DEFAULT NULL,
                                            `img_url` varchar(255) DEFAULT NULL,
                                            `name` varchar(255) DEFAULT NULL,
                                            `enabled` bit(1) NOT NULL,
                                            PRIMARY KEY (`id`)
);