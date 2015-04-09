WITH source as (
select 
distinct
geolocation_id,
cvterm_id,
value,
0 as rank
from staging.geolocationprop_mv
),
upd as	(
UPDATE
	nd_geolocationprop n
SET
    nd_geolocation_id = s.geolocation_id,
	type_id = s.cvterm_id,
	value = s.value,
	rank = s.rank
FROM
	source s
WHERE
	n.nd_geolocation_id = s.geolocation_id and n.type_id = s.cvterm_id
	RETURNING 
	n.nd_geolocation_id,
	n.type_id,
	n.value,
	n.rank
	)
INSERT INTO
nd_geolocationprop
(
nd_geolocation_id,
type_id,
value,
rank
)
SELECT
	s.geolocation_id,
	s.cvterm_id,
	s.value,
	s.rank
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.nd_geolocation_id = s.geolocation_id and t.type_id = s.cvterm_id
WHERE
	(t.nd_geolocation_id IS NULL and t.type_id is NULL)
GROUP BY
   s.geolocation_id,
	s.cvterm_id,
	s.value,
	s.rank;