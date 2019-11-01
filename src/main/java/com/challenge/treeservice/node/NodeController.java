package com.challenge.treeservice.node;

import com.challenge.treeservice.node.dto.AddChildDto;
import com.challenge.treeservice.node.dto.MoveNodeDto;
import com.challenge.treeservice.node.exceptions.NewParentIsDescendantException;
import com.challenge.treeservice.node.exceptions.NodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/node")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(final NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * List all descendants of the given node.
     * <p>
     * GET /node/descendants/{nodeId}
     * <p>
     * Returns 404 when the given node could not be found.
     *
     * @param nodeId
     * @return object representation of the descendants of the given node
     */
    @GetMapping("/descendants/{nodeId}")
    public List<Node> getDescendants(@PathVariable("nodeId") @NotNull final Long nodeId) {
        try {
            return this.nodeService.getDescendants(nodeId);
        } catch (NodeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Updates the parent of a node.
     * <p>
     * Returns 404 when the given node could not be found and 400 if the new parent is the descendant of the given node.
     *
     * @param moveNodeDto
     * @return object representation of updated node
     */
    @PutMapping("/moveNode")
    public Node moveNode(@RequestBody @NotNull @Valid final MoveNodeDto moveNodeDto) {
        try {
            return this.nodeService.moveNode(moveNodeDto);
        } catch (NodeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (NewParentIsDescendantException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Creates a new tree.
     *
     * @return object representation of the root node of the newly created tree.
     */
    @GetMapping("/createTree")
    public Node createTree() {
        return this.nodeService.createTree();
    }

    /**
     * Creates a new node as the child of the given node
     * <p>
     * Returns 404 if the given node could not be found.
     *
     * @param addChildDto
     * @return object representation of the newly created node
     */
    @PostMapping("/addChild")
    public Node addChild(@RequestBody @NotNull @Valid final AddChildDto addChildDto) {
        try {
            return this.nodeService.addChild(addChildDto);
        } catch (NodeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
