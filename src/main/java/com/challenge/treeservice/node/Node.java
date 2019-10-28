package com.challenge.treeservice.node;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "node")
public class Node {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_node_id")
    @SequenceGenerator(name = "seq_node_id", sequenceName = "seq_node_id")
    private int id;

    private int parentId;

    @NotNull
    private int rootId;

    @NotNull
    private int height;

    public Node(int id, int parentId, int rootId, int height) {
        this.id = id;
        this.parentId = parentId;
        this.rootId = rootId;
        this.height = height;
    }

    public Node() {}

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
