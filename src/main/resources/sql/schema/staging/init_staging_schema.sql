CREATE SCHEMA IF NOT EXISTS staging;

SET search_path = chado, public, batch, tair_stg, staging;

--DROP DB STRUCTURES

DROP TABLE IF EXISTS staging.stockprop CASCADE;
DROP MATERIALIZED VIEW IF EXISTS staging.stock_properties_all CASCADE;


-- CREATE STAGING STRUCTURES
CREATE TABLE staging.stockprop (
	stockprop_id bigint,
	stock_id bigint,
	type_id int4 ,
	"value" text,
	"rank" int4 DEFAULT 0
	);

CREATE materialized view staging.stock_properties_all
AS
WITH source as (
SELECT
	*, cast ('F' as text) as is_germplasm
FROM
	tair_stg.stock )
	,
	transpose as (
SELECT
	source.stock_id,
	unnest(
	array[
	'date_entered',
	'date_last_modified',
	'release date',
	'growth_temperature',
	'duration_of_growth',
	'kit_contents',
	'number_in_set',
	'box',
	'position',
	 'num_lines',
	 'location',
	 'has_stock_notes',
	 'abrc_comments',
	 'format_shipped',
	 'format_received',
	'media',
	 'is_germplasm',
	 'is_seed',
	 'is_restricted',
	 'is_molecular_mapping',
	  'is_classical_mapping'
	]
	)
	as key,
	unnest(
	array[
	cast(source.date_entered as text),
	cast(source.date_last_modified as text),
	cast(source.release_date as text),
	source.growth_temperature,
	source.duration_of_growth,
	source.kit_contents,
	cast(source.number_in_set as text),
	source.box,
	source.position,
	cast(source.num_lines as text),
	source.location,
	source.has_stock_notes,
	source.abrc_comments,
	source.format_shipped,
	source.format_received,
	source.media,
	source.is_germplasm,
	source.is_seed,
	source.is_restricted,
	source.is_molecular_mapping,
	source.is_classical_mapping
		]
	)
	as value
FROM
	source
ORDER BY
	source.stock_id
	)
SELECT
	*
FROM
	transpose t
	join 
	(
	select t.name, t.cvterm_id as type_id from
chado.cv 
join 
chado.cvterm t
on 
cv.cv_id = t.cv_id
where
cv.name='stock_property') v
on 
t.key = v.name
	
WHERE
	value IS NOT null;
	
CREATE INDEX stock_idx
  ON staging.stock_properties_all (stock_id);



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

