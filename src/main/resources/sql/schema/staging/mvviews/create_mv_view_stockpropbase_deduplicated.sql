create materialized view staging.stockpropbase_deduplicated AS
select * from staging.stockpropbase_non_obsolete t
where t.stock_id
not in
(
select  s.stock_id from staging.germplasm_mv_indiv g
join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id
left join
tair_stg.stock s
on s.stock_id = tos.stock_id and s.is_seed = 'T'
left join
tair_stg.stocktype st
on st.stock_type_id = s.stock_type_id and st.stock_type in ('individual_line', 'individual_pool')
join 
(
select count(germplasm_id), germplasm_id
from 
(
select distinct g.*, s.stock_id, s.tair_object_id stock_tair_object_id,
 s.name stock_name, s.abrc_comments, s.date_entered stock_date_entered, s.date_last_modified stock_last_modified, 
 s.description stock_description,
  s.duration_of_growth, s.is_classical_mapping, 
  s.is_molecular_mapping, s.growth_temperature,
  s.is_restricted, s.release_date,
  stp.stock_availability_type,
  s.stock_availability_type_id,
  st.stock_type,
  s.stock_type_id,
  s.is_seed
  ,
  s.is_obsolete stock_obsolete,
  s.stock_availability_comment
  from staging.germplasm_mv_indiv g left join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id
left join
tair_stg.stock s
on s.stock_id = tos.stock_id and s.is_seed = 'T'
left join
tair_stg.stocktype st
on st.stock_type_id = s.stock_type_id and st.stock_type in ('individual_line', 'individual_pool')
left join tair_stg.stockavailabilitytype stp
on stp.stock_availability_type_id = s.stock_availability_type_id
) V
group by germplasm_id
HAVING  count (germplasm_id) >1 )V1
on V1.germplasm_id = g.germplasm_id
where s.is_obsolete = 'F' and s.stock_id is not null)