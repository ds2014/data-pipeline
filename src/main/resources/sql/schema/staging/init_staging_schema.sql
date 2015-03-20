CREATE SCHEMA IF NOT EXISTS staging;

SET search_path = chado, public, batch, tair_stg, staging;


DROP TABLE IF EXISTS staging.stockprop CASCADE;
DROP TABLE IF EXISTS staging.stock_cvterm CASCADE;

DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties_all CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.tair_non_existing_stocks CASCADE;

--ALTER TABLE chado.stock ALTER COLUMN stock_id TYPE bigint;
--ALTER TABLE chado.stock_dbxref ALTER COLUMN stock_id TYPE bigint;

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

CREATE OR REPLACE FUNCTION staging.get_dbxref_by_accession (text)
   RETURNS int
AS
   $$
   DECLARE
      result   integer;
   BEGIN
      SELECT
       INTO result      dbxref_id
       FROM chado.dbxref
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
       FROM chado.db
      WHERE name =$1;

      RETURN result;
   END;
   $$
   LANGUAGE plpgsql;
   
   
   CREATE OR REPLACE FUNCTION staging.match_mutagen (text)
   RETURNS varchar
AS
   $$
   DECLARE
      result   varchar;
   BEGIN
   		IF $1 IS NOT NULL THEN
		IF $1 = 'T-DNA insertion'  THEN
			result := 't_dna_insertion';
		ELSEIF $1 = 'ethylmethane sulfonate' THEN
			result := 'ethylmethane_sulfonate';
		ELSEIF $1 = 'diepoxybutane' THEN
			result := 'diepoxybutane';
		ELSEIF $1 = 'nitrosomethyl urea' THEN
			result := 'nitrosomethyl_urea';
		ELSEIF $1 = 'HZE Ne' THEN
			result := 'hze_ne';
		ELSEIF $1 = 'dsRNA silencing' THEN
			result := 'dsRNA_silencing';
		ELSEIF $1 = 'X-rays' THEN
			result := 'x_rays';
		ELSEIF $1 = 'fast neutrons' THEN
			result := 'fast_neutrons';
		ELSEIF $1 = 'ethyl-nitrosourea' THEN
			result := 'ethyl_nitrosourea';
		ELSEIF $1 = 'tissue culture' THEN
			result := 'tissue_culture';
		ELSEIF $1 = 'spontaneous' THEN
			result := 'spontaneous';
		ELSEIF $1 = 'HZE U' THEN
			result := 'hze_u';
		ELSEIF $1 = 'nitrosomethyl biuret' THEN
			result := 'nitrosomethyl_biuret';
		ELSEIF $1 = 'Agrobacterium transformation' THEN
			result := 'agrobacterium_transformation';
		ELSEIF $1 = 'ionizing radiation' THEN
			result := 'ionizing_radiation';	
		ELSEIF $1 = '5-bromouracil' THEN
			result := '5_bromouracil';	
		ELSEIF $1 = 'sodium ascorbate' THEN
			result := 'sodium_ascorbate';	
		ELSEIF $1 = 'unknown' THEN
			result := 'unknown';	
		ELSEIF $1 = 'carbon ionization' THEN
			result := 'carbonionization';
		ELSEIF $1 = 'carbonionization' THEN
			result := 'carbonionization';	
		ELSEIF $1 = 'transposon insertion' THEN
			result := 'transposon_insertion';
		ELSEIF $1 = 'zinc finger nuclease' THEN
			result := 'zinc_finger_nuclease';
		ELSEIF $1 = 'HZE Kr' THEN
			result := 'hze_kr';
		ELSEIF $1 = 'HZE C' THEN
			result := 'hze_c';
		ELSEIF $1 = 'nitroguanidine' THEN
			result := 'nitroguanidine';
		ELSEIF $1 = 'gamma rays' THEN
			result := 'gamma_rays';
		ELSEIF $1 = '' THEN
			result := 'unknown';
		ELSE
			result := $1;
			
		END IF;
	ELSE
		result := NULL;
	END IF;
       		       
      RETURN result;
   END;
   $$
   LANGUAGE plpgsql;
   
   
  CREATE OR REPLACE FUNCTION staging.match_cvterm_by_name_cv (text, text)
   RETURNS int
AS
   $$
   DECLARE
      result   int;
    BEGIN
      SELECT
       INTO result  cvterm_id
       FROM chado.cvterm c
	   		join cv on
			cv.cv_id = c.cv_id
      WHERE c.name =$1 and cv.name=$2;

      RETURN result;
   END;
   $$
   LANGUAGE plpgsql;