import java.util.*;
/**
* Minerva Chen, Max Peterson
* 11/22/2013
* MyGraph
*
* A representation of a graph.
* Does not allow negative cost edges in the graph.
*/

public class MyGraph implements Graph {
	private Map<Vertex, Set<Edge>> graph; // v_k -> set of edges with v_k as their source

    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
	  * @throws IllegalArgumentException if: any edge has negative weight,
	  * 	any edge links to or from a vertex not in v, or two edges have the 
	  *	same source and destination but different weights.
     */
	public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
		checkNull(v, "vertices");
		checkNull(e, "edges");
		graph = new HashMap<Vertex, Set<Edge>>();
		for (Vertex vertex : v)
			graph.put(vertex, new HashSet<Edge>());
		for (Edge edge : e) {
			if (!graph.containsKey(edge.getSource()) || !graph.containsKey(edge.getDestination()))
				throw new IllegalArgumentException(edge + " either comes from or goes to " +
						"a nonexistent vertex");
			if (edge.getWeight() < 0)
				throw new IllegalArgumentException(edge + " has negative weight; not allowed");
			for (Edge otherEdge : e) /* two edges from vertices p to q with different weights
												 	 not allowed */
				if (edge.getSource().equals(otherEdge.getSource()) && 
				edge.getDestination().equals(otherEdge.getDestination())
				&& edge.getWeight() != otherEdge.getWeight())
					throw new IllegalArgumentException(edge + " and " + otherEdge + " are the " +
							"same except for their weights; not allowed.");
			graph.get(edge.getSource()).add(edge);
		}
	}

   /** 
    * Return the collection of vertices of this graph
    * @return the vertices as a collection (which is anything iterable)
    */
   public Collection<Vertex> vertices() {
		Set<Vertex> vertices = new HashSet<Vertex>();
		for (Vertex v : graph.keySet())
			vertices.add(v);
		return vertices;
	}

	/** 
	* Return the collection of edges of this graph
	* @return the edges as a collection (which is anything iterable)
	*/
	public Collection<Edge> edges() {
		Set<Edge> edges = new HashSet<Edge>();
		for (Vertex vertex : graph.keySet())
			for(Edge edge : graph.get(vertex))
				edges.add(edge);
		return edges;
	}

   /**
    * Return a collection of vertices adjacent to a given vertex v,
    *   i.e., the set of all vertices w where edges v -> w exist in the graph.
    * Return an empty collection if there are no adjacent vertices.
    * @param v one of the vertices in the graph
    * @return an iterable collection of vertices adjacent to v in the graph
    * @throws IllegalArgumentException if v does not exist.
    */
   public Collection<Vertex> adjacentVertices(Vertex v) {
		checkVertexExists(v);
		Set<Vertex> adjVertices = new HashSet<Vertex>();
		for (Edge edge : graph.get(v))
			adjVertices.add(edge.getDestination());
		return adjVertices;
   }

   /**
    * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
    * Assumes that we do not have negative cost edges in the graph.
    * @param a one vertex
    * @param b another vertex
    * @return cost of edge if there is a directed edge from a to b in the graph, 
    * return -1 otherwise.
    * @throws IllegalArgumentException if a or b do not exist.
    */
   public int edgeCost(Vertex a, Vertex b) {
		checkVertexExists(a);
		checkVertexExists(b);
		for (Edge edge : graph.get(a))
			if (edge.getDestination().equals(b))
				return edge.getWeight();
		return -1; // not found
   }

   /**
    * Returns the shortest path from a to b in the graph, or null if there is
    * no such path.  Assumes all edge weights are nonnegative.
    * Uses Dijkstra's algorithm.
    * @param start the starting vertex
    * @param end the destination vertex
    * @return a Path where the vertices indicate the path from start to end in order
    *   and contains start and end and the cost is the cost of 
    *   the path. Returns null if end is not reachable from start.
    * @throws NoSuchElementException if start or end does not exist.
    */
   public Path shortestPath(Vertex start, Vertex end) {
		checkVertexExists(start);
		checkVertexExists(end);
	 	DHeap<Vertex> pq = new DHeap<Vertex>(2);
		Map<Vertex, Vertex> prevMap = new HashMap<Vertex, Vertex>(); /* v_k -> v_k+1 if
					v_k+1 is the previous vertex on the shortest path to v_k */
		for (Vertex v : graph.keySet()) {
			if (v.equals(start))
				v.setCost(0);
			else
				v.setCost(Integer.MAX_VALUE);
			pq.insert(v);
		}
		Vertex current = pq.deleteMin(); // current.equals(start) here
		while(!pq.isEmpty() && !current.equals(end)) {
			for(Edge e : graph.get(current)) { 
				if (pq.contains(e.getDestination())) { /* if pq does not contain the 
						destination vertex, the shortest path to that vertex is known. */ 
					Vertex destination = pq.get(e.getDestination()); /* must do get() to get 
							corresponding Vertex within pq */
					if (current.getCost() + e.getWeight() < destination.getCost()) {
						destination.setCost(current.getCost() + e.getWeight());
						prevMap.put(destination, e.getSource());
						pq.decreaseKey(destination, e.getWeight() + current.getCost());
					}
				}
			}
			/* the minimum value in pq is the vertex with the cheapest cost from start
					of the unknown vertices */
			Vertex next = pq.deleteMin();
			current = next;
		}
		return getBackpointPath(prevMap, start, current);
	} 

	/**
	* follows backpointers from vertex to vertex to build up the found
	* shortest path from start to end
	* @param prevMap, v_k -> v_k+1 if v_k+1 is the previous node on the
	* 	found shortest path
	* @param start the initial starting vertex
	* @param the target vertex
	* @return Path is the shortest path from start to end
	* return null if no path from start to end exists
	*/
	private Path getBackpointPath(Map<Vertex, Vertex> prevMap, Vertex start, Vertex end) {
		List<Vertex> list = new LinkedList<Vertex>(); // a list of edges for Path
		Vertex prev = end;
		if (start.equals(end)) {
			list.add(start);
			return new Path(list, 0);
		} else
      while(prev != null && !prev.equals(start)) {
         list.add(0, prev);
         prev = prevMap.get(prev);
      }
		list.add(0, prev);
		// path may or may not exist
		return list.contains(start) && list.contains(end) ? new Path(list, end.getCost()) : null;
	}
	
	/**
	* Finds a minimum spanning tree
	* @return an unsorted set of the edges in an MST of graph
	* returns null if no MST exists.
	* Assumes and works for an undirected graph. If the graph is directed
	* and only weakly connected, may report an MST when one does not exist.
	*/
	public Set<Edge> getMST() {
      // uses Kruskal's algorithm
      DisjointSets disjEdges = new MyDisjSets(graph.keySet().size());
		// vertexTable maps each vertex to an element in disjoint sets
      Map<Vertex, Integer> vertexTable = new HashMap<Vertex, Integer>();
      Set<Edge> mst = new HashSet<Edge>();
      PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
      // fill priority queue with edges
      for (Vertex v : graph.keySet())
         for (Edge e : graph.get(v))
            pq.add(e);
      /* establishes a correspondence between the 0 to V-1 numbers in disj sets
      		and our vertices */
      int i = 0;
      for (Vertex v : graph.keySet()) {
         vertexTable.put(v, i);
         i++;
      }
		try {
      	// while at one or more vertices are not part of the mst
	      while (disjEdges.numSets() > 1) {
	         Edge edge = pq.remove(); // get edge of lowest cost.
				int sourceDisjElement = vertexTable.get(edge.getSource());
				int destDisjElement = vertexTable.get(edge.getDestination());
	         if (disjEdges.find(sourceDisjElement) != disjEdges.find(destDisjElement)) {
					// if there's no path from source group to destination group
					mst.add(edge);
					// union to reflect that an edge from source to dest now exists 
		         disjEdges.union(disjEdges.find(sourceDisjElement),
							disjEdges.find(destDisjElement));
	   		}   
			} 
			return mst;
		} catch (NoSuchElementException mstDNE) { // pq is empty but vertices not connected
			return null;
		}
	}
	
	/**
	* Checks if vertex exists
	* @throws NoSuchElementException if the vertex does not exist in the graph
	*/
	private void checkVertexExists(Vertex vertex) {
		if (!graph.containsKey(vertex))
			throw new NoSuchElementException("vertex " + vertex + " not in graph");
	}
	
	/**
	* Checks if an object is null
	* @throws IllegalArgumentException if object is null
	*/
	private void checkNull(Object object, String thing) {
		if (object == null)
			throw new IllegalArgumentException(thing + " must not be null");
	}
}

