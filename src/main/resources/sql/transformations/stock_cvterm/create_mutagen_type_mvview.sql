DROP MATERIALIZED VIEW staging.mutagen_type_lookup;
CREATE MATERIALIZED VIEW staging.mutagen_type_lookup AS
select mutagen, matched_mutagen, mut_c.cvterm_id mutagen_cvterm_id from 
(
select distinct(mutagen),  staging.match_mutagen(mutagen) matched_mutagen from staging.germplasm_stock_info s
where s.mutagen is not null ) v
join 
cvterm mut_c
on mut_c.name = matched_mutagen
UNION
select NULL mutagen, NULL matched_mutagen, NULL mutagen_cvterm_id;