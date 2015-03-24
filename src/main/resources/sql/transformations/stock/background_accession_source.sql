  SELECT g.tair_object_id, g.species_variant_id, g.name, wrk.abbrev_name, wrk.species_variant_id background_accession_id, wrk.taxon_id
	    FROM  tair_stg.germplasm g
   left join tair_stg.pedigree_wrk wrk
      on 
      wrk.GERMPLASM_ID = g.germplasm_id
   and wrk.SPECIES_VARIANT_ID is not null and g.GERMPLASM_TYPE  in ('individual_line', 'individual_pool');