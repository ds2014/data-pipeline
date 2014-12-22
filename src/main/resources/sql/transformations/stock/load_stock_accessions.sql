WITH source AS (
SELECT
	stock_id,
	name as stock_name,
	cast(tair_object_id as varchar(255)) as primary_accession,
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int)
	db_id,
	cast (
	'' as varchar(255))
	as version_db,
	cast (
	'TAIR Stock Primary Accession' as text)
	as description
FROM
	tair_stg.stock limit 2 )
SELECT
	*
FROM
	source;