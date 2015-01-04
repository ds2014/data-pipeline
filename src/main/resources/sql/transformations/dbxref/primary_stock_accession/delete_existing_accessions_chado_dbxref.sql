DELETE
FROM
	dbxref
WHERE
	dbxref_id IN (
SELECT
	dbx.dbxref_id
FROM
	dbxref dbx JOIN db
		ON
		dbx.db_id = db.db_id
WHERE
	db.db_id IN (
SELECT
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int)
	as db_id )
	)