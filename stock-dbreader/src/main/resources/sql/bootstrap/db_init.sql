DO $$ 
	BEGIN 
	WITH source AS(
SELECT
	'TAIR' as name,
	'http://arabidopsis.org' as url,
	'The Arabidopsis Information Resource' as description,
	null as urlprefix
UNION
SELECT
	'TAIR Stock' as name,
	'http://arabidopsis.org' as url,
	'TAIR Stock Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=stock&id=' as urlprefix
UNION
SELECT
	'TAIR Clone' as name,
	'http://arabidopsis.org' as url,
	'TAIR Clone Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=clone&id=' as urlprefix
UNION
SELECT
	'TAIR Gene' as name,
	'http://arabidopsis.org' as url,
	'TAIR Gene Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=gene&id=' as urlprefix
UNION
SELECT
	'TAIR Germplasm' as name,
	'http://arabidopsis.org' as url,
	'TAIR Germplasm Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=germplasm&id=' as urlprefix
UNION
SELECT
	'TAIR Locus' as name,
	'http://arabidopsis.org' as url,
	'TAIR Locus Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=locus&id=' as urlprefix
UNION
SELECT
	'TAIR Polymorphism' as name,
	'http://arabidopsis.org' as url,
	'TAIR Polymorphism Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=polymorphism&id=' as urlprefix
UNION
SELECT
	'TAIR Species Variant' as name,
	'http://arabidopsis.org' as url,
	'TAIR Species Variant Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=species_variant&id=' as 
	urlprefix
UNION
SELECT
	'TAIR Vector' as name,
	'http://arabidopsis.org' as url,
	'TAIR Species Vector Object Access' as description,
	'http://arabidopsis.org/servlets/TairObject?type=vector&id=' as urlprefix )
	,
	upd AS(
UPDATE
	chado.db t
SET
	name = s.name,
	url = s.url,
	description = s.description,
	urlprefix = s.urlprefix
FROM
	source s
WHERE
	t.name = s.name RETURNING t.name,
	t.url,
	t.description,
	t.urlprefix )
INSERT
	INTO chado.db (
	name,
	url,
	description,
	urlprefix)
SELECT
	s.name,
	s.url,
	s.description,
	s.urlprefix
FROM
	source s
		LEFT JOIN
		upd t
		ON
		t.name = s.name
WHERE
	t.name IS NULL
GROUP BY
	s.name,
	s.url,
	s.description,
	s.urlprefix; 
	END; 
	
	$$;
