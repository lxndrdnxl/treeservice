package com.challenge.treeservice.node;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/node")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(final NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/descendants/{parentId}")
    public Collection<Node> getDescendants(@PathVariable @NotNull final Integer parentId) {
        return this.nodeService.getDescendants(parentId);
    }
}
