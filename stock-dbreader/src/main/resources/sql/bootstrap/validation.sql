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
