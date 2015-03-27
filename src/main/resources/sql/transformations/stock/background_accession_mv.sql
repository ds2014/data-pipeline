CREATE MATERIALIZED VIEW staging.background_accession_mv AS
SELECT
	g.germplasm_id,
	g.tair_object_id,
	g.species_variant_id direct_background_accession_id,
	g.name germplasm_name,
	wrk.abbrev_name species_variant_abbreviation,
	wrk.taxon_id,
	staging.convert_to_integer(wrk.species_variant_id) background_accession_id,
	coalesce(staging.convert_to_integer(wrk.species_variant_id),g.species_variant_id, null) computed_bg_accession_id
	FROM
	staging.germplasm_mv_indiv g
		LEFT JOIN
		tair_stg.pedigree_wrk wrk
		ON
		wrk.GERMPLASM_ID = g.germplasm_id;