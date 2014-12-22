select  t.stock_type_id, t.stock_type,  count(t.stock_type_id) stock_count
from stock s
join
stocktype t
on 
s.stock_type_id = t.stock_type_id
group by t.stock_type_id, t.stock_type
order by count(t.stock_type_id);