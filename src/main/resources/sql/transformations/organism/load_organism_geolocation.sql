WITH source as (
select 
distinct
geolocation_id,
o.organism_id,
0 as rank
from staging.geolocation_mv g
join
organism o
on o.organism_id = g.geolocation_id
),
upd as	(
UPDATE
	organism_nd_geolocation n
SET
    nd_geolocation_id = s.geolocation_id,
    organism_id = s.organism_id,
    rank = s.rank
FROM
	source s
WHERE
	n.nd_geolocation_id = s.geolocation_id and n.organism_id = s.organism_id
	RETURNING 
	n.nd_geolocation_id,
	n.organism_id,
	n.rank
	)
INSERT INTO
organism_nd_geolocation
(
nd_geolocation_id,
organism_id,
rank
)
SELECT
	s.geolocation_id,
	s.organism_id,
	s.rank
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.nd_geolocation_id = s.geolocation_id and t.organism_id = s.organism_id
WHERE
	(t.nd_geolocation_id IS NULL and t.organism_id is NULL)
GROUP BY
   s.geolocation_id,
	s.organism_id,
	s.rank;