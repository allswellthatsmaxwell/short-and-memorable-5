import java.util.*;

public class DHeap<E extends Dijkstrable<? super E>> {  // extends PQable instead? {
	private E[] treeArray;
	private int size;
	private int d; // the d of this d-ary heap
	private Map<E, Integer> indexMap; // allows O(1) access to index of heap element
	private static final int INIT_SIZE = 10;

	// constructs a new d-ary heap (so each node has between 0 and d children inclusive)
	public DHeap(int d) {
		this.d = d;
		indexMap = new HashMap<E, Integer>();
		makeEmpty();
	}
  
  	// empties the heap of all elements
   public void makeEmpty() {
		treeArray = (E[]) new Dijkstrable[INIT_SIZE * d];
   }

	// returns true if the heap is empty, false otherwise
	public boolean isEmpty() {
		return size == 0;
	}

	// returns the number of elements in the heap
	public int size() {
		return size;
	}

	// 
	public void decreaseKey(E element, int priority) {
		decreaseKey(indexMap.get(element), priority);
	}

	// assumes new priority is more prioritous than current priority of item at i
	private void decreaseKey(int i, int priority) {
		if (i < 0)
			throw new IllegalArgumentException();
		treeArray[i].updatePriority(priority); // this method 
				// must be provided by the E class; that sucks
		percolateUp(i);
	}

	// returns the index of element in treeArray
	private int indexOf(E element) {
		for (int i = 0; i < treeArray.length; i++)
			if (treeArray[i].equals(element))
				return i;
		return -1; // not found
	}

	// inserts a value in the heap, preserving the heap property
	public void insert(E element) {
		size++;
		ensureCapacity();
		treeArray[size] = element;
		percolateUp();
	}

	// Heap must not be empty; throws EmptyPQException otherwise.
	// returns the highest priority item
	public Dijkstrable findMin() {
		checkException();
		return treeArray[1];
	}
	
	// Heap must not be empty; throws EmptyPQException otherwise.
	// removes and returns the highest priority item and also
	// preserves the heap property.
	public Dijkstrable deleteMin() {
		Dijkstrable min = findMin();
		treeArray[1] = treeArray[size];
		percolateDown();
		size--;
		return min;
	}

	// after removing the min, the heap needs to be adjusted to preserve the heap
	// property; this method does that. The lowest, rightmost leaf is placed 
	// temporarily at the root and then trades with the minimum child at every
	// level until there are no children with higher priority.
	private void percolateDown() {
		int i = 1;
		while (treeArray[i].compareTo(treeArray[minChildIndex(i)]) > 0) { // while there is a higher priority child
			int iNext = minChildIndex(i);
			E temp = treeArray[i];
			treeArray[i] = treeArray[iNext];
			treeArray[iNext] = temp;
			i = iNext;
			indexMap.put(treeArray[i], i);
			indexMap.put(treeArray[iNext], iNext);
		}	
	}

	// gets the index of the highest priority child of the node at index iParent
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

	// returns the index in treeArray of the leftmost child of the node at index iParent
	private int leftmostChildIndex(int iParent) {
		return d * (iParent - 1) + 2;
	}

	// returns the index in treeArray of the rightmost child of the node at index iParent
	private int rightmostChildIndex(int iParent) {
		return d * iParent + 1;
	}
	
	// when adding a value / priority to the tree, the correct place to put it
	// while preserving the heap property needs to be found; this method finds that place
	// and modifies the heap. i is the location in treeArray to start at.
	private void percolateUp(int i) {
		int iParent;
		indexMap.put(treeArray[i], i);
		while (i > 1 && treeArray[i].compareTo(treeArray[getParent(i)]) < 0) {
			iParent = getParent(i);
			indexMap.put(treeArray[i], iParent);
			indexMap.put(treeArray[iParent], i);
			E temp = treeArray[i];
			treeArray[i] = treeArray[iParent];
			treeArray[iParent] = temp;
			i = iParent;
		}
	}
 
	// when adding a value / priority to the tree, the correct place to put it
	// while preserving the heap property needs to be found; this method finds that place
	// and modifies the heap. 
	private void percolateUp() {
		percolateUp(size);
	}

	// throws EmptyPQException if the heap is empty
	private void checkException() {
		if (this.isEmpty())
			throw new EmptyPQException();
	}
   
	// checks if the array is big enough for the elements and doubles the array
   // size if not.
	private void ensureCapacity() {
		if (size >= treeArray.length) {
			E[] treeArray = (E[]) new Dijkstrable[this.treeArray.length * 2];
			for (int i = 0; i < this.treeArray.length; i++)
				treeArray[i] = this.treeArray[i];
			this.treeArray = treeArray;
		}
	}

	// gets the index of the parent of node i
	private int getParent(int i) {
		if (i % d > 1)
			return i / d + 1;
		else
			return i / d;
	}
}
