/**
 * Representation of a directed graph edge.
 */

// Minerva Chen, Max Peterson
// 11/16/2013
//
// Edge

public class Edge implements Comparable<Edge> {
	private final Vertex from,to;
	private final int w;

	/**
	 * Construct a new edge
	 * @param from start vertex
	 * @param to end vertex
	 * @param w weight of this edge
	 */
	public Edge(Vertex from, Vertex to, int w) {
		if(from == null || to == null)
			throw new IllegalArgumentException("null");
		this.from = from;
		this.to = to;
		this.w = w;
	}

	// returns positive if this edge's weight is greater than other's weight,
	// negative if it is less, and 0 if the edges are of equal weight.
	public int compareTo(Edge other) {
		return w - other.w;
	}

	// returns a copy of this edge
	public Edge clone() {
		return new Edge(from, to, w);
	}

   /**
	 * Get the source vertex
	 * @return the Vertex that is the source of the edge
	 */
	public Vertex getSource() {
		return from;
	}

   /**
	 * Get the destination vertex
	 * @return the Vertex that is the destination of the edge
	 */
	public Vertex getDestination() {
		return to;
	}

   /**
	 * Get the edge weight (a.k.a. cost)
	 * @return the weight of the edge
	 */
	public int getWeight() {
		return w;
	}

	/**
	 * A string representation of this object
	 * @return A string of the form <from, to, weight>
	 */
	public String toString() {
		return "<"+from+", "+to+", "+w+">";
	}

	//auto-generated: hashes on all fields
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + w;
		return result;
	}

	//auto-generated: compares all fields
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Edge other = (Edge) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return w==other.w;
	}
}
