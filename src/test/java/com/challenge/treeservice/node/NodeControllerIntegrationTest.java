package com.challenge.treeservice.node;


import com.challenge.treeservice.node.dto.AddChildDto;
import com.challenge.treeservice.node.dto.MoveNodeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureJdbc
public class NodeControllerIntegrationTest extends AbstractTreeserviceIntegrationTest {

    @Autowired
    NodeController nodeController;

    @Test
    public void shouldCreateNodesAndLoadAllDescendants_whenGetDescendants_givenRootNode() {
        // given
        Node root = nodeController.createTree();
        Node level1Child1 = addChild(root);
        Node level1Child2 = addChild(root);
        Node level2Child1 = addChild(level1Child1);
        Node level2Child2 = addChild(level1Child1);
        addChild(level1Child2);
        addChild(level1Child2);
        Node level3Child1 = addChild(level2Child2);
        Node level3Child2 = addChild(level2Child2);
        Node level4Child1 = addChild(level3Child1);
        // when
        List<Node> actualDescendants = nodeController.getDescendants(2L);
        // then
        assertThat(actualDescendants)
                .hasSize(5)
                .containsExactly(level2Child1, level2Child2, level3Child1, level3Child2, level4Child1);
    }

    @Test
    public void shouldThrowExceptionWithStatus404_whenGetDescendants_givenNodeDoesNotExist() {
        // given
        Node root = nodeController.createTree();
        addChild(root);
        addChild(root);
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeController.getDescendants(5L));
        // then
        assertThat(actualThrowable)
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldMoveNodesToAnotherTree_whenMoveNode_givenNodeInTreeAndParentInAnotherTree() {
        // given
        Node root = nodeController.createTree();
        Node tree1Level1Child1 = addChild(root);
        Node tree1Level1Child2 = addChild(root);
        addChild(tree1Level1Child1);
        Node tree1Level2Child2 = addChild(tree1Level1Child1);
        addChild(tree1Level1Child2);
        addChild(tree1Level1Child2);
        Node tree1Level3Child1 = addChild(tree1Level2Child2);
        Node tree1Level3Child2 = addChild(tree1Level2Child2);
        Node tree1Level4Child1 = addChild(tree1Level3Child1);
        Node root2 = nodeController.createTree();
        Node tree2Level1Child1 = addChild(root2);

        // when
        Node actualNode = nodeController.moveNode(new MoveNodeDto(tree1Level2Child2.getId(), tree2Level1Child1.getId()));
        // then
        assertThat(actualNode).isEqualTo(new Node(5L, 12L, 11L, 2L));
        assertThat(nodeController.getDescendants(5L))
                .hasSize(3)
                .extracting(Node::getId, Node::getRootId, Node::getHeight)
                .containsExactly(
                        tuple(tree1Level3Child1.getId(), 11L, 3L),
                        tuple(tree1Level3Child2.getId(), 11L, 3L),
                        tuple(tree1Level4Child1.getId(), 11L, 4L));
    }

    @Test
    public void shouldMoveNodesToAnotherTree_whenMoveNode_givenNodeIsRoot() {
        // given
        Node root = nodeController.createTree();
        Node tree1Level1Child1 = addChild(root);
        Node tree1Level1Child2 = addChild(root);
        addChild(tree1Level1Child1);
        Node tree1Level2Child2 = addChild(tree1Level1Child1);
        addChild(tree1Level1Child2);
        addChild(tree1Level1Child2);
        Node tree1Level3Child1 = addChild(tree1Level2Child2);
        addChild(tree1Level2Child2);
        addChild(tree1Level3Child1);
        Node root2 = nodeController.createTree();
        addChild(root2);

        // when
        Node actualNode = nodeController.moveNode(new MoveNodeDto(root.getId(), root2.getId()));
        // then
        assertThat(actualNode).isEqualTo(new Node(1L, 11L, 11L, 1L));
        assertThat(nodeController.getDescendants(1L))
                .hasSize(9)
                .extracting(Node::getRootId, Node::getHeight)
                .containsExactly(
                        tuple(11L, 2L),
                        tuple(11L, 2L),
                        tuple(11L, 3L),
                        tuple(11L, 3L),
                        tuple(11L, 3L),
                        tuple(11L, 3L),
                        tuple(11L, 4L),
                        tuple(11L, 4L),
                        tuple(11L, 5L));
    }

    @Test
    public void shouldThrowExceptionWithStatus404_whenMoveNode_givenNodeDoesNotExist() {
        // given
        Node root = nodeController.createTree();
        addChild(root);
        addChild(root);
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeController.moveNode(new MoveNodeDto(5L, 6L)));
        // then
        assertThat(actualThrowable)
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldThrowExceptionWithStatus400_whenMoveNode_givenParentIsDescendant() {
        // given
        Node root = nodeController.createTree();
        Node tree1Level1Child1 = addChild(root);
        addChild(tree1Level1Child1);
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeController.moveNode(new MoveNodeDto(2L, 3L)));
        // then
        assertThat(actualThrowable)
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldThrowExceptionWithStatus404_whenAddChild_givenNodeDoesNotExist() {
        // given
        Node root = nodeController.createTree();
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeController.addChild(new AddChildDto(5L)));
        // then
        assertThat(actualThrowable)
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    private Node addChild(Node node) {
        return nodeController.addChild(new AddChildDto(node.getId()));
    }
}
