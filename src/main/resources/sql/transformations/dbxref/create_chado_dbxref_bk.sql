DROP TABLE IF EXISTS staging.dbxref_bk CASCADE;
CREATE TABLE staging.dbxref_bk AS SELECT * FROM chado.dbxref;