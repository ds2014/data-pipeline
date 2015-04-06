DROP MATERIALIZED VIEW IF EXISTS staging.non_existing_stock_synonyms_mv CASCADE;
CREATE MATERIALIZED VIEW staging.non_existing_stock_synonyms_mv  AS
	WITH matched_stock_synonyms  AS (
select stn.stock_id, stn.synonym_id from chado.stock_synonym stn
join
staging.synonyms_source s
on 
stn.stock_id  = s.stock_id and stn.synonym_id = s.synonym_id)
select s.stock_id, s.synonym_id from staging.synonyms_source s
EXCEPT 
select
sm.stock_id, sm.synonym_id from 
matched_stock_synonyms sm;