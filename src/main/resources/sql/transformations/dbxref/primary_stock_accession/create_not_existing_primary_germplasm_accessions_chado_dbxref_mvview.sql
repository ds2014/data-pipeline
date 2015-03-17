DROP MATERIALIZED VIEW IF EXISTS staging.dbxref_not_existing_primary_germplasm_accessions;
CREATE
	materialized view staging.dbxref_not_existing_primary_germplasm_accessions AS
SELECT
	cast (tair_object_id as bigint) as accession,
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Germplasm')
	as int)
	db_id
FROM
	staging.germplasm_stock_info
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
	'TAIR Germplasm')
	as int)
	);
	
CREATE INDEX gm_accession_idx
  ON staging.dbxref_not_existing_primary_germplasm_accessions (accession);

