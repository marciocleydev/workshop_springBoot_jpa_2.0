-- faz insert de 10 produtos em cada categoria aleatoriamente
INSERT INTO tb_category_product (id_category, id_product)
SELECT c.id AS id_category, p.id AS id_product
FROM (SELECT id FROM tb_product WHERE id >= 4 AND id <= 12) p
         CROSS JOIN (SELECT id FROM tb_category ORDER BY RAND() LIMIT 10) c
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_category_product cp
    WHERE cp.id_category = c.id
      AND cp.id_product = p.id
);
