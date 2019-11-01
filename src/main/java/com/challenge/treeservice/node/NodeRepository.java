package com.challenge.treeservice.node;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends CrudRepository<Node, Long> {

    @Query(value = "WITH RECURSIVE descendants AS (" +
            "SELECT id, parent_id, root_id, height " +
            "FROM nodes t1 " +
            "WHERE parent_Id = :nodeId " +
            "UNION ALL " +
            "SELECT t2.id, t2.parent_id, t2.root_id, t2.height " +
            "FROM descendants " +
            "JOIN nodes t2 ON t2.parent_id = descendants.id" +
            ") SELECT id, parent_id, root_id, height FROM descendants" +
            " ORDER BY height asc, id asc;", nativeQuery = true)
    List<Node> getDescendants(@Param("nodeId") Long nodeId);

    @Query(value = "SELECT nextval('seq_node_id') as id", nativeQuery = true)
    Long getNextId();
}
