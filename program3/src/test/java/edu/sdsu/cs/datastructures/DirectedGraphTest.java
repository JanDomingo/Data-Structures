package edu.sdsu.cs.datastructures;

import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * An example test class / grading script for a directed graph.
 */
public class DirectedGraphTest {

    private final int DEFAULT_TEST_SIZE = 10;
    private final int NODE_INITIAL = 0;
    private final int NODE_FINAL = 200;
    private final int NODE_MISSING = -1;

    @Test
    public void defaultConstructor_initCorrectly() {
        // action
        DirectedGraph<String> sut = new DirectedGraph<>();

        assertEquals(0, sut.size());
        try {
            for (String label : sut.vertices())
                fail(String.format("Structure should be empty. Found: %s", label));
        } catch (NullPointerException e) {
            fail("Must implement vertex iterator");
            e.printStackTrace();
        }
    }

    @Test
    public void add_sequentialVerts_allPresent() {
        DirectedGraph<Integer> sut = new DirectedGraph<>();

        for (int i = 0; i < DEFAULT_TEST_SIZE; i++) {
            sut.add(i);
        }

        assertEquals(DEFAULT_TEST_SIZE, sut.size());
        for (int i = 0; i < DEFAULT_TEST_SIZE; i++) {
            assertTrue(sut.contains(i));
        }
    }

