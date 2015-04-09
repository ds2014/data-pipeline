	WITH source AS(
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
	then 'subspecies' 
			when (sp.speciesvariant_type is null)
	then 'unknown'
		else sp.speciesvariant_type
	end as type,
	sp.abbrev_name,
	sp.name ,
	sp.original_name ,
	t.genus || ' ' || t.species || ' var. ' || sp.abbrev_name as infraspecific_name
FROM
	tair_stg.germplasm g JOIN tair_stg.speciesvariant sp
		ON
		sp.species_variant_id = g.species_variant_id JOIN tair_stg.taxon t
		ON
		t.taxon_id = sp.taxon_id),
result_source as (		
SELECT
	*
FROM
	source s JOIN (
SELECT
	c.name,
	c.cvterm_id
FROM
	chado.cvterm c JOIN dbxref dbx
		ON
		dbx.dbxref_id = c.dbxref_id JOIN chado.db
		ON
		dbx.db_id = db.db_id
WHERE
	db.name = 'TAIR Species Variant')
	v
		ON
		v.name = s.type ),
upd as	(
UPDATE
	chado.organism o
SET
    abbreviation = s.abbreviation,
	genus = s.genus,
	species = s.species,
	common_name = s.abbrev_name,
	type_id = s.cvterm_id,
	infraspecific_name = s.infraspecific_name
	
FROM
	result_source s
WHERE
	o.infraspecific_name = s.infraspecific_name
	RETURNING 
	o.abbreviation,
	o.organism_id,
	o.type_id,
	o.genus,
	o.species,
	o.common_name,
	o.infraspecific_name)
	
	INSERT
	INTO chado.organism (
	abbreviation,
	genus,
	species,
	type_id,
	common_name,
	infraspecific_name
	)
SELECT
	s.abbreviation,
	s.genus,
	s.species,
	s.cvterm_id,
	s.abbrev_name,
	s.infraspecific_name
FROM
	result_source s
		LEFT JOIN
		upd t
		ON
		t.infraspecific_name = s.infraspecific_name
WHERE
	(t.infraspecific_name IS NULL)
GROUP BY
    t.organism_id,
	s.abbreviation,
	s.genus,
	s.species,
	s.abbrev_name,
	s.cvterm_id,
	s.infraspecific_name;