import java.util.*;
// Minerva Chen, Max Peterson
// 11/15/2013
//
// MyGraph
/**
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
	private Map<Vertex, Set<Edge>> graph;

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
		checkNull(v);
		checkNull(e);
		graph = new HashMap<Vertex, Set<Edge>>();
		List<Vertex> list = new ArrayList<Vertex>();
		int i = 0;
		for (Vertex vertex : v) {
			// vertex.setCost(i);
			graph.put(vertex.clone(), new TreeSet<Edge>());
			i++;
		}
		for (Edge edge : e) {
			if (!graph.containsKey(edge.getSource()) || !graph.containsKey(edge.getDestination()))
				throw new IllegalArgumentException(edge + " either comes from or goes to " +
						"a nonexistent vertex");
			if (edge.getWeight() < 0)
				throw new IllegalArgumentException(edge + " has negative weight; not allowed");
			for (Edge otherEdge : e)
				if (edge.getSource().equals(otherEdge.getSource()) && 
						edge.getDestination().equals(otherEdge.getDestination())
						&& edge.getWeight() != otherEdge.getWeight())
					throw new IllegalArgumentException(edge + " and " + otherEdge + " are the " +
							"same except for their weights; not allowed.");
			graph.get(edge.getSource()).add(edge.clone());
		}
	}

	/**
	* Checks if an object is null
	* @throws IllegalArgumentException if object is null
	*/
	private void checkNull(Object object) {
		if (object == null)
			throw new IllegalArgumentException(object + " must not be null");
	}

   /** 
    * Return the collection of vertices of this graph
    * @return the vertices as a collection (which is anything iterable)
    */
   public Collection<Vertex> vertices() {
		return Collections.unmodifiableSet(graph.keySet());
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
		return Collections.unmodifiableSet(edges);
	}

   /**
    * Return a collection of vertices adjacent to a given vertex v.
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
		return Collections.unmodifiableSet(adjVertices);
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
	* Checks if vertex exists
	* @throws new IllegalArgumentException if the vertex does not exist in the graph
	*/
	private void checkVertexExists(Vertex vertex) {
		if (!graph.containsKey(vertex))
			throw new IllegalArgumentException("vertex " + vertex + " not in graph");
	}
	
	/**
	* returns a minimum spanning tree of the graph. 
	* Assumes Edge is Comparable by edge weight.
	*/
   public Set<Edge> getMST() {
	       // uses Kruskal's algorithm
			 Set<Vertex> vert = new HashSet<Vertex>();
	       Set<Edge> mst = new HashSet<Edge>();
	       PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
	       for (Vertex v : graph.keySet())
	               for (Edge e : graph.get(v))
	                       pq.add(e);
	  		 int num = graph.keySet().size();
			 System.out.println("priority queue: " + pq);    
			 while (vert.size() < num) {
	            Edge edge = pq.remove();
					Vertex x = edge.getDestination();
					if(!vert.contains(x)){
						System.out.print("edges as added: " + edge);
						mst.add(edge);
	          		vert.add(x);  	
					}
	       }
			 System.out.println();
	       return Collections.unmodifiableSet(mst);
     }
  /**
    * Returns the shortest path from a to b in the graph, or null if there is
    * no such path.  Assumes all edge weights are nonnegative.
    * Uses Dijkstra's algorithm.
    * @param a the starting vertex
    * @param b the destination vertex
    * @return a Path where the vertices indicate the path from a to b in order
    *   and contains a (first) and b (last) and the cost is the cost of 
    *   the path. Returns null if b is not reachable from a.
    * @throws IllegalArgumentException if a or b does not exist.
    */
   public Path shortestPath(Vertex a, Vertex b) {
		checkVertexExists(a);
		checkVertexExists(b);
	 	DHeap pq = new DHeap(2);
		List<Vertex> list = new LinkedList<Vertex>();
		for (Vertex v : graph.keySet()) {
			if (v.equals(a))
				v.setCost(0);
			else
				v.setCost(Integer.MAX_VALUE);
			v.setKnown(false);
			pq.insert(v);
		}
		Vertex current = a;
		while(!current.equals(b) && !pq.isEmpty()) {
			for(Edge e : graph.get(current)) { 
            Vertex destination = e.getDestination();
				destination.setCost(current.getCost() + e.getWeight());
				pq.decreaseKey(destination, e.getWeight() + current.getCost());
			}
			Vertex next = (Vertex) pq.deleteMin();
			next.setKnown(true);
			next.setPrevious(current);
			current = next;
		}
      list.add(b);
      Vertex before = b.getPrevious();
      while(before != a){
         list.add(before);
         before = before.getPrevious();
      }
      list.add(a);
		return new Path(list, b.getCost());
		// return null;
	}
}

