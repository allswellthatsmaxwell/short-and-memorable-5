import java.util.*;

public class DHeap<E extends Dijkstrable<E>> {
	private E[] treeArray;
	private int size;
	private int d; // the d of this d-ary heap
	private Map<E, Integer> indexMap; // allows O(1) hash access to index of heap element
	private static final int INIT_SIZE = 10;

	/**
	* constructs a new d-ary heap (so each node has between 0 and d children inclusive)
	* @param d the number of nodes per level 
	*/
	public DHeap(int d) {
		this.d = d;
		indexMap = new HashMap<E, Integer>();
		makeEmpty();
	}
  
  	/**
	* empties the heap of all elements
	*/
	@SuppressWarnings("unchecked")
   public void makeEmpty() {
		treeArray = (E[]) new Dijkstrable[INIT_SIZE * d];
   }
	
	/**
	* @return true if the heap is empty, false otherwise
	*/
	public boolean isEmpty() {
		return size == 0;
	}

	/** 
	* @return the number of elements in the heap
	*/
	public int size() {
		return size;
	}

	/**
	* @return the element equal to this one.
	* Necessary to allow keeping the same object reference for decreaseKey.
	*/
	public E get(E element) {
		int i = indexMap.get(element);
		return treeArray[i].cloneIt();
	}

	/**
	* @return true if the heap contains element, false otherwise.
	*/
	public boolean contains(E element) {
		return indexMap.containsKey(element);
	}

	/**
	* Increases the priority of element.
	* @param element the element to increase priority of
	* @param priority the new priority
	* @throws NoSuchElementException if the element is not in the heap.
	*/
	public void decreaseKey(E element, int priority) {
		if (!contains(element))
			throw new NoSuchElementException();
		decreaseKey(indexMap.get(element), priority);
	}

	/**
	* @param i the index in treeArray of the element to increase the priority of
	* @param priority the new priority
	* @throws IllegalArgumentException if i < 0
	* @assumes new priority is more prioritous than current priority of item at i
	*/
	private void decreaseKey(int i, int priority) {
		if (i < 0)
			throw new IllegalArgumentException();
		treeArray[i].updatePriority(priority); 
		percolateUp(i);
	}

	/**
	* inserts a value in the heap, preserving the heap property
	* @param element the element to insert
	*/
	@SuppressWarnings("unchecked")
	public void insert(E element) {
		size++;
		ensureCapacity();
		treeArray[size] = element.cloneIt();
		percolateUp();
	}

	/**
	* @return the highest priority item
	* @throws EmptyPQException if heap is empty.	
	*/
	@SuppressWarnings("unchecked")
	public E findMin() {
		checkException();
		return treeArray[1].cloneIt();
	}
	
	/**
	* @return the highest priority item, preserving the heap property.
	* @throws EmptyPQException if heap is empty
	*/
	public E deleteMin() {
		E min = findMin();
		treeArray[1] = treeArray[size];
		indexMap.remove(min);
		percolateDown();
		size--;
		return min;
	}

	/** 
	* after removing the min, the heap needs to be adjusted to preserve the heap
	* property; this method does that. The lowest, rightmost leaf is placed 
	* temporarily at the root and then trades with the minimum child at every
	* level until there are no children with higher priority.
	*/
	private void percolateDown() {
		int i = 1;
		while (treeArray[i].compareTo(treeArray[minChildIndex(i)]) > 0) { // while there is a higher priority child
			int iNext = minChildIndex(i);
			E temp = treeArray[i];
			treeArray[i] = treeArray[iNext];
			treeArray[iNext] = temp;
			indexMap.put(treeArray[i], i);
			indexMap.put(treeArray[iNext], iNext);
			i = iNext;
		}	
	}
	
	/**
	* when adding a value / priority to the tree, the correct place to put it
	* while preserving the heap property needs to be found; this method finds that place
	* and modifies the heap. 
	* @param i is the location in treeArray at which to start percolating
	*/
	private void percolateUp(int i) {
		indexMap.put(treeArray[i], i);
		while (i > 1 && treeArray[i].compareTo(treeArray[getParent(i)]) < 0) {
			int iParent = getParent(i);
			E temp = treeArray[i];
			treeArray[i] = treeArray[iParent];
			treeArray[iParent] = temp;
			indexMap.put(treeArray[i], i);
			indexMap.put(treeArray[iParent], iParent);
			i = iParent;
		}
	}
 
	/**
	* when adding a value / priority to the tree, the correct place to put it
	* while preserving the heap property needs to be found; this method finds that place
	* and modifies the heap. 
	*/
	private void percolateUp() {
		percolateUp(size);
	}
   
	/**
	* checks if the array is big enough for the elements and doubles the array
   * size if not.
	*/
	@SuppressWarnings("unchecked")
	private void ensureCapacity() {
		if (size >= treeArray.length) {
			E[] treeArray = (E[]) new Dijkstrable[this.treeArray.length * 2];
			for (int i = 0; i < this.treeArray.length; i++)
				treeArray[i] = this.treeArray[i];
			this.treeArray = treeArray;
		}
	}
	
	/**
   * @param iParent an index
	* @return the index of the highest priority child of the node at iParent
	*/
	private int minChildIndex(int iParent) {
		int indexOfMinChild = iParent; 
		E minValue = treeArray[iParent]; // priming the loop
		for (int k = leftmostChildIndex(iParent); k <= Math.min(rightmostChildIndex(iParent), size); k++) {
			if (treeArray[iParent].compareTo(treeArray[k]) > 0 && minValue.compareTo(treeArray[k]) > 0) { 
				minValue = treeArray[k]; // this child is higher priority than 
						// our previous highest priority node
				indexOfMinChild = k;
			}
		}
		return indexOfMinChild;
	}
	
	/**
	* @param iParent an index
	* @return the index in treeArray of the leftmost child of the node at index iParent
	*/
	private int leftmostChildIndex(int iParent) {
		return d * (iParent - 1) + 2;
	}

	/**
	* @param iParent an index
	* @return the index in treeArray of the rightmost child of the node at index iParent
	*/
	private int rightmostChildIndex(int iParent) {
		return d * iParent + 1;
	}

	/**
	* @param i an index
	* @return the index of the parent of node i
	*/
	private int getParent(int i) {
		if (i % d > 1)
			return i / d + 1;
		else
			return i / d;
	}
	
	/**
	* @throws EmptyPQException if the heap is empty
	*/
	private void checkException() {
		if (this.isEmpty())
			throw new EmptyPQException();
	}
}
