select  t.stock_type_id, t.stock_type,  count(t.stock_type_id) stock_count
from stock s
join
stocktype t
on 
s.stock_type_id = t.stock_type_id
group by t.stock_type_id, t.stock_type
order by count(t.stock_type_id);


--explore avaialable stocks on chado
select s.*, dbx.*, db.*, c.* from stock s
join
dbxref dbx
on 
dbx.dbxref_id=s.dbxref_id
join
db
on db.db_id = dbx.db_id
join 
cvterm c
on 
c.cvterm_id = s.type_id


SELECT
	distinct t.stock_id,
	t.stock_type_id,
	--c.dbxref_id,
	6 organism_id ,
	t.name,
	t.name uniquename,
	t.description description,
	c.cvterm_id as type_id,
	c.name as type,
	0 is_obsolete
FROM
	tair_stg.stock t JOIN tair_stg.stocktype st
		ON
		t.stock_type_id = st.stock_type_id JOIN cvterm c
		ON
		c.name = st.stock_type JOIN dbxref dbx
		ON
		dbx.dbxref_id = c.dbxref_id
WHERE
	dbx.db_id = 1 LIMIT 10
SELECT
	*
FROM
	tair_stg.stocktype
SELECT
	c.*,
	dbx.*
FROM
	cvterm c JOIN dbxref dbx
		ON
		dbx.dbxref_id = c.dbxref_id
WHERE
	c.name = 'clone' AND
	dbx.db_id =1
SELECT
	c.*,
	dbx.*
FROM
	cvterm c JOIN dbxref dbx
		ON
		dbx.dbxref_id = c.dbxref_id
WHERE
	c.name = 'clone' AND
	dbx.db_id =1

	
	-- accessions
	
	select  db.* from stock s
join
dbxref dbx
on 
dbx.dbxref_id=s.dbxref_id
join
db
on db.db_id = dbx.db_id
join 
cvterm c
on 
c.cvterm_id = s.type_id
