WITH source as (
select 
distinct
organism_id,
cvterm_id,
value,
0 as rank
from staging.organismprop_mv
),
upd as	(
UPDATE
	organismprop o
SET
    organism_id = s.organism_id,
	type_id = s.cvterm_id,
	value = s.value,
	rank = s.rank
FROM
	source s
WHERE
	o.organism_id = s.organism_id and o.type_id = s.cvterm_id
	RETURNING 
	o.organism_id,
	o.type_id,
	o.value,
	o.rank
	)
INSERT INTO
organismprop
(
organism_id,
type_id,
value,
rank
)
SELECT
	s.organism_id,
	s.cvterm_id,
	s.value,
	s.rank
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.organism_id = s.organism_id and t.type_id = s.cvterm_id
WHERE
	(t.organism_id IS NULL and t.type_id is NULL)
GROUP BY
    s.organism_id,
	s.cvterm_id,
	s.value,
	s.rank;