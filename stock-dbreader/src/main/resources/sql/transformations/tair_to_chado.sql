WITH STOCK_TYPES AS(
SELECT t.stock_type_id, t.stock_type
  FROM public.dblink(
          'dbname=tair_stock_tables host=localhost user=tripal password=<>',
          'select stock_type_id, stock_type from stocktype') AS t(stock_type_id int, stock_type text))


 SELECT ST.stock_type_id, ST.stock_type  FROM STOCK_TYPES ST;