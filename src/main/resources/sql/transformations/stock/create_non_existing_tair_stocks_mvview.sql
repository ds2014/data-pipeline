DROP MATERIALIZED VIEW IF EXISTS staging.tair_non_existing_stocks CASCADE;
CREATE MATERIALIZED VIEW staging.tair_non_existing_stocks AS
WITH matching_records as (
SELECT
	s.dbxref_id,
	s.type_id,
	s.uniquename,
	s.organism_id,
	s.germplasm_id stock_id
FROM
	staging.germplasm_source s JOIN chado.stock t
	on
    s.germplasm_id = t.stock_id),

result as (
SELECT
	s.organism_id,
	s.dbxref_id,
	s.type_id,
	s.name,
	s.uniquename,
	s.description,
	s.is_obsolete,
	s.germplasm_id stock_id
	
FROM
	staging.germplasm_source s
WHERE
	NOT EXISTS (
SELECT
	t.stock_id
FROM
	matching_records t
WHERE
	 s.germplasm_id = t.stock_id
	 ))
	 
select s.* from result s;

create index organism_gm_stock_idx on staging.tair_non_existing_stocks(organism_id);
create index dbxref_gm_stock_idx on staging.tair_non_existing_stocks(dbxref_id);
create index type_gm_stock_idx on staging.tair_non_existing_stocks(type_id);
create index uniquename_gm_stock_idx on staging.tair_non_existing_stocks(uniquename);
create index stock_id_gm_stock_idx on staging.tair_non_existing_stocks(stock_id);