DROP MATERIALIZED VIEW IF EXISTS staging.geolocation_mv CASCADE;
CREATE
	MATERIALIZED VIEW staging.geolocation_mv AS 
SELECT
	*
FROM
	(
SELECT
	o.organism_id geolocation_id,
	sp.min_longitude,
	sp.max_longitude,
	sp.min_latitude,
	sp.min_altitude,
	sp.max_altitude,
	case when (
	length(
	sp.country)
	>0 AND
	length(
	sp.location)
	>0)
	then sp.country || ', ' || location when (
	length(
	sp.country)
	>0)
	then sp.country when (
	length(
	sp.location)
	>0)
	then sp.location when (
	length(
	sp.country)
	= 0 AND
	length(
	sp.location)
	= 0)
	then null end as description,
	sp.country,
	sp.location,
	sp.daily_temperature,
	sp.monthly_precipitation
FROM
	tair_stg.speciesvariant sp JOIN staging.organism_lookup_mv o_mv
		ON
		o_mv.species_variant_id = sp.species_variant_id JOIN organism o
		ON
		o.organism_id = o_mv.chado_organism_id )
	V
WHERE
	V.description IS NOT null;