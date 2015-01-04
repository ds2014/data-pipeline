DROP MATERIALIZED VIEW IF EXISTS staging.dbxref_not_existing_primary_stock_accessions;
CREATE
	materialized view staging.dbxref_not_existing_primary_stock_accessions AS
SELECT
	cast (stock_id as bigint) as accession,
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int)
	db_id
FROM
	tair_stg.stock
	EXCEPT
	ALL
SELECT
	cast(dbx.accession as bigint) asaccession,
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
	);
CREATE INDEX accession_idx
  ON staging.dbxref_not_existing_primary_stock_accessions (accession);

