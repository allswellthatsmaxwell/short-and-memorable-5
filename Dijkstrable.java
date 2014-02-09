/**
* Minerva Chen, Max Peterson
* 11/25/2013
* Dijkstrable
*
* An interface for objects that support a DHeap's decreaseKey operation.
*/

public interface Dijkstrable<E> extends Comparable<E> {
	/**
	* update the priority of this to priority
	* @param the new priority
	*/
	public void updatePriority(int priority);
	
	/**
	* @return a copy of this object
	*/
	public E cloneIt();
}
