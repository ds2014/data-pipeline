WITH source as (
select  
s.accession,
db.db_id,
cast ('' as text) as version
from staging.external_dbxref_transformation s
join chado.db db
on db.name = s.db_name
),
upd as (
UPDATE chado.dbxref dbx
SET
db_id = s.db_id,
accession = s.accession,
version = s.version
FROM source s
WHERE dbx.accession = s.accession and dbx.db_id = s.db_id
RETURNING
dbx.db_id,
dbx.dbxref_id,
dbx.accession,
dbx.version
)
INSERT INTO
chado.dbxref(
db_id,
accession,
version
)
SELECT
s.db_id,
s.accession,
s.version
FROM
source s
LEFT JOIN upd t
ON t.accession = s.accession and t.db_id = s.db_id
where (t.accession IS NULL and t.db_id IS NULL)
GROUP BY
t.dbxref_id,
s.db_id,
s.accession,
s.version;