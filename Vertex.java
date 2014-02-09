/**
* Minerva Chen, Max Peterson
* 11/25/2013
* Vertex
*
* Representation of a graph vertex
*/
public class Vertex implements Dijkstrable<Vertex> {
	private final String label; // label attached to this vertex
	private int costFromStart;

	/**
	 * Construct a new vertex
	 * @param label the label attached to this vertex
	 * @throws IllegalArgumentException if label is null
	 * makes a new vertex with its cost from start set to ~infinity
	 * and its previous set to null
	 */
	public Vertex(String label) {
		this(label, 0);
	}

	/**
	* Construct a new vertex
	* @param label the label attached to this vertex
	* @param the distance from a predefined start vertex to this vertex
	* @throws IllegalArgumentException if label is null
	* makes a new vertex with the label and the distance from start.
	*/
	private Vertex(String label, int costFromStart) {
		if(label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		this.costFromStart = costFromStart;
	}
	
	/**
	* @return a copy of this Vertex
	*/
	public Vertex cloneIt() {
		return new Vertex(label, costFromStart);
	}

	/**
	* updates the cost from a predefined start vertex to this vertex.
	* @param the new cost
	*/
	public void updatePriority(int priority) {
		costFromStart = priority;
	}

	/**
	* @return the distance from a predefined start vertex
	*/
	public int getCost() {
		return costFromStart;
	}

	/** 
	* sets the cost from a predefined start vertex
	* @param the new cost
	*/
	public void setCost(int cost) {
		costFromStart = cost;
	}

	/** 
	* @return a positive int if this node is thought to be costlier from a 
	* predefined start vertex than other, negative if this is thought to 
	* be cheaper, and 0 if they're the same cost from start
	*/
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

	/**
	* @return an int to hash on based on label
	* auto-generated: hashes on label
	*/
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	/**
	* auto-generated: compares labels
	*/
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
