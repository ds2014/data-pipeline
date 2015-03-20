DROP MATERIALIZED VIEW staging.stock_cvterm_mv_all;
DROP MATERIALIZED VIEW staging.stockprop_base_mvview;
DROP MATERIALIZED VIEW staging.mutagen_type_lookup;

CREATE MATERIALIZED VIEW staging.mutagen_type_lookup AS
select mutagen, matched_mutagen, mut_c.cvterm_id mutagen_cvterm_id from 
(
select distinct(mutagen),  staging.match_mutagen(mutagen) matched_mutagen from staging.germplasm_stock_info s
where s.mutagen is not null ) v
join 
cvterm mut_c
on mut_c.name = matched_mutagen
UNION
select NULL mutagen, NULL matched_mutagen, NULL mutagen_cvterm_id;


create MATERIALIZED VIEW staging.stockprop_base_mvview AS
SELECT
    v.*
FROM
    chado.stock s
    join
    (
    select rs.*, l.mutagen_cvterm_id from staging.germplasm_stock_info rs
    left join
    staging.mutagen_type_lookup l
    on rs.mutagen = l.mutagen)
    v
    on s.stock_id = v.germplasm_id;
 
    
CREATE MATERIALIZED VIEW staging.stock_cvterm_mv_all AS
WITH source as (
SELECT
	germplasm_id,
	is_mutant,
	mutagen_cvterm_id,
	is_natural_variant,
	is_aneuploid,
	has_polymorphisms,
	has_foreign_dna,
	is_mapping_strain,
	is_classical_mapping,
	is_molecular_mapping
FROM
	staging.stockprop_base_mvview )
	,
	transpose as (
SELECT
	source.germplasm_id,
	unnest(
	array[
	'is_mutant',
    'mutagen_cvterm_id',
	'is_natural_variant',
	'is_aneuploid',
	'has_polymorphisms',
	'has_foreign_dna',
	'is_mapping_strain',
	'is_classical_mapping',
	'is_molecular_mapping'
		]
	)
	as key,
	unnest(
	array[
	cast(source.is_mutant as text),
	cast(source.mutagen_cvterm_id as text),
	cast(source.is_natural_variant as text),
	cast(source.is_aneuploid as text),
	cast(source.has_polymorphisms as text),
	cast(source.has_foreign_dna as text),
	cast(source.is_mapping_strain as text),
	cast(source.is_classical_mapping as text),
	cast(source.is_molecular_mapping as text)
		]
	)
	as value
FROM
	source
ORDER BY
	source.germplasm_id
	)
SELECT
	germplasm_id as stock_id,
	case 
	when 
		  (value = 'F')
		  then
		  true
	when  (value = 'T')
		  then 
		  	false
	else
			false
	end as is_not,
	case when 
		(key = 'has_foreign_dna')
			then staging.match_cvterm_by_name_cv ('has_foreign_dna', 'germplasm_type')
		when (key = 'has_polymorphisms')
			then staging.match_cvterm_by_name_cv ('has_polymorphisms', 'germplasm_type')
		when (key = 'is_aneuploid')
			then staging.match_cvterm_by_name_cv ('aneuploid', 'chromosomal_constitution')
		when (key = 'is_natural_variant')
			then staging.match_cvterm_by_name_cv ('natural_variant', 'germplasm_type')
	    when (key = 'is_mutant')
			then staging.match_cvterm_by_name_cv ('mutant', 'germplasm_type')
		when (key = 'mutagen_cvterm_id')
			then cast (value as int)
	end as cvterm_id
		
FROM
	transpose t
	WHERE value IS NOT null;
;

CREATE INDEX stock_cvterm_all_idx
  ON staging.stock_cvterm_mv_all (stock_id);