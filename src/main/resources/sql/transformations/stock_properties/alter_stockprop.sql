WITH source as (
SELECT
	stockprop_id,
	stock_id,
	type_id,
	value,
	rank
FROM
	staging.stockprop )
INSERT
	INTO chado.stockprop (
	stockprop_id,
	stock_id,
	type_id,
	value,
	rank)
SELECT
	stockprop_id,
	stock_id,
	type_id,
	value,
	rank
FROM
	source;