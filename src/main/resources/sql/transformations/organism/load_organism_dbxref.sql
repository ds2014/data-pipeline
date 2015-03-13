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
	cast (sp.species_variant_id as varchar(255)) accession,
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
	s.*, v.*, acc.*, o.*
	FROM
	source s JOIN (
SELECT
	c.name,
	c.cvterm_id,
	dbx.db_id
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
		v.name = s.type
		join
		dbxref acc
		on s.accession = acc.accession and acc.db_id = v.db_id
		join
		organism o 
		on
		o.infraspecific_name = s.infraspecific_name ),
		
upd as	(
UPDATE
	chado.organism_dbxref od
SET
    organism_id = s.organism_id,
	dbxref_id = s.dbxref_id	
FROM
	result_source s
WHERE
	od.organism_id = s.organism_id and od.dbxref_id = s.dbxref_id 
	RETURNING 
	od.organism_id,
	od.dbxref_id
	)
INSERT
	INTO organism_dbxref (
	organism_id,
	dbxref_id
	)
SELECT
	s.organism_id,
	s.dbxref_id
FROM
	result_source s
		LEFT JOIN
		upd t
		ON
		t.organism_id = s.organism_id and t.dbxref_id = s.dbxref_id
WHERE
	(t.organism_id IS NULL and t.dbxref_id is NULL)
GROUP BY
    s.organism_id,
	s.dbxref_id;