    @Test
    public void neighbors_invalidVertex_exceptionThrown() {
        DirectedGraph<Integer> sut = new DirectedGraph<>();

        sut.add(NODE_INITIAL);
        sut.add(NODE_FINAL);

        try {
            sut.neighbors(NODE_MISSING);
            fail("Start Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    @Test
    public void connect_invalidVertex_exceptionThrown() {
        DirectedGraph<Integer> sut = new DirectedGraph<>();

        sut.add(NODE_INITIAL);
        sut.add(NODE_FINAL);

        try {
            sut.connect(NODE_MISSING, NODE_INITIAL);
            fail("Start Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            sut.connect(NODE_INITIAL, NODE_MISSING);
            fail("Destination Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

    }

    @Test
    public void shortestPath_invalidVertex_exceptionThrown() {
        DirectedGraph<Integer> sut = new DirectedGraph<>();

        sut.add(0);
        sut.add(2);

        try {
            sut.connect(NODE_MISSING, NODE_INITIAL);
            fail("Start Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            sut.shortestPath(0, 1);
            fail("Destination Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

    }

    @Test
    public void remove_invalidVertex_exceptionThrown() {
        DirectedGraph<Integer> sut = new DirectedGraph<>();

        sut.add(NODE_INITIAL);
        sut.add(NODE_FINAL);

        try {
            sut.remove(NODE_MISSING);
            fail("Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

    }

    @Test
    public void remove_middleLayer_validSize(){
        DirectedGraph<Integer> sut = buildSimpleThreeLayer(new DirectedGraph<Integer>());

        sut.remove(10);
        sut.remove(11);
        sut.remove(12);

        assertEquals(2, sut.size());
    }

    @Test
    public void remove_middleLayer_isConnectedBreaks(){
        DirectedGraph<Integer> sut = buildSimpleThreeLayer(new DirectedGraph<Integer>());

        assertTrue(sut.isConnected(NODE_INITIAL,200));
        sut.remove(10);
        assertTrue(sut.isConnected(NODE_INITIAL,200));
        sut.remove(11);
        assertTrue(sut.isConnected(NODE_INITIAL,200));
        sut.remove(12);

        assertFalse(sut.isConnected(NODE_INITIAL,200));
    }

    private DirectedGraph<Integer> buildSimpleThreeLayer(DirectedGraph<Integer> sut) {
        // initial layer
        sut.add(NODE_INITIAL);
        // middle layer
        sut.add(10);
        sut.connect(NODE_INITIAL,10);
        sut.add(11);
        sut.connect(NODE_INITIAL,11);
        sut.add(12);
        sut.connect(NODE_INITIAL,12);
        // final layer
        sut.add(NODE_FINAL);
        sut.connect(10,NODE_FINAL);
        sut.connect(11,NODE_FINAL);
        sut.connect(12,NODE_FINAL);

        return sut;
    }


    @Test
    public void disconnect_middleLayer_validSize(){
        DirectedGraph<Integer> sut = buildSimpleThreeLayer(new DirectedGraph<Integer>());

        sut.disconnect(NODE_INITIAL,10);
        sut.disconnect(NODE_INITIAL,11);
        sut.disconnect(NODE_INITIAL,12);

        assertEquals(5, sut.size());
    }

    @Test
    public void disconnect_middleLayer_isConnectedBreaks(){
        DirectedGraph<Integer> sut = buildSimpleThreeLayer(new DirectedGraph<Integer>());

        assertTrue(sut.isConnected(NODE_INITIAL,200));
        sut.disconnect(NODE_INITIAL,10);
        assertTrue(sut.isConnected(NODE_INITIAL,200));
        sut.disconnect(NODE_INITIAL,11);
        assertTrue(sut.isConnected(NODE_INITIAL,200));
        sut.disconnect(NODE_INITIAL,12);

        assertFalse(sut.isConnected(NODE_INITIAL,200));
    }

    @Test
    public void disconnect_invalidVertex_exceptionThrown() {
        DirectedGraph<Integer> sut = new DirectedGraph<>();

        sut.add(NODE_INITIAL);
        sut.add(NODE_FINAL);

        try {
            sut.disconnect(NODE_MISSING, NODE_INITIAL);
            fail("Start Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            sut.disconnect(NODE_INITIAL, NODE_MISSING);
            fail("Destination Exception missing");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

    }

    @Test
    public void connect_sequentialVerts_allPresent() {
        DirectedGraph<Integer> sut = straightLineGraph(new DirectedGraph<Integer>());

        for (Integer current = 0; current < DEFAULT_TEST_SIZE - 1; current++) {
            if (!sut.neighbors(current).iterator().hasNext())
                fail(String.format("No neighbors: [%d]", current));
            for (Integer neighbor : sut.neighbors(current))
                assertEquals(current + 1, (int) neighbor);
        }
    }

    private DirectedGraph<Integer> straightLineGraph(DirectedGraph<Integer> sut) {
        int previous = 0;
        sut.add(previous);
        for (int current = 1; current < DEFAULT_TEST_SIZE; current++) {
            sut.add(current);
            sut.connect(previous, current);
            previous = current;
        }

        return sut;
    }

    @Test
    public void connectedGraph_subgraph_correct() {

        DirectedGraph<Integer> original = new DirectedGraph<>();

        straightLineGraph(original);

        int subIndex = DEFAULT_TEST_SIZE >> 1;
        IGraph<Integer> sut = original.connectedGraph(subIndex);


        for (int current = 0; current < subIndex; current++) {
            assertFalse(sut.contains(current));
        }
        for (int current = subIndex; current < DEFAULT_TEST_SIZE; current++) {
            assertTrue(sut.contains(current));
        }

        assertTrue(sut.isConnected(subIndex, DEFAULT_TEST_SIZE - 1));
        assertFalse(sut.isConnected(DEFAULT_TEST_SIZE - 1, subIndex));
    }

    @Test
    public void shortestPath_singlePathInGraph_correctSizeAndSequence() {
        DirectedGraph<Integer> sut = straightLineGraph(new DirectedGraph<Integer>());

        List<Integer> path = sut.shortestPath(0, DEFAULT_TEST_SIZE - 1);

        assertEquals(DEFAULT_TEST_SIZE, path.size());
        for (int step = 0; step < DEFAULT_TEST_SIZE; step++) {
            assertEquals((Integer) step, path.get(step));
        }

    }

    @Test
    public void shortestPath_twoPathsInGraph_correctSizeAndSequence() {
        DirectedGraph<Integer> sut = straightLineGraph(new DirectedGraph<Integer>());
        sut.connect(0, DEFAULT_TEST_SIZE - 1);

        List<Integer> path = sut.shortestPath(0, DEFAULT_TEST_SIZE - 1);

        assertEquals(2, path.size());
        assertEquals((Integer) 0, path.get(0));
        assertEquals((Integer) (DEFAULT_TEST_SIZE - 1), path.get(1));

    }

    @Test
    public void clear_sequentialGraph_empty(){
        DirectedGraph<Integer> sut = straightLineGraph(new DirectedGraph<Integer>());

        sut.clear();

        assertEquals(0, sut.size());
        for( Integer vertex : sut.vertices()){
            fail(String.format("Found vertex: %s", vertex));
        }
    }
}