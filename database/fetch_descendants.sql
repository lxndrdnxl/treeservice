WITH RECURSIVE descendants AS (
SELECT id, parent_id, root_id, height FROM node t1 WHERE parent_id = 0
	UNION ALL 
	SELECT t2.id, t2.parent_id, t2.root_id, t2.height FROM descendants JOIN node t2 ON t2.parent_id = descendants.id
)
SELECT * FROM descendants;