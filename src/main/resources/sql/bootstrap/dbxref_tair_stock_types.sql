WITH source AS(
SELECT
	t.stock_type_id,
	t.stock_type,
	staging.get_tair_db_id_by_name(
	'TAIR')
	db_id,
	cast (
	'' as text)
	version_db,
	cast (
	'TAIR Stock Types' as text)
	description
FROM
	tair_stg.stocktype t)
	,
	upd AS(
UPDATE
	chado.dbxref t
SET
	db_id = s.db_id,
	accession = s.stock_type,
	version = s.version_db,
	description = s.description
FROM
	source s
WHERE
	t.db_id = s.db_id AND
	t.accession = s.stock_type
	RETURNING 
	t.db_id,
	t.accession,
	t.version,
	t.description )
INSERT
	INTO chado.dbxref (
	db_id,
	accession,
	version,
	description)
SELECT
	s.db_id,
	s.stock_type,
	s.version_db,
	s.description
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.db_id = s.db_id AND
	t.accession = s.stock_type
WHERE
	(
	t.db_id IS NULL)
	AND
	(
	t.accession IS NULL)
GROUP BY
	s.stock_type_id,
	s.stock_type,
	s.db_id,
	s.version_db,
	s.description;
