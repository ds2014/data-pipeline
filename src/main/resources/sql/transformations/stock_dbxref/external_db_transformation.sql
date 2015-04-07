DROP MATERIALIZED VIEW IF EXISTS staging.db_transformation CASCADE;
CREATE
	materialized view staging.db_transformation AS
WITH source as (
select distinct 
case when (db_name like '%Show confirmation sequences for line GK%')
	then 'GABI-Kat'
	else db_name
end as name
,
url,
urlprefix
from 
staging.external_url_transformation
)
,
result_source as (

select 
s.name,
s.url,
s.urlprefix
,
dense_rank() OVER (
      PARTITION BY s.name
      ORDER BY s.url
    ) AS rank
  from source s
)
select 
r.name,
r.url,
r.urlprefix
from result_source r
where rank  = 1
;