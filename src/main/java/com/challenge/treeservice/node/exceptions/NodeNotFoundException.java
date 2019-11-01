package com.challenge.treeservice.node.exceptions;


public class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(Long id) {
        super("Node id not found: " + id);
    }
}
