--CREATE ROLE rightnabove PASSWORD 'rightnabove' LOGIN;
--CREATE DATABASE rightnabove OWNER rightnabove encoding 'UTF8'; 

CREATE TABLE departments (
	id BIGINT CONSTRAINT pk_departments PRIMARY KEY,
	name VARCHAR(4000)
);

CREATE TABLE employees (
	id BIGINT CONSTRAINT pk_employees PRIMARY KEY,
	department_id BIGINT REFERENCES DEPARTMENTS (id),
	first_name VARCHAR(4000) NOT NULL,
	last_name VARCHAR(4000) NOT NULL,
	salary NUMERIC(15, 2) NOT NULL,
	birthdate DATE NOT NULL,
	active BOOLEAN NOT NULL
);

CREATE SEQUENCE seq_id START 1001;
--GRANT USAGE, SELECT ON SEQUENCE seq_id TO rightnabove;

COMMIT;