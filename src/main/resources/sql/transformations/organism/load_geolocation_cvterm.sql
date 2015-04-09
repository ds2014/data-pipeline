WITH source as (
select 
distinct
geolocation_id,
cvterm_id,
0 as rank
from staging.geolocation_cvterm
),
upd as	(
UPDATE
	nd_geolocation_cvterm n
SET
    nd_geolocation_id = s.geolocation_id,
    cvterm_id = s.cvterm_id,
	rank = s.rank
FROM
	source s
WHERE
	n.nd_geolocation_id = s.geolocation_id and n.cvterm_id = s.cvterm_id
	RETURNING 
	n.nd_geolocation_id,
	n.cvterm_id,
	n.rank
	)
INSERT INTO
nd_geolocation_cvterm
(
nd_geolocation_id,
cvterm_id,
rank
)
SELECT
	s.geolocation_id,
	s.cvterm_id,
	s.rank
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.nd_geolocation_id = s.geolocation_id and t.cvterm_id = s.cvterm_id
WHERE
	(t.nd_geolocation_id IS NULL and t.cvterm_id is NULL)
GROUP BY
   s.geolocation_id,
	s.cvterm_id,
	s.rank;