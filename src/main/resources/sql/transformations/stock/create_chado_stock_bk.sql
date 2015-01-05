DROP TABLE IF EXISTS staging.stock_bk CASCADE;
CREATE TABLE staging.stock_bk AS SELECT * FROM chado.stock;