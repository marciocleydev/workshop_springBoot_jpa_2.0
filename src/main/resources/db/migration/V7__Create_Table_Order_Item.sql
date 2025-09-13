-- Dumping structure for table salesapp.tb_order_item
CREATE TABLE IF NOT EXISTS `tb_order_item` (
                                               `id_order` bigint NOT NULL,
                                               `id_product` bigint NOT NULL,
                                               `price` double DEFAULT NULL,
                                               `quantity` int DEFAULT NULL,
                                               PRIMARY KEY (`id_order`,`id_product`),
                                               KEY `FKsyqvyxrypqwkjdbe64m1macnu` (`id_product`),
                                               CONSTRAINT `FK90dn0gmn8mf14qcj49vcu6h2u` FOREIGN KEY (`id_order`) REFERENCES `tb_order` (`id`),
                                               CONSTRAINT `FKsyqvyxrypqwkjdbe64m1macnu` FOREIGN KEY (`id_product`) REFERENCES `tb_product` (`id`)
) ;