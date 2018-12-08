//Created by:
// Andrew Bostros cssc0727
// Jan Domingo cssc0749
package edu.sdsu.cs.datastructures;

import javax.management.openmbean.InvalidKeyException;
import java.util.*;

/**
 * Identifies the functions required for a graph object.
 **
 @param <V> The type to use for unique vertex names (e.g. String)
 */
public class DirectedGraph <V> implements IGraph<V> {

    private Map<V, List<V>> adjlist = new HashMap<V, List<V>>();

    public DirectedGraph() {
        Map<V, List<V>> adjlist = new HashMap<V, List<V>>();
    }

    int size = 0;

    /**
     * Inserts a vertex with the specified name into the Graph if it
     * is not already present.
     * *
     *
     * @param vertexName The label to associate with the vertex
     */
    public void add(V vertexName) {
        if (adjlist.containsKey(vertexName)) {
            throw new InvalidKeyException("Duplicate vertex");
        }
        if (!adjlist.containsKey(vertexName)) {
            adjlist.put(vertexName, new ArrayList<V>());
            size++;
        }
    }


    /**
     * Adds a connection between the named vertices if one does not
     * yet exist.
     * *
     *
     * @param start       The first vertex for the edge
     * @param destination The second vertex
     * @throws java.util.NoSuchElementException if either vertex are
     *                                          not present in the graph
     */
    public void connect(V start, V destination) {
        if (!adjlist.containsKey(start) || !adjlist.containsKey(destination)) {
            throw new java.util.NoSuchElementException("Vertex not present in graph");
        } else adjlist.get(start).add(destination);
    }


    /**
     * Resets the graph to an empty state.
     */
    public void clear() {
        adjlist.clear();
        size = 0;
    }


    /**
     * Reports if a vertex with the specified label is stored within
     * the graph.
     * *@param label The vertex name to find
     *
     * @return true if within the graph, false if not.
     */
    public boolean contains(V label) {
        return adjlist.containsKey(label);
    }


    /**
     * Removes the specified edge, if it exists, from the Graph.
     * *
     *
     * @param start       The name of the origin vertex
     * @param destination The name of the terminal vertex
     * @throws NoSuchElementException if either vertex are
     *                                not present in the graph
     */
    public void disconnect(V start, V destination) {
        if (!adjlist.containsKey(start) || !adjlist.containsKey(destination)) {
            throw new NoSuchElementException("Vertices do not exist in the graph");
        } else {
            adjlist.get(start).remove(destination);
        }
    }


    /**
     * Identifies if a path exists between the two vertices.
     * *
     * When the start and destination node names are the same, this
     * method shall only return true if there exists a self-edge on
     * the specified vertex.
     * *
     *
     * @param start       The initial Vertex
     * @param destination The terminal vertex
     * @return True if any path exists between them
     * @throws java.util.NoSuchElementException if either vertex are
     *                                          not present in the graph
     */


    public boolean isConnected(V start, V destination) {
        if (adjlist.containsKey(start) && adjlist.containsKey(destination))
            return !shortestPath(start,destination).isEmpty();

        throw new java.util.NoSuchElementException("Vertex not in graph");
    }



    /**
     * Provides a collection of vertex names directly connected
     * through a single outgoing edge to the target vertex.
     * *
     * Changes to the returned Iterable object (e.g., .remove())
     * shall NOT impact or change the graph.
     * *
     *
     * @param vertexName The target vertex
     * @return An iterable, possibly empty, containing all
     * neighboring vertices.
     * @throws java.util.NoSuchElementException if the vertex is not
     *                                          present in the graph
     */
    public Iterable<V> neighbors(V vertexName) {
        ArrayList<V> neighborList = new ArrayList<>();

        //Checks if the vertexName given exists in the graph
        if (!adjlist.containsKey(vertexName)) {
            throw new java.util.NoSuchElementException("Vertex not found in graph");
        } else {
            int neighborSize = adjlist.get(vertexName).size();
            for (int i = 0; i < neighborSize; i++) {
                neighborList.add(adjlist.get(vertexName).get(i));
            }
        }
        //Returns an arraylist of all the neighbors of the vertex
        return neighborList;
    }


    /**
     * Deletes all trace of the specified vertex from within the
     * graph.
     * <p>
     * This method deletes the vertex from the graph as well as every
     * edge using the specified vertex as a start (out) or
     * destination (in) vertex.
     * *
     *
     * @param vertexName The vertex name to remove from the graph
     * @throws java.util.NoSuchElementException if the origin vertex
     *                                          is not present in this graph
     */
    public void remove(V vertexName) {
        if (adjlist.containsKey(vertexName)) {
            size--;
            adjlist.remove(vertexName);

            //Remove the vertexName node from the list of neighbors for each node in adjlist
            for (V vertex : adjlist.keySet()) {
                if (neighbors(vertex) == (vertexName));
                    adjlist.get(vertex).remove(vertexName);

            }
        } else {
            throw new java.util.NoSuchElementException("Vertex does not exist on the graph");
        }
    }

