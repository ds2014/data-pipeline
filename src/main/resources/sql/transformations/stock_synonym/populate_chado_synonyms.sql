INSERT
	INTO chado.synonym(
	name,
	type_id,
	synonym_sgml
	)
select
synonym_name,
type_id,
''
from
staging.non_existing_germplasm_synonyms_mv;