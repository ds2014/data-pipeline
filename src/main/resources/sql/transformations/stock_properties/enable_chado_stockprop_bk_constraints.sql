ALTER TABLE staging.stockprop_bk ALTER COLUMN stock_id SET NOT NULL;
ALTER TABLE staging.stockprop_bk ALTER COLUMN type_id SET NOT NULL;
ALTER TABLE staging.stockprop_bk ALTER COLUMN rank SET NOT NULL;

ALTER TABLE staging.stockprop_bk ADD FOREIGN KEY (type_id) 
	REFERENCES chado.cvterm (cvterm_id);
	
ALTER TABLE staging.stockprop_bk
	ADD FOREIGN KEY (stock_id) 
	REFERENCES chado.stock (stock_id);
	
ALTER TABLE staging.stockprop_bk
	ADD PRIMARY KEY (stockprop_id);
	
ALTER TABLE staging.stockprop_bk
  ADD CONSTRAINT stockprop_c1
  UNIQUE (stock_id, type_id, rank);
  
  
	
