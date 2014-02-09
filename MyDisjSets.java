/** Minerva Chen, Max Peterson
* 11/25/2013
* MyDisjSets
*
* Implements disjoint sets. Uses weighted union and path compression to
* improve asymptotic speed.
*/

public class MyDisjSets implements DisjointSets {
	private int[] up;
	private int numElements;
	private int numSets;
	
	/**
	* Required constructor.
	* @param numElements is the total number of elements, each element is 
	* initially in its own set.
	*/	
	public MyDisjSets(int numElements) {
		this.numElements = numElements;
		up = new int[numElements];
		for (int i = 0; i < numElements; i++)
			up[i] = -1; // Negative denotes root. Each element is initially a tree of size 1
		this.numElements = numElements;
		numSets = numElements;
	}

	/** 
	* @return the total number of sets
	*/
	public int numSets() {
		return numSets;
	}
	
	/** 
	* @param set1 the name of a set
	* @param set2 the name of another set
	* @throws InvalidElementException if set1 or set2 are not valid set elements.
	* @throws InvalidSetNameException if set1 or set2 are not set names.	
	* combines set1 and set2 into a larger set containing all elements of both sets.
	* Makes the smaller set a part of the larger set, i.e. performs weighted union.
	*/
	public void union(int set1, int set2) {
		checkException(set1);
		checkException(set2);
		// perform weighted union - make the smaller set part of the larger set,
		// never vice-versa.
		if (set1 != set2) {
			if (up[set1] < up[set2]) { // set1 is the larger set
				up[set1] += up[set2];
				up[set2] = set1; 
			} else { // set2 is larger than set1 or same size
				up[set2] += up[set1];
				up[set1] = set2;
			}
			numSets--;
		}
	}
	
	/**
	* @throws InvalidElementException if setNum is not a valid element of a set. 
	* @throws InvalidSetNameException if setNum is not a valid name of a set.
	* @return total number of elements in setNum.
	*/
	public int numElements(int setNum) {
		checkException(setNum);
		return -up[setNum]; // roots have size as a negative number, so negate for positive
	}
	
	/**
	* @throws InvalidElementException if setNum is not a valid element of a set. 
	* @throws InvalidSetNameException if setNum is not a valid name of a set.
	* @return true if setName is the name of a set, false otherwise
	*/
	public boolean isSetName(int setName) {
		checkValidElement(setName);
		return up[setName] < 0;
	}

	/**
	* @throws InvalidElementException if setNum is not a valid element of a set. 
	* @throws InvalidSetNameException if setNum is not a valid name of a set.
	* Prints the elements of the set in the form 
	* {element, element, ..., element}.
	*/
	public void printSet(int setNum) {
		checkException(setNum);
		int[] elements = getElements(setNum);
		System.out.print("{");
		for (int i = 0; i < elements.length - 1; i++)
			System.out.print(elements[i] + ", ");
		System.out.println(elements[elements.length - 1] + "}");
	}
		
	/**
	* @throws InvalidElementException if x is not a valid element.
	* @return the name of the set x is in. 
	* Performs path compression to make future finds faster.
	*/
	public int find(int x) {
		checkValidElement(x);
		int root = x; 
		while (root >= 0 && up[root] >= 0) // find element
			root = up[root]; 
		if (x != root) { // if x not a root, do path compression
			int oldParent = up[x]; // prime loop
			while (oldParent != root) { 
				up[x] = root; // point directly at your root; this is the path compression. 
			 	x = oldParent; // go one level up the tree
			 	oldParent = up[x]; 
		 	} 
		}
		return root;
	}
	
	/**
	* @throws InvalidElementException if setNum is not a valid element of a set. 
	* @throws InvalidSetNameException if setNum is not a valid name of a set.
	* @return the an array of the elements of setNum. 
	* Performs path compression 
	* to make future calls on finds and getElements faster.
	*/
	public int[] getElements(int setNum) {
		checkException(setNum);
		int[] result = new int[-up[setNum]];
		int indexSoFar = 0;
		int i = 0;
		while (i < up.length && indexSoFar < result.length) { // don't check all of up
			if (find(i) == setNum) { // if the ith element is a member of setNum
				result[indexSoFar] = i;
				indexSoFar++; 
			}
			i++;
		}
		return result;
	}

	/**
	* @throws InvalidElementException if setNum is not a valid element of a set. 
	* @throws InvalidSetNameException if setNum is not a valid name of a set.
	*/
	private void checkException(int n) {
		checkValidElement(n);
		checkName(n);
	}

	/**
	* @throws InvalidSetNameException if setName is not the name of a set.
	*/
	private void checkName(int setName) {
		if (!isSetName(setName))
			throw new InvalidSetNameException();
	}
	
	/**
	* @throws InvalidElementException if element is not an element of any set
	* i.e. not in the range 0 to N - 1, where N is the integer passed to the constructor.
	*/
	private void checkValidElement(int element) {
		if (up.length <= element || element < 0)
			throw new InvalidElementException();
	}
}
