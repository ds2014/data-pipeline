ALTER
	TABLE chado.stockprop	DROP CONSTRAINT IF EXISTS  stockprop_pkey CASCADE;

ALTER
	TABLE chado.stockprop
ALTER
	stockprop_id
DROP
	NOT NULL,
ALTER
	stock_id
DROP
	NOT NULL,
ALTER
	type_id
DROP
	NOT NULL,
ALTER
	rank
DROP
	NOT NULL;
	
ALTER
	TABLE chado.stockprop
DROP
	CONSTRAINT IF EXISTS stockprop_c1 CASCADE;
ALTER
	TABLE chado.stockprop
DROP
	CONSTRAINT IF EXISTS stockprop_stock_id_fkey CASCADE;
ALTER
	TABLE chado.stockprop
DROP
	CONSTRAINT IF EXISTS stockprop_type_id_fkey CASCADE;
DROP
	INDEX IF EXISTS stockprop_idx1 CASCADE;
DROP
	INDEX IF EXISTS stockprop_idx2 CASCADE;