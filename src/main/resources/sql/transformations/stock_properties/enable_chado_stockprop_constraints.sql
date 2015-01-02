ALTER TABLE chado.stockprop ALTER COLUMN stock_id SET NOT NULL;
ALTER TABLE chado.stockprop ALTER COLUMN type_id SET NOT NULL;
ALTER TABLE chado.stockprop ALTER COLUMN rank SET NOT NULL;

ALTER TABLE chado.stockprop ADD FOREIGN KEY (type_id) 
	REFERENCES chado.cvterm (cvterm_id);

ALTER TABLE chado.stockprop
	ADD FOREIGN KEY (stock_id) 
	REFERENCES chado.stock (stock_id);
	
ALTER TABLE chado.stockprop
	ADD PRIMARY KEY (stockprop_id);
	
ALTER TABLE chado.stockprop
  ADD CONSTRAINT stockprop_c1
  UNIQUE (stock_id, type_id, rank);
  
  
	
