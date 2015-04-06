DROP MATERIALIZED VIEW IF EXISTS staging.dbxref_not_existing_stock_accessions;
CREATE
	materialized view staging.dbxref_not_existing_stock_accessions AS
	WITH source AS(
SELECT
	distinct
	cast (g.stock_id as bigint) as accession,
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int)
	db_id
FROM
	staging.germplasm_stock_info g
	EXCEPT
	ALL
SELECT
	cast(dbx.accession as bigint) as accession,
	db.db_id as db_id
FROM
	dbxref dbx JOIN db
		ON
		dbx.db_id = db.db_id
WHERE
	db.db_id IN (
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int)
	))
	
SELECT s.*, cast ('' as text) as version, cast('TAIR Stock Accession' as text) as description FROM source s
where s.accession is not null;