DROP MATERIALIZED VIEW IF EXISTS staging.germplasm_source CASCADE;
CREATE MATERIALIZED VIEW staging.germplasm_source
AS
select o.organism_id,o.abbreviation, o.infraspecific_name, o.common_name, o.stock_id,  V.* from
(
SELECT
substring(
	t.genus,
	1,
	1)
	|| '.' || substring(
	t.species,
	1)
	as organism_abbreviation,
	null as organism_infraspecific_name,
	 acc.dbxref_id,
	c.cvterm_id as type_id,
	cast(
	s.name as varchar(
	255)
	)
	as name,
	cast(
	s.name as text)
	as uniquename,
	case 
		when (s.stock_description is null)
			then cast(s.description as text)
		else
			cast(s.description as text) || ' Stock Description:' || cast(s.stock_description as text)
	end as description,
	cast (
	s.is_obsolete as boolean)
	as is_obsolete,
	s.germplasm_id
FROM
	staging.germplasm_stock_info s JOIN chado.cvterm c
		ON
		c.name = s.germplasm_type JOIN chado.cv
		ON
		cv.cv_id = c.cv_id AND
	cv.name = 'germplasm_type' 
	JOIN dbxref dbx
	on c.dbxref_id = dbx.dbxref_id 
	JOIN
	db on
	db.db_id = dbx.db_id and dbx.db_id = staging.get_tair_db_id_by_name(
	'TAIR:germplasm:term')
	JOIN chado.dbxref acc
		ON
		cast(
	s.tair_object_id as varchar(
	255)
	)
	= acc.accession AND
	acc.db_id = staging.get_tair_db_id_by_name(
	'TAIR Germplasm')
	join 
	tair_stg.taxon t
	on 
	t.taxon_id = s.taxon_id
	where s.species_variant_id is null
	 ) V
	join chado.organism o
	on 
	o.abbreviation = V.organism_abbreviation and o.infraspecific_name is null and o.stock_id is null
	UNION
	select o.organism_id,o.abbreviation, o.infraspecific_name, o.common_name, o.stock_id,  V.* from
(
SELECT
substring(
	t.genus,
	1,
	1)
	|| '.' || substring(
	t.species,
	1)
	as organism_abbreviation,
	t.genus || ' ' || t.species || ' var. ' || sp.abbrev_name as organism_infraspecific_name,
	 acc.dbxref_id,
	c.cvterm_id as type_id,
	cast(
	s.name as varchar(
	255)
	)
	as name,
	cast(
	s.name as text)
	as uniquename,
	case 
		when (s.stock_description is null)
			then cast(s.description as text)
		else
			cast(s.description as text) || ' Stock Description:' || cast(s.stock_description as text)
	end as description,
	cast (
	s.is_obsolete as boolean)
	as is_obsolete
	,
	s.germplasm_id
FROM
	staging.germplasm_stock_info s JOIN chado.cvterm c
		ON
		c.name = s.germplasm_type JOIN chado.cv
		ON
		cv.cv_id = c.cv_id AND
	cv.name = 'germplasm_type' 
	JOIN dbxref dbx
	on c.dbxref_id = dbx.dbxref_id 
	JOIN
	db on
	db.db_id = dbx.db_id and dbx.db_id = staging.get_tair_db_id_by_name(
	'TAIR:germplasm:term')
	JOIN chado.dbxref acc
		ON
		cast(
	s.tair_object_id as varchar(
	255)
	)
	= acc.accession AND
	acc.db_id = staging.get_tair_db_id_by_name(
	'TAIR Germplasm')
	join 
	tair_stg.taxon t
	on 
	t.taxon_id = s.taxon_id
	join tair_stg.speciesvariant sp 
	on
	sp.species_variant_id = s.species_variant_id 
	where s.species_variant_id  is not null
	) V
	join chado.organism o
	on 
	o.abbreviation = V.organism_abbreviation and o.infraspecific_name = V.organism_infraspecific_name;
	
	create index organism_gm_source_idx on staging.germplasm_source(organism_id);
	create index dbxref_gm_source_idx on staging.germplasm_source(dbxref_id);
	create index type_gm_source_idx on staging.germplasm_source(type_id);
	create index uniquename_gm_source_idx on staging.germplasm_source(uniquename);
	create index germplasm_gm_source_idx on staging.germplasm_source(germplasm_id);