package com.challenge.treeservice.node.exceptions;

public class NewParentIsDescendantException extends RuntimeException {

    public NewParentIsDescendantException(Long nodeId, Long newParentId) {
        super("Node " + newParentId + " is parent of node " + nodeId);
    }
}
