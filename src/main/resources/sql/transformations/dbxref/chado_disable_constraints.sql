ALTER
	TABLE chado.dbxref
DROP
	CONSTRAINT IF EXISTS dbxref_pkey CASCADE;

ALTER
	TABLE chado.dbxref
DROP
	CONSTRAINT IF EXISTS dbxref_db_id_fkey CASCADE;
ALTER
	TABLE chado.dbxref
DROP
	CONSTRAINT IF EXISTS dbxref_c1;
ALTER
	TABLE chado.dbxref
ALTER
	dbxref_id
DROP
	NOT NULL,
ALTER
	db_id
DROP
	NOT NULL,
ALTER
	accession
DROP
	NOT NULL,
ALTER
	version
DROP
	NOT NULL;
