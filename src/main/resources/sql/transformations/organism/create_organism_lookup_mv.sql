CREATE
	MATERIALIZED VIEW staging.organism_lookup_mv AS WITH organism_source AS(
SELECT
	distinct t.taxon_id,
	t.common_name,
	t.genus,
	t.species,
	substring(
	t.genus,
	1,
	1)
	|| '.' || substring(
	t.species,
	1)
	as abbreviation,
	sp.species_variant_id,
	case when (
	sp.speciesvariant_type = 'sub_species')
	then 'subspecies' when (
	sp.speciesvariant_type IS null)
	then 'unknown' else sp.speciesvariant_type end as type,
	sp.abbrev_name,
	sp.name ,
	sp.original_name ,
	t.genus || ' ' || t.species || ' var. ' || sp.abbrev_name as infraspecific_name
FROM
	tair_stg.germplasm g JOIN tair_stg.speciesvariant sp
		ON
		sp.species_variant_id = g.species_variant_id JOIN tair_stg.taxon t
		ON
		t.taxon_id = sp.taxon_id)
SELECT
	o.organism_id chado_organism_id,
	os.species_variant_id,
	o.abbreviation,
	o.infraspecific_name
FROM
	organism o JOIN organism_source os
		ON
		os.infraspecific_name = o.infraspecific_name;
