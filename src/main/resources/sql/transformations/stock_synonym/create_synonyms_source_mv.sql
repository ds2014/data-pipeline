DROP MATERIALIZED VIEW IF EXISTS staging.synonyms_source  CASCADE;
CREATE MATERIALIZED VIEW staging.synonyms_source  AS
	WITH synonyms_source  AS (

SELECT
distinct
	t.stock_id,
	esn.synonym_id
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
staging.existing_synonyms esn
on esn.type_id = c.cvterm_id and esn.name = a.original_alias
WHERE
	cv.name = 'synonym_type' )
select * from synonyms_source;

create index stock_id_synonyms_source_idx on staging.synonyms_source (stock_id);
create index synonym_id_synonyms_source_idx on staging.synonyms_source (synonym_id);