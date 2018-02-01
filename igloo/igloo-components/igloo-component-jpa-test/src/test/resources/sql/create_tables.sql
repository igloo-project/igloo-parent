create schema igloo_test;

set search_path = igloo_test;

create table person (
	id integer not null,
	firstName text not null,
	lastName text not null,
	primary key(id)
);

create sequence person_id_seq owned by person.id;

create table label (
	id text not null,
	value text,
	primary key(id)
);