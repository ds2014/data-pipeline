DROP MATERIALIZED VIEW IF EXISTS staging.external_url_transformation;
CREATE
	materialized view staging.external_url_transformation AS
select 
germplasm_id,
germplasm_tair_object_id,
germplasm_name,
case when (url like '%http://www.gabi-kat.de/db/showseq.php%')
	then substring(url, 1, position('=' in substring(url, 1, position('=' in url))))
	else
		url
end as urlprefix,
case when (url like '%http://www.gabi-kat.de/db/showseq.php%')
	then substring
	(
	substring(
	url,
	position(
	'http' IN url)
	+length(
	'http')
	+3)
	,1,
	position(
	'/' in
	substring(
	url,
	position(
	'http' IN url)
	+length(
	'http')
	+3)
	)
  )
	else
		url
end as url,
case when (url like '%http://www.gabi-kat.de/db/showseq.php%')
	then substring(
	url,
	position(
	'=' IN url)
	+1
	)
	else null
end as accession
,
web_site_name db_name
from staging.germplasm_external_url

