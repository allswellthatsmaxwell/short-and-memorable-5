/**
 * Representation of a graph vertex
 */
 //just making sure I can edit-Minerva
public class Vertex implements Dijkstrable<Vertex> {
	private final String label;   // label attached to this vertex
	private int costFromStart;
	private boolean known;
	private Vertex previous; 

	/**
	 * Construct a new vertex
	 * @param label the label attached to this vertex
	 * @throws IllegalArgumentException if label is null
	 * makes a new vertex with its cost from start set to ~infinity
	 * and its previous set to null
	 */
	public Vertex(String label) {
		this(label, Integer.MAX_VALUE, false, null);
	}

	// makes a new vertex with the label, the distance from start,
	// a known value for whether or not we have the shortest path to this
	// node during Dijkstra's algorithm, and the previous vertex on the path
	// to get here.
	private Vertex(String label, int costFromStart, boolean known, Vertex previous) {
		if(label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		this.costFromStart = costFromStart;
		this.known = known;
		this.previous = previous;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public void updatePriority(int priority) {
		costFromStart = priority;
	}
	
	// sets the previous node on the path, i.e. wherever we came from to get here.
	// for Dijkstra's algorithm.
	public void setPrevious(Vertex vertex) {
		previous = vertex;
	}

	// returns the previous node on the path, i.e. wherever we came from to get here.
	// for Dijkstra's algorithm.
	public Vertex getPrevious() {
		return previous;
	}
	
	// returns a copy of this Vertex
	public Vertex clone() {
		return new Vertex(label, costFromStart, known, previous);
	}

	// returns the distance from start
	public int getCost() {
		return costFromStart;
	}

	// sets the distance from start
	public void setCost(int cost) {
		costFromStart = cost;
	}

	// returns whether or not the shortest path to this node is known, for Dijkstra's
	// algorithm
	public boolean isKnown() {
		return known;
	}
	
	// returns a positive int if this node is thought to be further from start
	// than other, negative if this is thought to be closer, and 0 if they're the same
	// distance from start
	public int compareTo(Vertex other) {
		return costFromStart - other.costFromStart;
	}

	/**
	 * Get a vertex label
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * A string representation of this object
	 * @return the label attached to this vertex
	 */
	public String toString() {
		return label;
	}

	//auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	//auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null)
      	return other.label == null;
		else
		   return label.equals(other.label);
	}
}
