package com.challenge.treeservice.node;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureJdbc
class NodeRepositoryIntegrationTest extends AbstractTreeserviceIntegrationTest {

    @Autowired
    private NodeRepository nodeRepository;

    @Test
    public void shouldLoadAllDescendants_whenGetDescendants_givenRootNode() {
        // given
        createAndSaveNode(1L, null, 1L, 0L);
        createAndSaveNode(2L, 1L, 1L, 1L);
        createAndSaveNode(3L, 1L, 1L, 1L);
        createAndSaveNode(4L, 1L, 1L, 1L);
        createAndSaveNode(5L, 2L, 1L, 2L);
        createAndSaveNode(6L, 2L, 1L, 2L);
        createAndSaveNode(7L, 3L, 1L, 2L);
        createAndSaveNode(8L, 3L, 1L, 2L);
        createAndSaveNode(9L, 4L, 1L, 2L);
        createAndSaveNode(10L, 4L, 1L, 2L);
        createAndSaveNode(11L, null, 11L, 0L);
        createAndSaveNode(12L, 11L, 11L, 1L);
        createAndSaveNode(13L, 12L, 11L, 2L);
        createAndSaveNode(14L, 12L, 11L, 2L);
        // when
        List<Node> actualDescendants = nodeRepository.getDescendants(11L);
        // then
        assertThat(actualDescendants)
                .hasSize(3)
                .containsExactly(new Node(12L, 11L, 11L, 1L),
                        new Node(13L, 12L, 11L, 2L),
                        new Node(14L, 12L, 11L, 2L));
    }

    @Test
    public void shouldLoadDescendants_whenGetDescendants_givenParentHasMultipleChildren() {
        // given
        createAndSaveNode(1L, null, 1L, 0L);
        createAndSaveNode(2L, 1L, 1L, 1L);
        createAndSaveNode(3L, 1L, 1L, 1L);
        createAndSaveNode(4L, 1L, 1L, 1L);
        createAndSaveNode(5L, 2L, 1L, 2L);
        createAndSaveNode(6L, 2L, 1L, 2L);
        createAndSaveNode(7L, 3L, 1L, 2L);
        createAndSaveNode(8L, 3L, 1L, 2L);
        createAndSaveNode(9L, 4L, 1L, 2L);
        createAndSaveNode(10L, 4L, 1L, 2L);
        // when
        List<Node> actualDescendants = nodeRepository.getDescendants(2L);
        // then
        assertThat(actualDescendants)
                .hasSize(2)
                .containsExactly(new Node(5L, 2L, 1L, 2L),
                        new Node(6L, 2L, 1L, 2L));
    }

    @Test
    public void shouldReturnEmptyList_whenGetDescendants_givenParentHasNoChildren() {
        // given
        createAndSaveNode(1L, null, 1L, 0L);
        createAndSaveNode(2L, 1L, 1L, 1L);
        createAndSaveNode(3L, 1L, 1L, 1L);
        createAndSaveNode(4L, 1L, 1L, 1L);
        createAndSaveNode(5L, 2L, 1L, 2L);
        createAndSaveNode(6L, 2L, 1L, 2L);
        createAndSaveNode(7L, 3L, 1L, 2L);
        createAndSaveNode(8L, 3L, 1L, 2L);
        createAndSaveNode(9L, 4L, 1L, 2L);
        createAndSaveNode(10L, 4L, 1L, 2L);
        // when
        List<Node> actualDescendants = nodeRepository.getDescendants(10L);
        // then
        assertThat(actualDescendants).isEmpty();
    }

    @Test
    public void shouldReturnEmptyList_whenGetDescendants_givenParentDoesNotExist() {
        // given
        List<Node> actualDescendants = nodeRepository.getDescendants(10L);
        // then
        assertThat(actualDescendants).isEmpty();
    }

    @Test
    public void shouldReturnNewId_whenGetNextId() {
        // when
        Long actualId = nodeRepository.getNextId();
        // then
        assertThat(actualId).isEqualTo(1L);
    }

    public void createAndSaveNode(Long id, Long parentId, Long rootId, Long height) {
        Node node = new Node(id, parentId, rootId, height);
        nodeRepository.save(node);
    }

}
