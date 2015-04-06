select distinct V.*, c.cvterm_id type_id from 
(SELECT
	t.stock_id,
	cast ('ABRC_stock_number' as text) synonym_type,
	a.original_alias synonym_name
FROM
	staging.germplasm_alias a JOIN chado.stock t
		ON
		a.germplasm_id = t.stock_id
		where a.stock_name is not null ) V
		join
		cvterm c
		on c.name = V.synonym_type;