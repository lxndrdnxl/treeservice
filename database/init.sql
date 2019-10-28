CREATE USER challenger;
ALTER USER challenger PASSWORD 'secret';
CREATE DATABASE challenger;
GRANT ALL PRIVILEGES ON DATABASE challenger TO challenger;
\connect challenger;
CREATE TABLE node (
id int4 NOT NULL,
parent_id int4,
root_id int4 NOT NULL,
height int4 NOT NULL
);
ALTER TABLE node ADD CONSTRAINT pk_node PRIMARY KEY (id);
ALTER TABLE node ADD CONSTRAINT fk_node_parent_id FOREIGN KEY (parent_id) REFERENCES node(id) ON DELETE CASCADE;
ALTER TABLE node ADD CONSTRAINT fk_node_root_id FOREIGN KEY (root_id) REFERENCES node(id) ON DELETE CASCADE;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE node TO challenger;
CREATE SEQUENCE seq_node_id INCREMENT BY 1 START WITH 1 CACHE 1000;
GRANT ALL PRIVILEGES ON SEQUENCE seq_node_id TO challenger;