package com.challenge.treeservice.node;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "nodes")
public class Node {

    @Id
    @NotNull
    private Long id;

    private Long parentId;

    @NotNull
    private Long rootId;

    @NotNull
    private Long height;

    public Node(Long id, Long parentId, Long rootId, Long height) {
        this.id = id;
        this.parentId = parentId;
        this.rootId = rootId;
        this.height = height;
    }

    public Node() {}

    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id) &&
                Objects.equals(parentId, node.parentId) &&
                rootId.equals(node.rootId) &&
                height.equals(node.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, rootId, height);
    }
}
