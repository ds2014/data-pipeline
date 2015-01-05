ALTER TABLE chado.stock ALTER COLUMN stock_id SET NOT NULL;
ALTER TABLE chado.stock ALTER COLUMN uniquename SET NOT NULL;
ALTER TABLE chado.stock ALTER COLUMN type_id SET NOT NULL;
ALTER TABLE chado.stock ALTER COLUMN is_obsolete SET NOT NULL;

ALTER TABLE stock
  ADD CONSTRAINT stock_pkey
  PRIMARY KEY (stock_id);

ALTER TABLE stock
  ADD CONSTRAINT stock_c1
  UNIQUE (organism_id, uniquename, type_id);
 
ALTER TABLE stock
  ADD CONSTRAINT stock_organism_id_fkey
  FOREIGN KEY (organism_id) REFERENCES chado.organism(organism_id) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
 
ALTER TABLE stock
  ADD CONSTRAINT stock_dbxref_id_fkey
  FOREIGN KEY (dbxref_id) REFERENCES dbxref(dbxref_id) ON DELETE SET NULL DEFERRABLE INITIALLY DEFERRED;
 
ALTER TABLE stock
  ADD CONSTRAINT stock_type_id_fkey
  FOREIGN KEY (type_id) REFERENCES cvterm(cvterm_id) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
 
 CREATE INDEX stock_idx1 ON chado.stock USING btree (dbxref_id);
 CREATE INDEX stock_idx2 ON chado.stock USING btree (organism_id);
 CREATE INDEX stock_idx3 ON chado.stock USING btree (type_id);
 CREATE INDEX stock_idx4 ON chado.stock USING btree (uniquename);
 CREATE INDEX stock_name_ind1 ON chado.stock USING btree (name);
 
 



  
	
