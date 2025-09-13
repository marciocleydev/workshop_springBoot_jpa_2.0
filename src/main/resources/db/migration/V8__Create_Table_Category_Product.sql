-- Dumping structure for table salesapp.tb_category_product
CREATE TABLE IF NOT EXISTS `tb_category_product`
(
    `id_category` bigint NOT NULL,
    `id_product`  bigint NOT NULL,
    PRIMARY KEY (`id_category`, `id_product`),
    KEY `FKiqkoyjrug53yl680re9gvbguo` (`id_product`),
    CONSTRAINT `FK8se264iaqrk1gcy5gf1fb2hor` FOREIGN KEY (`id_category`) REFERENCES `tb_category` (`id`),
    CONSTRAINT `FKiqkoyjrug53yl680re9gvbguo` FOREIGN KEY (`id_product`) REFERENCES `tb_product` (`id`)
);