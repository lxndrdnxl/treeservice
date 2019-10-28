package com.challenge.treeservice.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    @Autowired
    public NodeService(final NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public Collection<Node> getDescendants(final Integer parentId) {
        /*if (!this.nodeRepository.existsById(parentId)) {

        }*/
        return this.nodeRepository.getDescendants(parentId);
    }
}