    /**
     * Returns one shortest path through the graph from the starting
     * vertex and ending in the destination vertex.
     * *
     *
     * @param start       The vertex from which to begin the search
     * @param destination The terminal vertex within the graph
     * @return A sequence of vertices to visit requiring the fewest
     * steps through the graph from its starting position
     * (at index 0 in the list) to its terminus at the list's end.
     * If no path exists between the nodes, this method
     * returns an empty list.
     * @throws java.util.NoSuchElementException if either vertex are
     *                                          not present in the graph
     */

    ArrayList<V> shortestPathList=new ArrayList<V>();
    ArrayList<V> visited = new ArrayList<>();
    public List<V> shortestPath(V start, V destination) {

        if (!adjlist.containsKey(start) || !adjlist.containsKey(destination)) {
            throw new java.util.NoSuchElementException("Vertex does not exist on the graph");
        }

        Queue<V> queue = new ArrayDeque<>();
        shortestPathList.clear();
        queue.add(start);

        //Uses Breadth First Search to get the shortest path in an unweighted graph
            while (!queue.isEmpty()) {
                V current = queue.poll();
                if (current.equals(destination)) {

                    int i = shortestPathList.size()-1; //Last index in shortestPathList
                    V previous = shortestPathList.get(i);//Gets the parent Node

                    if (adjlist.get(previous).size() > 1); {
                        //If the parent node have multiple neighbors,
                        //remove searched neighbors on the same 'level' to get the shortest path
                        for (V neighbor : adjlist.get(previous)) {
                            for (int k = 0; k < adjlist.get(previous).size(); k++) {
                                if (!neighbor.equals(destination)) {
                                    shortestPathList.remove(i);
                                    i--;
                                }
                                shortestPathList.add(current);
                                visited.add(current);
                                return shortestPathList;
                            }
                        }
                    }
                    shortestPathList.add(current);
                    return shortestPathList;
                } else {
                    if (adjlist.get(current).isEmpty())
                        return shortestPathList;
                    else
                        queue.addAll((Collection<? extends V>) neighbors(current));
                }
                visited.add(current);
                shortestPathList.add(current);
                }
        return shortestPathList;
    }


    /**
     * Reports the number of vertices in the Graph.
     * *
     *
     * @return a non-negative number.
     */
    public int size() {
        return size;
    }


    /**
     * Provides a collection of vertex names currently in the graph.
     * *
     *
     * @return The names of the vertices within the graph.
     */
    public Iterable<V> vertices() {
        return adjlist.keySet();
    }


    /**
     * Produces a graph of only those vertices and edges reachable
     * from the origin vertex.
     * *
     *
     * @param origin The vertex to build the graph from
     * @return A new graph with only the Vertices and Edges
     * reachable from the parameter Vertex.
     * @throws java.util.NoSuchElementException if the origin vertex
     *                                          is not present in this graph
     */

    Map<V, List<V>> connectedList = new HashMap<V, List<V>>();
    public IGraph<V> connectedGraph(V origin) {

        if (!adjlist.containsKey(origin)) {
            throw new java.util.NoSuchElementException("Vertex not found in graph");
        }

        DirectedGraph<V> connectedGraph = new DirectedGraph<>();

        //Puts all the nodes that are connected to the origin into a list called connectedList
            for (V vertex : adjlist.keySet()) {
                if (adjlist.containsKey(vertex)) {
                    connectedList.put((V) adjlist.get(vertex), adjlist.get(vertex).subList(0, adjlist.get(vertex).size()));
                    connectedGraph.add(vertex);
                }
            }
        return connectedGraph;
    }


    //toString method to output information about the graph
    public String toString() {
        return this.output();
    }

    private String output() {
        String print;
        if (this.size > 0) {
            print = "Information about the graph: \n\n";
            print += "Amount of vertices: " + this.size + "\n\n";
            print += "All the vertices: ";
            for (V vertex : adjlist.keySet()) {
                print += "\n\t" + vertex;
                IGraph<V> connectedVertices = connectedGraph(vertex);
            }

            print += "\n\nAll the connections: ";
            for (V neighbor : connectedList.keySet()) {
                print += "\n\t" + neighbor;
            }
            }
        else {
            print = "Graph is empty. No vertices found.";
        }
        return print;
    }
}
