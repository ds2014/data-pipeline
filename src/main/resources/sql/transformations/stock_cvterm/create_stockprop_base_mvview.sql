DROP MATERIALIZED VIEW staging.stockprop_base_mvview;
create MATERIALIZED VIEW staging.stockprop_base_mvview AS
SELECT
    v.*
FROM
    chado.stock s
    join
    (
    select rs.*, l.mutagen_cvterm_id from staging.germplasm_stock_info rs
    left join
    staging.mutagen_type_lookup l
    on rs.mutagen = l.mutagen)
    v
    on s.stock_id = v.germplasm_id;