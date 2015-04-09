ALTER TABLE synonym ADD COLUMN rank int4 default 0;
 
ALTER TABLE synonym DROP constraint synonym_c1; 
ALTER TABLE synonym ADD constraint synonym_c1 unique (name,type_id, rank);

DROP constraint synonym_c1 unique (name,type_id);

ALTER TABLE synonym DROP constraint synonym_c1; 
ALTER TABLE synonym ADD constraint synonym_c1 unique (name,type_id, rank);

 drop table organism_cvterm cascade;
create table organism_cvterm (
       organism_cvterm_id serial not null,
       primary key (organism_cvterm_id),
       organism_id int not null,
       foreign key (organism_id) references organism (organism_id) on delete cascade INITIALLY
DEFERRED,
       cvterm_id int not null,
       foreign key (cvterm_id) references cvterm (cvterm_id) on delete cascade INITIALLY DEFERRED,
       rank int not null default 0,
       pub_id int not null,
       foreign key (pub_id) references pub (pub_id) on delete cascade INITIALLY DEFERRED,
       constraint organism_cvterm_c1 unique(organism_id,cvterm_id,pub_id) 
);
create index organism_cvterm_idx1 on organism_cvterm (organism_id);
create index organism_cvterm_idx2 on organism_cvterm (cvterm_id);

COMMENT ON TABLE organism_cvterm IS 'organism to cvterm associations. Examples: taxonomic name';

COMMENT ON COLUMN organism_cvterm.rank IS 'Property-Value
ordering. Any organism_cvterm can have multiple values for any particular
property type - these are ordered in a list using rank, counting from
zero. For properties that are single-valued rather than multi-valued,
the default 0 value should be used';

drop table organism_cvtermprop cascade;
create table organism_cvtermprop (
    organism_cvtermprop_id serial not null,
    primary key (organism_cvtermprop_id),
    organism_cvterm_id int not null,
    foreign key (organism_cvterm_id) references organism_cvterm (organism_cvterm_id) on delete cascade,
    type_id int not null,
    foreign key (type_id) references cvterm (cvterm_id) on delete cascade INITIALLY DEFERRED,
    value text null,
    rank int not null default 0,
    constraint organism_cvtermprop_c1 unique (organism_cvterm_id,type_id,rank)
);
create index organism_cvtermprop_idx1 on organism_cvtermprop (organism_cvterm_id);
create index organism_cvtermprop_idx2 on organism_cvtermprop (type_id);

COMMENT ON TABLE organism_cvtermprop IS 'Extensible properties for
organism to cvterm associations. Examples: qualifiers';

COMMENT ON COLUMN organism_cvtermprop.type_id IS 'The name of the
property/slot is a cvterm. The meaning of the property is defined in
that cvterm. ';

COMMENT ON COLUMN organism_cvtermprop.value IS 'The value of the
property, represented as text. Numeric values are converted to their
text representation. This is less efficient than using native database
types, but is easier to query.';

COMMENT ON COLUMN organism_cvtermprop.rank IS 'Property-Value
ordering. Any organism_cvterm can have multiple values for any particular
property type - these are ordered in a list using rank, counting from
zero. For properties that are single-valued rather than multi-valued,
the default 0 value should be used';


create table organism_synonym (
	organism_synonym_id serial not null,
	primary key (organism_synonym_id),
	organism_id int not null,
	foreign key (organism_id) references organism (organism_id) on delete cascade INITIALLY DEFERRED,
	synonym_id int not null,
	foreign key (synonym_id) references synonym (synonym_id) on delete cascade INITIALLY DEFERRED,
	pub_id int null,
	foreign key (pub_id) references pub (pub_id) on delete cascade INITIALLY DEFERRED,
	is_current boolean not null default 'false',
	is_internal boolean not null default 'false',
	constraint organism_synonym_c1 unique (synonym_id,organism_id,pub_id)
);
create index organism_synonym_idx1 on organism_synonym (synonym_id);
create index organism_synonym_idx2 on organism_synonym (organism_id);
create index organism_synonym_idx3 on organism_synonym (pub_id);


ALTER TABLE nd_geolocation ADD column max_latitude real;
ALTER TABLE nd_geolocation ADD column min_latitude real;
ALTER TABLE nd_geolocation ADD column max_longitude real;
ALTER TABLE nd_geolocation ADD column min_longitude real;


ALTER TABLE nd_geolocation ADD COLUMN min_altitude float4;
ALTER TABLE nd_geolocation ADD COLUMN max_altitude float4;
	
	
ALTER TABLE nd_geolocation ALTER COLUMN min_longitude TYPE text;
ALTER TABLE nd_geolocation ADD COLUMN max_longitude TYPE text;
	
ALTER TABLE nd_geolocation ALTER COLUMN min_latitude TYPE text;
ALTER TABLE nd_geolocation ALTER COLUMN max_latitude TYPE text;



CREATE TABLE nd_geolocation_cvterm (
	nd_geolocation_cvterm_id serial NOT NULL,
	nd_geolocation_id int4 NOT NULL,
	cvterm_id int4 NOT NULL,
	"rank" int4 DEFAULT 0 NOT NULL,
	pub_id int4,
	PRIMARY KEY (nd_geolocation_cvterm_id)
);

ALTER TABLE nd_geolocation_cvterm
	ADD FOREIGN KEY (nd_geolocation_id) 
	REFERENCES nd_geolocation (nd_geolocation_id);

ALTER TABLE nd_geolocation_cvterm
	ADD FOREIGN KEY (cvterm_id) 
	REFERENCES cvterm (cvterm_id);

ALTER TABLE nd_geolocation_cvterm
	ADD FOREIGN KEY (pub_id) 
	REFERENCES pub (pub_id);
	
	ALTER TABLE nd_geolocation_cvterm ADD constraint nd_geolocation_cvterm_c1 unique(nd_geolocation_id,cvterm_id,pub_id) ;


drop table IF EXISTS organism_nd_geolocation;

create table organism_nd_geolocation (
       organism_nd_geolocation_id serial not null,
       primary key (organism_nd_geolocation_id),
       organism_id int not null,
       foreign key (organism_id) references organism (organism_id) on delete cascade INITIALLY
DEFERRED,
       nd_geolocation_id int not null,
       foreign key (nd_geolocation_id) references nd_geolocation (nd_geolocation_id) on delete cascade INITIALLY DEFERRED,
       rank int not null default 0,
       pub_id int null,
       foreign key (pub_id) references pub (pub_id) on delete cascade INITIALLY DEFERRED,
       constraint organism_nd_geolocation_id_c1 unique(organism_id,nd_geolocation_id,pub_id) 
);

create index organism_nd_geolocation_id_idx1 on organism_nd_geolocation (organism_id);
create index organism_nd_geolocation_idx2 on organism_nd_geolocation (nd_geolocation_id);

