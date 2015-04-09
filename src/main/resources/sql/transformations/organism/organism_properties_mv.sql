DROP MATERIALIZED VIEW IF EXISTS staging.organismprop_mv CASCADE;
CREATE
	MATERIALIZED VIEW staging.organismprop_mv AS 
WITH source as (
SELECT
	*
FROM
	tair_stg.speciesvariant)
	,
	transpose as (
SELECT
	cast(source.species_variant_id as bigint),
	source.abbrev_name ,
	unnest(
	array[
	'description',
	'date_entered',
	'date_last_modified',
	'date_collected',
	'isolated_from',
	'accession_number',
	'sort_accession'
	,
	'habitat'
	]
	)
	as key,
	unnest(
	array[
	cast(source.description as text),
	cast(source.date_entered as text),
	cast(source.date_last_modified as text),
	cast(source.date_collected as text),
	cast(source.isolated_from as text),
	cast(source.accession_number as text),
	cast(source.sort_accession as text),
	cast(source.habitat as text)
	]
	)
	as value
FROM
	source
ORDER BY
	source.species_variant_id
	)
	SELECT
	o.organism_id,
	t.*
	,
	v.*
	FROM
	transpose t
	 join
	(
	select c.cvterm_id, c.name from cvterm c
	where c.cvterm_id in (
	select c.cvterm_id from cvterm c
join
cv
on cv.cv_id = c.cv_id
where cv.name in ('metadata_timestamp_property','organism_property'))
	) v
	on lower(trim(v.name)) = lower(trim(t.key))
	join staging.organism_lookup_mv o_mv
	on 
	o_mv.species_variant_id = t.species_variant_id
	join
	organism o
	on o.organism_id = o_mv.chado_organism_id
	where 
	value IS NOT null and length(value) >0 
	order by
	t.species_variant_id;