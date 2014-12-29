
select dbx.* from chado.stockprop p join
chado.stock s on s.stock_id = p.stock_id
join chado.cvterm c
on c.cvterm_id = p.type_id
join chado.dbxref dbx
on dbx.dbxref_id = c.dbxref_id
join chado.db on db.db_id = dbx.db_id
join chado.cv on
cv.cv_id = c.cv_id
where db.db_id = 94