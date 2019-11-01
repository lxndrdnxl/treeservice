CREATE TABLE nodes (
id int8 NOT NULL,
parent_id int8,
root_id int8 NOT NULL,
height int8 NOT NULL
);
ALTER TABLE nodes ADD CONSTRAINT pk_nodes PRIMARY KEY (id);
ALTER TABLE nodes ADD CONSTRAINT fk_nodes_parent_id FOREIGN KEY (parent_id) REFERENCES nodes(id) ON DELETE CASCADE;
CREATE INDEX idx_nodes_root_id_id ON nodes(root_id, id);
CREATE SEQUENCE seq_node_id INCREMENT BY 1 START WITH 1 CACHE 1000;