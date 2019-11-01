package com.challenge.treeservice.node;

import com.challenge.treeservice.node.dto.AddChildDto;
import com.challenge.treeservice.node.dto.MoveNodeDto;
import com.challenge.treeservice.node.exceptions.NewParentIsDescendantException;
import com.challenge.treeservice.node.exceptions.NodeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

public class NodeServiceUnitTest {

    @Mock
    NodeRepository nodeRepositoryMock;

    @InjectMocks
    NodeService nodeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void throwsNodeNotFoundException_whenGetDescendants_givenParentDoesNotExist() {
        // given
        Long givenParentId = 1L;
        when(nodeRepositoryMock.existsById(givenParentId)).thenReturn(false);
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeService.getDescendants(givenParentId));
        // then
        assertThat(actualThrowable).isInstanceOf(NodeNotFoundException.class).hasMessageContaining("1");
    }

    @Test
    public void shouldReturnListOfDescendants_whenGetDescendants_givenParentHasChildren() {
        // given
        Long givenParentId = 1L;
        List<Node> expectedDescendants = Arrays.asList(
                new Node(2L, 1L, 1L, 1L),
                new Node(3L, 1L, 1L, 1L));
        when(nodeRepositoryMock.existsById(givenParentId)).thenReturn(true);
        when(nodeRepositoryMock.getDescendants(givenParentId)).thenReturn(expectedDescendants);
        // when
        List<Node> actualDescendants = nodeService.getDescendants(givenParentId);
        // then
        assertThat(actualDescendants).isEqualTo(expectedDescendants);
    }

    @Test
    public void shouldReturnNewRootNode_whenCreateTree() {
        // given
        Node newNode = new Node(1L, null, 1L, 0L);
        when(nodeRepositoryMock.getNextId()).thenReturn(1L);
        when(nodeRepositoryMock.save(newNode)).thenReturn(newNode);
        // when
        Node actualNode = nodeService.createTree();
        // then
        assertThat(actualNode).isEqualTo(newNode);
    }

    @Test
    public void shouldThrowNodeNotFoundException_whenAddChild_givenParentDoesNotExist() {
        // given
        AddChildDto givenAddChildDto = new AddChildDto(1L);
        when(nodeRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeService.addChild(givenAddChildDto));
        // then
        assertThat(actualThrowable).isInstanceOf(NodeNotFoundException.class).hasMessageContaining("1");
    }

    @Test
    public void shouldReturnNewChild_whenAddChild_givenParentExists() {
        // given
        AddChildDto givenAddChildDto = new AddChildDto(1L);
        Node expectedNode = new Node(2L, 1L, 1L, 1L);
        when(nodeRepositoryMock.findById(1L)).thenReturn(Optional.of(new Node(1L, null, 1L, 0L)));
        when(nodeRepositoryMock.getNextId()).thenReturn(2L);
        when(nodeRepositoryMock.save(expectedNode)).thenReturn(expectedNode);
        // when
        Node actualNode = nodeService.addChild(givenAddChildDto);
        // then
        assertThat(actualNode).isEqualTo(expectedNode);
    }

    @Test
    public void shouldThrowNodeNotFoundException_whenMoveNode_givenNodeDoesNotExist() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(1L, 2L);
        when(nodeRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeService.moveNode(givenMoveNodeDto));
        // then
        assertThat(actualThrowable).isInstanceOf(NodeNotFoundException.class).hasMessageContaining("1");
    }

    @Test
    public void shouldThrowNodeNotFoundException_whenMoveNode_givenParentDoesNotExist() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(1L, 2L);
        when(nodeRepositoryMock.findById(1L)).thenReturn(Optional.of(new Node(1L, null, 1L, 0L)));
        when(nodeRepositoryMock.findById(2L)).thenReturn(Optional.empty());
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeService.moveNode(givenMoveNodeDto));
        // then
        assertThat(actualThrowable).isInstanceOf(NodeNotFoundException.class).hasMessageContaining("2");
    }

    @Test
    public void shouldThrowNewParentIsDescendantException_whenMoveNode_givenNewParentIsDescendant() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(1L, 2L);
        Node node = new Node(1L, null, 1L, 0L);
        Node newParent = new Node(2L, 1L, 1L, 1L);
        when(nodeRepositoryMock.findById(1L)).thenReturn(Optional.of(node));
        when(nodeRepositoryMock.findById(2L)).thenReturn(Optional.of(newParent));
        when(nodeRepositoryMock.getDescendants(1L)).thenReturn(Arrays.asList(newParent));
        // when
        Throwable actualThrowable = catchThrowable(() -> nodeService.moveNode(givenMoveNodeDto));
        // then
        assertThat(actualThrowable).isInstanceOf(NewParentIsDescendantException.class).hasMessageContainingAll("1", "2");
    }

    @Test
    public void shouldUpdateNode_whenMoveNode_givenNodeIsNotRootAndDoesNotHaveDescendants() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(2L, 3L);
        Node node = new Node(2L, 1L, 1L, 1L);
        Node newParent = new Node(3L, 1L, 1L, 1L);
        Node expectedNode = new Node(2L, 3L, 1L, 2L);
        when(nodeRepositoryMock.findById(2L)).thenReturn(Optional.of(node));
        when(nodeRepositoryMock.findById(3L)).thenReturn(Optional.of(newParent));
        when(nodeRepositoryMock.getDescendants(2L)).thenReturn(Collections.emptyList());
        // when
        Node actualNode = nodeService.moveNode(givenMoveNodeDto);
        // then
        assertThat(actualNode).isEqualTo(expectedNode);
    }

    @Test
    public void shouldUpdateNodeAndDescendants_whenMoveNode_givenNodeIsNotRootAndHasDescendants() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(2L, 3L);
        Node node = new Node(2L, 1L, 1L, 1L);
        Node newParent = new Node(3L, 1L, 1L, 1L);
        List<Node> descendants = Arrays.asList(
                new Node(4L, 2L, 1L, 2L),
                new Node(5L, 2L, 1L, 2L),
                new Node(6L, 4L, 1L, 3L),
                new Node(7L, 6L, 1L, 4L));
        Node expectedNode = new Node(2L, 3L, 1L, 2L);
        when(nodeRepositoryMock.findById(2L)).thenReturn(Optional.of(node));
        when(nodeRepositoryMock.findById(3L)).thenReturn(Optional.of(newParent));
        when(nodeRepositoryMock.getDescendants(2L)).thenReturn(descendants);
        // when
        Node actualNode = nodeService.moveNode(givenMoveNodeDto);
        // then
        assertThat(actualNode).isEqualTo(expectedNode);
        assertThat(descendants).extracting(Node::getHeight).containsExactly(3L, 3L, 4L, 5L);
    }

    @Test
    public void shouldUpdateNodeAndDescendants_whenMoveNode_givenNodeIsRootAndHasDescendants() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(2L, 1L);
        Node node = new Node(2L, null, 2L, 0L);
        Node newParent = new Node(1L, null, 1L, 0L);
        List<Node> descendants = Arrays.asList(
                new Node(3L, 2L, 2L, 1L),
                new Node(4L, 2L, 2L, 1L),
                new Node(5L, 3L, 2L, 2L),
                new Node(6L, 5L, 2L, 3L));
        Node expectedNode = new Node(2L, 1L, 1L, 1L);
        when(nodeRepositoryMock.findById(2L)).thenReturn(Optional.of(node));
        when(nodeRepositoryMock.findById(1L)).thenReturn(Optional.of(newParent));
        when(nodeRepositoryMock.getDescendants(2L)).thenReturn(descendants);
        // when
        Node actualNode = nodeService.moveNode(givenMoveNodeDto);
        // then
        assertThat(actualNode).isEqualTo(expectedNode);
        assertThat(descendants)
                .extracting(Node::getRootId, Node::getHeight)
                .containsExactly(
                        tuple(1L, 2L),
                        tuple(1L, 2L),
                        tuple(1L, 3L),
                        tuple(1L, 4L));
    }

    @Test
    public void shouldUpdateNodeAndDescendants_whenMoveNode_givenNodeIsNotRootAndHasDescendantsAndNewParentHasLowerHeight() {
        // given
        MoveNodeDto givenMoveNodeDto = new MoveNodeDto(10L, 2L);
        Node node = new Node(10L, 9L, 7L, 3L);
        Node newParent = new Node(2L, 1L, 1L, 1L);
        List<Node> descendants = Arrays.asList(
                new Node(11L, 10L, 7L, 4L),
                new Node(12L, 10L, 7L, 4L),
                new Node(13L, 11L, 7L, 5L),
                new Node(14L, 13L, 7L, 6L));
        Node expectedNode = new Node(10L, 2L, 1L, 2L);
        when(nodeRepositoryMock.findById(10L)).thenReturn(Optional.of(node));
        when(nodeRepositoryMock.findById(2L)).thenReturn(Optional.of(newParent));
        when(nodeRepositoryMock.getDescendants(10L)).thenReturn(descendants);
        // when
        Node actualNode = nodeService.moveNode(givenMoveNodeDto);
        // then
        assertThat(actualNode).isEqualTo(expectedNode);
        assertThat(descendants)
                .extracting(Node::getRootId, Node::getHeight)
                .containsExactly(
                        tuple(1L, 3L),
                        tuple(1L, 3L),
                        tuple(1L, 4L),
                        tuple(1L, 5L));
    }
}
