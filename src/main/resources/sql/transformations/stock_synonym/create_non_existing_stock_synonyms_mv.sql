DROP MATERIALIZED VIEW IF EXISTS staging.non_existing_stock_synonyms_mv CASCADE;
CREATE MATERIALIZED VIEW staging.non_existing_stock_synonyms_mv AS
	WITH candidate_synonyms_matching_records AS (
SELECT
	t.stock_id,
	c.cvterm_id type_id,
	a.original_alias synonym_name
FROM
	staging.germplasm_alias a JOIN chado.stock t
		ON
		a.germplasm_id = t.stock_id 
		JOIN staging.synonym_lookup_mv l JOIN cvterm c
		ON
		c.name = l.synonym_type
		ON
		l.note = a.note JOIN cv
		ON
		cv.cv_id = c.cv_id 
		join
		chado.synonym sn 
		on 
		sn.type_id = c.cvterm_id
		and
		sn.name = a.original_alias
WHERE
	cv.name = 'synonym_type'),
existing_synonyms as(
select distinct sn.synonym_id, sn.type_id, sn.name
from chado.synonym sn
join
candidate_synonyms_matching_records ca
on 
ca.type_id = sn.type_id
and
ca.synonym_name = sn.name),

source as (

SELECT
distinct
	t.stock_id,
	esn.synonym_id
	FROM
	staging.germplasm_alias a JOIN chado.stock t
		ON
		a.germplasm_id = t.stock_id JOIN staging.synonym_lookup_mv l JOIN cvterm c
		ON
		c.name = l.synonym_type
		ON
		l.note = a.note JOIN cv
		ON
		cv.cv_id = c.cv_id
join
existing_synonyms esn
on esn.type_id = c.cvterm_id and esn.name = a.original_alias
WHERE
	cv.name = 'synonym_type' ),
matched_stock_synonyms as (	
select stn.stock_id, stn.synonym_id from chado.stock_synonym stn
join
source s
on 
stn.stock_id  = s.stock_id and stn.synonym_id = s.synonym_id)

select s.stock_id, s.synonym_id from source s
EXCEPT 
select
sm.stock_id, sm.synonym_id from 
matched_stock_synonyms sm;


