CREATE SCHEMA IF NOT EXISTS staging;

SET search_path = chado, public, batch, tair_stg, staging;


DROP TABLE IF EXISTS staging.stockprop CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties_all CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties CASCADE;

DROP SEQUENCE IF EXISTS staging.global_id_sequence CASCADE;
CREATE SEQUENCE staging.global_id_sequence;

CREATE OR REPLACE FUNCTION staging.id_generator(OUT result bigint) AS $$  
DECLARE  
    our_epoch bigint := 1314220021721;
    seq_id bigint;
    now_millis bigint;
    shard_id int := 1;
BEGIN  
    SELECT nextval('staging.global_id_sequence') % 1024 INTO seq_id;

    SELECT FLOOR(EXTRACT(EPOCH FROM clock_timestamp()) * 1000) INTO now_millis;
    result := (now_millis - our_epoch) << 23;
    result := result | (shard_id << 10);
    result := result | (seq_id);
END;  
$$ LANGUAGE PLPGSQL;


CREATE TABLE staging.stockprop (
    stockprop_id bigint default staging.id_generator(),
	stock_id bigint,
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