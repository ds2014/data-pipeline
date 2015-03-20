INSERT
	INTO chado.stock_cvterm (
	stock_id,
	cvterm_id,
	is_not,
	rank)
	(
SELECT
	stock_id,
	cvterm_id,
	is_not,
	rank
FROM
	staging.stock_cvterm where cvterm_id <> 0);