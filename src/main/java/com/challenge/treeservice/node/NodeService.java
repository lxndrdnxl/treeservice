package com.challenge.treeservice.node;

import com.challenge.treeservice.node.dto.AddChildDto;
import com.challenge.treeservice.node.dto.MoveNodeDto;
import com.challenge.treeservice.node.exceptions.NewParentIsDescendantException;
import com.challenge.treeservice.node.exceptions.NodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    @Autowired
    public NodeService(final NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    /**
     * Lists the descendants of the given node.
     * Includes the children of the children and so on but excludes the given node.
     *
     * @param nodeId
     * @return list of nodes ordered by their height and id
     * @throws NodeNotFoundException when the given node does not exist
     */
    public List<Node> getDescendants(final Long nodeId) {
        if (!this.nodeRepository.existsById(nodeId)) {
            throw new NodeNotFoundException(nodeId);
        }
        return this.nodeRepository.getDescendants(nodeId);
    }

    /**
     * Changes the parent of a node.
     * Updates the root id and the height of the given node and it's descendants according to the new parent.
     *
     * @param moveNodeDto request object containing the id of a node and the id of the new parent
     * @return object representation of the updated node
     * @throws NodeNotFoundException          when the given nodes do not exist
     * @throws NewParentIsDescendantException if the new parent is a descendant of the given node
     */
    @Transactional
    public Node moveNode(MoveNodeDto moveNodeDto) {
        Node node = this.nodeRepository.findById(moveNodeDto.getNodeId()).
                orElseThrow(() -> new NodeNotFoundException(moveNodeDto.getNodeId()));
        Node newParent = this.nodeRepository.findById(moveNodeDto.getNewParentId())
                .orElseThrow(() -> new NodeNotFoundException(moveNodeDto.getNewParentId()));
        List<Node> descendants = this.nodeRepository.getDescendants(moveNodeDto.getNodeId());
        if (descendants.contains(newParent)) {
            throw new NewParentIsDescendantException(node.getId(), newParent.getId());
        }
        Long heightAdjustmentFactor = newParent.getHeight() - node.getHeight() + 1;
        node.setParentId(newParent.getId());
        node.setHeight(node.getHeight() + heightAdjustmentFactor);
        node.setRootId(newParent.getRootId());
        for (Node descendant : descendants) {
            descendant.setRootId(newParent.getRootId());
            descendant.setHeight(descendant.getHeight() + heightAdjustmentFactor);
        }
        return node;
    }

    /**
     * Creates a new node without a parent.
     *
     * @return object representation of new root node
     */
    @Transactional
    public Node createTree() {
        Long rootId = this.nodeRepository.getNextId();
        Node newRoot = new Node(rootId, null, rootId, 0L);
        return this.nodeRepository.save(newRoot);
    }

    /**
     * Creates a new node as a child of the given node.
     *
     * @param addChildDto request object containing id of the node where a new child is to be created
     * @return object representation of newly created child node
     * @throws NodeNotFoundException when the node does not exist
     */
    @Transactional
    public Node addChild(AddChildDto addChildDto) {
        Node node = this.nodeRepository.findById(addChildDto.getNodeId())
                .orElseThrow(() -> new NodeNotFoundException(addChildDto.getNodeId()));
        Long childId = this.nodeRepository.getNextId();
        Node child = new Node(childId, node.getId(), node.getRootId(), node.getHeight() + 1);
        return this.nodeRepository.save(child);
    }
}
