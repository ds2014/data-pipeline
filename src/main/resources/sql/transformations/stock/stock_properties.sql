WITH source as (
SELECT
	*, cast ('F' as text) as is_germplasm
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
	'release date',
	'growth_temperature',
	'duration_of_growth',
	'kit_contents',
	'number_in_set',
	'box',
	'position',
	 'num_lines',
	 'location',
	 'has_stock_notes',
	 'abrc_comments',
	 'format_shipped',
	 'format_received',
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
	cast(source.release_date as text),
	source.growth_temperature,
	source.duration_of_growth,
	source.kit_contents,
	cast(source.number_in_set as text),
	source.box,
	source.position,
	cast(source.num_lines as text),
	source.location,
	source.has_stock_notes,
	source.abrc_comments,
	source.format_shipped,
	source.format_received,
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
	source.stock_id
	)
SELECT
	*
FROM
	transpose
WHERE
	field_value IS NOT null 