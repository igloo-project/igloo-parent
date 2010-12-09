create schema owsiexample_test;

set search_path = owsiexample_test;

create table company (
	id integer not null,
	name text not null,
	primary key(id)
);

create sequence company_id_seq owned by company.id;

create table person (
	id integer not null,
	firstName text not null,
	lastName text not null,
	company_id integer references company(id) on update restrict on delete restrict,
	primary key(id)
);

create sequence person_id_seq owned by person.id;

create table project (
	id integer not null,
	name text not null unique,
	company_id integer references company(id) on update restrict on delete restrict,
	creator_id integer references person(id) on update restrict on delete restrict,
	primary key(id)
);

create sequence project_id_seq owned by project.id;

create table person_project (
	persons_id integer not null references person(id) on update restrict on delete restrict,
	projects_id integer not null references project(id) on update restrict on delete restrict,
	primary key(persons_id, projects_id)
);

create table company_employees (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees_id)
);

create table company_employees1 (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees1_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees1_id)
);

create table company_employees2 (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees2_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees2_id)
);

create table company_employees3 (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees3_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees3_id)
);

create table company_employees4 (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees4_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees4_id)
);

create table company_employees5 (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees5_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees5_id)
);

create table company_employees6 (
	company_id integer not null references company(id) on update restrict on delete restrict,
	employees6_id integer not null references person(id) on update restrict on delete restrict,
	primary key(company_id, employees6_id)
);