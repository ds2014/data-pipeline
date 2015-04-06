INSERT INTO chado.stock_dbxref
(stock_id, dbxref_id, is_current)
select
stock_id,
dbxref_id,
true
from
staging.dbxref_not_existing_stock_dbxref;