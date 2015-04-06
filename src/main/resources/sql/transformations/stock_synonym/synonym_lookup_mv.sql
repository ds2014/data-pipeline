DROP MATERIALIZED VIEW IF EXISTS staging.synonym_lookup_mv CASCADE;

CREATE MATERIALIZED VIEW staging.synonym_lookup_mv AS
select distinct note ,
case when (trim(note) = 'NASC stock number')
	then 'NASC_stock_number'
	when (trim(note) = 'named by stock donor')
	then 'ABRC_donor_stock_number'
	when (trim(note) = 'donor name')
	then 'ABRC_donor_stock_number'
	when (trim(note) = 'stock donor other name')
	then 'ABRC_donor_stock_number_other'
	when (trim(note) = 'NASC stock id')
	then 'NASC_stock_id'
	when (trim(note) = 'ABRC other number')
	then 'ABRC_other_number'
	when (trim(note) = '')
	then 'undefined'
	when (trim(note) is null)
	then 'undefined'
	else
	trim(note)
end synonym_type
from staging.germplasm_alias;