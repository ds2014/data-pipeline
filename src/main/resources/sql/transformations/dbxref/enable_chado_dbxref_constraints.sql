ALTER TABLE chado.dbxref ALTER COLUMN dbxref_id SET NOT NULL;
ALTER TABLE chado.dbxref ALTER COLUMN db_id SET NOT NULL;
ALTER TABLE chado.dbxref ALTER COLUMN accession SET NOT NULL;
ALTER TABLE chado.dbxref ALTER COLUMN version SET NOT NULL;

ALTER TABLE chado.dbxref ADD CONSTRAINT dbxref_db_id_fkey FOREIGN KEY (db_id) REFERENCES chado.db(db_id) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE chado.dbxref ADD CONSTRAINT dbxref_c1  UNIQUE (db_id, accession, version);
ALTER TABLE chado.dbxref ADD CONSTRAINT dbxref_pkey PRIMARY KEY (dbxref_id);

CREATE INDEX dbxref_idx1 ON chado.dbxref USING btree (db_id);
CREATE INDEX dbxref_idx2 ON chado.dbxref USING btree (accession);
CREATE INDEX dbxref_idx3 ON chado.dbxref USING btree (version);
 



  
	
