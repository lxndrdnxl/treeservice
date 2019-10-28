package com.challenge.treeservice.node;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NodeRepository extends CrudRepository<Node, Integer> {

    @Query(value = "WITH RECURSIVE descendants AS (" +
            "SELECT id, parent_id, root_id, height " +
            "FROM node t1 " +
            "WHERE parent_id = :parentId " +
            "UNION ALL " +
            "SELECT t2.id, t2.parent_id, t2.root_id, t2.height " +
            "FROM descendants " +
            "JOIN node t2 ON t2.parent_id = descendants.id" +
            ") SELECT id, parent_id, root_id, height FROM descendants;", nativeQuery = true)
    Collection<Node> getDescendants(@Param("parentId") Integer parentId);
}
