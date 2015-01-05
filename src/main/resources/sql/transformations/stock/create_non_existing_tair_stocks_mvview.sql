DROP MATERIALIZED VIEW IF EXISTS staging.tair_non_existing_stocks CASCADE;
CREATE MATERIALIZED VIEW staging.tair_non_existing_stocks AS
	WITH source AS(
SELECT
	s.stock_id,
	acc.dbxref_id,
	c.cvterm_id as type_id,
	cast(
	s.name as varchar(
	255)
	)
	as name,
	cast(
	s.name as text)
	as uniquename,
	cast(
	s.description as text)
	as description,
	cast (
	s.is_obsolete as boolean)
	as is_obsolete
FROM
	tair_stg.stock s JOIN tair_stg.stocktype st
		ON
		s.stock_type_id = st.stock_type_id JOIN cvterm c
		ON
		c.name = st.stock_type JOIN dbxref dbx
		ON
		dbx.dbxref_id = c.dbxref_id JOIN dbxref acc
		ON
		cast(
	s.stock_id as varchar(
	255)
	)
	= acc.accession AND
	acc.db_id = staging.get_tair_db_id_by_name(
	'TAIR Stock')
	AND
	dbx.db_id = 1)
	,
	matching_records AS(
SELECT
	s.stock_id,
	o.organism_id
FROM
	source s JOIN chado.stock t
		ON
		t.stock_id = s.stock_id AND
	t.type_id = s.type_id AND
	t.uniquename = s.name JOIN chado.organism o
		ON
		t.organism_id = o.organism_id
WHERE
	o.common_name = 'mouse-ear cress')
SELECT
	s.stock_id,
	s.dbxref_id,
	s.type_id,
	s.name,
	s.description,
	s.is_obsolete
FROM
	source s
WHERE
	NOT EXISTS (
SELECT
	stock_id
FROM
	matching_records t
WHERE
	s.stock_id = t.stock_id );
CREATE INDEX tair_stock_idx on staging.tair_non_existing_stocks (stock_id);