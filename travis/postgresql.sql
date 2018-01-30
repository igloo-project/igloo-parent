CREATE USER basic_application_test WITH PASSWORD 'basic_application_test';
CREATE USER igloo_test WITH PASSWORD 'igloo_test';
CREATE USER iglooexample_test WITH PASSWORD 'iglooexample_test';

CREATE DATABASE basic_application_test WITH OWNER basic_application_test;
CREATE DATABASE igloo_test WITH OWNER igloo_test;
CREATE DATABASE iglooexample_test WITH OWNER iglooexample_test;

\c igloo_test
CREATE SCHEMA igloo_test AUTHORIZATION igloo_test;

\c basic_application_test
CREATE SCHEMA basic_application_test AUTHORIZATION basic_application_test;
