create schema owsicore;

set search_path = owsicore;

create table person (
	id integer not null,
	firstName text not null,
	lastName text not null,
	primary key(id)
);

create sequence person_id_seq owned by person.id;