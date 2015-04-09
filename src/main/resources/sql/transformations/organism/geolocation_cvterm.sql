DROP MATERIALIZED VIEW IF EXISTS staging.geolocation_cvterm;
CREATE
	MATERIALIZED VIEW staging.geolocation_cvterm AS 
select 
g.geolocation_id,
c.cvterm_id
,
c.name
from 
staging.geolocation_mv g
join cvterm c
on
c.name = g.country
join cv on cv.cv_id = c.cv_id
where cv.name = 'country_type'
UNION
select 
g.geolocation_id,
c.cvterm_id
,
c.name
from 
staging.geolocation_mv g
join cvterm c
on
c.name = g.location
join cv on cv.cv_id = c.cv_id
where cv.name = 'location_type'
order by geolocation_id, cvterm_id;