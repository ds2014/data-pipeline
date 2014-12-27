	WITH source AS (
SELECT
	s.stock_id,
	acc.dbxref_id,
	6 organism_id,
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
	c.cvterm_id as type_id,
	cast (s.is_obsolete as boolean) as is_obsolete
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
WHERE
	dbx.db_id = 1 
	
	and stock_id = 1000
	 ),
	
	upd AS(
UPDATE
	chado.stock t
SET
    stock_id = s.stock_id,
	dbxref_id = s.dbxref_id,
	organism_id = s.organism_id,
	name = s.name,
	uniquename = s.name,
	description = s.description,
	type_id = s.type_id,
	is_obsolete = s.is_obsolete
	
FROM
	source s
WHERE
	t.stock_id = s.stock_id
	RETURNING 
	t.stock_id,
	t.dbxref_id,
	t.organism_id,
	t.name,
	t.uniquename,
	t.description,
	t.type_id,
	t.is_obsolete
	)
INSERT
	INTO chado.stock (
	stock_id,
	dbxref_id,
	organism_id,
	name,
	uniquename,
	description,
	type_id,
	is_obsolete
	)
SELECT
	s.stock_id,
	s.dbxref_id,
	s.organism_id,
	s.name,
	s.uniquename,
	s.description,
	s.type_id,
	s.is_obsolete
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.stock_id = s.stock_id
WHERE
	(t.stock_id IS NULL)
GROUP BY
	s.stock_id,
	s.dbxref_id,
	s.organism_id,
	s.name,
	s.uniquename,
	s.description,
	s.type_id,
	s.is_obsolete;
