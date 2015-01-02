CREATE SCHEMA IF NOT EXISTS staging;

SET search_path = chado, public, batch, tair_stg, staging;

--DROP DB STRUCTURES

DROP TABLE IF EXISTS staging.stockprop CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties_all CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties CASCADE;



-- CREATE STAGING STRUCTURES
CREATE TABLE staging.stockprop (
    stockprop_id bigint default staging.id_generator(),
	stock_id int8,
	type_id int4,
	"value" text,
	"rank" int4 DEFAULT 0
);



CREATE OR REPLACE FUNCTION staging.get_dbxref_by_accession (text)
   RETURNS int
AS
   $$
   DECLARE
      result   integer;
   BEGIN
      SELECT
       INTO result      dbxref_id
       FROM dbxref
      WHERE accession = $1;

      RETURN result;
   END;
   $$
   LANGUAGE plpgsql;

END

CREATE OR REPLACE FUNCTION staging.get_tair_db_id_by_name (text)
   RETURNS int
AS
   $$
   DECLARE
      result   integer;
   BEGIN
      SELECT
       INTO result      db_id
       FROM db
      WHERE name =$1;

      RETURN result;
   END;
   $$
   LANGUAGE plpgsql;

