WITH source AS(
SELECT
	t.stock_type_id,
	t.stock_type,
	dbx.dbxref_id,
	dbx.db_id,
	0 is_obsolete,
	0 is_relationshiptype
FROM
	tair_stg.stocktype t
	JOIN dbxref dbx
		ON
		dbx.accession = t.stock_type
WHERE
	dbx.db_id = staging.get_tair_db_id_by_name(
	'TAIR')
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
	t.cv_id = s.stock_type_id AND
	t.dbxref_id = s.dbxref_id
	RETURNING t.cv_id,
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
		t.cv_id = s.stock_type_id AND
	t.dbxref_id = s.dbxref_id
WHERE
	(
	t.cv_id IS NULL)
	AND
	(
	t.dbxref_id IS NULL)
GROUP BY
	s.stock_type_id,
	s.stock_type,
	definition,
	s.dbxref_id,
	s.is_obsolete,
	s.is_relationshiptype