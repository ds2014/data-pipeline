-- validate if stock type exists in the dbxref table

SELECT *
  FROM chado.dbxref
 WHERE accession = 'stock_type';


-- validate if stock type exists in the Controlled Vocabularies table

select * from CV where name = 'stock_type'