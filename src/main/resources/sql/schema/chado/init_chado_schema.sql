--
-- Chado Database Schema Initilization
--

-- Organism Handling

ALTER TABLE ORGANISM ADD COLUMN type_id int4;
ALTER TABLE ORGANISM ADD COLUMN infraspecific_name varchar(255);
ALTER TABLE ORGANISM ADD CONSTRAINT organism_c1 unique (genus,species, type_id, infraspecific_name);

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'ecotype');

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'subspecies');

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'cultivar');

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'strain');

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'variety');


INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'subvariety');

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'forma');

INSERT INTO dbxref
(db_id, accession)
VALUES(187, 'subforma');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792540, 'ecotype');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792541, 'subspecies');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792542, 'cultivar');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792543, 'strain');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792544, 'variety');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792545, 'subvariety');

INSERT INTO cvterm
(cv_id, dbxref_id, name)
values(65, 792546, 'forma');
