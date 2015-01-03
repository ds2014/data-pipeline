DROP TABLE IF EXISTS staging.stockprop_bk CASCADE;
CREATE TABLE staging.stockprop_bk AS SELECT * FROM staging.stockprop;