
DO $$

BEGIN
WITH source AS(
SELECT 'stock_type' as name, 'Contains a list of types for stocks' as definition
UNION
SELECT 'stock_availability' as name, 'Contains a list of availabilities for stocks' as definition
),
upd AS(
UPDATE chado.cv t
SET name = s.name, definition = s.definition
FROM source s
WHERE t.name = s.name
RETURNING t.name, t.definition
)
INSERT INTO chado.cv (name,definition)
SELECT s.name, s.definition from source s
LEFT JOIN
upd t ON
t.name = s.name
WHERE t.name is NULL
GROUP BY s.name, s.definition;
END;

$$;
