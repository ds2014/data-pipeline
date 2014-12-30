WITH source as (
SELECT
	*, cast ('false' as text) as is_germplasm
FROM
	tair_stg.stock )
	,
	transpose as (
SELECT
	source.stock_id,
	unnest(
	array[
	'date_entered',
	'date_last_modified',
	'growth_temperature',
	'duration_of_growth',
	'format_shipped',
	'kit_contents',
	'media',
	 'is_germplasm',
	 'is_seed',
	 'is_restricted',
	 'is_molecular_mapping'
	]
	)
	as field_label,
	unnest(
	array[
	cast(source.date_entered as text),
	cast(source.date_last_modified as text),
	source.growth_temperature,
	source.duration_of_growth,
	source.kit_contents,
	source.format_shipped,
	source.media,
	source.is_germplasm,
	source.is_seed,
	source.is_restricted,
	is_molecular_mapping
		]
	)
	as field_value
FROM
	source
WHERE
	source.stock_id = 2
ORDER BY
	source.stock_id,
	field_label )
SELECT
	*
FROM
	transpose
WHERE
	field_value IS NOT null