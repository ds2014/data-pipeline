DROP MATERIALIZED VIEW IF EXISTS staging.existing_synonyms CASCADE;
CREATE MATERIALIZED VIEW staging.existing_synonyms AS
	WITH existing_synonyms AS (
select distinct sn.synonym_id, sn.type_id, sn.name
from chado.synonym sn
join
staging.candidate_synonyms_matching_records ca
on 
ca.type_id = sn.type_id
and
ca.synonym_name = sn.name)
select * from existing_synonyms;

create index synonym_id_existing_synonyms_idx on staging.existing_synonyms (synonym_id);
create index name_id_existing_synonyms_idx on staging.existing_synonyms (name);