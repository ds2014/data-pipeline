INSERT INTO chado.dbxref
(dbxref_id, accession, db_id, version, description)
select
nextval(pg_get_serial_sequence('chado.dbxref', 'dbxref_id')) as dbxref_id,
accession,
db_id,
version,
description
from
staging.dbxref_not_existing_stock_accessions;