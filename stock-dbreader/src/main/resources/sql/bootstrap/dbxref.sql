DO $$

BEGIN
WITH upd AS(
UPDATE chado.dbxref dbx
SET version = '', description = NULL
WHERE accession='stock_type'
RETURNING dbx.dbxref_id, dbx.db_id, dbx.accession, dbx.version, dbx.description
)
INSERT INTO chado.dbxref (db_id, accession, version, description)
SELECT 1, 'stock_type', '', NULL
WHERE NOT EXISTS(SELECT * FROM upd);

END;

$$;

