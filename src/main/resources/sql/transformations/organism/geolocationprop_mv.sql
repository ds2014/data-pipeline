DROP MATERIALIZED VIEW IF EXISTS staging.geolocationprop_mv CASCADE;
CREATE
	MATERIALIZED VIEW staging.geolocationprop_mv AS 

WITH source as (
SELECT
	*
FROM
	staging.geolocation_mv)
	,
	transpose as (
SELECT
	source.geolocation_id,
	source.description,
	unnest(
	array[
	'daily_temperature',
	'monthly_precipitation'
	]
	)
	as key,
	unnest(
	array[
	cast(source.daily_temperature as text),
	cast(source.monthly_precipitation as text)
	]
	)
	as value
FROM
	source
ORDER BY
	source.geolocation_id
	)
	SELECT
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
where cv.name in ('geo_location'))
	) v
	on lower(trim(v.name)) = lower(trim(t.key))
	where 
	value IS NOT null and length(value) >0 
	order by
	t.geolocation_id,
	t.description;