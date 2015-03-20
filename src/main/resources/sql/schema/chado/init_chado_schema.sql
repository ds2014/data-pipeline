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

-- index db structures
create index germplasm_idx on tair_stg.germplasm(germplasm_id);

create index tair_object_g_idx on tair_stg.germplasm(tair_object_id);

create index tair_object_ts_idx on tair_stg.tairobject_stock(tair_object_id);

create index stock_ts_idx on tair_stg.tairobject_stock(stock_id);

create index stock_idx on tair_stg.stock(stock_id);

create index tair_object_s_idx on tair_stg.stock(tair_object_id);

create index stock_type_idx on tair_stg.stock(stock_type_id);

create index seed_idx on tair_stg.stock(is_seed);

create index obsolete_idx on tair_stg.stock(is_obsolete);

create index germplasm_type_idx on tair_stg.germplasm(germplasm_type);

select s.* from tair_stg.stock s
join tair_stg.stocktype st on
st.stock_type_id = s.stock_type_id 
where st.stock_type in ('individual_line', 'individual_pool') and s.is_seed = 'T' 
and s.is_obsolete = 'F';

select * from tair_stg.germplasm g
where g.germplasm_type in ('individual_line','individual_pool');

select * from tair_stg.germplasm g
where g.germplasm_type = 'individual_line';

select count(*) from staging.seed_stocks_not_obsolete s
join tair_stg.tairobject_stock tos
on s.stock_id = tos.stock_id;

CREATE materialized view staging.seed_stocks_not_obsolete
AS
select s.*, st.stock_type from tair_stg.stock s
join tair_stg.stocktype st on
st.stock_type_id = s.stock_type_id 
where st.stock_type in ('individual_line', 'individual_pool') and s.is_seed = 'T' 
and s.is_obsolete = 'F';

CREATE INDEX stock_seedmv_idx
  ON staging.seed_stocks_not_obsolete (stock_id);
  
CREATE INDEX tair_object_id_seedmv_idx
  ON staging.seed_stocks_not_obsolete (tair_object_id);
  
  
CREATE materialized view staging.germplasm_mv_indiv
AS
select * from tair_stg.germplasm g
where g.germplasm_type in ('individual_line','individual_pool');

create index germplasm_mv_idx on staging.germplasm_mv_indiv(germplasm_id);

create index tair_object_g_mv_idx on staging.germplasm_mv_indiv(tair_object_id);

create index germplasm_type_mv_idx on staging.germplasm_mv_indiv(germplasm_type);

select * from staging.germplasm_mv_indiv g left join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id;

select * from staging.germplasm_mv_indiv g left join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id;
where g.is_obsolete = 'T' order by g.name;

select count(*) from staging.germplasm_mv_indiv g left join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id
where g.is_obsolete = 'T';

select count(g.germplasm_id), g.name from staging.germplasm_mv_indiv g left join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id
where g.is_obsolete = 'T' 
group by g.name
HAVING count(g.germplasm_id) >1;

--GERMPPLASM_INFO;


String query =
		    "SELECT g.tair_object_id, g.original_name,  sv.abbrev_name, p.polymorphism_id, p.original_name as poly_original_name, s.name, s.stock_id, " + 
                " (SELECT stock_availability_type " + 
                " FROM StockAvailabilityType " + 
                " WHERE stock_availability_type_id = s.stock_availability_type_id) AS stock_availability_type, " + 
            " ph.phenotype, gp.reference_id, r.reference_type as reference_type, " + 
            " CASE " + 
                " WHEN UPPER(r.reference_type) = 'PUBLICATION' " + 
                " THEN (SELECT CASE " + 
                " WHEN authors IS NULL " + 
                " THEN NULL " + 
                " ELSE CASE " + 
                    " WHEN instr(',',authors) > 0 " + 
                    " THEN substr(authors, 1, instr(',',authors)) || ' et al.' " + 
                    " ELSE substr(authors, 1, 100) " + 
                    " END " + 
                " END || '(' || cast(publication_year AS varchar(20)) || ')' " + 
            " FROM Publication " + 
            " WHERE reference_id = gp.reference_id) " + 
            " WHEN UPPER(r.reference_type) = 'COMMUNICATION' " + 
            " THEN (SELECT name || '(' || cast(communication_date AS varchar(20)) || ')' " + 
            " FROM Communication " + 
            " WHERE reference_id = gp.reference_id) " + 
            " WHEN UPPER(r.reference_type) = 'ANALYSIS_REFERENCE' " + 
            " THEN (SELECT name || '(' || cast(date_run AS varchar(20)) || ')' " + 
            " FROM AnalysisReference " + 
            " WHERE reference_id = gp.reference_id) " + 
            " ELSE NULL " + 
            " END AS label " + 
            " FROM Germplasm g " + 
            " LEFT OUTER JOIN SpeciesVariant sv ON g.species_variant_id = sv.species_variant_id " + 
            " LEFT OUTER JOIN Germplasm_MapElement gme ON g.germplasm_id = gme.germplasm_id " + 
            " LEFT OUTER JOIN MapElement me ON gme.map_element_id = me.map_element_id " + 
            " LEFT OUTER JOIN Polymorphism p ON gme.map_element_id = p.map_element_id " + 
            " LEFT OUTER JOIN TairObject_Stock tos ON g.tair_object_id = tos.tair_object_id " + 
            " LEFT OUTER JOIN Stock s ON tos.stock_id = s.stock_id " + 
            " AND s.is_obsolete = 'F' " + 
            " LEFT OUTER JOIN Germplasm_Phenotype gp ON g.germplasm_id = gp.germplasm_id " + 
            " LEFT OUTER JOIN Phenotype ph ON gp.phenotype_id = ph.phenotype_id " + 
            " LEFT OUTER JOIN Reference r ON gp.reference_id = r.reference_id " + 
            " WHERE g.tair_object_id = ? " + 
            " ORDER BY g.tair_object_id, p.polymorphism_id, s.stock_id, ph.phenotype "; 


            
            /work/git/tair-chado-batchflow/stock-dbreader
            
            
CREATE materialized view staging.germplasm_stock_info
AS
select distinct g.*, s.stock_id, s.tair_object_id stock_tair_object_id,
 s.name stock_name, s.abrc_comments, s.date_entered stock_date_entered, s.date_last_modified stock_last_modified, 
 s.description stock_description,
  s.duration_of_growth, s.is_classical_mapping, 
  s.is_molecular_mapping, s.growth_temperature,
  s.is_restricted, s.release_date,
  stp.stock_availability_type,
  s.stock_availability_type_id,
  s.stock_type,
  s.stock_type_id,
  s.is_seed
   from staging.germplasm_mv_indiv g left join
tair_stg.tairobject_stock tos
on
g.tair_object_id = tos.tair_object_id
left join
staging.seed_stocks_not_obsolete s
join tair_stg.stockavailabilitytype stp
on stp.stock_availability_type_id = s.stock_availability_type_id
on s.stock_id = tos.stock_id;


create index germplasm_info_idx on staging.germplasm_stock_info(germplasm_id);

create index tair_object_germplasm_info_idx on staging.germplasm_stock_info(tair_object_id);

create index tair_object_stock_info_idx on staging.germplasm_stock_info(stock_tair_object_id);

create index stock_stock_info_idx on staging.germplasm_stock_info(stock_id);

