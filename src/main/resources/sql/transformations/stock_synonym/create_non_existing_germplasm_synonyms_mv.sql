DROP MATERIALIZED VIEW IF EXISTS staging.non_existing_germplasm_synonyms_mv CASCADE;
CREATE MATERIALIZED VIEW staging.non_existing_germplasm_synonyms_mv AS
	WITH matching_records_set1 AS (
SELECT
	t.stock_id,
	c.cvterm_id type_id,
	c.name synonym_type,
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
matching_records_set2 as(
select distinct V.stock_id, c.cvterm_id type_id, V.synonym_type, V.synonym_name from 
(SELECT
	t.stock_id,
	cast ('ABRC_stock_number' as text) synonym_type,
	a.stock_name synonym_name
FROM
	staging.germplasm_alias a JOIN chado.stock t
		ON
		a.germplasm_id = t.stock_id
		where a.stock_name is not null ) V
		join
		cvterm c
		on c.name = V.synonym_type
	join
		chado.synonym sn 
		on 
		sn.type_id = c.cvterm_id
		and
		sn.name = V.synonym_name),
matched_records_set3 as(
select * from matching_records_set1 
UNION
select * from matching_records_set2),
matching_records as (
select * from matched_records_set3 where length(synonym_name) > 0),
source as (
SELECT
	t.stock_id,
	c.cvterm_id type_id,
	c.name synonym_type,
	a.original_alias synonym_name
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
WHERE
	cv.name = 'synonym_type'
UNION
select distinct V.stock_id, c.cvterm_id type_id, V.synonym_type, V.synonym_name from 
(SELECT
	t.stock_id,
	cast ('ABRC_stock_number' as text) synonym_type,
	a.stock_name synonym_name
FROM
	staging.germplasm_alias a JOIN chado.stock t
		ON
		a.germplasm_id = t.stock_id
		where a.stock_name is not null ) V
		join
		cvterm c
		on c.name = V.synonym_type
	where length(V.synonym_name) >0
	)
	
select distinct type_id, synonym_name from 
source s
where not exists (
SELECT
	1
FROM	matching_records t
WHERE
		s.type_id = t.type_id and s.synonym_name = t.synonym_name)
		where where length(s.synonym_name) >0
		
create index non_existing_germplasm_type_id_idx on staging.non_existing_germplasm_synonyms_mv(type_id);
create index non_existing_synonym_name_idx on staging.non_existing_germplasm_synonyms_mv(synonym_name); 
