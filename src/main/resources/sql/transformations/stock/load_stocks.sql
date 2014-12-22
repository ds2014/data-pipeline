SELECT
	t.stock_id,
	t.stock_type_id,
	c.dbxref_id cv_term_dbxref,
	6 organism_id ,
	t.name,
	t.name uniquename,
	t.description description,
	c.cvterm_id as type_id,
	c.name as type,
	0 is_obsolete
FROM
	tair_stg.stock t JOIN tair_stg.stocktype st
		ON
		t.stock_type_id = st.stock_type_id JOIN cvterm c
		ON
		c.name = st.stock_type JOIN dbxref dbx
		ON
		dbx.dbxref_id = c.dbxref_id JOIN dbxref acc
		ON
		cast(
	t.stock_id as varchar(
	255)
	)
	= acc.accession AND
	acc.db_id = staging.get_tair_db_id_by_name(
	'TAIR Stock')
WHERE
	dbx.db_id = 1 LIMIT 2