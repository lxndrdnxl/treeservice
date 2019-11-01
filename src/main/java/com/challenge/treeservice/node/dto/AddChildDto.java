package com.challenge.treeservice.node.dto;

import javax.validation.constraints.NotNull;

public class AddChildDto {

    @NotNull
    private Long nodeId;

    public AddChildDto(Long nodeId) {
        this.nodeId = nodeId;
    }

    public AddChildDto() {}

    public Long getNodeId() {
        return nodeId;
    }

}
