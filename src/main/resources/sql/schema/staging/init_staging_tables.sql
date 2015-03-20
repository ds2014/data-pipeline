CREATE TABLE staging.stockprop (
    stockprop_id bigint default staging.id_generator(),
	stock_id bigint,
	type_id int4,
	value text,
	rank int4 DEFAULT 0
);

CREATE TABLE staging.stock_cvterm (
    stock_cvterm_id bigint default staging.id_generator(),
	stock_id bigint,
	cvterm_id int4,
	pub_id int4,
	is_not boolean,
	rank int4 DEFAULT 0
);


