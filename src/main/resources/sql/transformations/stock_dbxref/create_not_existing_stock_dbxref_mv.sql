DROP MATERIALIZED VIEW IF EXISTS staging.dbxref_not_existing_stock_dbxref;
CREATE
	materialized view staging.dbxref_not_existing_stock_dbxref AS
WITH source_dbxref  as (
select dbx.dbxref_id from chado.dbxref dbx
JOIN db
		ON
		dbx.db_id = db.db_id
WHERE
	db.db_id IN (
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int))
except
select stf.dbxref_id from stock_dbxref stf
join
dbxref dbx on 
dbx.dbxref_id 	= stf.dbxref_id JOIN db
		ON
		dbx.db_id = db.db_id
WHERE
	db.db_id IN (
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int))),
source_dbxref_accession as (
select dbxref_id, accession from dbxref dbx
where dbx.dbxref_id in 
(
select dbxref_id from 
source_dbxref
))

SELECT
	distinct
	cast (g.stock_id as bigint) as accession,
	st.stock_id,
	dba.dbxref_id
FROM
	staging.germplasm_stock_info g
	join
	chado.stock st
	on st.stock_id = g.germplasm_id 
	join 
	source_dbxref_accession dba
	on cast(dba.accession as bigint) = cast (g.stock_id as bigint);

