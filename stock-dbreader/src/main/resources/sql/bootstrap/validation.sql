-- validate if stock type exists in the dbxref table

SELECT *
  FROM chado.dbxref
 WHERE accession = 'stock_type';

-- validate if stock availability exists in the dbxref table

SELECT *
  FROM chado.dbxref
 WHERE accession = 'stock_availability';

-- validate if stock type exists in the Controlled Vocabularies table

SELECT *
  FROM CV
 WHERE name = 'stock_type'
 
 SELECT *
 FROM chado.CV
 WHERE name = 'stock_availability';

 -- validate number of records in cvterm equal to the stock_types
SELECT
	*
FROM
	chado.cvterm cv JOIN dbxref dbx
		ON
		dbx.dbxref_id = cv.dbxref_id
WHERE
	dbx.db_id = staging.get_tair_db_id_by_name(
	'TAIR')
