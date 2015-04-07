DROP MATERIALIZED VIEW IF EXISTS staging.external_dbxref_transformation CASCADE;
CREATE
	MATERIALIZED VIEW staging.external_dbxref_transformation AS
select distinct 
accession,
db_name
from staging.external_url_transformation s;