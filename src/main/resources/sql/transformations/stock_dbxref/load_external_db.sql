WITH source as (
SELECT 
name,
url,
urlprefix
FROM
staging.db_transformation
),
upd as (
UPDATE
chado.db db
SET
name = s.name,
url = s.url,
urlprefix = s.urlprefix
FROM
source s
where
db.name = s.name
RETURNING
db.db_id,
db.name,
db.url,
db.urlprefix)
INSERT INTO
chado.db
(
name,
url,
urlprefix
)
SELECT
s.name,
s.url,
s.urlprefix
FROM source s
LEFT JOIN
	upd t
	ON
	t.name = s.name
	where (t.name IS NULL)
GROUP BY
t.db_id,
s.name,
s.url,
s.urlprefix;