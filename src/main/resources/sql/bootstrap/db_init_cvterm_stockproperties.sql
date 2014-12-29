WITH source  AS(
select cast ('stock_term' as varchar(255)) as name,
cast ('terms related to stock' as varchar(255)) as description,
cast('' as varchar(255)) as urlprefix,
cast ('' as varchar(255)) as url 
),
upd AS(
UPDATE
	chado.db t
SET
	name = s.name,
	url = s.url,
	description = s.description,
	urlprefix = s.urlprefix
FROM
	source s
WHERE
	t.name = s.name RETURNING t.name,
	t.url,
	t.description,
	t.urlprefix )
INSERT
	INTO chado.db (
	name,
	url,
	description,
	urlprefix)
SELECT
	s.name,
	s.url,
	s.description,
	s.urlprefix
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.name = s.name
WHERE
	t.name IS NULL
GROUP BY
	s.name,
	s.url,
	s.description,
	s.urlprefix; 