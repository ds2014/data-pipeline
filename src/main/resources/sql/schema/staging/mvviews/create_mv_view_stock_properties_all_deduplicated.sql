CREATE materialized view staging.stock_properties_all_mview
AS
WITH source as (
SELECT
	*
FROM
	staging.stockpropbase_deduplicated)
	,
	transpose as (
SELECT
	source.germplasm_id stock_id,
	source.name germplasm_name,
	unnest(
	array[
	'ploidy',
	'date_entered',
	'date_last_modified',
	'release_date',
	'growth_temperature',
	'duration_of_growth',
	'special_growth_conditions',
	'abrc_comments',
	'stock_availability_comment'
	]
	)
	as key,
	unnest(
	array[
	cast(source.ploidy as text),
	cast(source.date_entered as text),
	cast(source.date_last_modified as text),
	cast(source.release_date as text),
	source.growth_temperature,
	source.duration_of_growth,
	source.special_growth_conditions,
	coalesce(trim(abrc_comments), '')
	]
	)
	as value
FROM
	source
ORDER BY
	source.germplasm_id
	)
SELECT
	t.*
	,
	v.name cvterm_name,
	v.cvterm_id type_id,
	0 rank
	
FROM
	transpose t
	join
	(
	select c.name, c.cvterm_id from cvterm c
join
cv
on cv.cv_id = c.cv_id
where cv.name in ('metadata_timestamp_property','comment_property', 'seed_growth_condition_property', 'chromosomal_constitution')
	) v
	on lower(v.name) = t.key
WHERE
	value IS NOT null and length(value) >0;
	