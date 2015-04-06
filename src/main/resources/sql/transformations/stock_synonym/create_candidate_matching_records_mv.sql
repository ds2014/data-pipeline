DROP MATERIALIZED VIEW IF EXISTS staging.candidate_synonyms_matching_records CASCADE;
CREATE MATERIALIZED VIEW staging.candidate_synonyms_matching_records AS
	WITH candidate_synonyms_matching_records AS (
SELECT
	t.stock_id,
	c.cvterm_id type_id,
	a.original_alias synonym_name
FROM
	staging.germplasm_alias a JOIN chado.stock t
		ON
		a.germplasm_id = t.stock_id 
		JOIN staging.synonym_lookup_mv l 
		ON
		l.note = a.note 
		JOIN cvterm c
		ON
		c.name = l.synonym_type
		JOIN cv
		ON
		cv.cv_id = c.cv_id 
		join
		chado.synonym sn 
		on 
		sn.type_id = c.cvterm_id
		and
		sn.name = a.original_alias
WHERE
	cv.name = 'synonym_type')
select * from candidate_synonyms_matching_records;

create index stock_id_stock_synonym_idx on staging.candidate_synonyms_matching_records (stock_id);
create index name_stock_ca_synonym_idx on staging.candidate_synonyms_matching_records (synonym_name);