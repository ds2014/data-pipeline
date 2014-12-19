DO $$
BEGIN
CREATE SCHEMA IF NOT EXISTS staging;

CREATE OR REPLACE FUNCTION staging.get_dbxref_by_accession (text)
   RETURNS int
AS
   $$
   DECLARE
      result   integer;
   BEGIN
      SELECT
       INTO result      dbxref_id
       FROM dbxref
      WHERE accession = $1;

      RETURN result;
   END;
   $$
   LANGUAGE plpgsql;

END
$$;