UPDATE
	chado.cvterm t
SET
	cv_id =:cv_id,
	name =:name,
	definition =:definition,
	dbxref_id =:dbxref_id,
	is_obsolete = :is_obsolete,
	is_relationshiptype=:is_relationshiptype
where t.cv_id=:cv_id and t.dbxref_id=:dbxref_id