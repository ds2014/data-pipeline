	WITH source AS(
SELECT
	t.stock_type_id,
	t.stock_type,
	staging.get_dbxref_by_accession(
	'stock_type')
	dbxref_id,
	0 is_obsolete,
	0 is_relationshiptype
FROM
	public.dblink(
	'dbname=tair_stock_tables host=pgsql-lan-dev user=tripal password=iPv8yR44s',
	'select stock_type_id, stock_type from stocktype')
	AS t(
	stock_type_id int,
	stock_type text )
	)
	,
	upd AS(
UPDATE
	chado.cvterm t
SET
	cv_id = s.stock_type_id,
	name = s.stock_type,
	definition = null,
	dbxref_id = s.dbxref_id,
	is_obsolete = 0,
	is_relationshiptype = 0
FROM
	source s
WHERE
	t.cv_id = s.stock_type_id RETURNING t.cv_id,
	t.name,
	t.definition,
	t.dbxref_id,
	t.is_obsolete,
	t.is_relationshiptype )
INSERT
	INTO chado.cvterm (
	cv_id,
	name,
	definition,
	dbxref_id,
	is_obsolete,
	is_relationshiptype)
SELECT
	s.stock_type_id,
	s.stock_type,
	null definition,
	s.dbxref_id,
	s.is_obsolete,
	s.is_relationshiptype
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.cv_id = s.stock_type_id
WHERE
	t.cv_id IS NULL
GROUP BY
	s.stock_type_id,
	s.stock_type,
	definition,
	s.dbxref_id,
	s.is_obsolete,
	s.is_relationshiptype

