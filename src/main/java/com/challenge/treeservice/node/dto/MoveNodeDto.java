package com.challenge.treeservice.node.dto;

import javax.validation.constraints.NotNull;

public class MoveNodeDto {

    @NotNull
    private Long nodeId;

    private Long newParentId;

    public MoveNodeDto(Long nodeId, Long newParentId) {
        this.nodeId = nodeId;
        this.newParentId = newParentId;
    }

    public MoveNodeDto() {}

    public Long getNodeId() {
        return nodeId;
    }

    public Long getNewParentId() {
        return newParentId;
    }
}
