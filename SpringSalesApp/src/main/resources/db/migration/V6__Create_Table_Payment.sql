-- Dumping structure for table salesapp.tb_payment
CREATE TABLE IF NOT EXISTS `tb_payment` (
                                            `moment` datetime(6) DEFAULT NULL,
                                            `order_id` bigint NOT NULL,
                                            PRIMARY KEY (`order_id`),
                                            CONSTRAINT `FKokaf4il2cwit4h780c25dv04r` FOREIGN KEY (`order_id`) REFERENCES `tb_order` (`id`)
);