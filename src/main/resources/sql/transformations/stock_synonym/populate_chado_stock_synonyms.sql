INSERT INTO chado.stock_synonym
(synonym_id, stock_id, is_current, is_internal)
select
s.synonym_id,
s.stock_id,
true,
false
from
staging.non_existing_stock_synonyms_mv s;