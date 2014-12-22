WITH source AS (
SELECT
	cast (stock_id as varchar(255)) as primary_accession,
	name as stock_name,
	cast(tair_object_id as varchar(255)) as secondary_accession,
	cast (
	staging.get_tair_db_id_by_name(
	'TAIR Stock')
	as int)
	db_id,
	cast (
	'' as varchar(255))
	as version_db,
	cast (
	'TAIR Stock Primary Accession' as text)
	as description
FROM
	tair_stg.stock limit 2 ),
	
	upd AS(
UPDATE
	chado.dbxref t
SET
	db_id = s.db_id,
	accession = s.primary_accession,
	version = s.version_db,
	description = s.description
FROM
	source s
WHERE
	t.db_id = s.db_id AND
	t.accession = s.primary_accession
	RETURNING 
	t.db_id,
	t.accession,
	t.version,
	t.description)
INSERT
	INTO chado.dbxref (
	db_id,
	accession,
	version,
	description)
SELECT
	s.db_id,
	s.primary_accession,
	s.version_db,
	s.description
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.db_id = s.db_id AND
	t.accession = s.primary_accession
WHERE
	(
	t.db_id IS NULL)
	AND
	(
	t.accession IS NULL)
GROUP BY
	s.db_id,
	s.primary_accession,
	s.version_db,
	s.description;