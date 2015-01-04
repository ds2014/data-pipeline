select count(1) from
(

SELECT
	dbx.dbxref_id as dbxref_id,
	db.db_id as db_id
FROM
	dbxref dbx JOIN db
		ON
		dbx.db_id = db.db_id
WHERE
	db.db_id IN (
	select 
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int) as db_id
	)) v
