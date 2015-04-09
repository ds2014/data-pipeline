WITH source as (
select 
distinct
geolocation_id,
description,
min_longitude,
max_longitude,
min_latitude,
min_latitude max_latitude,
case when (min_altitude ~* '[a-z]')
	then null
	when (length(min_altitude) > 0)
	then cast (min_altitude as float4)
	else null
end as min_altitude,
case when (max_altitude ~* '[a-z]')
	then null
	when (length(max_altitude) > 0 )
	then cast (max_altitude as float4)
	else null
end as max_altitude
from staging.geolocation_mv
),
upd as	(
UPDATE
	nd_geolocation n
SET
    nd_geolocation_id = s.geolocation_id,
	description = s.description,
	min_longitude = s.min_longitude,
	max_longitude = s.max_longitude,
	min_latitude = s.min_latitude,
	max_latitude = s.max_latitude,
	min_altitude = s.min_altitude,
	max_altitude = s.max_altitude
FROM
	source s
WHERE
	n.nd_geolocation_id = s.geolocation_id
	RETURNING 
	n.nd_geolocation_id,
	n.description,
	n.min_longitude,
	n.max_longitude,
	n.min_latitude,
	n.max_latitude,
	n.min_altitude,
	n.max_altitude
	)
INSERT INTO
nd_geolocation
(
nd_geolocation_id,
description,
min_longitude,
max_longitude,
min_latitude,
max_latitude,
min_altitude,
max_altitude
)
SELECT
	s.geolocation_id,
	s.description,
	s.min_longitude,
	s.max_longitude,
	s.min_latitude,
	s.max_latitude,
	s.min_altitude,
	s.max_altitude
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.nd_geolocation_id = s.geolocation_id
WHERE
	(t.nd_geolocation_id IS NULL)
GROUP BY
    s.geolocation_id,
    s.description,
	s.min_longitude,
	s.max_longitude,
	s.min_latitude,
	s.max_latitude,
	s.min_altitude,
	s.max_altitude;
	